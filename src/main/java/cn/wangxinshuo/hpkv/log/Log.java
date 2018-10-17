package cn.wangxinshuo.hpkv.log;

import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

/**
 * 全局只有一个log文件
 *
 * @author wszgr
 */
public class Log {
    private String logPath;
    private String mode;
    private final int KV_NUMBER = 16384;
    private volatile RandomAccessFile randomAccessFile;

    public Log(String path) throws EngineException {
        logPath = path;
        mode = "rws";
        randomAccessFile = null;
        initResources();
    }

    public Log(String path, String mode) throws EngineException {
        this(path);
        this.mode = mode;
    }

    public int getKeyValueNumberInLogFile() {
        return KV_NUMBER;
    }

    /**
     * 只有在新生成log的时候才调用此函数
     */
    private void initResources() throws EngineException {
        String logFileName = logPath + "/log.bin";
        File file = new File(logFileName);
        try {
            if (!file.exists()) {
                final boolean newFile = file.createNewFile();
            }
            this.randomAccessFile = new RandomAccessFile(file, this.mode);
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    public HashMap<byte[], byte[]> deserializeFromFile() throws EngineException {
        HashMap<byte[], byte[]> map =
                new HashMap<byte[], byte[]>(KV_NUMBER);
        try {
            if (randomAccessFile.length() == 0) {
                return map;
            } else {
                // 日志文件中的是byte格式的key-value
                // 所以重启数据库之后需要从日志文件构建MemTable
                randomAccessFile.seek(0);
                // 计算日志文件中有多少对key-value
                int singleKeyValueSize = 8 + 4 * 1024;
                int bytesInFile = (int) randomAccessFile.length();
                byte[] key = new byte[8];
                byte[] value = new byte[4 * 1024];
                for (int i = 0; i < bytesInFile / singleKeyValueSize; i++) {
                    randomAccessFile.read(key);
                    randomAccessFile.read(value);
                    map.put(key, value);
                }
                return map;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    public void eraseLog() throws EngineException {
        File file = new File(logPath + "/log.bin");
        try {
            close();
            final boolean delete = file.delete();
            final boolean newFile = file.createNewFile();
            this.randomAccessFile = new RandomAccessFile(file, mode);
            randomAccessFile.setLength(0);
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    public int getLogFileLength() throws EngineException {
        try {
            return (int) randomAccessFile.length();
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    public RandomAccessFile getRandomAccessFile() {
        return randomAccessFile;
    }

    public void close() throws EngineException {
        try {
            this.randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    @Override
    protected void finalize() throws EngineException {
        close();
    }
}
