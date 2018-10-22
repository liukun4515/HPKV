package cn.wangxinshuo.hpkv.tree;

import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;

/**
 * @author 王新硕
 */
interface Node {
    /**
     * 得到对象序列化后的byte
     *
     * @return byte[]
     */
    public byte[] getBytes() throws EngineException;
}
