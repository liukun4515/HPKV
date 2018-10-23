package cn.wangxinshuo.hpkv.util.disk;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * InsertWrite
 */
public class InsertWrite {
    public static void insertWrite(
            File file, long start, byte[] data) throws IOException {
        insertWrite(new RandomAccessFile(file, "rwd"), start, data);
    }

    public static void insertWrite(
            RandomAccessFile file, long start, byte[] data) throws IOException {
        int length = data.length > 8192 ? data.length : 8192;
        byte[] buffer = ReadDisk.read(file, start, length);
        byte[] temp;
        WriteDisk.write(file, start, data);
        for (long i = start + length; i < file.length(); i += length) {
            temp = ReadDisk.read(file, i, length);
            WriteDisk.write(file, i - length + data.length, buffer);
            System.arraycopy(buffer, 0, temp, 0, temp.length);
        }
        System.gc();
    }
}
