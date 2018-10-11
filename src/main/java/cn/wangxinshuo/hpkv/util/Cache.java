package cn.wangxinshuo.hpkv.util;

/**
 * @author wszgr
 */
public class Cache {
    private int fileIndex;
    private byte[] data;

    public Cache(int fileIndex, byte[] data) {
        this.fileIndex = fileIndex;
        this.data = data;
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public byte[] getData() {
        return data;
    }
}
