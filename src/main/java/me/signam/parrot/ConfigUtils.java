package me.signam.parrot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConfigUtils {

    private final Properties properties = new Properties();
    private final String configFile = "config.properties";
    private final File file = new File(configFile);

    public ConfigUtils() {

        try {
            if (!file.exists())
                file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(FileInputStream fileInputStream = new FileInputStream(configFile)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getToken() {
        return properties.get("token").toString();
    }

    //channels for sending messages
    public String getInputChannel() {
        return properties.get("input.channel").toString();
    }

    //channels for outputting messages
    public String getOutputChannel() {
        return properties.get("output.channel").toString();
    }


}
