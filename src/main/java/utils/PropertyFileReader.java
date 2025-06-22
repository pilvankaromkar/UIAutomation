package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileReader {

    private static Properties prop;

    public static void loadProperties(String env) {
        try {
            FileInputStream fis = new FileInputStream( "src/main/resources/configurations/"+ env + ".properties");
            prop = new Properties();
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
}
