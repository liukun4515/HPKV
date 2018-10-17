package cn.wangxinshuo.hpkv.cache;


import java.util.HashMap;

/**
 * @author wszgr
 */
public class FileCache {
    private int fileIndex;
    private HashMap<byte[], byte[]> map;

    public FileCache(int fileIndex, HashMap<byte[], byte[]> map) {
        this.fileIndex = fileIndex;
        this.map = map;
    }

    public void add(byte[] key, byte[] value) {
        map.put(key, value);
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public HashMap<byte[], byte[]> getData() {
        return map;
    }
}
