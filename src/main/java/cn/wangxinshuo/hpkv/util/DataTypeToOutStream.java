package cn.wangxinshuo.hpkv.util;

import cn.wangxinshuo.hpkv.util.interfaces.KeyInterface;
import cn.wangxinshuo.hpkv.util.interfaces.KeyValueInterface;

public class DataTypeToOutStream {
    private byte[] data;

    public DataTypeToOutStream(KeyInterface keyInterface){

    }

    public DataTypeToOutStream(KeyValueInterface keyValueInterface){

    }

    public byte[] getData() {
        return data;
    }
}
