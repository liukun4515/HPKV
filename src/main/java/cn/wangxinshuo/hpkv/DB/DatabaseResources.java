package cn.wangxinshuo.hpkv.DB;

import java.io.RandomAccessFile;

/**
 * @author wangxinshuo
 */
public class DatabaseResources {
    private static RandomAccessFile databaseFile;

    public DatabaseResources() {

    }

    public static RandomAccessFile getDatabaseFile() {
        return databaseFile;
    }
}
