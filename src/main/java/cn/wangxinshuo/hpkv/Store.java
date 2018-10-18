package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.log.Log;
import cn.wangxinshuo.hpkv.resources.DatabaseResources;
import cn.wangxinshuo.hpkv.resources.IndexResources;
import cn.wangxinshuo.hpkv.util.desrialize.DeserializeFromFile;
import cn.wangxinshuo.hpkv.util.key.Key;
import cn.wangxinshuo.hpkv.util.serialize.SerializeToFile;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;

import java.util.HashMap;

/**
 * @author wszgr
 */
public class Store {
    private DatabaseResources databaseResources;
    private IndexResources indexResources;
    private HashMap<Key, byte[]> map;
    private int fileIndex;
    private Log log;

    public Store(DatabaseResources databaseResources,
                 IndexResources indexResources,
                 Log log, HashMap<Key, byte[]> map) {
        this.databaseResources = databaseResources;
        this.indexResources = indexResources;
        fileIndex = 0;
        this.log = log;
        this.map = map;
    }

    private int getFileIndex() {
        if (fileIndex == databaseResources.getNumberOfFiles()) {
            fileIndex = 0;
            return fileIndex;
        }
        return fileIndex++;
    }

    public synchronized void put(byte[] key, byte[] value) throws EngineException {
        // 首先判断状态
        if (map.size() >= log.getKeyValueNumberInLogFile()) {
            // 为可以持久化到文件的状态
            int fileIndex = getFileIndex();
            // 将map持久化到文件
            HashMap<Key, byte[]> mapInFile =
                    DeserializeFromFile.deserializeFromFile(
                            databaseResources, fileIndex, true);
            if (mapInFile != null) {
                map.putAll(mapInFile);
                mapInFile.clear();
            }
            System.out.println("MapSzie:" + map.size());
            SerializeToFile.serializeToFile(databaseResources, fileIndex, map);
            // 将map里面的key持久化到文件中
            for (Key eachKey :
                    map.keySet()) {
                indexResources.write(eachKey.getData());
            }
            // 清场
            log.eraseLog();
            map.clear();
        }
        // 为不可以持久化到文件的状态
        log.write(key);
        log.write(value);
        // 放入MemTable
        map.put(new Key(key), value);
    }
}
