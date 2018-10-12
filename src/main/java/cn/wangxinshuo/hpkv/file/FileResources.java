package cn.wangxinshuo.hpkv.file;

import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 总数据量大概260GB左右
 * @author wszgr
 */
public class FileResources {
    private String path;
    private String mode;
    private short canWriteFileNumber;
    private static final int NUMBER_OF_FILES = 256;
    private RandomAccessFile[] streams;

    public FileResources(String path) throws IOException {
        this.path = path;
        mode = "rws";
        streams = new RandomAccessFile[NUMBER_OF_FILES];
        this.createFile();
    }

    public FileResources(String path, String mode) throws IOException {
        this(path);
        this.mode = mode;
    }

    private void createFile() throws IOException {
        for (int i = 0; i < NUMBER_OF_FILES; i++) {
            String name = "/KV_" + Integer.toString(i) + ".map";
            File file = new File(path + name);
            final boolean newFile = file.createNewFile();
            streams[i] = new RandomAccessFile(file, mode);
        }
    }

    public RandomAccessFile getWriteSources() throws EngineException {
        if (canWriteFileNumber < NUMBER_OF_FILES) {
            return streams[canWriteFileNumber++];
        } else {
            throw new EngineException(RetCodeEnum.FULL, "FULL");
        }
    }

    public RandomAccessFile getReadSources(int index) throws EngineException {
        if (index < NUMBER_OF_FILES) {
            return streams[index];
        } else {
            throw new EngineException(RetCodeEnum.CORRUPTION, "CORRUPTION");
        }
    }

    public void close() throws IOException {
        for (int i = 0; i < NUMBER_OF_FILES; i++) {
            streams[i].close();
        }
    }

    @Override
    protected void finalize() throws IOException {
        this.close();
    }
}
