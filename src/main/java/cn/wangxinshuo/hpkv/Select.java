package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.file.FileResources;
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
    private FileResources resources;

    public Select(FileResources resources) {
        this.resources = resources;
    }

    public byte[] get(byte[] inKey) throws IOException, EngineException {
        UnsignedLong key = ByteArrayToUnsignedLong.getKey(inKey);
        InputStream stream = resources.getFileResources(key, false);
        if (stream.available() == 0) {
            throw new EngineException(RetCodeEnum.CORRUPTION, "CORRUPTION！");
        }
        HashMap<UnsignedLong, byte[]> map = SerializationUtils.deserialize(stream);
        byte[] result = map.get(key);
        if (result == null) {
            throw new EngineException(RetCodeEnum.NOT_FOUND, "NOT_FOUND");
        }
        return result;
    }
}
