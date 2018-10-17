package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.file.FileResources;
import cn.wangxinshuo.hpkv.log.Log;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import com.google.common.io.Files;
import org.apache.commons.lang3.SerializationUtils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Random;

/**
 * @author wszgr
 */
public class Store {
    private FileResources resources;
    private HashMap<byte[], byte[]> map;
    private Log log;

    private Store(FileResources resources) {
        this.resources = resources;
    }

    public Store(FileResources resources, Log log, HashMap<byte[], byte[]> map) {
        this(resources);
        this.log = log;
        this.map = map;
    }

    public synchronized void put(byte[] key, byte[] inValue) throws EngineException {
        // 首先判断状态
        if (map.size() >= log.getKeyValueNumberInLogFile()) {
            // 为可以持久化到文件的状态
            int fileIndex =
                    Math.abs(new Random().nextInt()) % resources.getNumberOfFiles();
            BufferedOutputStream outputStream = null;
            RandomAccessFile keyFile = null;
            try {
                // 将map持久化到文件并清空
                byte[] input = Files.toByteArray(resources.getReadSources(fileIndex));
                System.out.println("InputStream大小为：" + input.length);
                if (input.length > 0) {
                    HashMap<byte[], byte[]> mapInFile =
                            SerializationUtils.deserialize(input);
                    System.out.println("文件中的Map大小为：" + mapInFile.size());
                    map.putAll(mapInFile);
                    System.out.println("重载后的Map大小为：" + map.size());
                }
                outputStream = new BufferedOutputStream(
                        new FileOutputStream(resources.getWriteSources(fileIndex)));
                byte[] afterInputObjectArray = SerializationUtils.serialize(map);
                System.out.println("afterInputObjectArray: " + afterInputObjectArray.length);
                outputStream.write(afterInputObjectArray);
                outputStream.flush();
                // 将map里面的key持久化到文件中
                keyFile = new RandomAccessFile(
                        resources.getKeyFile(), "rws");
                keyFile.seek(keyFile.length());
                for (byte[] eachKey :
                        map.keySet()) {
                    keyFile.write(eachKey);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    if (keyFile != null) {
                        keyFile.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 清场
            log.eraseLog();
            map.clear();
        }
        // 为不可以持久化到文件的状态
        // log文件初始化
        RandomAccessFile logFile = log.getRandomAccessFile();
        // 为不可以持久化到文件的状态
        try {
            logFile.seek(logFile.length());
            logFile.write(key);
            logFile.write(inValue);
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
        // 放入MemTable
        map.put(key, inValue);
    }
}
