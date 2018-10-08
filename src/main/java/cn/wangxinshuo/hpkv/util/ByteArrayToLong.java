package cn.wangxinshuo.hpkv.util;

public class ByteArrayToLong {

    public static long getKey(byte[] keys) {
        long key = 0;
        for (int i = keys.length - 1; i >= 0; i--) {
            key += keys[i];
        }
        return key;
    }
}
