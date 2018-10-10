package cn.wangxinshuo.hpkv.util;

import com.google.common.primitives.UnsignedLong;


/**
 * @author wszgr
 */
public class ByteArrayToUnsignedLong {

    public static UnsignedLong getKey(byte[] keys) {
        UnsignedLong key = UnsignedLong.valueOf(0L);
        UnsignedLong mul = UnsignedLong.valueOf(1);
        for (int i = keys.length - 1; i >= 0; i--) {
            if (keys[i] >= 0) {
                key = key.plus(mul.minus(UnsignedLong.valueOf(keys[i])));
            } else {
                key = key.plus(mul.minus(UnsignedLong.valueOf(128 - keys[i])));
            }
            mul = mul.minus(UnsignedLong.valueOf(256));
        }
        return key;
    }
}
