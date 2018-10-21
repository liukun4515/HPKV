package cn.wangxinshuo.b.plus.tree;

import cn.wangxinshuo.hpkv.util.disk.ReadDisk;
import cn.wangxinshuo.hpkv.util.disk.WriteDisk;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author 王新硕
 */
public class AdelsonVelskyLandTree {
    private RandomAccessFile file;


    public AdelsonVelskyLandTree(RandomAccessFile file) {
        this.file = file;
    }

    public AdelsonVelskyLandTree(File file) throws EngineException {
        try {
            this.file = new RandomAccessFile(file, "rws");
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
    }

    public void insert(byte[] key, byte[] value) {

    }

    public byte[] select(byte[] key) {
        return null;
    }

    private void write(long start, byte[] data) throws EngineException {
        WriteDisk.write(file, start, data);
    }

    private byte[] read(long start, int length) throws EngineException {
        return ReadDisk.read(file, start, length);
    }
}

class Node {
    private long left, right;
    private byte[] value;
    private byte[] key;

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public long getLeft() {
        return left;
    }

    public void setLeft(long left) {
        this.left = left;
    }

    public long getRight() {
        return right;
    }

    public void setRight(long right) {
        this.right = right;
    }
}
