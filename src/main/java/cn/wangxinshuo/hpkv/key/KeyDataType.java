package cn.wangxinshuo.hpkv.key;

import cn.wangxinshuo.hpkv.util.ByteArrayToLong;
import cn.wangxinshuo.hpkv.util.interfaces.KeyInterface;
import com.google.common.primitives.UnsignedLong;

/**
 * @author wszgr
 */
public class KeyDataType implements KeyInterface {
    private UnsignedLong key;

    public KeyDataType(UnsignedLong unsignedLong) {
        key = unsignedLong;
    }

    public KeyDataType(byte[] keys) {
        this.key = ByteArrayToLong.getKey(keys);
    }

    public UnsignedLong getKey() {
        return this.key;
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

    /**
     * 比较大小
     *
     * @param keyInterface@return 比较结果
     */
    public int compareTo(KeyInterface keyInterface) {
        return keyInterface.getKey().compareTo(this.key);
    }
}
