package cn.wangxinshuo.hpkv.tree;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

/**
 * @author 王新硕
 */
public class Configuration {
    private static JSONParser parser = new JSONParser();
    private static int nodeNumber = nodeNumber();
    private static int keySize = keySize();
    private static int valueSize = valueSize();

    public static int getNodeNumber() {
        return nodeNumber;
    }

    public static int getKeySize() {
        return keySize;
    }

    public static int getValueSize() {
        return valueSize;
    }

    private static int nodeNumber() {
        try {
            JSONObject object = (JSONObject) parser.parse(
                    new FileReader("src/main/resources/configuration.json"));
            return ((Long) object.get("NodeNumber")).intValue();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 3;
    }

    private static int keySize() {
        try {
            JSONObject object = (JSONObject) parser.parse(
                    new FileReader("src/main/resources/configuration.json"));
            return ((Long) object.get("KeySize")).intValue();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 8;
    }

    private static int valueSize() {
        try {
            JSONObject object = (JSONObject) parser.parse(
                    new FileReader("src/main/resources/configuration.json"));
            return ((Long) object.get("ValueSize")).intValue();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 4096;
    }
}
