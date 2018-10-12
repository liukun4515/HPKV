package com.alibabacloud.polar_race.engine.common;

import cn.wangxinshuo.hpkv.Select;
import cn.wangxinshuo.hpkv.Store;
import cn.wangxinshuo.hpkv.file.FileResources;
import cn.wangxinshuo.hpkv.log.Log;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import com.google.common.primitives.UnsignedLong;

import java.util.HashMap;

/**
 * @author wszgr
 */
public class EngineRace extends AbstractEngine {
    private volatile HashMap<UnsignedLong, byte[]> map;
    private FileResources resources;
    private Store store;
    private Select select;
    private Log log;

    @Override
    public void open(String path) throws EngineException {
        resources = new FileResources(path);
        log = new Log(path);
        map = log.deserializeFromFile();
        store = new Store(resources, log, map);
        select = new Select(resources, log, map);
    }

    @Override
    public void write(byte[] key, byte[] value) throws EngineException {
        int keyLength = 8, valueLength = 4 * 1024;
        if (key.length != keyLength && value.length != valueLength) {
            throw new EngineException(RetCodeEnum.INVALID_ARGUMENT, "INVALID_ARGUMENT");
        }
        try {
            store.put(key, value);
        } catch (OutOfMemoryError e) {
            throw new EngineException(RetCodeEnum.OUT_OF_MEMORY, "OUT_OF_MEMORY！");
        }
    }

    @Override
    public byte[] read(byte[] key) throws EngineException {
        // 开始查询相关工作
        if (key.length != 8) {
            throw new EngineException(RetCodeEnum.INVALID_ARGUMENT, "INVALID_ARGUMENT");
        }
        byte[] value = null;
        value = select.get(key);
        if (value == null) {
            throw new EngineException(RetCodeEnum.NOT_FOUND, "NOT_FOUND");
        }
        return value;
    }

    @Override
    public void range(byte[] lower, byte[] upper, AbstractVisitor visitor) throws EngineException {

    }

    @Override
    public void close() {
        try {
            log.close();
            map.clear();
            resources.close();
        } catch (EngineException e) {
            e.printStackTrace();
        }
    }
}
