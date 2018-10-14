package cn.wangxinshuo.hpkv;

import cn.wangxinshuo.hpkv.file.FileResources;
import cn.wangxinshuo.hpkv.log.Log;
import cn.wangxinshuo.hpkv.range.key.SortedList;
import cn.wangxinshuo.hpkv.util.ByteArrayToUnsignedLong;
import com.alibabacloud.polar_race.engine.common.exceptions.EngineException;
import com.alibabacloud.polar_race.engine.common.exceptions.RetCodeEnum;
import com.google.common.io.Files;
import com.google.common.primitives.UnsignedLong;
import org.apache.commons.lang3.SerializationUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Random;

/**
 * @author wszgr
 */
public class Store {
    private FileResources resources;
    private HashMap<UnsignedLong, byte[]> map;
    private SortedList sortedList;
    private Log log;

    private Store(FileResources resources) {
        this.resources = resources;
    }

    public Store(FileResources resources, Log log,
                 HashMap<UnsignedLong, byte[]> map, SortedList sortedList) {
        this(resources);
        this.log = log;
        this.map = map;
        this.sortedList = sortedList;
    }

    public void put(byte[] inKey, byte[] inValue) throws EngineException {
        UnsignedLong key = ByteArrayToUnsignedLong.getKey(inKey);
        // 首先判断状态
        if (map.size() >= log.getKeyValueNumberInLogFile()) {
            System.out.println("进行数据持久化！");
            // 为可以持久化到文件的状态
            int fileIndex =
                    Math.abs(new Random().nextInt()) % resources.getNumberOfFiles();
            try {
                // 将map持久化到文件并清空
                byte[] input = Files.toByteArray(resources.getReadSources(fileIndex));
                System.out.println("InputStream大小为：" + input.length);
                if (input.length > 0) {
                    HashMap<UnsignedLong, byte[]> mapInFile =
                            SerializationUtils.deserialize(input);
                    System.out.println("文件中的Map大小为：" + mapInFile.size());
                    map.putAll(mapInFile);
                    System.out.println("重载后的Map大小为：" + map.size());
                }
                OutputStream outputStream =
                        new FileOutputStream(
                                resources.getWriteSources(fileIndex));
                byte[] afterInputObjectArray = SerializationUtils.serialize(map);
                System.out.println("afterInputObjectArray: " + afterInputObjectArray.length);
                outputStream.write(afterInputObjectArray);
                outputStream.flush();
                outputStream.close();
                // 将key持久化到文件
                OutputStream keyOutput =
                        new FileOutputStream(resources.getKeyFile());
                byte[] serializeSortedList = SerializationUtils.serialize(sortedList.getLinkedList());
                keyOutput.write(serializeSortedList);
                keyOutput.flush();
                keyOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
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
            logFile.write(inKey);
            logFile.write(inValue);
        } catch (IOException e) {
            e.printStackTrace();
            throw new EngineException(RetCodeEnum.IO_ERROR, "IO_ERROR");
        }
        // 放入MemTable
        map.put(key, inValue);
    }
}
