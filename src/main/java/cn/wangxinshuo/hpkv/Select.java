package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.cache.FileCache;
import cn.wangxinshuo.hpkv.file.FileResources;
import cn.wangxinshuo.hpkv.range.key.SortedList;
import cn.wangxinshuo.hpkv.util.ByteArrayToUnsignedLong;
import cn.wangxinshuo.hpkv.util.UnsignedLongToByteArray;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import com.google.common.primitives.UnsignedLong;
import org.apache.commons.lang3.SerializationUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author wszgr
 */
public class Select {
    private FileResources resources;
    /**
     * 利用LRU缓存淘汰算法来进行缓存
     * LRU:Least Recently Used
     */
    private LinkedList<FileCache> fileCaches;
    /**
     * FILE_CACHE_LEN 数量需要控制，单文件
     * 的大小最后会在1~2GB左右，所以不能占用太多内存
     */
    private final int FILE_CACHE_LEN = 128;
    private HashMap<UnsignedLong, byte[]> map;
    private SortedList sortedList;

    private Select(FileResources resources) {
        fileCaches = new LinkedList<FileCache>();
        this.resources = resources;
    }

    public Select(FileResources resources,
                  HashMap<UnsignedLong, byte[]> map, SortedList sortedList) {
        this(resources);
        this.sortedList = sortedList;
        this.map = map;
    }

    private synchronized byte[] get(UnsignedLong key) throws EngineException {
        // 去MemTable中查找，由于map初始化的时候log文件就已经写入map，
        // 所以不需要再去log文件里面查找
        if (map.containsKey(key)) {
            System.out.println("从map中获得查询的数据！");
            return map.get(key);
        }
        // 去FileCache中查找
        for (FileCache cache :
                fileCaches) {
            HashMap<UnsignedLong, byte[]> map =
                    cache.getData();
            if (map.containsKey(key)) {
                return map.get(key);
            }
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
                        // 加入Cache
                        if (fileCaches.size() >= this.FILE_CACHE_LEN) {
                            fileCaches.removeLast();
                        }
                        fileCaches.addFirst(new FileCache(i, map));
                        return map.get(key);
                    }
                }
            } catch (IOException e) {
                throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
            }
        }
        throw new EngineException(RetCodeEnum.NOT_FOUND, "NOT_FOUND");
    }

    public byte[] get(byte[] inKey) throws EngineException {
        UnsignedLong key = ByteArrayToUnsignedLong.getKey(inKey);
        return get(key);
    }

    public HashMap<byte[], byte[]> range(byte[] start, byte[] end) throws EngineException {
        HashMap<byte[], byte[]> rangeMap = new HashMap<byte[], byte[]>();
        LinkedList<UnsignedLong> list = sortedList.get(start, end);
        for (UnsignedLong key :
                list) {
            rangeMap.put(UnsignedLongToByteArray.getKey(key), get(key));
        }
        return rangeMap;
    }

    public int getFileCacheLen() {
        return FILE_CACHE_LEN;
    }
}
