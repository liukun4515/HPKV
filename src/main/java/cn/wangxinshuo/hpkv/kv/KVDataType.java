package cn.wangxinshuo.hpkv.kv;

import cn.wangxinshuo.hpkv.util.ByteArrayToUnsignedLong;
import cn.wangxinshuo.hpkv.util.interfaces.KeyValueInterface;
import com.google.common.primitives.UnsignedLong;

/**
 * @author wszgr
 */
public class KVDataType implements KeyValueInterface {
    private UnsignedLong key;
    private byte[] value;

    public KVDataType(UnsignedLong unsignedLong, byte[] value) {
        this.key = unsignedLong;
        this.value = value;
    }

    public KVDataType(byte[] key, byte[] value) {
        this.key = ByteArrayToUnsignedLong.getKey(key);
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
