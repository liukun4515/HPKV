package cn.wangxinshuo.hpkv.util;

import cn.wangxinshuo.hpkv.util.interfaces.KeyInterface;
import com.google.common.primitives.UnsignedLong;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wszgr
 */
public class SortedList {
    private List<KeyInterface> list;

    public SortedList() {
        list = new LinkedList<KeyInterface>();
    }

    /**
     * 将对应的数据结构插入有序链表
     *
     * @param keyInterface 将KeyInterface插入
     */
    public void add(KeyInterface keyInterface) {
        this.list.add(keyInterface);
        Collections.sort(list);
    }

    /**
     * 根据上下限得到数据集合
     *
     * @param down 下限
     * @param up   上限
     * @return List<KeyInterface>
     */
    public List<KeyInterface> get(UnsignedLong down, UnsignedLong up) {
        List<KeyInterface> list = new LinkedList<KeyInterface>();
        for (KeyInterface k :
                this.list) {
            if (k.getKey().compareTo(down) >= 0 &&
                    k.getKey().compareTo(up) <= 0) {
                list.add(k);
            }
        }
        return list;
    }

}
