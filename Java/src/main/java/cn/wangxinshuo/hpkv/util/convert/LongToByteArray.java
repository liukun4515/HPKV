package cn.wangxinshuo.hpkv.util.convert;

import java.nio.ByteBuffer;

/**
 * LongToByteArray
 */
public class LongToByteArray {
    public static byte[] convert(long data) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(data);
        return buffer.array();
    }
}
