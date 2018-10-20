package cn.wangxinshuo.hpkv.cache;


import cn.wangxinshuo.hpkv.key.Key;

import java.util.HashMap;

/**
 * @author wszgr
 */
public class FileCache {
    private int fileIndex;
    private HashMap<Key, byte[]> map;

    public FileCache(int fileIndex, HashMap<Key, byte[]> map) {
        this.fileIndex = fileIndex;
        this.map = map;
    }

    public void add(Key key, byte[] value) {
        map.put(key, value);
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public HashMap<Key, byte[]> getData() {
        return map;
    }
}
