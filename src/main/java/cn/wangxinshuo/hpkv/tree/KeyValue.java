package cn.wangxinshuo.hpkv.tree;

import cn.wangxinshuo.hpkv.key.Key;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;

/**
 * @author 王新硕
 */
public class KeyValue {
    private Key key;
    private byte[] value;

    public KeyValue(Key key, byte[] value) {
        this.key = key;
        this.value = value;
    }

    public KeyValue(byte[] key, byte[] value) throws EngineException {
        this.key = new Key(key);
        this.value = value;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
