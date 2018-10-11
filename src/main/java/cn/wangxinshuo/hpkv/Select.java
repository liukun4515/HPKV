package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.file.FileResources;
import cn.wangxinshuo.hpkv.util.ByteArrayToUnsignedLong;
import cn.wangxinshuo.hpkv.util.Cache;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import com.google.common.primitives.UnsignedLong;
import org.apache.commons.lang3.SerializationUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author wszgr
 */
public class Select {
    private FileResources resources;
    private ArrayList<Cache> lruList;

    public Select(FileResources resources) {
        this.resources = resources;
        lruList = new ArrayList<Cache>(4);
    }

    public byte[] get(byte[] inKey) throws EngineException {
        UnsignedLong key = ByteArrayToUnsignedLong.getKey(inKey);
        final short numberOfFiles = 256;
        // 从缓存中查找
        for (Cache aLruList : lruList) {
            HashMap<UnsignedLong, byte[]> map =
                    SerializationUtils.deserialize(aLruList.getData());
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }
        // 去文件中查找
        for (int i = 0; i <= numberOfFiles; i++) {
            FileInputStream stream = resources.getReadSources(i);
            try {
                if (stream.available() != 0) {
                    // 读取序列化之后的byte
                    int availableLength = stream.available();
                    byte[] serializedArray = new byte[availableLength];
                    final int read = stream.read(serializedArray);
                    // 反序列化
                    HashMap<UnsignedLong, byte[]> map =
                            SerializationUtils.deserialize(serializedArray);
                    if (map.containsKey(key)) {
                        lruList.add(new Cache(i, serializedArray));
                        return map.get(key);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
            }
        }
        throw new EngineException(RetCodeEnum.NOT_FOUND, "NOT_FOUND");
    }
}
