package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.cache.FileCache;
import cn.wangxinshuo.hpkv.key.Key;
import cn.wangxinshuo.hpkv.log.Log;
import cn.wangxinshuo.hpkv.resources.DatabaseResources;
import cn.wangxinshuo.hpkv.resources.IndexResources;
import cn.wangxinshuo.hpkv.util.desrialize.DeserializeFromFile;
import cn.wangxinshuo.hpkv.util.key.KeyCompare;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author wszgr
 */
public class Select {
    private DatabaseResources databaseResources;
    private IndexResources indexResources;
    /**
     * 利用LRU缓存淘汰算法来进行缓存
     * LRU:Least Recently Used
     */
    private LinkedList<FileCache> fileCaches;
    private HashMap<Key, byte[]> map;

    public Select(DatabaseResources databaseResources,
                  IndexResources indexResources,
                  HashMap<Key, byte[]> map) {
        fileCaches = new LinkedList<FileCache>();
        this.databaseResources = databaseResources;
        this.indexResources = indexResources;
        this.map = map;
    }

    public synchronized byte[] get(byte[] inKey) throws EngineException {
        Key key = new Key(inKey);
        // 去文件中查找
        for (int i = 0; i < databaseResources.getNumberOfFiles(); i++) {
            byte[] value = getFromFile(i, inKey);
            if (value != null) {
                return value;
            }
        }
        throw new EngineException(RetCodeEnum.NOT_FOUND, "NOT_FOUND");
    }

    private synchronized byte[] getFromMap(byte[] inKey) {
        // 去MemTable中查找，由于map初始化的时候log文件就已经写入map，
        // 所以不需要再去log文件里面查找
        Key key = null;
        try {
            key = new Key(inKey);
        } catch (EngineException e) {
            e.printStackTrace();
        }
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return null;
    }

    private synchronized byte[] getFromCache(byte[] inKey) {
        Key key = null;
        try {
            key = new Key(inKey);
            // 去FileCache中查找
            for (FileCache cache :
                    fileCaches) {
                HashMap<Key, byte[]> mapAlreadyExist =
                        cache.getData();
                if (mapAlreadyExist.containsKey(key)) {
                    if (fileCaches.size() > 0) {
                        fileCaches.removeLast();
                    }
                    fileCaches.addFirst(cache);
                    return mapAlreadyExist.get(key);
                }
            }
        } catch (EngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    private synchronized byte[] getFromFile(int fileIndex, byte[] inKey) {
        try {
            Key key = new Key(inKey);
            HashMap<Key, byte[]> mapAlreadyExist =
                    DeserializeFromFile.deserializeFromFile(
                            databaseResources, fileIndex, true);
            if (mapAlreadyExist != null && mapAlreadyExist.containsKey(key)) {
                // 加入Cache
                if (fileCaches.size() > 0) {
                    fileCaches.removeLast();
                }
                fileCaches.addFirst(new FileCache(fileIndex, mapAlreadyExist));
                return mapAlreadyExist.get(key);
            }
        } catch (EngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<Key, byte[]> range(byte[] start,
                                      byte[] end) throws EngineException {
        HashMap<Key, byte[]> rangeMap =
                new HashMap<Key, byte[]>();
        int indexLength = indexResources.getIndexLength();
        for (int i = 0; i < indexResources.getIndexFileLength(); i += indexLength) {
            System.out.println("第：" + i + "个Key");
            byte[] key = indexResources.read(i);
            if (KeyCompare.compare(start, key) >= 0) {
                if (KeyCompare.compare(key, end) >= 0) {
                    int index = (i / Log.KV_NUMBER) % databaseResources.getNumberOfFiles();
                    byte[] value = getFromMap(key);
                    if (value == null) {
                        value = getFromCache(key);
                        if (value == null) {
                            value = getFromFile(index, key);
                            if (value == null) {
                                value = get(key);
                            }
                        }
                    }
                    rangeMap.put(new Key(key), value);
                }
            }
        }
        return rangeMap;
    }
}
