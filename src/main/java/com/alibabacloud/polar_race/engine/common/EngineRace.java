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
        if (path.endsWith("/")) {
            this.path = path.substring(0, path.length() - 1);
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
    public synchronized void write(byte[] key, byte[] value) throws EngineException {
        try {
            RandomAccessFile databaseFile = DatabaseResources.getDatabaseFile(this.path);
            RandomAccessFile indexFiles = IndexResources.getIndexFiles(this.path);
            // work
            long valuePosition = databaseFile.length();
            // write value
            WriteDisk.write(databaseFile, valuePosition, value);
            // write key into file
            byte[] byteValuePosition = LongToByteArray.convert(valuePosition);
            WriteDisk.write(indexFiles, indexFiles.length(), key);
            WriteDisk.write(indexFiles, indexFiles.length(), byteValuePosition);
            // write key cache
            keyTreeMap.put(new Key(key), byteValuePosition);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized byte[] read(byte[] key) throws EngineException {
        byte[] valuePosition = this.keyTreeMap.get(new Key(key));
        long position = ByteArrayToLong.convert(valuePosition);
        try {
            return ReadDisk.read(
                    DatabaseResources.getDatabaseFile(this.path),
                    position, Configuration.ValueSize);
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    @Override
    public synchronized void range(byte[] lower, byte[] upper,
                                   AbstractVisitor visitor) throws EngineException {
        final SortedMap<Key, byte[]> keySortedMap =
                keyTreeMap.subMap(new Key(lower), new Key(upper));
        for (Key key :
                keySortedMap.keySet()) {
            visitor.visit(key.getData(), keySortedMap.get(key));
        }
    }

    @Override
    public void close() {
        try {
            DatabaseResources.close();
            IndexResources.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
