package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.file.CreateFileResources;
import cn.wangxinshuo.hpkv.key.KeyDataType;
import cn.wangxinshuo.hpkv.util.ByteArrayToLong;
import cn.wangxinshuo.hpkv.value.KeyValueDataType;
import com.google.common.primitives.UnsignedLong;

import java.util.ArrayList;

/**
 * @author wszgr
 */
public class Select {
    private CreateFileResources resources;
    private ArrayList<KeyDataType> list1;
    private ArrayList<KeyValueDataType> list2;
    private final int NUMBER_OF_FILES = 1024;

    public Select(CreateFileResources resources) {
        this.resources = resources;
        list1 = new ArrayList<KeyDataType>(NUMBER_OF_FILES);
        list2 = new ArrayList<KeyValueDataType>(NUMBER_OF_FILES / 4);
    }

    private void loadKeyCache() {

    }

    public byte[] get(byte[] inKey) {
        UnsignedLong key = ByteArrayToLong.getKey(inKey);

        return null;
    }
}
