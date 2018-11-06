package cn.wangxinshuo.hpkv.util.disk;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author 王新硕
 */
public class ReadDisk {
    public static byte[] read(RandomAccessFile file, long start, int length) throws IOException {
        FileChannel channel = file.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(length);
        channel.read(buffer);
        return buffer.array();
    }
}
