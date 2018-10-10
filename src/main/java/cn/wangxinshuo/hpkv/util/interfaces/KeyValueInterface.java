package cn.wangxinshuo.hpkv.util.interfaces;

import com.google.common.primitives.UnsignedLong;


/**
 * @author wszgr
 */
public interface KeyValueInterface {
    /**
     * get the key of KVDataType
     *
     * @return UnsignedLong
     */
    UnsignedLong getKey();

    /**
     * get the kv of KVDataType
     *
     * @return byte[]
     */
    byte[] getValue();
}
