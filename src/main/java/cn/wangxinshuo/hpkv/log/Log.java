package cn.wangxinshuo.hpkv.log;

import cn.wangxinshuo.hpkv.conf.Configuration;
import cn.wangxinshuo.hpkv.key.Key;
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
    private final int KV_NUMBER = Configuration.MaxLogNumber;
    private RandomAccessFile randomAccessFile;
    private String path;

    public Log(String path) throws IOException {
        this.path = path;
        randomAccessFile = null;
        initResources();
    }

    /**
     * 只有在新生成log的时候才调用此函数
     */
    private void initResources() throws IOException {
        String logFileName = path + "/data.log";
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
            int singleKeyValueSize = Configuration.KeySize + Configuration.ValueSize;
            int bytesInFile = (int) randomAccessFile.length();
            byte[] key = new byte[Configuration.KeySize];
            byte[] value = new byte[Configuration.ValueSize];
            for (int i = 0; i < bytesInFile / singleKeyValueSize; i++) {
                randomAccessFile.read(key);
                randomAccessFile.read(value);
                map.put(new Key(key), value);
            }
            return map;
        }
    }

    public void delete() throws IOException {
        close();
        File file = new File(path + "/data.log");
        final boolean delete = file.delete();
        final boolean newFile = file.createNewFile();
        this.randomAccessFile = new RandomAccessFile(file, "rwd");
        randomAccessFile.setLength(0);
    }

    public void write(byte[] data) throws IOException {
        WriteDisk.write(randomAccessFile, randomAccessFile.length(), data);
    }

    public void close() throws IOException {
        this.randomAccessFile.close();
    }

    @Override
    protected void finalize() throws IOException {
        close();
    }
}
