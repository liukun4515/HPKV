package cn.wangxinshuo.hpkv.value;

import cn.wangxinshuo.hpkv.util.ByteArrayToLong;
import cn.wangxinshuo.hpkv.util.interfaces.KeyValueInterface;

public class KeyValueDataType implements KeyValueInterface {
    private long key;
    private byte[] value;

    public KeyValueDataType(byte[] key, byte[] value) {
        this.key = ByteArrayToLong.getKey(key);
        this.value = value;
    }

    public long getKey() {
        return this.key;
    }

    public byte[] getValue() {
        return this.value;
    }
}
