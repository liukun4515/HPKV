package cn.wangxinshuo.hpkv.file;

import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import com.google.common.primitives.UnsignedLong;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 总数据量大概260GB左右
 * @author wszgr
 */
public class FileResources {
    private String path;
    private short canWriteFileNumber;
    private static final int NUMBER_OF_FILES = 256;
    private FileOutputStream[] outputStreams;
    private FileInputStream[] inputStreams;

    public FileResources(String path) throws IOException {
        this.path = path;
        outputStreams = new FileOutputStream[NUMBER_OF_FILES];
        inputStreams = new FileInputStream[NUMBER_OF_FILES];
        this.createFile();
    }

    private void createFile() throws IOException {
        for (int i = 0; i < NUMBER_OF_FILES; i++) {
            String name = "/KV_" + Integer.toString(i) + ".map";
            File file = new File(path + name);
            final boolean newFile = file.createNewFile();
            outputStreams[i] = new FileOutputStream(file);
            inputStreams[i] = new FileInputStream(file);
        }
    }

    public FileOutputStream getWriteSources() throws EngineException {
        if (canWriteFileNumber < NUMBER_OF_FILES) {
            return outputStreams[canWriteFileNumber++];
        } else {
            throw new EngineException(RetCodeEnum.FULL, "FULL");
        }
    }

    public FileInputStream getReadSources(int index) throws EngineException {
        if (index < NUMBER_OF_FILES) {
            return inputStreams[index];
        } else {
            throw new EngineException(RetCodeEnum.CORRUPTION, "CORRUPTION");
        }
    }

    public static int getIndex(UnsignedLong key) {
        // 2^54 = 18014398509481984L
        UnsignedLong div = UnsignedLong.valueOf(18014398509481984L);
        UnsignedLong mod = UnsignedLong.valueOf(1024L);
        return key.dividedBy(div).mod(mod).intValue();
    }

    public void close() throws IOException {
        for (int i = 0; i < NUMBER_OF_FILES; i++) {
            outputStreams[i].close();
        }
    }

    @Override
    protected void finalize() throws IOException {
        this.close();
    }
}
