package cn.wangxinshuo.hpkv.skiplist;

import cn.wangxinshuo.hpkv.conf.Configuration;
import cn.wangxinshuo.hpkv.key.KeyCompare;
import cn.wangxinshuo.hpkv.util.convert.ByteArrayToLong;
import cn.wangxinshuo.hpkv.util.convert.LongToByteArray;
import cn.wangxinshuo.hpkv.util.disk.InsertWrite;
import cn.wangxinshuo.hpkv.util.disk.ReadDisk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * B PLus Tree also need move
 * many nodes when insert node
 *
 * @author wangxinshuo
 */
public class SkipList {
    private RandomAccessFile[] indexFiles;
    private RandomAccessFile databaseFile;

    public SkipList(RandomAccessFile[] indexFiles,
                    RandomAccessFile databaseFile) throws IOException {
        this.indexFiles = indexFiles;
        this.databaseFile = databaseFile;
    }

    private long selectSearch(byte[] key) throws IOException {
        long startOfLevel = 0;
        for (RandomAccessFile indexFile : indexFiles) {
            for (long j = startOfLevel; j < indexFile.length();
                 j += Configuration.KeySize + Configuration.PointerSize * 2) {
                byte[] readFromFile = ReadDisk.read(indexFile, j, Configuration.KeySize);
                if (KeyCompare.compare(readFromFile, key) == 0) {
                    return ByteArrayToLong.convert(readFromFile);
                }
                if (KeyCompare.compare(readFromFile, key) > 0) {
                    byte[] position = ReadDisk.read(indexFile,
                            j - Configuration.PointerSize * 2, Configuration.KeySize);
                    startOfLevel = ByteArrayToLong.convert(position);
                    break;
                }
            }
        }
        return -1;
    }

    private void insertSearch(byte[] key, List<Long> list) throws IOException {
        long startOfLevel = 0;
        int indexSize = Configuration.KeySize + Configuration.PointerSize * 2;
        for (int i = 0; i < indexFiles.length; i++) {
            // get position
            if (i == indexFiles.length - 1) {
                byte[] now = ReadDisk.read(indexFiles[i], startOfLevel, Configuration.KeySize);
                for (long j = startOfLevel; j < indexFiles[i].length(); j += indexSize) {
                    if (startOfLevel + 2 * indexSize <= indexFiles[i].length()) {
                        // read key
                        byte[] after = ReadDisk.read(indexFiles[i],
                                startOfLevel + indexSize, Configuration.KeySize);
                        // compare
                        if (KeyCompare.compare(now, key) > 0) {
                            if (KeyCompare.compare(key, after) < 0) {
                                byte[] result =
                                        ReadDisk.read(indexFiles[i],
                                                startOfLevel + Configuration.KeySize,
                                                Configuration.PointerSize);
                                // add
                                list.add(ByteArrayToLong.convert(result));
                                return;
                            }
                        }
                        now = after;
                    } else {
                        list.add(indexFiles[i].length());
                        return;
                    }
                }
                list.add(0L);
                return;
            }
            // before-half time loop
            for (long j = startOfLevel; j < indexFiles[i].length(); j += indexSize) {
                // read key
                byte[] readFromFile = ReadDisk.read(indexFiles[i], j, Configuration.KeySize);
                // update startOfLevel
                if (KeyCompare.compare(readFromFile, key) > 0) {
                    byte[] position = ReadDisk.read(indexFiles[i],
                            j - Configuration.PointerSize * 2, Configuration.KeySize);
                    startOfLevel = ByteArrayToLong.convert(position);
                    // record history of insert position
                    list.add(startOfLevel);
                    break;
                }
            }
        }
        list.add(0L);
    }

    public void insert(byte[] key, byte[] value) throws IOException {
        LinkedList<Long> list = new LinkedList<>();
        insertSearch(key, list);
        for (int i = Configuration.MaxLevel - getRandomLevel(); i < list.size(); i++) {
            long position = list.get(i);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.write(key);
            stream.write(LongToByteArray.convert(list.getLast()));
            if (i == list.size() - 1) {
                stream.write(LongToByteArray.convert(list.getLast()));
            } else {
                stream.write(LongToByteArray.convert(list.get(i + 1)));
            }
            InsertWrite.insertWrite(indexFiles[i], position, stream.toByteArray());
            System.out.println("在" + position + "写入第" + i + "个Key成功！");
        }
        InsertWrite.insertWrite(databaseFile, list.getLast(), value);
        System.out.println("在" + list.getLast() + "写入Value成功！");
        list.clear();
    }

    public byte[] select(byte[] key) throws Exception {
        long position = selectSearch(key);
        if (position != -1) {
            return ReadDisk.read(databaseFile, selectSearch(key), Configuration.ValueSize);
        }
        throw new Exception("Not found!");
    }

    private int getRandomLevel() {
        int level = 1;
        Random random = new Random();
        while (random.nextInt() % 2 == 1) {
            level++;
        }
        return Configuration.MaxLevel > level ? level : Configuration.MaxLevel;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.gc();
    }
}
