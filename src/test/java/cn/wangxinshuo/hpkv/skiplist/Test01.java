package cn.wangxinshuo.hpkv.skiplist;

import cn.wangxinshuo.hpkv.conf.Configuration;
import com.alibabacloud.polar_race.engine.common.EngineRace;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import org.junit.Test;

import java.util.Random;

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
        try {
            EngineRace race = new EngineRace();
            race.open("./data");
            for (int i = 0; i < 200; i++) {
                race.write(get(Configuration.KeySize), get(Configuration.ValueSize));
            }
        } catch (EngineException e) {
            e.printStackTrace();
        }
    }
}
