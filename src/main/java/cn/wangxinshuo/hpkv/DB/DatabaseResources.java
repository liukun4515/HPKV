package cn.wangxinshuo.hpkv.DB;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author wangxinshuo
 */
public class DatabaseResources {
    private static RandomAccessFile databaseFile = null;

    public static RandomAccessFile getDatabaseFile(String path) throws IOException {
        if (databaseFile == null) {
            databaseFile = new RandomAccessFile(new File(path + "/data.db"), "rwd");
        }
        return databaseFile;
    }
}
