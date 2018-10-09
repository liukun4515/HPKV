package cn.wangxinshuo.hpkv.key;

import cn.wangxinshuo.hpkv.util.SortedList;
import org.apache.commons.lang3.SerializationUtils;

import java.io.InputStream;

/**
 * @author wszgr
 */
public class StreamToKey {
    public static SortedList get(byte[] val) {
        return (SortedList) SerializationUtils.deserialize(val);
    }

    public static SortedList get(InputStream val) {
        return (SortedList) SerializationUtils.deserialize(val);
    }
}

