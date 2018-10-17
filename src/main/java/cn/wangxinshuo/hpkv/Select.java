package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.cache.FileCache;
import cn.wangxinshuo.hpkv.file.FileResources;
import cn.wangxinshuo.hpkv.util.KeyCompare;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import org.apache.commons.lang3.SerializationUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
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
    private HashMap<byte[], byte[]> map;

    private Select(FileResources resources) {
        fileCaches = new LinkedList<FileCache>();
        this.resources = resources;
    }

    public Select(FileResources resources, HashMap<byte[], byte[]> map) {
        this(resources);
        this.map = map;
    }

    public synchronized byte[] get(byte[] key) throws EngineException {
        // 去MemTable中查找，由于map初始化的时候log文件就已经写入map，
        // 所以不需要再去log文件里面查找
        if (map.containsKey(key)) {
            System.out.println("从map中获得查询的数据！");
            return map.get(key);
        }
        // 去FileCache中查找
        for (FileCache cache :
                fileCaches) {
            HashMap<byte[], byte[]> map =
                    cache.getData();
            if (map.containsKey(key)) {
                fileCaches.remove(cache);
                fileCaches.addFirst(cache);
                return map.get(key);
            }
        }
        // 去文件中查找
        for (int i = 0; i < resources.getNumberOfFiles(); i++) {
            InputStream stream = null;
            try {
                stream = new FileInputStream(resources.getReadSources(i));
                if (stream.available() != 0) {
                    // 读取序列化之后的byte
                    byte[] serializedArray = new byte[stream.available()];
                    final int read = stream.read(serializedArray);
                    // 反序列化
                    HashMap<byte[], byte[]> map =
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
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new EngineException(RetCodeEnum.NOT_FOUND, "NOT_FOUND");
    }

    public HashMap<byte[], byte[]> range(byte[] start, byte[] end) throws EngineException {
        HashMap<byte[], byte[]> rangeMap =
                new HashMap<byte[], byte[]>(8);
        RandomAccessFile keyFile = null;
        try {
            keyFile = new RandomAccessFile(
                    resources.getKeyFile(), "rws");
            keyFile.seek(0);
            byte[] key = new byte[8];
            for (int i = 0; i < keyFile.length() / 8; i++) {
                keyFile.read(key);
                if (KeyCompare.compare(start, key) >= 0) {
                    if (KeyCompare.compare(key, end) <= 0) {
                        rangeMap.put(key, get(key));
                    }
                }
            }
            keyFile.close();
            return rangeMap;
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        } finally {
            try {
                if (keyFile != null) {
                    keyFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getFileCacheLen() {
        return FILE_CACHE_LEN;
    }
}
