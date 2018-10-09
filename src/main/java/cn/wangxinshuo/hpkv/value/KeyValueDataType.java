package cn.wangxinshuo.hpkv.value;

import cn.wangxinshuo.hpkv.util.ByteArrayToLong;
import cn.wangxinshuo.hpkv.util.interfaces.KeyValueInterface;
import com.google.common.primitives.UnsignedLong;

/**
 * @author wszgr
 */
public class KeyValueDataType implements KeyValueInterface {
    private UnsignedLong key;
    private byte[] value;

    public KeyValueDataType(UnsignedLong unsignedLong, byte[] value) {
        this.key = unsignedLong;
        this.value = value;
    }

    public KeyValueDataType(byte[] key, byte[] value) {
        this.key = ByteArrayToLong.getKey(key);
        this.value = value;
    }

    public UnsignedLong getKey() {
        return this.key;
    }

    public byte[] getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return this.key.intValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UnsignedLong) {
            return this.key.equals(obj);
        }
        return false;
    }
}
