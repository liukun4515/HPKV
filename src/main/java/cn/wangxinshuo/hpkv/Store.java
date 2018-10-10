package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.file.FileResources;
import cn.wangxinshuo.hpkv.kv.KVToStream;
import cn.wangxinshuo.hpkv.kv.StreamToKV;
import cn.wangxinshuo.hpkv.util.ByteArrayToUnsignedLong;
import com.google.common.primitives.UnsignedLong;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * @author wszgr
 */
public class Store {
    private FileResources resources;
    private boolean[] existData;

    public Store(FileResources resources) {
        this.resources = resources;
        int numberOfFiles = 1024;
        existData = new boolean[numberOfFiles];
        for (int i = 0; i < numberOfFiles; i++) {
            existData[i] = false;
        }
    }

    public void put(byte[] inKey, byte[] inValue) throws IOException {
        UnsignedLong key = ByteArrayToUnsignedLong.getKey(inKey);
        OutputStream out = resources.getFileResources(key);
        int fileIndex = FileResources.getIndex(key);
        if (existData[fileIndex]) {
            // 之前存在map在文件内
            InputStream stream = resources.getFileResources(key, false);
            // 创建一个新数组用来存放序列化后的对象
            byte[] objArray = new byte[stream.available()];
            final int read = stream.read(objArray);
            // 进行反序列化
            HashMap<UnsignedLong, byte[]> map = StreamToKV.get(objArray);
            map.put(ByteArrayToUnsignedLong.getKey(inKey), inValue);
            byte[] write = new KVToStream(map).getStream();
            out.write(write);
            out.flush();
        } else {
            // 之前没有map在文件内
            // 构造新map  64000000 / 1024 = 62500
            HashMap<UnsignedLong, byte[]> map =
                    new HashMap<UnsignedLong, byte[]>(62500);
            // 写入map
            map.put(ByteArrayToUnsignedLong.getKey(inKey), inValue);
            byte[] write = new KVToStream(map).getStream();
            out.write(write);
            out.flush();
            // 标记文件里map已经存在
            existData[fileIndex] = true;
        }
    }
}
