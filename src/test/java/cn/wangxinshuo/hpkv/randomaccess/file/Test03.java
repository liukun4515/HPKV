package cn.wangxinshuo.hpkv.randomaccess.file;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class Test03 {
    private static byte[] getByte(int len) {
        byte[] result = new byte[len];
        new Random().nextBytes(result);
        return result;
    }

    @Test
    public void test01() {
        try {
            RandomAccessFile file =
                    new RandomAccessFile(
                            new File("D:\\HPKV\\test.test"), "rws");
            byte[] a = {0, 0, 0, 0, 0, 0, 0, 0};
            file.write(a);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
