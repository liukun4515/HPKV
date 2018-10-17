package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.desrialize.DeserializeFromFile;
import cn.wangxinshuo.hpkv.log.Log;
import cn.wangxinshuo.hpkv.resources.DatabaseResources;
import cn.wangxinshuo.hpkv.resources.IndexResources;
import cn.wangxinshuo.hpkv.serialize.SerializeToFile;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;

import java.util.HashMap;

/**
 * @author wszgr
 */
public class Store {
    private DatabaseResources databaseResources;
    private IndexResources indexResources;
    private HashMap<byte[], byte[]> map;
    private int fileIndex;
    private Log log;

    public Store(DatabaseResources databaseResources,
                 IndexResources indexResources,
                 Log log, HashMap<byte[], byte[]> map) {
        this.databaseResources = databaseResources;
        this.indexResources = indexResources;
        this.log = log;
        this.map = map;
    }

    private int getFileIndex() {
        if (fileIndex == databaseResources.getNumberOfFiles()) {
            fileIndex = 0;
        }
        return ++fileIndex;
    }

    public synchronized void put(byte[] key, byte[] value) throws EngineException {
        // 首先判断状态
        if (map.size() >= log.getKeyValueNumberInLogFile()) {
            // 为可以持久化到文件的状态
            int fileIndex = getFileIndex();
            // 将map持久化到文件并清空
            HashMap<byte[], byte[]> mapInFile =
                    DeserializeFromFile.deserializeFromFile(
                            databaseResources, fileIndex, true);
            if (mapInFile != null) {
                map.putAll(mapInFile);
            }
            System.out.println("MapSzie:" + map.size());
            SerializeToFile.serializeToFile(databaseResources, fileIndex, map);
            // 将map里面的key持久化到文件中
            for (byte[] eachKey :
                    map.keySet()) {
                indexResources.write(eachKey);
            }
            // 清场
            log.eraseLog();
            map.clear();
        }
        // 为不可以持久化到文件的状态
        log.write(key);
        log.write(value);
        // 放入MemTable
        map.put(key, value);
    }
}
