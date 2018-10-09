package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.file.CreateFileResources;
import cn.wangxinshuo.hpkv.util.ByteArrayToLong;
import com.google.common.primitives.UnsignedLong;
import org.apache.commons.lang3.SerializationUtils;

import java.io.InputStream;
import java.util.HashMap;

/**
 * @author wszgr
 */
public class Select {
    private CreateFileResources resources;
    private final int NUMBER_OF_FILES = 1024;

    public Select(CreateFileResources resources) {
        this.resources = resources;
    }

    public byte[] get(byte[] inKey) {
        UnsignedLong key = ByteArrayToLong.getKey(inKey);
        InputStream stream = resources.getKeyResources(key, false);
        HashMap<UnsignedLong, byte[]> map = SerializationUtils.deserialize(stream);
        return map.get(key);
    }
}
