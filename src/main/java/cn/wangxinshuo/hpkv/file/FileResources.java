package cn.wangxinshuo.hpkv.file;

import com.google.common.primitives.UnsignedLong;

import java.io.*;
import java.util.ArrayList;

/**
 * @author wszgr
 */
public class FileResources {
    private String path;
    private static final int NUMBER_OF_FILES = 1024;
    private ArrayList<OutputStream> outputStreamArrayList;
    private ArrayList<InputStream> inputStreamArrayList;

    public FileResources(String path) {
        this.path = path;
        outputStreamArrayList = new ArrayList<OutputStream>(NUMBER_OF_FILES);
        inputStreamArrayList = new ArrayList<InputStream>(NUMBER_OF_FILES);
        this.createKeyValueFile();
    }

    private void createKeyValueFile() {
        for (int i = 0; i < NUMBER_OF_FILES; i++) {
            String name = "/KeyAndValue_" + Integer.toString(i) + ".bin";
            File file = new File(path + name);
            getStream(i, file, outputStreamArrayList, inputStreamArrayList);
        }
    }

    private void getStream(int i, File file,
                           ArrayList<OutputStream> tempFiles1,
                           ArrayList<InputStream> tempFiles2) {
        try {
            final boolean newFile = file.createNewFile();
            tempFiles1.add(i, new FileOutputStream(file));
            tempFiles2.add(i, new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OutputStream getFileResources(UnsignedLong key) {
        int index = getIndex(key);
        return outputStreamArrayList.get(index);
    }

    public InputStream getFileResources(UnsignedLong key, boolean bool) {
        int index = getIndex(key);
        return inputStreamArrayList.get(index);
    }

    public static int getIndex(UnsignedLong key) {
        // 2^54 = 18014398509481984L
        UnsignedLong div = UnsignedLong.valueOf(18014398509481984L);
        UnsignedLong mod = UnsignedLong.valueOf(1024L);
        int result = key.dividedBy(div).mod(mod).intValue();
        System.out.println(key + "输入到：" + result);
        return result;
    }

    public void close() throws IOException {
        for (int i = 0; i < NUMBER_OF_FILES; i++) {
            outputStreamArrayList.get(i).close();
            inputStreamArrayList.get(i).close();
        }
    }

    @Override
    protected void finalize() throws IOException {
        this.close();
    }
}
