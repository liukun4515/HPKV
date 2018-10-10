package cn.wangxinshuo.hpkv.file;

import com.google.common.primitives.UnsignedLong;

import java.io.*;
import java.util.ArrayList;

/**
 * @author wszgr
 */
public class CreateFileResources {
    private String path;
    private ArrayList<OutputStream> file1;
    private ArrayList<InputStream> file2;
    private static final int NUMBER_OF_FILES = 1024;

    public CreateFileResources(String path) {
        this.path = path;
        file1 = new ArrayList<OutputStream>(NUMBER_OF_FILES);
        file2 = new ArrayList<InputStream>(NUMBER_OF_FILES);
        this.createKeyValueFile();
    }

    private void createKeyValueFile() {
        for (int i = 0; i < NUMBER_OF_FILES; i++) {
            String name = "/KeyAndValue_" + Integer.toString(i) + ".bin";
            File file = new File(path + name);
            getStream(i, file, file1, file2);
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

    public OutputStream getKeyValueResources(UnsignedLong key) {
        return file1.get(getIndex(key));
    }

    public InputStream getKeyValueResources(UnsignedLong key, boolean bool) {
        return file2.get(getIndex(key));
    }

    public static int getIndex(UnsignedLong key) {
        // 2^54 = 18014398509481984L
        UnsignedLong a = UnsignedLong.valueOf(0L);
        UnsignedLong step = UnsignedLong.valueOf(18014398509481984L);
        for (int i = 0; i < NUMBER_OF_FILES; i++, a.plus(step)) {
            if (key.compareTo(a) > 0
                    && key.compareTo(a.plus(step)) < 0) {
                return i;
            }
        }
        return 0;
    }

    public void close() throws IOException {
        for (int i = 0; i < NUMBER_OF_FILES; i++) {
            file1.get(i).close();
            file2.get(i).close();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
    }
}
