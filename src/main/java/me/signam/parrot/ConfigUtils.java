package me.signam.parrot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    //option to toggle debug mode
    public boolean isDebugMode() {
        return Boolean.parseBoolean(properties.getProperty("debug", "false"));
    }
}

