package com.alibabacloud.polar_race.engine.common;

import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;

/**
 * @author wszgr
 */
public class EngineRace extends AbstractEngine {
    @Override
    public void open(String path) throws EngineException {

    }

    @Override
    public void write(byte[] key, byte[] value) throws EngineException {

    }

    @Override
    public byte[] read(byte[] key) throws EngineException {
        return null;
    }

    @Override
    public void range(byte[] lower, byte[] upper, AbstractVisitor visitor) throws EngineException {

    }

    @Override
    public void close() {

    }
}
