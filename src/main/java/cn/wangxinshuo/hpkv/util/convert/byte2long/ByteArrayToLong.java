package cn.wangxinshuo.hpkv.util.convert.byte2long;

import java.nio.ByteBuffer;

/**
 * ByteArrayToLong
 */
public class ByteArrayToLong {
    public static long convert(byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(data);
        buffer.flip();//need flip
        return buffer.getLong();
    }
}
