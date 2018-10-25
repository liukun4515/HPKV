package cn.wangxinshuo.hpkv.conf;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

/**
 *
 */
public class Configuration {
    private static JSONParser json = new JSONParser();
    public static int MaxLogNumber = getMaxLogNumber();
    public static int KeySize = getKeySize();
    public static int ValueSize = getValueSize();
    public static int InsertBufferSize = getInsertBufferSize();
    public static int PointerSize = getPointerSize();
    public static int MaxLevel = getMaxLevel();

    private static int getMaxLogNumber() {
        return getConfiguration("MaxLogNumber");
    }

    private static int getKeySize() {
        return getConfiguration("KeySize");
    }

    private static int getValueSize() {
        return getConfiguration("ValueSize");
    }

    private static int getInsertBufferSize() {
        return getConfiguration("InsertBufferSize");
    }

    private static int getPointerSize() {
        return getConfiguration("PointerSize");
    }

    private static int getMaxLevel() {
        return getConfiguration("MaxLevel");
    }

    private static int getConfiguration(String key) {
        try {
            JSONObject object =
                    (JSONObject) json.parse(
                            new FileReader("src/main/resources/configuration.json"));
            return ((Long) object.get(key)).intValue();
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
