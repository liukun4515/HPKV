package cn.wangxinshuo.hpkv.tree;

import org.apache.commons.lang3.SerializationUtils;
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
        System.out.println(Configuration.getNodeNumber());
    }

    @Test
    public void test03() {
        byte[] data = SerializationUtils.serialize(Long.MAX_VALUE);
        System.out.println(data.length);
    }
}
