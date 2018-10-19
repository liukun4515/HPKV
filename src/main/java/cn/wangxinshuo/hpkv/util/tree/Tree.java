package cn.wangxinshuo.hpkv.util.tree;

import cn.wangxinshuo.hpkv.util.key.Key;

/**
 * @author wszgr
 */
public class Tree {
    static {
        System.loadLibrary("tree");
    }

    public native void add(String fileName, Key key, byte[] data);

    public native byte[] get(String fileName, Key key);
}