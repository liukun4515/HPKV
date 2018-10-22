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
public class InternalNode implements Node {
    private long parentNode = -1;
    private ArrayList<Key> keys;
    private long leftChildNode = -1;
    private long midChildNode = -1;
    private long rightChildNode = -1;

    public void add(Key key) {
        keys.add(key);
    }

    public void add(byte[] key) throws EngineException {
        keys.add(new Key(key));
    }

    public byte[] getBytes() throws EngineException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            stream.write(SerializationUtils.serialize(parentNode));
            stream.write(SerializationUtils.serialize(keys));
            stream.write(SerializationUtils.serialize(leftChildNode));
            stream.write(SerializationUtils.serialize(midChildNode));
            stream.write(SerializationUtils.serialize(rightChildNode));
            return stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    public long getParentNode() {
        return parentNode;
    }

    public void setParentNode(long parentNode) {
        this.parentNode = parentNode;
    }

    public long getLeftChildNode() {
        return leftChildNode;
    }

    public void setLeftChildNode(long leftChildNode) {
        this.leftChildNode = leftChildNode;
    }

    public long getMidChildNode() {
        return midChildNode;
    }

    public void setMidChildNode(long midChildNode) {
        this.midChildNode = midChildNode;
    }

    public int getKeyNumber() {
        return keys.size();
    }

    public long getRightChildNode() {
        return rightChildNode;
    }

    public void setRightChildNode(long rightChildNode) {
        this.rightChildNode = rightChildNode;
    }
}
