package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.file.CreateFileResources;
import cn.wangxinshuo.hpkv.key.KeyDataType;
import cn.wangxinshuo.hpkv.key.KeyToStream;
import cn.wangxinshuo.hpkv.key.StreamToKey;
import cn.wangxinshuo.hpkv.util.ByteArrayToLong;
import cn.wangxinshuo.hpkv.util.SortedList;
import cn.wangxinshuo.hpkv.value.KeyValueDataType;
import cn.wangxinshuo.hpkv.value.KeyValueToStream;
import cn.wangxinshuo.hpkv.value.StreamToKeyValue;
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
        UnsignedLong key = ByteArrayToLong.getKey(inKey);
        OutputStream out1 = resources.getKeyResources(key);
        OutputStream out2 = resources.getKeyValueResources(key);
        int fileIndex = CreateFileResources.getIndex(key);
        if (haveWrite[fileIndex]) {
            // 之前存在树在文件内
            SortedList list =
                    StreamToKey.get(resources.getKeyResources(key, false));
            HashMap<UnsignedLong, byte[]> map =
                    StreamToKeyValue.get(resources.getKeyValueResources(key, false));
            synchronized (this) {
                out1.write(new KeyToStream(list).getStream(new KeyDataType(key)));
                out2.write(new KeyValueToStream(map).getStream(new KeyValueDataType(key, inValue)));
            }

        } else {
            // 之前没有树在文件内
            // 构造新树
            SortedList list = new SortedList();
            // 64000000 / 1024 = 62500
            HashMap<UnsignedLong, byte[]> map = new HashMap<UnsignedLong, byte[]>(62500);
            // 写入
            synchronized (this) {
                out1.write(new KeyToStream(list).getStream(new KeyDataType(key)));
                out2.write(new KeyValueToStream(map).getStream(new KeyValueDataType(key, inValue)));
                // 标记文件里已经存在树
                haveWrite[fileIndex] = true;
            }
        }
    }
}
