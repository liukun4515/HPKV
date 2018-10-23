package cn.wangxinshuo.hpkv.key;

import cn.wangxinshuo.hpkv.util.convert.byte2long.ByteArrayToLong;

import java.util.Arrays;

/**
 * @author wszgr
 */
public class Key implements Comparable<Key> {
    private byte[] data;

    private Key(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        return data[0] * data[1] * data[3];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Key) {
            return KeyCompare.compare(((Key) obj).getData(), this.data) == 0;
        } else {
            return false;
        }
    }

    public int compareTo(Key o) {
        return KeyCompare.compare(this.data, o.data);
    }
}

/**
 * @author wszgr
 */
class KeyCompare {
    private static boolean isEqual(byte[] a, byte[] b) {
        return Arrays.equals(a, b);
    }

    static int compare(byte[] a, byte[] b) {
        if (isEqual(a, b)) {
            return 0;
        }
        long numA = ByteArrayToLong.convert(a);
        long numB = ByteArrayToLong.convert(b);
        return 0;
    }
}
