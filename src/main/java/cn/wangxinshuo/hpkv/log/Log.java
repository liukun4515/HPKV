package cn.wangxinshuo.hpkv.log;

import cn.wangxinshuo.hpkv.conf.Configuration;
import cn.wangxinshuo.hpkv.key.Key;
import cn.wangxinshuo.hpkv.util.disk.ReadDisk;
import cn.wangxinshuo.hpkv.util.disk.WriteDisk;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局只有一个log文件
 *
 * @author wszgr
 */
public class Log {
    private static final int KV_NUMBER = Configuration.MaxLogNumber;
    private String path;
    private volatile RandomAccessFile randomAccessFile;

    public Log(String path) throws IOException {
        this.path = path;
        randomAccessFile = null;
        initResources();
    }

    public int getKeyValueNumberInLogFile() {
        return KV_NUMBER;
    }

    /**
     * 只有在新生成log的时候才调用此函数
     */
    private void initResources() throws IOException {
        String logFileName = path + "/log.bin";
        File file = new File(logFileName);
        if (!file.exists()) {
            final boolean newFile = file.createNewFile();
        }
        this.randomAccessFile = new RandomAccessFile(file, "rwd");
    }

    public ConcurrentHashMap<Key, byte[]> deserializeFromFile() throws IOException {
        ConcurrentHashMap<Key, byte[]> map =
                new ConcurrentHashMap<Key, byte[]>(KV_NUMBER);
        if (randomAccessFile.length() == 0) {
            return map;
        } else {
            // 日志文件中的是byte格式的key-value
            // 所以重启数据库之后需要从日志文件构建MemTable
            randomAccessFile.seek(0);
            // 计算日志文件中有多少对key-value
            int singleKeyValueSize = Configuration.keySize + Configuration.ValueSize;
            int bytesInFile = (int) randomAccessFile.length();
            byte[] key = new byte[Configuration.keySize];
            byte[] value = new byte[Configuration.ValueSize];
            for (int i = 0; i < bytesInFile / singleKeyValueSize; i++) {
                randomAccessFile.read(key);
                randomAccessFile.read(value);
                map.put(new Key(key), value);
            }
            return map;
        }
    }

    public void eraseLog() throws IOException {
        close();
//        File file = new File(path + "/log.bin");
//        final boolean delete = file.delete();
//        final boolean newFile = file.createNewFile();
//        this.randomAccessFile = new RandomAccessFile(file, "rwd");
        randomAccessFile.setLength(0);
    }

    public int getLogFileLength() throws IOException {
        return (int) randomAccessFile.length();
    }

    private void write(long offset, byte[] data) throws IOException {
        WriteDisk.write(randomAccessFile, offset, data);
    }

    public void write(byte[] data) throws IOException {
        WriteDisk.write(randomAccessFile, randomAccessFile.length(), data);
    }

    public void read(long offset, byte[] data) throws IOException {
        System.arraycopy(ReadDisk.read(
                randomAccessFile, offset, data.length),
                0, data, 0, data.length);
    }

    public void close() throws IOException {
        this.randomAccessFile.close();
    }

    @Override
    protected void finalize() throws IOException {
        close();
    }
}
