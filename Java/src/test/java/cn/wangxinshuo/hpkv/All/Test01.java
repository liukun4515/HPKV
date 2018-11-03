package cn.wangxinshuo.hpkv.All;

import cn.wangxinshuo.hpkv.conf.Configuration;
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
        try {
            long start = System.nanoTime();
            //engineRace.open("D:\\data");
            engineRace.open("./data/");
            for (int i = 0; i < 1000000; i++) {
                byte[] key = get(Configuration.KeySize);
                engineRace.write(key, get(Configuration.ValueSize));
                engineRace.read(key);
            }
            engineRace.close();
            System.out.println(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
        } catch (EngineException e) {
            e.printStackTrace();
        }
    }
}
