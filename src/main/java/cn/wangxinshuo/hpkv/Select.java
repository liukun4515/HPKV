package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.cache.Cache;
import cn.wangxinshuo.hpkv.file.FileResources;
import cn.wangxinshuo.hpkv.log.Log;
import cn.wangxinshuo.hpkv.util.ByteArrayToUnsignedLong;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import com.google.common.primitives.UnsignedLong;
import org.apache.commons.lang3.SerializationUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author wszgr
 */
public class Select {
    private FileResources resources;
    /**
     * 利用LRU缓存淘汰算法来进行缓存
     * LRU:Least Recently Used
     */
    private ArrayList<Cache> lruList;
    private final int CACHE_LEN = 128;
    private HashMap<UnsignedLong, byte[]> map;
    private Log log;

    private Select(FileResources resources) {
        this.resources = resources;
        lruList = new ArrayList<Cache>(CACHE_LEN);
    }

    public Select(FileResources resources, Log log,
                  HashMap<UnsignedLong, byte[]> map) {
        this(resources);
        this.log = log;
        this.map = map;
    }

    public byte[] get(byte[] inKey) throws EngineException {
        UnsignedLong key = ByteArrayToUnsignedLong.getKey(inKey);
        // 从缓存中查找
        for (Cache aLruList : lruList) {
            HashMap<UnsignedLong, byte[]> map = aLruList.getData();
            if (map.containsKey(key)) {
                byte[] value = map.get(key);
                lruList.remove(aLruList);
                lruList.add(0, aLruList);
                return value;
            }
        }
        // 去MemTable中查找
        if (map.containsKey(key)) {
            return map.get(key);
        }
        // 去日志文件中查找
        try {
            RandomAccessFile randomAccessFile = log.getRandomAccessFile();
            randomAccessFile.seek(0);
            byte[] keyFromFile = new byte[8];
            for (int i = 0; i < log.getLogNumber(); i++) {
                randomAccessFile.read(keyFromFile);
                boolean isEqual = true;
                for (int j = 0; j < keyFromFile.length; j++) {
                    if (keyFromFile[j] != inKey[j]) {
                        isEqual = false;
                        break;
                    }
                }
                if (isEqual) {
                    byte[] wantValue = new byte[4 * 1024];
                    randomAccessFile.read(wantValue);
                    return wantValue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
        // 去文件中查找
        for (int i = 0; i < resources.getNumberOfFiles(); i++) {
            try {
                InputStream stream = new FileInputStream(resources.getReadSources(i));
                if (stream.available() != 0) {
                    // 读取序列化之后的byte
                    byte[] serializedArray = new byte[stream.available()];
                    final int read = stream.read(serializedArray);
                    // 反序列化
                    HashMap<UnsignedLong, byte[]> map =
                            SerializationUtils.deserialize(serializedArray);
                    if (map.containsKey(key)) {
                        if (map.size() == CACHE_LEN) {
                            lruList.remove(CACHE_LEN);
                        }
                        lruList.add(0, new Cache(i, map));
                        return map.get(key);
                    }
                }
            } catch (IOException e) {
                throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
            }
        }
        throw new EngineException(RetCodeEnum.NOT_FOUND, "NOT_FOUND");
    }
}
