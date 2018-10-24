package cn.wangxinshuo.hpkv.key;

/**
 * @author wszgr
 */
public class Key implements Comparable<Key> {
    private byte[] data;

    public Key(byte[] data) {
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

