package cn.wangxinshuo.hpkv.range.key;


import cn.wangxinshuo.hpkv.util.ByteArrayToUnsignedLong;
import com.google.common.primitives.UnsignedLong;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

/**
 * @author wszgr
 */
public class SortedList implements Serializable {
    private LinkedList<UnsignedLong> linkedList;

    public SortedList() {
        linkedList = new LinkedList<UnsignedLong>();
        linkedList.clear();
    }

    public SortedList(byte[] key) {
        this();
        this.add(key);
    }

    public void add(byte[] inKey) {
        linkedList.add(ByteArrayToUnsignedLong.getKey(inKey));
    }

    public LinkedList<UnsignedLong> get(byte[] start, byte[] end) {
        Collections.sort(linkedList);
        UnsignedLong startKey = ByteArrayToUnsignedLong.getKey(start);
        UnsignedLong endKey = ByteArrayToUnsignedLong.getKey(end);
        LinkedList<UnsignedLong> result = new LinkedList<UnsignedLong>();
        for (UnsignedLong key :
                linkedList) {
            if (key.compareTo(startKey) >= 0) {
                if (key.compareTo(endKey) <= 0) {
                    result.addLast(key);
                }
            }
        }
        return result;
    }
}
