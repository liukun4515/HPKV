package cn.wangxinshuo.hpkv.kv;

import org.apache.commons.lang3.SerializationUtils;

import java.io.InputStream;
import java.util.HashMap;

/**
 * @author wszgr
 */
public class StreamToKV {
    public static HashMap get(byte[] val) {
        return (HashMap) SerializationUtils.deserialize(val);
    }

    public static HashMap get(InputStream val) {
        /*
         * 流在此可能被关闭
         * */
        return (HashMap) SerializationUtils.deserialize(val);
    }
}
