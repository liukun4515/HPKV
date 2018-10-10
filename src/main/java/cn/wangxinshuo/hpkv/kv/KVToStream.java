package cn.wangxinshuo.hpkv.kv;

import com.google.common.primitives.UnsignedLong;
import org.apache.commons.lang3.SerializationUtils;

import java.util.HashMap;

/**
 * @author wszgr
 */
public class KVToStream {
    private HashMap<UnsignedLong, byte[]> map;

    public KVToStream(HashMap<UnsignedLong, byte[]> map) {
        this.map = map;
    }

    public byte[] getStream() {
        return SerializationUtils.serialize(map);
    }
}

