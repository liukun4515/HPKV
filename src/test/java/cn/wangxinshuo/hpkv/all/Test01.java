package cn.wangxinshuo.hpkv.all;

import com.alibabacloud.polar_race.engine.common.AbstractVisitor;
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
            for (int i = 0; i < 1000000; i++) {
                engineRace.write(getByte(8), getByte(4 * 1024));
            }
            engineRace.close();
            System.out.println("store:" + TimeUnit.NANOSECONDS.toMillis(
                    System.nanoTime() - start) + " ms");
        } catch (EngineException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test02() {
        byte[] start = {0, 0, 0, 0, 0, 0, 0, 0};
        byte[] end = {-128, -128, -128, -128, -128, -128, -128, -128};
        EngineRace engineRace = new EngineRace();
        try {
            engineRace.open("D:\\HPKV");
            long startTime = System.nanoTime();
            engineRace.range(start, end, new AbstractVisitor() {
                @Override
                public void visit(byte[] key, byte[] value) {

                }
            });
            System.out.println("select:" + TimeUnit.NANOSECONDS.toMillis(
                    System.nanoTime() - startTime) + " ms");
        } catch (EngineException e) {
            e.printStackTrace();
        }
    }
}
