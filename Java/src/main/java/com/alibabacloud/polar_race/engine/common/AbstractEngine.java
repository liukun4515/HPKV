package com.alibabacloud.polar_race.engine.common;

import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;

/**
 * Copyright [2018] Alibaba Cloud All rights reserved
 * <p>
 * Complete the functions below to implement your own engine
 */

public abstract class AbstractEngine {
    /**
     * open Engine
     *
     * @param path the path of engine store data.
     * @throws EngineException
     */
    public abstract void open(String path) throws EngineException;

    /**
     * close Engine
     */
    public abstract void close();

    /**
     * write a key-kv pair into engine
     *
     * @param key
     * @param value
     * @throws EngineException
     */
    public abstract void write(byte[] key, byte[] value) throws EngineException;

    /**
     * read kv of a key
     *
     * @param key
     * @return kv
     * @throws EngineException
     */
    public abstract byte[] read(byte[] key) throws EngineException;

    /**
     * applies the given AbstractVisitor.Visit() function to the result of every key-kv pair in the key range [first, last),
     * in order
     *
     * @param lower   start key
     * @param upper   end key
     * @param visitor is check key-kv pair,you just call visitor.visit(String key, String kv) function in your own engine.
     * @throws EngineException
     */
    public abstract void range(byte[] lower, byte[] upper, AbstractVisitor visitor) throws EngineException;


}
