package cn.wangxinshuo.hpkv.util;

import java.util.Arrays;

/**
 * @author wszgr
 */
public class KeyCompare {
    public static boolean isEqual(byte[] a, byte[] b) {
        return Arrays.equals(a, b);
    }

    public static int compare(byte[] a, byte[] b) {
        if (isEqual(a, b)) {
            return 0;
        }
        if (a.length > b.length) {
            return -1;
        } else if (b.length > a.length) {
            return 1;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] < 0 && b[i] < 0) {
                if (Math.abs(a[i]) > Math.abs(b[i])) {
                    return -1;
                }
                if (Math.abs(a[i]) < Math.abs(b[i])) {
                    return 1;
                }
            }
            if (a[i] < 0 && b[i] >= 0) {
                return -1;
            }
            if (a[i] >= 0 && b[i] < 0) {
                return 1;
            }
            if (a[i] >= 0 && b[i] >= 0) {
                if (a[i] > b[i]) {
                    return -1;
                }
                if (a[i] < b[i]) {
                    return 1;
                }
            }
        }
        return 0;
    }
}
