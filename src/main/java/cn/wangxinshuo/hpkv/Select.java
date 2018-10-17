package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.cache.FileCache;
import cn.wangxinshuo.hpkv.desrialize.DeserializeFromFile;
import cn.wangxinshuo.hpkv.key.Key;
import cn.wangxinshuo.hpkv.resources.DatabaseResources;
import cn.wangxinshuo.hpkv.resources.IndexResources;
import cn.wangxinshuo.hpkv.util.KeyCompare;
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
        // 去MemTable中查找，由于map初始化的时候log文件就已经写入map，
        // 所以不需要再去log文件里面查找
        Key key = new Key(inKey);
        if (map.containsKey(key)) {
            return map.get(key);
        }
        // 去FileCache中查找
        for (FileCache cache :
                fileCaches) {
            HashMap<Key, byte[]> mapInFileCache =
                    cache.getData();
            if (mapInFileCache.containsKey(key)) {
                fileCaches.remove(cache);
                fileCaches.addFirst(cache);
                return mapInFileCache.get(key);
            }
            mapInFileCache.clear();
        }
        // 去文件中查找
        for (int i = 0; i < databaseResources.getNumberOfFiles(); i++) {
            HashMap<Key, byte[]> mapInFileResources =
                    DeserializeFromFile.deserializeFromFile(
                            databaseResources, i, true);
            if (mapInFileResources != null && mapInFileResources.containsKey(key)) {
                // 加入Cache
                fileCaches.addFirst(new FileCache(i, mapInFileResources));
                return mapInFileResources.get(key);
            }
        }
        throw new EngineException(RetCodeEnum.NOT_FOUND, "NOT_FOUND");
    }

    public HashMap<Key, byte[]> range(byte[] start,
                                      byte[] end) throws EngineException {
        HashMap<Key, byte[]> rangeMap =
                new HashMap<Key, byte[]>();
        int indexLength = indexResources.getIndexLength();
        for (int i = 0; i < indexResources.getIndexFileLength(); i += indexLength) {
            byte[] key = indexResources.read(i);
            if (KeyCompare.compare(start, key) >= 0) {
                if (KeyCompare.compare(key, end) >= 0) {
                    rangeMap.put(new Key(key), get(key));
                }
            }
        }
        return rangeMap;
    }
}
