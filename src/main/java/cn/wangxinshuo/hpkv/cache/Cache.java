package cn.wangxinshuo.hpkv.cache;

import com.google.common.primitives.UnsignedLong;

import java.util.HashMap;

/**
 * @author wszgr
 */
public class Cache {
    private int fileIndex;
    private HashMap<UnsignedLong, byte[]> map;

    public Cache(int fileIndex, HashMap<UnsignedLong, byte[]> map) {
        this.fileIndex = fileIndex;
        this.map = map;
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public HashMap<UnsignedLong, byte[]> getData() {
        return map;
    }
}
