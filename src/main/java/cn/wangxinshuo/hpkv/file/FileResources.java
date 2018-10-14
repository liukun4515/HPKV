package cn.wangxinshuo.hpkv.file;

import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;

import java.io.File;
import java.io.IOException;

/**
 * 总数据量大概260GB左右
 * @author wszgr
 */
public class FileResources {
    private String path;
    private static final int NUMBER_OF_FILES = 128;
    private File[] files;
    private File keyFile;

    public int getNumberOfFiles() {
        return NUMBER_OF_FILES;
    }

    public FileResources(String path) throws EngineException {
        this.path = path;
        files = new File[NUMBER_OF_FILES];
        this.createFile();
    }

    private void createFile() throws EngineException {
        try {
            // 创建KeyValueFile
            for (int i = 0; i < NUMBER_OF_FILES; i++) {
                String name = "/KV_" + Integer.toString(i) + ".map";
                files[i] = new File(path + name);
                final boolean newFile = files[i].createNewFile();
            }
            // 创建KeyFile
            keyFile = new File(path + "/K.list");
            final boolean newFile = keyFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    public File getWriteSources(int index) {
        return files[index];
    }

    public File getReadSources(int index) throws EngineException {
        if (index < NUMBER_OF_FILES) {
            return files[index];
        } else {
            throw new EngineException(RetCodeEnum.INVALID_ARGUMENT,
                    "INVALID_ARGUMENT");
        }
    }

    public File getKeyFile() {
        return keyFile;
    }

    public void close() {
    }
}
