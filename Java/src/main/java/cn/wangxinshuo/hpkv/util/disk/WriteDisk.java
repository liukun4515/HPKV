package cn.wangxinshuo.hpkv.util.disk;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author 王新硕
 */
public class WriteDisk {
    public static void write(
            RandomAccessFile file, long start, byte[] data) throws IOException {
//        MappedByteBuffer buffer =
//                file.getChannel().map(FileChannel
//                        .MapMode.READ_WRITE, start, data.length);
//        buffer.put(data);
//        buffer.force();
        file.seek(start);
        file.write(data);
    }
}
