package cn.wangxinshuo.hpkv.tree;

import cn.wangxinshuo.hpkv.key.Key;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author 王新硕
 */
public class LeafNode implements Node {
    private ArrayList<KeyValue> list;
    private long behindNode = -1;

    public void add(byte[] key, byte[] value) throws EngineException {
        Key insertKey = new Key(key);
        add(insertKey, value);
    }

    public void add(Key key, byte[] value) {
        KeyValue kv = new KeyValue(key, value);
        list.add(kv);
    }

    public byte[] getBytes() throws EngineException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            stream.write(SerializationUtils.serialize(list));
            stream.write(SerializationUtils.serialize(behindNode));
            return stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    public boolean isFull() {
        return list.size() >= Configuration.getNodeNumber() - 1;
    }

    public long getBehindNode() {
        return behindNode;
    }

    public void setBehindNode(long behindNode) {
        this.behindNode = behindNode;
    }
}
