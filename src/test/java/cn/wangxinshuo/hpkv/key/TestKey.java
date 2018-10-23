package cn.wangxinshuo.hpkv.key;

import cn.wangxinshuo.hpkv.util.convert.byte2long.ByteArrayToLong;
import org.junit.Test;

public class TestKey {
    @Test
    public void test01() {
        byte[] a = {1, 2, 3};
        byte[] b = {1, 2, 3};
        System.out.println(ByteArrayToLong.convert(a));
        System.out.println(ByteArrayToLong.convert(b));
    }
}
