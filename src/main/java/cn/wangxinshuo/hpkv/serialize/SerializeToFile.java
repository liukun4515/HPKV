package cn.wangxinshuo.hpkv.serialize;

import cn.wangxinshuo.hpkv.resources.DatabaseResources;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import org.apache.commons.lang3.SerializationUtils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author wszgr
 */
public final class SerializeToFile {
    public static void serializeToFile(DatabaseResources resources,
                                       int index, byte[] serializeArray)
            throws EngineException {
        BufferedOutputStream stream = null;
        try {
            stream = new BufferedOutputStream(
                    new FileOutputStream(
                            resources.getReSources(index)));
            stream.write(serializeArray);
            stream.flush();
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

    public static void serializeToFile(DatabaseResources resources,
                                       int index, HashMap<byte[], byte[]> map)
            throws EngineException {
        byte[] serializeArray = SerializationUtils.serialize(map);
        serializeToFile(resources, index, serializeArray);
    }
}
