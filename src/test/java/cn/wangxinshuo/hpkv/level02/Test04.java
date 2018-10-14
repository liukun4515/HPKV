package cn.wangxinshuo.hpkv.level02;

import com.alibabacloud.polar_race.engine.common.EngineRace;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Test04 {
    private static byte[] getKey() {
        byte[] result = new byte[8];
        new Random().nextBytes(result);
        return result;
    }

    private static byte[] getValue() {
        byte[] result = new byte[4 * 1024];
        new Random().nextBytes(result);
        return result;
    }

    private static void print(byte[] arr) {
        for (byte val :
                arr) {
            System.out.print(val);
        }
        System.out.println();
    }

    @Test
    public void test01() {
        int runTimes = 1000000;
        EngineRace engineRace = new EngineRace();
        // 存
        try {
            engineRace.open("./data");
            long start = System.nanoTime();
            for (int i = 0; i < runTimes; i++) {
                byte[] key = getKey();
                byte[] value = getValue();
                engineRace.write(key, value);
            }
            System.out.println("store:" + TimeUnit.NANOSECONDS.toMillis(
                    System.nanoTime() - start) + " ms");
        } catch (EngineException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test02() {

    }
}
