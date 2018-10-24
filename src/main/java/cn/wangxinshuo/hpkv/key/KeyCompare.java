package cn.wangxinshuo.hpkv.key;

import cn.wangxinshuo.hpkv.util.convert.ByteArrayToLong;

import java.util.Arrays;

/**
 * @author wszgr
 */
public class KeyCompare {
    private static boolean isEqual(byte[] a, byte[] b) {
        return Arrays.equals(a, b);
    }

    public static int compare(byte[] a, byte[] b) {
        if (isEqual(a, b)) {
            return 0;
        }
        long numA = ByteArrayToLong.convert(a);
        long numB = ByteArrayToLong.convert(b);
        if (numA < 0 && numB < 0) {
            return numA < numB ? -1 : 1;
        }
        if (numA < 0) {
            return -1;
        }
        if (numA > 0 && numB < 0) {
            return 1;
        }
        return numA > numB ? -1 : 1;
    }
}
