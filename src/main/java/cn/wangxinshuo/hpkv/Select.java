package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.file.CreateFileResources;
import cn.wangxinshuo.hpkv.util.ByteArrayToUnsignedLong;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import com.google.common.primitives.UnsignedLong;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
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

    public byte[] get(byte[] inKey) throws IOException, EngineException {
        UnsignedLong key = ByteArrayToUnsignedLong.getKey(inKey);
        InputStream stream = resources.getKeyValueResources(key, false);
        if (stream.available() == 0) {
            throw new EngineException(RetCodeEnum.CORRUPTION, "异常！");
        }
        HashMap<UnsignedLong, byte[]> map = SerializationUtils.deserialize(stream);
        return map.get(key);
    }
}
