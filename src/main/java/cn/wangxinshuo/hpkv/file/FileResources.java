package cn.wangxinshuo.hpkv.file;

import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import com.google.common.primitives.UnsignedLong;
import org.apache.commons.lang3.SerializationUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;

/**
 * 总数据量大概260GB左右
 * @author wszgr
 */
public class FileResources {
    private String path;
    private static final int NUMBER_OF_FILES = 256;
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

    /**
     * 此函数不符合设计理念，但实用
     */
    public LinkedList<UnsignedLong> getSortedList() throws EngineException {
        try {
            BufferedInputStream stream =
                    new BufferedInputStream(
                            new FileInputStream(
                                    this.getKeyFile()));
            if (stream.available() > 0) {
                return SerializationUtils.deserialize(stream);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.fillInStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }

    }

    public void close() {
    }
}
