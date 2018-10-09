package cn.wangxinshuo.hpkv.key;

import cn.wangxinshuo.hpkv.util.SortedList;
import cn.wangxinshuo.hpkv.util.interfaces.KeyInterface;
import org.apache.commons.lang3.SerializationUtils;

/**
 * @author wszgr
 */
public class KeyToStream {
    private SortedList list;

    public KeyToStream() {
        list = new SortedList();
    }

    public KeyToStream(SortedList list) {
        this.list = list;
    }

    public byte[] getStream(KeyInterface keyInterface) {
        this.list.add(keyInterface);
        return SerializationUtils.serialize(this.list);
    }
}
