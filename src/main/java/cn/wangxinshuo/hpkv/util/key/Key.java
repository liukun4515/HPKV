package cn.wangxinshuo.hpkv.util.key;

import cn.wangxinshuo.hpkv.util.KeyCompare;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;

import java.io.Serializable;

/**
 * @author wszgr
 */
public class Key implements Serializable {
    private byte[] data;
    private short dataLen = 8;
    private static final long serialVersionUID = -4870523500791682929L;

    public Key(byte[] data) throws EngineException {
        if (data.length == dataLen) {
            this.data = data;
        } else {
            throw new EngineException(RetCodeEnum.INVALID_ARGUMENT, "INVALID_ARGUMENT");
        }
    }

    public static Key valueOf(byte[] data) throws EngineException {
        return new Key(data);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setDataLen(short len) {
        this.dataLen = len;
    }

    @Override
    public int hashCode() {
        return data[0] * data[1] * data[3];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Key) {
            return KeyCompare.compare(((Key) obj).getData(), this.data) == 0;
        } else {
            return false;
        }
    }
}
