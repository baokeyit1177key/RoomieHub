package org.example.roomiehub;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestMySQLConnection {
    public static void main(String[] args) {
        try {
            String url = "jdbc:mysql://tramway.proxy.rlwy.net:20407/railway";
            String username = "root";
            String password = "iuRiBkXWyZLBJszzekCFGwCjaYNABnMr";

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Kết nối thành công!");
            conn.close();
        } catch (Exception e) {
            System.out.println("❌ Kết nối thất bại!");
            e.printStackTrace();
        }
    }
}
