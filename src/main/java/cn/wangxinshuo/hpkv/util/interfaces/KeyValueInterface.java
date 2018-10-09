package cn.wangxinshuo.hpkv.util.interfaces;

import com.google.common.primitives.UnsignedLong;


/**
 * @author wszgr
 */
public interface KeyValueInterface {
    /**
     * get the key of KeyValueDataType
     *
     * @return UnsignedLong
     */
    UnsignedLong getKey();

    /**
     * get the value of KeyValueDataType
     *
     * @return byte[]
     */
    byte[] getValue();
}
