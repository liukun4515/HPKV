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
public class Store {
    private FileResources resources;
    private HashMap<UnsignedLong, byte[]> map;

    public Store(FileResources resources) throws IOException {
        this.resources = resources;
        map = new HashMap<UnsignedLong, byte[]>(4096);
    }

    public void put(byte[] inKey, byte[] inValue) throws IOException, EngineException {
        UnsignedLong key = ByteArrayToUnsignedLong.getKey(inKey);
        int maxContainNumbers = 4000;
        if (map.size() < maxContainNumbers) {
            map.put(key, inValue);
        } else {
            RandomAccessFile stream = resources.getWriteSources();
            // 进行序列化
            byte[] serializedBytes = SerializationUtils.serialize(map);
            stream.seek(0);
            stream.write(serializedBytes);
            // 清空并准备下一次写入文件
            map.clear();
            map.put(key, inValue);
        }
    }

    public void storeIncompleteMap() throws EngineException {
        RandomAccessFile stream = resources.getWriteSources();
        // 进行序列化
        byte[] serializedBytes = SerializationUtils.serialize(map);
        try {
            stream.seek(0);
            stream.write(serializedBytes);
        } catch (IOException e) {
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }
}
