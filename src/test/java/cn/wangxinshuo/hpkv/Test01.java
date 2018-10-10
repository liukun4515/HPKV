package cn.wangxinshuo.hpkv;

import com.alibabacloud.polar_race.engine.common.EngineRace;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import org.junit.Test;

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
}
