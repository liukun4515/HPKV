package cn.wangxinshuo.hpkv.util;

import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import com.google.common.io.ByteSource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author wszgr
 */
public class ByteArrayToInputStream {
    private ByteSource source;

    public ByteArrayToInputStream() {
        source = ByteSource.empty();
    }

    public ByteArrayToInputStream(byte[] val) {
        source = ByteSource.wrap(val);
    }

    public void add(byte val) {
        byte[] a = {val};
        ByteSource mid = ByteSource.wrap(a);
        source = ByteSource.concat(source, mid);
    }

    public void add(byte[] val) {
        ByteSource mid = ByteSource.wrap(val);
        source = ByteSource.concat(source, mid);
    }

    public InputStream getInputStream() throws EngineException {
        InputStream stream;
        try {
            stream = source.openStream();
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
        if (stream == null) {
            throw new EngineException(RetCodeEnum.INVALID_ARGUMENT, "INVALID_ARGUMENT");
        }
        return stream;
    }

}
