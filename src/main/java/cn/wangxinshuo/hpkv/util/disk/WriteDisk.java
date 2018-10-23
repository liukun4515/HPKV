package cn.wangxinshuo.hpkv.util.disk;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author 王新硕
 */
public class WriteDisk {
    public static void write(
            File file, long start, byte[] data) throws IOException {
        write(new RandomAccessFile(file, "rwd"), start, data);
    }

    public static void write(
            RandomAccessFile file, long start, byte[] data) throws IOException {
        MappedByteBuffer buffer =
                file.getChannel().map(FileChannel
                        .MapMode.READ_WRITE, start, data.length);
        buffer.put(data);
        buffer.force();
    }
}
