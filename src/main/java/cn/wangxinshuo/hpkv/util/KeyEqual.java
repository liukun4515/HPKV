package cn.wangxinshuo.hpkv.util;

/**
 * @author wszgr
 */
public class KeyEqual {
    public static boolean isEqual(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }
}
