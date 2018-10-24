package cn.wangxinshuo.hpkv.DB;

import java.io.RandomAccessFile;

/**
 * @author wangxinshuo
 */
public class IndexResources {
    private static RandomAccessFile[] indexFiles;

    public IndexResources() {

    }

    public static RandomAccessFile[] getIndexFiles() {
        return indexFiles;
    }
}
