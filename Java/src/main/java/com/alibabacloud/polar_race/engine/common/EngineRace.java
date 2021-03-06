package com.alibabacloud.polar_race.engine.common;

import cn.wangxinshuo.hpkv.DB.DatabaseResources;
import cn.wangxinshuo.hpkv.DB.IndexResources;
import cn.wangxinshuo.hpkv.cache.KeyCache;
import cn.wangxinshuo.hpkv.conf.Configuration;
import cn.wangxinshuo.hpkv.key.Key;
import cn.wangxinshuo.hpkv.util.convert.ByteArrayToLong;
import cn.wangxinshuo.hpkv.util.convert.LongToByteArray;
import cn.wangxinshuo.hpkv.util.disk.ReadDisk;
import cn.wangxinshuo.hpkv.util.disk.WriteDisk;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author wszgr
 */
public class EngineRace extends AbstractEngine {
    private String path;
    private TreeMap<Key, byte[]> keyTreeMap;

    @Override
    public void open(String path) throws EngineException {
        if (path == null) {
            throw new EngineException(RetCodeEnum.INVALID_ARGUMENT, "INVALID_ARGUMENT");
        } else {
            this.path = path;
            System.out.println(":::::::::::::::::::::" + path + "::::::::::::::::::::::");
        }
        File pathFile = new File(this.path);
        if (!pathFile.exists()) {
            boolean mkdir = pathFile.mkdir();
        }
        try {
            KeyCache.setFile(IndexResources.getIndexFiles(this.path));
            this.keyTreeMap = KeyCache.getMap();
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    @Override
    public void write(byte[] key, byte[] value) throws EngineException {
        try {
            RandomAccessFile databaseFile = DatabaseResources.getDatabaseFile(this.path);
            RandomAccessFile indexFiles = IndexResources.getIndexFiles(this.path);
            // work
            synchronized (this) {
                long valuePosition = databaseFile.length();
                // write value
                WriteDisk.write(databaseFile, valuePosition, value);
                // write key into file
                byte[] byteValuePosition = LongToByteArray.convert(valuePosition);
                WriteDisk.write(indexFiles, indexFiles.length(), key);
                WriteDisk.write(indexFiles, indexFiles.length(), byteValuePosition);
                // write key cache
                keyTreeMap.put(new Key(key), byteValuePosition);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] read(byte[] key) throws EngineException {
        byte[] valuePosition = this.keyTreeMap.get(new Key(key));
        long position = ByteArrayToLong.convert(valuePosition);
        try {
            synchronized (this) {
                return ReadDisk.read(
                        DatabaseResources.getDatabaseFile(this.path),
                        position, Configuration.ValueSize);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    @Override
    public void range(byte[] lower, byte[] upper,
                      AbstractVisitor visitor) throws EngineException {
        final SortedMap<Key, byte[]> keySortedMap =
                keyTreeMap.subMap(new Key(lower), new Key(upper));
        for (Key key :
                keySortedMap.keySet()) {
            try {
                byte[] value = ReadDisk.read(
                        IndexResources.getIndexFiles(this.path),
                        ByteArrayToLong.convert(keySortedMap.get(key)),
                        Configuration.ValueSize);
                visitor.visit(key.getData(), value);
            } catch (IOException e) {
                e.printStackTrace();
                throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
            }
        }
    }

    @Override
    public void close() {
        try {
            DatabaseResources.close();
            IndexResources.close();
            keyTreeMap.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
