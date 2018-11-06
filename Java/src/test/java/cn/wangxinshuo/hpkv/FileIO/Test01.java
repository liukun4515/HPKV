package cn.wangxinshuo.hpkv.FileIO;

import cn.wangxinshuo.hpkv.util.disk.ReadDisk;
import cn.wangxinshuo.hpkv.util.disk.WriteDisk;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Test01 {
    private byte[] get(int num) {
        byte[] result = new byte[num];
        new Random().nextBytes(result);
        return result;
    }

    @Test
    public void test01() {
        try (RandomAccessFile file = new RandomAccessFile(new File("./data/x.txt"), "rws")) {
            byte[] data = get(4096);
            long start = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                WriteDisk.write(file, 0, data);
            }
            System.out.println("store:" + TimeUnit.NANOSECONDS.toMillis(
                    System.nanoTime() - start) + " ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test02() {
        byte[] data = {1, 2, 3, 4, 5, 6, 7, 8};
        try {
            RandomAccessFile file =
                    new RandomAccessFile(
                            new File("./data/test.txt"), "rwd");
            WriteDisk.write(file, 0, data);
            WriteDisk.write(file, data.length, data);
            byte[] result = ReadDisk.read(file, 0, 16);
            for (byte a :
                    result) {
                System.out.println(a);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
