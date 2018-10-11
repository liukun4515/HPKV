package com.alibabacloud.polar_race.engine.common;

import cn.wangxinshuo.hpkv.Select;
import cn.wangxinshuo.hpkv.Store;
import cn.wangxinshuo.hpkv.file.FileResources;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;

import java.io.IOException;

/**
 * @author wszgr
 */
public class EngineRace extends AbstractEngine {
    private FileResources resources;
    private Store store;
    private Select select;

    @Override
    public void open(String path) throws EngineException {
        try {
            resources = new FileResources(path, "rw");
            store = new Store(resources);
            select = new Select(resources);
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    @Override
    public void write(byte[] key, byte[] value) throws EngineException {
        int keyLength = 8, valueLength = 4 * 1024;
        if (key.length != keyLength && value.length != valueLength) {
            throw new EngineException(RetCodeEnum.INVALID_ARGUMENT, "INVALID_ARGUMENT");
        }
        try {
            store.put(key, value);
        } catch (IOException e) {
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR！");
        } catch (OutOfMemoryError e) {
            throw new EngineException(RetCodeEnum.OUT_OF_MEMORY, "OUT_OF_MEMORY！");
        }
    }

    @Override
    public byte[] read(byte[] key) throws EngineException {
        if (key.length != 8) {
            throw new EngineException(RetCodeEnum.INVALID_ARGUMENT, "INVALID_ARGUMENT");
        }
        byte[] value = null;
        try {
            value = select.get(key);
        } catch (IOException e) {
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
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
            resources.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
