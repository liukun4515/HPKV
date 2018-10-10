package cn.wangxinshuo.hpkv;

import com.alibabacloud.polar_race.engine.common.EngineRace;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import org.junit.Test;

public class Test01 {
    @Test
    public void test01() {
//        CreateFileResources createFileResources = new CreateFileResources("data");
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
            engineRace.write(key, value);
            value = engineRace.read(key);
            if (value != null) {
                for (byte val :
                        value) {
                    System.out.print(val);
                }
            } else {
                throw new EngineException(RetCodeEnum.NOT_FOUND, "键值对应的不存在!");
            }
        } catch (EngineException e) {
            e.printStackTrace();
        }
    }
}
