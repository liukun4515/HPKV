package cn.wangxinshuo.hpkv.key;

import cn.wangxinshuo.hpkv.util.convert.ByteArrayToLong;
import org.junit.Test;

public class TestKey {
    @Test
    public void test01() {
        byte[] a = {1, 2, 3, 4, 5, 6, 7, 8};
        byte[] b = {-1, 2, 3, 4, 5, 6, 7, 8};
        System.out.println(ByteArrayToLong.convert(a));
        System.out.println(ByteArrayToLong.convert(b));
    }
}
