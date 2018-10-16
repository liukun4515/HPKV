package cn.wangxinshuo.hpkv.level03;

import com.alibabacloud.polar_race.engine.common.AbstractVisitor;
import com.alibabacloud.polar_race.engine.common.EngineRace;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class Test05 {
    @Test
    public void test01() {
        EngineRace engineRace = new EngineRace();
        try {
            engineRace.open("./data");
            byte[] startKey = {-128, -128, -128, -128, -128, -128, -128, -128,};
            byte[] endKey = {127, 127, 127, 127, 127, 127, 127, 127};
            long start = System.nanoTime();
            engineRace.range(startKey, endKey, new AbstractVisitor() {
                @Override
                public void visit(byte[] key, byte[] value) {

                }
            });
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
