package Koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conn {
    public static String url = "jdbc:mysql://127.0.0.1:3306/tugas_project";
    public static String user = "root";
    public static String password = "";

    public static Connection getConnection() throws SQLException
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver tidak ditemukan", e);
        }
        return DriverManager.getConnection(url, user, password);
    }
}
