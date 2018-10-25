package cn.wangxinshuo.hpkv.DB;

import cn.wangxinshuo.hpkv.conf.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author wangxinshuo
 */
public class IndexResources {
    private static RandomAccessFile[] indexFiles = null;

    public static RandomAccessFile[] getIndexFiles(String path) throws IOException {
        if (indexFiles == null) {
            indexFiles = new RandomAccessFile[Configuration.MaxLevel];
            for (int i = 0; i < indexFiles.length; i++) {
                indexFiles[i] =
                        new RandomAccessFile(
                                new File(path + "/" +
                                        Integer.toString(i) +
                                        "_level.index"), "rwd");
            }
        }
        return indexFiles;
    }
}
