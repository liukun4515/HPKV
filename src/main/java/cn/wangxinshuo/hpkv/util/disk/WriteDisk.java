package cn.wangxinshuo.hpkv.util.disk;

import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author 王新硕
 */
public class WriteDisk {
    public static void write(File file, long start, byte[] data)
            throws EngineException {
        try {
            write(new RandomAccessFile(file, "rws"), start, data);
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    public static void write(File file, long start, long data) {
        write(file, start, Long.valueOf(data).byteValue());
    }

    public static void write(RandomAccessFile file, long start, byte[] data)
            throws EngineException {
        try {
            MappedByteBuffer buffer =
                    file.getChannel().map(FileChannel
                            .MapMode.READ_WRITE, start, data.length);
            buffer.put(data);
            buffer.force();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
