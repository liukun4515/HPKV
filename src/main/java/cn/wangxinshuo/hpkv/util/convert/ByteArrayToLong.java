package cn.wangxinshuo.hpkv.util.convert;

import java.nio.ByteBuffer;

/**
 * ByteArrayToLong
 */
public class ByteArrayToLong {
    public static long convert(byte[] data) {
        if (data.length != 8) {
            System.out.println("Length is not enough!");
        }
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(data);
        buffer.flip();//need flip
        return buffer.getLong();
    }
}
