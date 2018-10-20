package cn.wangxinshuo.hpkv.util.require.check;

/**
 * @author 王新硕
 */
public class RequireNotNull {
    public static void requireNotNull(Object parameter, String message) throws Exception {
        if (parameter == null) {
            throw new Exception(message);
        }
    }
}
