package com.alibabacloud.polar_race.engine.common;

import cn.wangxinshuo.hpkv.DB.DatabaseResources;
import cn.wangxinshuo.hpkv.DB.IndexResources;
import cn.wangxinshuo.hpkv.conf.Configuration;
import cn.wangxinshuo.hpkv.key.Key;
import cn.wangxinshuo.hpkv.log.Log;
import cn.wangxinshuo.hpkv.skiplist.SkipList;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wszgr
 */
public class EngineRace extends AbstractEngine {
    private Log log;
    private SkipList sl;
    private ConcurrentHashMap<Key, byte[]> map;

    @Override
    public void open(String path) throws EngineException {
        try {
            log = new Log(path);
            sl = new SkipList(IndexResources.getIndexFiles(path),
                    DatabaseResources.getDatabaseFile(path));
            map = log.deserializeFromFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    @Override
    public void write(byte[] key, byte[] value) throws EngineException {
        if (map.size() == Configuration.MaxLogNumber) {
            try {
                for (Key k :
                        map.keySet()) {
                    sl.insert(k.getData(), map.get(k));
                }
                map.clear();
            } catch (IOException e) {
                e.printStackTrace();
                throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
            }
        }
        map.put(new Key(key), value);
        try {
            log.write(key);
            log.write(value);
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    @Override
    public byte[] read(byte[] key) throws EngineException {
        try {
            return sl.select(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.NOT_FOUND, "NOT_FOUND");
        }
    }

    @Override
    public void range(byte[] lower, byte[] upper, AbstractVisitor visitor) throws EngineException {

    }

    @Override
    public void close() {

    }
}
