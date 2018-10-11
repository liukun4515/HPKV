package cn.wangxinshuo.hpkv.file;

import com.google.common.primitives.UnsignedLong;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * @author wszgr
 */
public class FileResources {
    private String path, mode;
    private static final int NUMBER_OF_FILES = 1024;
    private ArrayList<RandomAccessFile> streams;

    public FileResources(String path) throws IOException {
        this.path = path;
        this.mode = "rws";
        streams = new ArrayList<RandomAccessFile>(NUMBER_OF_FILES);
        this.createKeyValueFile();
    }

    public FileResources(String path, String mode) throws IOException {
        this(path);
        this.mode = mode;
    }

    private void createKeyValueFile() throws IOException {
        for (int i = 0; i < NUMBER_OF_FILES; i++) {
            String name = "/KeyAndValue_" + Integer.toString(i) + ".bin";
            File file = new File(path + name);
            final boolean newFile = file.createNewFile();
            streams.add(i, new RandomAccessFile(file, "rws"));
        }
    }

    public RandomAccessFile getFileResources(int key) {
        return streams.get(key);
    }

    public static int getIndex(UnsignedLong key) {
        // 2^54 = 18014398509481984L
        UnsignedLong div = UnsignedLong.valueOf(18014398509481984L);
        UnsignedLong mod = UnsignedLong.valueOf(1024L);
        return key.dividedBy(div).mod(mod).intValue();
    }

    public void close() throws IOException {
        for (int i = 0; i < NUMBER_OF_FILES; i++) {
            streams.get(i).close();
        }
    }

    @Override
    protected void finalize() throws IOException {
        this.close();
    }
}
