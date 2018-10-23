package cn.wangxinshuo.hpkv.util.disk;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author 王新硕
 */
public class ReadDisk {
    public static byte[] read(File file,
                              long start, int length) throws IOException {
        return read(new RandomAccessFile(file, "rwd"), start, length);
    }


    private static MappedByteBuffer getBuffer(
            RandomAccessFile file, long start, int length) throws IOException {
        return file.getChannel().map(FileChannel.MapMode.READ_ONLY, start, length);
    }

    public static byte[] read(
            RandomAccessFile file, long start, int length) throws IOException {
        MappedByteBuffer buffer = getBuffer(file, start, length);
        byte[] result;
        if (length > buffer.remaining()) {
            result = new byte[buffer.remaining()];
        } else {
            result = new byte[length];
        }
        buffer.get(result);
        return result;
    }
}
