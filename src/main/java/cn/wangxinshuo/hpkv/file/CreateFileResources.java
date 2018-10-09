package cn.wangxinshuo.hpkv.file;

import com.google.common.primitives.UnsignedLong;

import java.io.*;
import java.util.ArrayList;

/**
 * @author wszgr
 */
public class CreateFileResources {
    private String path;
    private ArrayList<OutputStream> files1, files2;
    private ArrayList<InputStream> files3, files4;
    private static final int NUMBER_OF_FILES = 1024;

    public CreateFileResources(String path) {
        this.path = path;
        files1 = new ArrayList<OutputStream>(NUMBER_OF_FILES);
        files2 = new ArrayList<OutputStream>(NUMBER_OF_FILES);
        files3 = new ArrayList<InputStream>(NUMBER_OF_FILES);
        files4 = new ArrayList<InputStream>(NUMBER_OF_FILES);
        this.createKeyFile();
        this.createKeyValueFile();
    }

    private void createKeyFile() {
        for (int i = 0; i < NUMBER_OF_FILES; i++) {
            String name = "/Key_" + Integer.toString(i) + ".bin";
            File file = new File(path + name);
            getStream(i, file, files1, files3);
        }
    }

    private void createKeyValueFile() {
        for (int i = 0; i < NUMBER_OF_FILES; i++) {
            String name = "/KeyAndValue_" + Integer.toString(i) + ".bin";
            File file = new File(path + name);
            getStream(i, file, files2, files4);
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

    public OutputStream getKeyResources(UnsignedLong key) {
        return files1.get(getIndex(key));
    }

    public OutputStream getKeyValueResources(UnsignedLong key) {
        return files2.get(getIndex(key));
    }

    public InputStream getKeyResources(UnsignedLong key, boolean bool) {
        return files3.get(getIndex(key));
    }

    public InputStream getKeyValueResources(UnsignedLong key, boolean bool) {
        return files4.get(getIndex(key));
    }

    public ArrayList<InputStream> getAllKeyResources() {
        return files3;
    }

    public static int getIndex(UnsignedLong key) {
        // 2^54 = 18014398509481984L
        long a = 0L;
        final long step = 18014398509481984L;
        for (int i = 0; i < NUMBER_OF_FILES; i++, a += step) {
            if (key.compareTo(UnsignedLong.valueOf(a)) > 0
                    && key.compareTo(UnsignedLong.valueOf(a + step)) < 0) {
                return i;
            }
        }
        return 0;
    }

    @Override
    protected void finalize() throws Throwable {
        for (int i = 0; i < NUMBER_OF_FILES; i++) {
            files1.get(i).close();
            files2.get(i).close();
            files3.get(i).close();
            files4.get(i).close();
        }
    }
}
