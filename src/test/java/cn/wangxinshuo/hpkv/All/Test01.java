package cn.wangxinshuo.hpkv.All;

import com.alibabacloud.polar_race.engine.common.EngineRace;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Test01 {
    private byte[] get(int num) {
        byte[] result = new byte[num];
        new Random().nextBytes(result);
        return result;
    }
    @Test
    public void test01() {
        EngineRace engineRace = new EngineRace();
        long start = System.nanoTime();
        try {
            engineRace.open("D:\\data");
            //engineRace.open("./data");
            ConcurrentWrite[] writes = new ConcurrentWrite[64];
            for (int i = 0; i < 64; i++) {
                writes[i] = new ConcurrentWrite();
                writes[i].setEngineRace(engineRace);
                writes[i].run();
            }
            engineRace.close();
        } catch (EngineException e) {
            e.printStackTrace();
        }
        System.out.println(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
    }

    @Test
    public void test02() {
        System.out.println(new Random().nextDouble());
    }

    @Test
    public void test03() {

    }
}
