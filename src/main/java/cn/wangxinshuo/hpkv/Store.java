package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.file.FileResources;
import cn.wangxinshuo.hpkv.util.ByteArrayToUnsignedLong;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.google.common.primitives.UnsignedLong;
import org.apache.commons.lang3.SerializationUtils;

import java.io.FileOutputStream;
import java.io.IOException;
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
            FileOutputStream stream = resources.getWriteSources();
            // 进行序列化
            byte[] serializedBytes = SerializationUtils.serialize(map);
            stream.write(serializedBytes);
            // 清空并准备下一次写入文件
            map.clear();
            map.put(key, inValue);
        }
    }
}
