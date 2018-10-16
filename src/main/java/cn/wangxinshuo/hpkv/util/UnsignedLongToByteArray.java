package cn.wangxinshuo.hpkv.util;

import com.google.common.primitives.UnsignedLong;

/**
 * @author wszgr
 */
public class UnsignedLongToByteArray {
    public static byte[] getKey(UnsignedLong key) {
        byte[] result = new byte[8];
        UnsignedLong c = UnsignedLong.valueOf(255);
        for (int i = 0; i < result.length; i++) {
            int temp = key.mod(c).intValue();
            if (temp > 127) {
                result[i] = (byte) (temp - 255);
            } else {
                result[i] = (byte) temp;
            }
            c = c.times(UnsignedLong.valueOf(256));
        }
        return result;
    }
}
