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
    public static int KeySize = getKeySize();
    public static int ValueSize = getValueSize();
    public static int PointerSize = getPointerSize();

    private static int getKeySize() {
        return getConfiguration("KeySize");
    }

    private static int getValueSize() {
        return getConfiguration("ValueSize");
    }

    private static int getPointerSize() {
        return getConfiguration("PointerSize");
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
