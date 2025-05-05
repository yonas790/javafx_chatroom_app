package com.yonas.chatfx.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManagement {

    public static Connection conn;

    /**
     * This method creates a connection with the corresponding data from db.properties, which will be used throughout the program execution.
     * The correct usage of the DAL would be to create and close connections for each database query. However, due to the program's refresh rate, this is not possible.
     * Ideally, WebSockets would be used to avoid such refreshes.
     * Preconditions: None
     * Postconditions: The connection will not be closed during the program's usage.
     *
     * @throws SQLException
     */
    public static void getConnection() throws SQLException {
        conn = DriverManager.getConnection(PropertiesClass.getProperty("URL"), PropertiesClass.getProperty("USERNAME"), PropertiesClass.getProperty("PASSWORD"));
    }

    /**
     * This method closes the connection used throughout the program. Normally, the connection should close on program termination, but we will close it anyway.
     * Preconditions: None
     * Postconditions: None
     *
     * @throws SQLException
     */
    public static void closeConnection() throws SQLException {
        if(conn != null){
            conn.close();
        }
    }
}
