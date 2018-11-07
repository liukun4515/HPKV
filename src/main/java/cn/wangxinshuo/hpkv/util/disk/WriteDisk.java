package cn.wangxinshuo.hpkv.util.disk;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author 王新硕
 */
public class WriteDisk {
    public static void write(RandomAccessFile file, long start, byte[] data) throws IOException {
        FileChannel channel = file.getChannel();
        channel = channel.position(start);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        channel.write(buffer);
        //channel.force(false);
    }
}
