/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author nducmd
 */
public class Database {
    public static Connection connectDB() throws SQLException{
        String url = "jdbc:sqlserver://NDUCMD:1433;databaseName=qlcafe;useUnicode=true;characterEncoding=UTF-8";
        String dbUsername = "sa";
        String dbPassword = "123";
        try{
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            //System.out.println("Ket noi toi database");
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
