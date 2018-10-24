package cn.wangxinshuo.hpkv.skiplist;

import cn.wangxinshuo.hpkv.DB.DatabaseResources;
import cn.wangxinshuo.hpkv.DB.IndexResources;
import cn.wangxinshuo.hpkv.conf.Configuration;
import cn.wangxinshuo.hpkv.key.KeyCompare;
import cn.wangxinshuo.hpkv.util.convert.ByteArrayToLong;
import cn.wangxinshuo.hpkv.util.disk.ReadDisk;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * B PLus Tree also need move
 * many nodes when insert node
 *
 * @author wangxinshuo
 */
public class SkipList {
    private RandomAccessFile[] indexFiles;
    private RandomAccessFile databaseFile;

    public SkipList() {
        this.indexFiles = IndexResources.getIndexFiles();
        this.databaseFile = DatabaseResources.getDatabaseFile();
    }

    public SkipList(RandomAccessFile[] indexFiles,
                    RandomAccessFile databaseFile) throws IOException {
        this.indexFiles = indexFiles;
        this.databaseFile = databaseFile;
    }

    public long selectSearch(byte[] key) throws IOException {
        long startOfLevel = 0;
        byte[] readFromFile =
                new byte[Configuration.keySize + Configuration.PointerSize * 2];
        for (RandomAccessFile indexFile : indexFiles) {
            for (long j = startOfLevel; j < indexFile.length(); j += readFromFile.length) {
                ReadDisk.read(indexFile, j, readFromFile.length);
                if (compare(readFromFile, key) == 0) {
                    byte[] position = new byte[Configuration.PointerSize];
                    System.arraycopy(readFromFile, 0, position,
                            Configuration.keySize, position.length);
                    return ByteArrayToLong.convert(position);
                }
                if (compare(readFromFile, key) > 0) {
                    byte[] position = new byte[Configuration.PointerSize];
                    ReadDisk.read(indexFile, j - readFromFile.length, readFromFile.length);
                    System.arraycopy(readFromFile,
                            Configuration.keySize + Configuration.PointerSize,
                            position, Configuration.keySize, position.length);
                    startOfLevel = ByteArrayToLong.convert(position);
                    break;
                }
            }
        }
        return -1;
    }

    public long insertSearch(byte[] key) {
        return 0;
    }

    public void insert(byte[] key, byte[] Value) {

    }

    /**
     * @param a Key and pointer byte array
     * @param b want key
     * @return int -1;0;1
     */
    private int compare(byte[] a, byte[] b) {
        byte[] data = new byte[Configuration.keySize];
        System.arraycopy(a, 0, data, 0, Configuration.keySize);
        return KeyCompare.compare(data, b);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.gc();
    }
}
