package cn.wangxinshuo.hpkv.util.convert.long2byte;

import java.nio.ByteBuffer;

/**
 * LongToByteArray
 */
public class LongToByteArray {
    private final static byte[] nullPointer = convert(-1);

    public static byte[] convert(long data) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(data);
        return buffer.array();
    }

    public static byte[] getNullPointer() {
        return nullPointer;
    }
}
