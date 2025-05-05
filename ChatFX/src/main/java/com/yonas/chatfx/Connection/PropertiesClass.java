package com.yonas.chatfx.Connection;

import java.io.*;
import java.util.Properties;

public class PropertiesClass {
    public static final String FILE_NAME = "db.properties";

    public static String getProperty(String field) {
        String fieldValue;
        try (InputStream input = PropertiesClass.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            fieldValue = properties.getProperty(field);
        } catch (IOException e) {
            throw new RuntimeException("Error reading properties file", e);
        }
        return fieldValue;
    }
}
