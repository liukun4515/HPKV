package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.file.FileResources;
import cn.wangxinshuo.hpkv.util.ByteArrayToUnsignedLong;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import com.google.common.primitives.UnsignedLong;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
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
        RandomAccessFile stream = resources.getFileResources(FileResources.getIndex(key));
        if (stream.length() == 0) {
            throw new EngineException(RetCodeEnum.NOT_FOUND, "NOT_FOUND！");
        }
        byte[] streamObject = new byte[(int) stream.length()];
        stream.seek(0);
        stream.readFully(streamObject);
        HashMap<UnsignedLong, byte[]> map = SerializationUtils.deserialize(streamObject);
        byte[] result = map.get(key);
        if (result == null) {
            throw new EngineException(RetCodeEnum.NOT_FOUND, "NOT_FOUND");
        }
        return result;
    }
}
