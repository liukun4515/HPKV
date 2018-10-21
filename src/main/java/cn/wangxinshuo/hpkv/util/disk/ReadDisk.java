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
public class ReadDisk {
    public static byte[] read(File file, long start, int length) throws EngineException {
        MappedByteBuffer buffer = getBuffer(file, start, length);
        byte[] result = new byte[length];
        buffer.get(result);
        return result;
    }

    public static byte[] read(RandomAccessFile file, long start, int length)
            throws EngineException {
        MappedByteBuffer buffer = getBuffer(file, start, length);
        byte[] result = new byte[length];
        buffer.get(result);
        return result;
    }

    public static long read(File file, long start, int length, boolean isLong)
            throws EngineException {
        MappedByteBuffer buffer = getBuffer(file, start, length);
        return buffer.getLong();
    }

    private static MappedByteBuffer getBuffer(RandomAccessFile file, long start, int length)
            throws EngineException {
        try {
            return file.getChannel().map(FileChannel.MapMode.READ_ONLY, start, length);
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    private static MappedByteBuffer getBuffer(File file, long start, int length)
            throws EngineException {
        try {
            return getBuffer(new RandomAccessFile(file, "rws"), start, length);
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }
}
