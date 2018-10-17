package cn.wangxinshuo.hpkv.desrialize;

import cn.wangxinshuo.hpkv.key.Key;
import cn.wangxinshuo.hpkv.resources.DatabaseResources;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import org.apache.commons.lang3.SerializationUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author wszgr
 */
public final class DeserializeFromFile {
    private static byte[] deserializeFromFile(DatabaseResources resources,
                                              int index) throws EngineException {
        BufferedInputStream stream = null;
        try {
            stream = new BufferedInputStream(new FileInputStream(
                    resources.getResources(index)));
            if (stream.available() > 0) {
                byte[] result = new byte[stream.available()];
                final int read = stream.read(result);
                return result;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static HashMap<Key, byte[]> deserializeFromFile(DatabaseResources resources,
                                                           int index, boolean hashMap)
            throws EngineException {
        byte[] deserializeArray = deserializeFromFile(resources, index);
        if (deserializeArray != null) {
            return SerializationUtils.deserialize(deserializeArray);
        } else {
            return null;
        }
    }
}
