package cn.wangxinshuo.hpkv.level01;

import com.google.common.primitives.UnsignedLong;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Random;

public class Test03 {
    private byte[] getValue() {
        byte[] result = new byte[4 * 1024];
        new Random().nextBytes(result);
        return result;
    }

    // 测试部分写入文件是否可行
    @Test
    public void test01() {
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>(4);
        map.put(1, 1);
        map.put(3, 3);
        byte[] before = SerializationUtils.serialize(map);
        map.put(2, 2);
        byte[] after = SerializationUtils.serialize(map);
        System.out.println("Before length:" + before.length);
        System.out.println("After length:" + after.length);
        for (int i = 0; i < before.length; i++) {
            if (before[i] != after[i]) {
                System.out.println("第" + i + "个不一样，Before为：" + before[i] +
                        ",After为：" + after[i]);
            }
        }
    }

    // 测试增长率
    @Test
    public void test02() {
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>(4);
        for (int i = 0; i < 1000; i++) {
            map.put(i, i);
            System.out.println("" + (i + 1) + "," + SerializationUtils.serialize(map).length);
        }
    }

    // 测试实际增长率
    @Test
    public void test03() {
        HashMap<UnsignedLong, byte[]> map = new HashMap<UnsignedLong, byte[]>(1024);
        for (int i = 0; i < 1000; i++) {
            map.put(UnsignedLong.valueOf(i), getValue());
            System.out.println("" + (i + 1) + "," + SerializationUtils.serialize(map).length);
        }
    }
}
