package com.yonas.chatfx.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManagement {

    public static Connection conn;

    public static void getConnection() throws SQLException {
        conn = DriverManager.getConnection(PropertiesClass.getProperty("URL"), PropertiesClass.getProperty("USERNAME"), PropertiesClass.getProperty("PASSWORD"));
    }

    public static void closeConnection() throws SQLException {
        if(conn != null){
            conn.close();
        }
    }
}
