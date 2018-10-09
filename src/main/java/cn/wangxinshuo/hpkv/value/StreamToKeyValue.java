package cn.wangxinshuo.hpkv.value;

import org.apache.commons.lang3.SerializationUtils;

import java.io.InputStream;
import java.util.HashMap;

/**
 * @author wszgr
 */
public class StreamToKeyValue {
    public static HashMap get(byte[] val) {
        return (HashMap) SerializationUtils.deserialize(val);
    }

    public static HashMap get(InputStream val) {
        return (HashMap) SerializationUtils.deserialize(val);
    }
}
