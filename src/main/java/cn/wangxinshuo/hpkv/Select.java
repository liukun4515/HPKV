package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.file.FileResources;
import cn.wangxinshuo.hpkv.util.ByteArrayToUnsignedLong;
import cn.wangxinshuo.hpkv.util.Cache;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import com.google.common.primitives.UnsignedLong;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
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

    public Select(FileResources resources) {
        this.resources = resources;
        lruList = new ArrayList<Cache>(CACHE_LEN);
    }

    public byte[] get(byte[] inKey) throws EngineException {
        UnsignedLong key = ByteArrayToUnsignedLong.getKey(inKey);
        final short numberOfFiles = 256;
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
        // 去文件中查找
        for (int i = 0; i < numberOfFiles; i++) {
            RandomAccessFile stream = resources.getReadSources(i);
            try {
                if (stream.length() != 0) {
                    // 读取序列化之后的byte
                    int availableLength = (int) stream.length();
                    byte[] serializedArray = new byte[availableLength];
                    stream.seek(0);
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
