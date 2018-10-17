package cn.wangxinshuo.hpkv.Test01;

import com.alibabacloud.polar_race.engine.common.EngineRace;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Test01 {
    private static byte[] getByte(int len) {
        byte[] result = new byte[len];
        new Random().nextBytes(result);
        return result;
    }

    @Test
    public void test01() {
        EngineRace engineRace = new EngineRace();
        try {
            engineRace.open("D:\\HPKV");
            long start = System.nanoTime();
            for (int i = 0; i < 64000000; i++) {
                engineRace.write(getByte(8), getByte(4 * 1024));
            }
            engineRace.close();
            System.out.println("store:" + TimeUnit.NANOSECONDS.toMillis(
                    System.nanoTime() - start) + " ms");
        } catch (EngineException e) {
            e.printStackTrace();
        }
    }
}
