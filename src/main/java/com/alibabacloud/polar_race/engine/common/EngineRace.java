package com.alibabacloud.polar_race.engine.common;

import cn.wangxinshuo.hpkv.Select;
import cn.wangxinshuo.hpkv.Store;
import cn.wangxinshuo.hpkv.file.CreateFileResources;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;

import java.io.IOException;

/**
 * @author wszgr
 */
public class EngineRace extends AbstractEngine {
    private CreateFileResources resources;
    private Store store;
    private Select select;

    @Override
    public void open(String path) throws EngineException {
        resources = new CreateFileResources(path);
        store = new Store(resources);
        select = new Select(resources);
    }

    @Override
    public void write(byte[] key, byte[] value) throws EngineException {
        try {
            store.put(key, value);
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO异常！");
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.OUT_OF_MEMORY, "内存不足！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] read(byte[] key) throws EngineException {
        byte[] value = null;
        resources = null;
        return value;
    }

    @Override
    public void range(byte[] lower, byte[] upper, AbstractVisitor visitor) throws EngineException {
    }

    @Override
    public void close() {
    }

}
