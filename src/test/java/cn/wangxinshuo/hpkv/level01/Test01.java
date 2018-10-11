package cn.wangxinshuo.hpkv.level01;

import cn.wangxinshuo.hpkv.file.FileResources;
import com.alibabacloud.polar_race.engine.common.EngineRace;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.google.common.primitives.UnsignedLong;
import org.junit.Test;

import java.util.Random;

public class Test01 {
    @Test
    public void test01() {
//        FileResources createFileResources = new FileResources("data");
//        OutputStream o2 = createFileResources.getKeyResources(UnsignedLong.valueOf(0L));
//        try {
//            o2.write(12);
//            o2.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void test02() {
        EngineRace engineRace = new EngineRace();
        try {
            engineRace.open("./data");
            byte[] key = {12, 12, 12, 12, 12, 12, 12, 12};
            byte[] value = {12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12,
                    12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 13};
            byte[] v = {12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12,
                    12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12};
            engineRace.write(key, value);
            engineRace.write(key, v);
            value = engineRace.read(key);
            for (byte val :
                    value) {
                System.out.print(val);
            }
        } catch (EngineException e) {
            e.printStackTrace();
        }
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
    public void test03() {
        EngineRace engineRace = new EngineRace();
        try {
            engineRace.open("./data");
            for (int i = 0; i < 1000000; i++) {
                System.out.println(i);
                engineRace.write(getKey(), getValue());
            }
        } catch (EngineException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test04() {
        System.out.println(FileResources.getIndex(UnsignedLong.valueOf(3 * 18014398509481984L - 1)));
    }
}
