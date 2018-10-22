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

    public static int getNumber() {
        try {
            JSONObject object = (JSONObject) parser.parse(
                    new FileReader("src/main/resources/configuration.json"));
            return Integer.parseInt((String) object.get("NodeNumber"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 3;
    }
}
