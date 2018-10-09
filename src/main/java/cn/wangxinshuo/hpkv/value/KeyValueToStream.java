package cn.wangxinshuo.hpkv.value;

import cn.wangxinshuo.hpkv.util.interfaces.KeyValueInterface;
import com.google.common.primitives.UnsignedLong;
import org.apache.commons.lang3.SerializationUtils;

import java.util.HashMap;

/**
 * @author wszgr
 */
public class KeyValueToStream {
    private HashMap<UnsignedLong, byte[]> map;

    public KeyValueToStream() {
        this.map = new HashMap<UnsignedLong, byte[]>();
    }

    public KeyValueToStream(HashMap<UnsignedLong, byte[]> map) {
        this.map = map;
    }

    public byte[] getStream(KeyValueInterface keyValueInterface) {
        this.map.put(keyValueInterface.getKey(), keyValueInterface.getValue());
        return SerializationUtils.serialize(map);
    }
}

