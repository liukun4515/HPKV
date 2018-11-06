package cn.wangxinshuo.hpkv.All;

import cn.wangxinshuo.hpkv.conf.Configuration;
import com.alibabacloud.polar_race.engine.common.EngineRace;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;

import java.util.Random;

/**
 *
 */
public class ConcurrentWrite implements Runnable {
    private EngineRace engineRace;

    public void setEngineRace(EngineRace engineRace) {
        this.engineRace = engineRace;
    }

    private byte[] get(int num) {
        byte[] result = new byte[num];
        new Random().nextBytes(result);
        return result;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 1000000; i++) {
                byte[] key = get(Configuration.KeySize);
                engineRace.write(key, get(Configuration.ValueSize));
            }
        } catch (EngineException e) {
            e.printStackTrace();
        }
    }
}
