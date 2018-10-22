package cn.wangxinshuo.hpkv.tree;

import org.junit.Test;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test01 {
    @Test
    public void test01() {
        File file = new File("");
        try {
            DataOutputStream stream = new DataOutputStream(new FileOutputStream(file));
            stream.writeLong(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test02() {
        System.out.println(Configuration.getNumber());
    }
}
