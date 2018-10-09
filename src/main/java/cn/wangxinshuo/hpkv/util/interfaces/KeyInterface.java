package cn.wangxinshuo.hpkv.util.interfaces;

import com.google.common.primitives.UnsignedLong;

/**
 * @author wszgr
 */
public interface KeyInterface extends Comparable<KeyInterface> {
    /**
     * get the key of KeyDataType
     *
     * @return UnsignedLong
     */
    UnsignedLong getKey();

    /**
     * 比较大小
     *
     * @param keyInterface 待比较的对象
     * @return 比较结果
     */
    int compareTo(@SuppressWarnings("NullableProblems") KeyInterface keyInterface);
}
