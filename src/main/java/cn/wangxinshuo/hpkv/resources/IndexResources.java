package cn.wangxinshuo.hpkv.resources;

import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author wszgr
 */
public class IndexResources {
    private RandomAccessFile randomAccessFile;
    private int indexLength = 8;

    public IndexResources(String path) throws EngineException {
        // 创建KeyFile
        File keyFile = new File(path + "/K.list");
        try {
            final boolean newFile = keyFile.createNewFile();
            randomAccessFile = new RandomAccessFile(keyFile, "rws");
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    private void write(long offset, byte[] data) throws EngineException {
        try {
            randomAccessFile.seek(offset);
            randomAccessFile.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    public void write(byte[] data) throws EngineException {
        try {
            long offset = randomAccessFile.length();
            write(offset, data);
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    private byte[] read(long offset, int length) throws EngineException {
        try {
            if (offset + length > randomAccessFile.length()) {
                throw new EngineException(RetCodeEnum.INVALID_ARGUMENT, "INVALID_ARGUMENT");
            }
            byte[] result = new byte[length];
            randomAccessFile.seek(offset);
            randomAccessFile.read(result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    public byte[] read(long offset) throws EngineException {
        return read(offset, indexLength);
    }

    public long getIndexFileLength() throws EngineException {
        final long length;
        try {
            length = randomAccessFile.length();
            return length;
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    public void setIndexLength(int indexLength) {
        this.indexLength = indexLength;
    }

    public int getIndexLength() {
        return indexLength;
    }

    public void close() throws EngineException {
        try {
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }
}
