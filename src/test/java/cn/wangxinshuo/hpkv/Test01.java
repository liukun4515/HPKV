package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.file.CreateFileResources;
import com.google.common.primitives.UnsignedLong;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

public class Test01 {
    @Test
    public void test01() {
        CreateFileResources createFileResources = new CreateFileResources("data");
        OutputStream o1 = createFileResources.geKeyValueResources(UnsignedLong.valueOf(0L));
        OutputStream o2 = createFileResources.getKeyResources(UnsignedLong.valueOf(0L));
        try {
            o1.write(12);
            o1.flush();
            o2.write(12);
            o2.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
