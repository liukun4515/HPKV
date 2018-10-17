package cn.wangxinshuo.hpkv.key.compare;

import cn.wangxinshuo.hpkv.util.KeyCompare;
import org.junit.Test;

public class Test02 {
    @Test
    public void test01() {
        byte[] key = {-84, 112, -12, -51, -118, -83, 89, -11};
        byte[] end = {-128, -128, -128, -128, -128, -128, -128, -128};
        byte[] start = {127, 127, 127, 127, 127, 127, 127, 127};
        System.out.println(KeyCompare.compare(start, key));
        System.out.println(KeyCompare.compare(key, end));
    }
}
