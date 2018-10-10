package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.file.CreateFileResources;
import cn.wangxinshuo.hpkv.kv.KVDataType;
import cn.wangxinshuo.hpkv.kv.KVToStream;
import cn.wangxinshuo.hpkv.kv.StreamToKV;
import cn.wangxinshuo.hpkv.util.ByteArrayToUnsignedLong;
import com.google.common.primitives.UnsignedLong;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * @author wszgr
 */
public class Store {
    private CreateFileResources resources;
    private boolean[] haveWrite;

    public Store(CreateFileResources resources) {
        this.resources = resources;
        int numberOfFiles = 1024;
        haveWrite = new boolean[numberOfFiles];
        for (int i = 0; i < numberOfFiles; i++) {
            haveWrite[i] = false;
        }
    }

    public void put(byte[] inKey, byte[] inValue) throws IOException {
        UnsignedLong key = ByteArrayToUnsignedLong.getKey(inKey);
        OutputStream out = resources.getKeyValueResources(key);
        int fileIndex = CreateFileResources.getIndex(key);
        if (haveWrite[fileIndex]) {
            // 之前存在树在文件内
            HashMap<UnsignedLong, byte[]> map =
                    StreamToKV.get(resources.getKeyValueResources(key, false));
            synchronized (this) {
                out.write(new KVToStream(map).getStream(new KVDataType(key, inValue)));
                out.flush();
            }

        } else {
            // 之前没有树在文件内
            // 构造新树  64000000 / 1024 = 62500
            HashMap<UnsignedLong, byte[]> map = new HashMap<UnsignedLong, byte[]>(62500);
            // 写入
            synchronized (this) {
                out.write(new KVToStream(map).getStream(new KVDataType(key, inValue)));
                out.flush();
                // 标记文件里已经存在树
                haveWrite[fileIndex] = true;
            }
        }
    }
}
