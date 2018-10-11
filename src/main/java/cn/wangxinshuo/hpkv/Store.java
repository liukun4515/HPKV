package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.file.FileResources;
import cn.wangxinshuo.hpkv.kv.KVToStream;
import cn.wangxinshuo.hpkv.kv.StreamToKV;
import cn.wangxinshuo.hpkv.util.ByteArrayToUnsignedLong;
import com.google.common.primitives.UnsignedLong;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

/**
 * @author wszgr
 */
public class Store {
    private FileResources resources;
    private boolean[] existData;

    public Store(FileResources resources) throws IOException {
        this.resources = resources;
        int numberOfFiles = 1024;
        existData = new boolean[numberOfFiles];
        for (int i = 0; i < numberOfFiles; i++) {
            existData[i] = resources.getFileResources(i).length() != 0;
        }
    }

    /**
     * 2^31 = 2GB
     * 因此是可以使用int类型的
     */
    private HashMap<Integer, byte[]> compare(byte[] before, byte[] after) {
        int length = after.length - before.length + 4;
        HashMap<Integer, byte[]> map = new HashMap<Integer, byte[]>(Math.abs(length));
        for (int i = 0; i < before.length; i++) {
            if (after[i] != before[i]) {
                byte[] val = {after[i]};
                map.put(i, val);
            }
        }
        for (int i = before.length; i < after.length; i++) {
            byte[] val = {after[i]};
            map.put(i, val);
        }
        return map;
    }

    public void put(byte[] inKey, byte[] inValue) throws IOException {
        UnsignedLong key = ByteArrayToUnsignedLong.getKey(inKey);
        int fileIndex = FileResources.getIndex(key);
        RandomAccessFile stream = resources.getFileResources(fileIndex);
        if (existData[fileIndex]) {
            // 之前存在map在文件内
            // 创建一个新数组用来存放序列化后的对象
            byte[] before = new byte[(int) stream.length()];
            // 进行反序列化
            stream.seek(0);
            stream.readFully(before);
            HashMap<UnsignedLong, byte[]> map = StreamToKV.get(before);
            // 加入元素
            map.put(ByteArrayToUnsignedLong.getKey(inKey), inValue);
            // 进行反序列化
            byte[] after = new KVToStream(map).getStream();
            // 进行比较
            HashMap<Integer, byte[]> diff = compare(before, after);
            System.out.println("此次写入：" + diff.size() + " Byte！");
            // 部分写入
            for (int index :
                    diff.keySet()) {
                stream.seek(index);
                stream.write(diff.get(index), 0, 1);
            }
        } else {
            // 之前没有map在文件内
            // 构造新map  64000000 / 1024 = 62500
            HashMap<UnsignedLong, byte[]> map =
                    new HashMap<UnsignedLong, byte[]>(62500);
            // 写入map
            map.put(ByteArrayToUnsignedLong.getKey(inKey), inValue);
            byte[] write = new KVToStream(map).getStream();
            System.out.println("此次写入：" + write.length + " Byte！");
            stream.seek(0);
            stream.write(write);
            // 标记文件里map已经存在
            existData[fileIndex] = true;
        }
    }
}
