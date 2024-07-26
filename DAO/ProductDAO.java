/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Product;
import Model.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author nducmd
 */
public class ProductDAO {

    public ProductDAO() {
    }
    public int getNumberOfProduct() {
        String query = "SELECT COUNT(*) FROM product WHERE deleted = 0";
        try (Connection connection = Database.connectDB();
                Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                int productCount = resultSet.getInt(1);
                return productCount;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public boolean add(Product p) throws SQLException {
        String query = "INSERT INTO product (product_name, unit, price, image, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.connectDB();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, p.getProductName());
            preparedStatement.setString(2, p.getUnit());
            preparedStatement.setInt(3, p.getPrice());
            preparedStatement.setString(4, p.getImage());
            preparedStatement.setString(5, p.getStatus());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    public boolean deleteProductById(int id) {
        String query = "UPDATE product SET deleted = 1  WHERE product_id = ?";
        
        try (Connection connection = Database.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }
    public ObservableList<Product> getProductList(String name) throws SQLException {
        ObservableList<Product> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM product WHERE product_name LIKE ? AND deleted = 0";
        try (Connection connection = Database.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + name + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Product(resultSet.getInt("product_id"), resultSet.getString("product_name"), resultSet.getString("unit"), resultSet.getInt("price"), resultSet.getString("image"), resultSet.getString("status")));
            }
            // Đóng kết nối
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public boolean updateProduct(Product p) throws SQLException {
        String query = "UPDATE product SET product_name = ?, unit = ?, price = ?, image = ?, status = ? WHERE product_id = ?";
        try (Connection connection = Database.connectDB();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, p.getProductName());
            preparedStatement.setString(2, p.getUnit());
            preparedStatement.setInt(3, p.getPrice());
            preparedStatement.setString(4, p.getImage());
            preparedStatement.setString(5, p.getStatus());
            preparedStatement.setInt(6, p.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
