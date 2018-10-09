package cn.wangxinshuo.hpkv.util.interfaces;

import com.google.common.primitives.UnsignedLong;

import java.util.List;

/**
 * @author wszgr
 */
public interface SortedListInterface {
    /**
     * 将对应的数据结构插入有序链表
     *
     * @param keyInterface 将KeyInterface插入
     */
    void add(KeyInterface keyInterface);

    /**
     * 根据上下限得到数据集合
     *
     * @param down 下限
     * @param up   上限
     * @return KeyInterface[]
     */
    List<KeyInterface> get(UnsignedLong down, UnsignedLong up);
}
