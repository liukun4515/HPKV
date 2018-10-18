package cn.wangxinshuo.hpkv.hash;

import cn.wangxinshuo.hpkv.util.key.Key;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import org.junit.Test;

import java.util.HashMap;

public class Test04 {
    @Test
    public void test01() {
        // byte[]不能当作索引
        byte[] a = {0, 0, 0, 0, 0, 0, 0, 0,};
        byte[] b = {0, 0, 0, 0, 0, 0, 0, 0,};
        HashMap<Key, Integer> map = new HashMap<Key, Integer>();
        try {
            map.put(new Key(a), 2);
            map.put(new Key(b), 1);
            System.out.println(map.get(new Key(a)));
            System.out.println(map.get(new Key(b)));
        } catch (EngineException e) {
            e.printStackTrace();
        }
    }
}
