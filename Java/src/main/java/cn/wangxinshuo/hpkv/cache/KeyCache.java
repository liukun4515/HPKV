package cn.wangxinshuo.hpkv.cache;

import cn.wangxinshuo.hpkv.conf.Configuration;
import cn.wangxinshuo.hpkv.key.Key;
import cn.wangxinshuo.hpkv.util.disk.ReadDisk;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.TreeMap;

/**
 *
 */
public class KeyCache {
    private static RandomAccessFile file;
    // key  :  key
    //byte[]: value position
    private static TreeMap<Key, byte[]> map;

    public static void setFile(RandomAccessFile file) {
        KeyCache.file = file;
        map = null;
    }

    public static TreeMap<Key, byte[]> getMap() throws IOException {
        if (map == null) {
            if (file.length() <= 0) {
                return new TreeMap<>(Key::compareTo);
            } else {
                map = new TreeMap<>(Key::compareTo);
                for (long i = 0; i < file.length(); i += Configuration.KeySize + Configuration.PointerSize) {
                    byte[] key = ReadDisk.read(file, i, Configuration.KeySize);
                    byte[] valuePosition = ReadDisk.read(file, i + key.length, Configuration.PointerSize);
                    map.put(new Key(key), valuePosition);
                }
                return map;
            }
        } else {
            return map;
        }
    }
}
