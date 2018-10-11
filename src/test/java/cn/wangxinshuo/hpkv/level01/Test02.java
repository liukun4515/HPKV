package cn.wangxinshuo.hpkv.level01;

import cn.wangxinshuo.hpkv.file.FileResources;
import com.alibabacloud.polar_race.engine.common.EngineRace;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.google.common.primitives.UnsignedLong;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Test02 {
    private void print(byte[] arr) {
        for (byte var :
                arr) {
            System.out.print(var + ",");
        }
        System.out.println();
    }

    private byte[] getKey() {
        byte[] result = new byte[8];
        new Random().nextBytes(result);
        return result;
    }

    private byte[] getValue() {
        byte[] result = new byte[4 * 1024];
        new Random().nextBytes(result);
        return result;
    }

    @Test
    public void test01() {
        long start = System.nanoTime();
        EngineRace engineRace = new EngineRace();
        try {
            engineRace.open("./data");
            for (int i = 0; i < 10000; i++) {
                System.out.println(i);
                byte[] key = getKey();
                byte[] value = getValue();
                engineRace.write(key, value);
            }
        } catch (EngineException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        System.out.println("used:" + TimeUnit.NANOSECONDS.toMillis(end - start) + " ms");
    }

    @Test
    public void test02() {
        long start = System.nanoTime();
        try {
            FileResources resources = new FileResources("./data");
            for (int i = 0; i < 1024; i++) {
                RandomAccessFile fileResources = resources.getWriteSources(i);
                if (fileResources.length() != 0) {
                    byte[] arr = new byte[(int) fileResources.length()];
                    fileResources.readFully(arr);
                    HashMap<UnsignedLong, byte[]> map = SerializationUtils.deserialize(arr);
                    for (UnsignedLong var :
                            map.keySet()) {
                        print(map.get(var));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("used:" + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + " ms");
    }
}
