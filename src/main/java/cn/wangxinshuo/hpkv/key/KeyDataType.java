package cn.wangxinshuo.hpkv.key;

import cn.wangxinshuo.hpkv.util.ByteArrayToLong;
import cn.wangxinshuo.hpkv.util.interfaces.KeyInterface;

public class KeyDataType implements KeyInterface {
    private long key;

    public KeyDataType(byte[] key) {
        this.key = ByteArrayToLong.getKey(key);
    }

    public long getKey() {
        return this.key;
    }
}
