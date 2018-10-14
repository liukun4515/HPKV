package cn.wangxinshuo.hpkv.cache;

import com.google.common.primitives.UnsignedLong;

import java.util.HashMap;

/**
 * @author wszgr
 */
public class FileCache {
    private int fileIndex;
    private HashMap<UnsignedLong, byte[]> map;

    public FileCache(int fileIndex, HashMap<UnsignedLong, byte[]> map) {
        this.fileIndex = fileIndex;
        this.map = map;
    }

    public void add(UnsignedLong key, byte[] value) {
        map.put(key, value);
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public HashMap<UnsignedLong, byte[]> getData() {
        return map;
    }
}
