package cn.wangxinshuo.hpkv.desrialize;

import cn.wangxinshuo.hpkv.resources.DatabaseResources;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import org.apache.commons.lang3.SerializationUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * @author wszgr
 */
public final class DeserializeFromFile {
    public static byte[] deserializeFromFile(DatabaseResources resources,
                                             int index) throws EngineException {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(resources.getReSources(index));
            if (inputStream.available() > 0) {
                byte[] result = new byte[inputStream.available()];
                final int read = inputStream.read(result);
                return result;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static HashMap<byte[], byte[]> deserializeFromFile(DatabaseResources resources,
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
