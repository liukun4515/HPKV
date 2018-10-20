package cn.wangxinshuo.hpkv.util.tree;

import cn.wangxinshuo.hpkv.key.Key;

/**
 * @author wszgr
 */
public class Tree {
    static {
        System.loadLibrary("tree");
    }

    public void add(String fileName, Key key, byte[] data) {

    }

    public byte[] get(String fileName, Key key) {
        return null;
    }
}