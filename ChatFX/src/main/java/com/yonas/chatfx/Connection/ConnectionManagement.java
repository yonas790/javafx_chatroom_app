package com.yonas.chatfx.Connection;

import java.sql.*;

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
