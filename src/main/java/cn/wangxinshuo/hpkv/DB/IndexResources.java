package cn.wangxinshuo.hpkv.DB;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author wangxinshuo
 */
public class IndexResources {
    private static volatile RandomAccessFile indexFiles = null;

    public static RandomAccessFile getIndexFiles(String path) throws IOException {
        if (indexFiles == null) {
            indexFiles = new RandomAccessFile(new File(path + "/key.db"), "rwd");
        }
        return indexFiles;
    }

    public static void close() throws IOException {
        indexFiles.close();
    }
}
