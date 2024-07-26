/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Customer;
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
public class CustomerDAO {

    public CustomerDAO() {
    }
    public int getNumberOfCustomer() {
        String query = "SELECT COUNT(*) FROM customer WHERE deleted = 0";
        try (Connection connection = Database.connectDB();
                Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                int customerCount = resultSet.getInt(1);
                return customerCount;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public boolean checkPhoneNumberExists(String phone) throws SQLException {
        // Kết nối với cơ sở dữ liệu
        Connection connection = Database.connectDB();

        try {
            // Kiểm tra xem số điện thoại đã tồn tại trong cơ sở dữ liệu chưa
            String query = "SELECT 1 FROM customer WHERE phone_number = ? AND deleted = 0";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, phone);
            try (ResultSet resultSet = statement.executeQuery()) {

                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateCustomer(Customer c) throws SQLException {
        String query = "UPDATE customer SET last_name = ?, first_name = ?, gender = ?, birthdate = ?, phone_number = ?, address = ? WHERE customer_id = ?";
        try (Connection connection = Database.connectDB();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, c.getLastName());
            preparedStatement.setString(2, c.getFirstName());
            preparedStatement.setString(3, c.getGender());
            preparedStatement.setString(4, c.getBirthdate());
            preparedStatement.setString(5, c.getPhoneNumber());
            preparedStatement.setString(6, c.getAddress());
            preparedStatement.setInt(7, c.getCustomerId());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    public void updateCustomerPoint(String phone, int customerPoint) throws SQLException {
        if (checkPhoneNumberExists(phone)) {
            String query = "UPDATE customer SET customer_point = ?, customer_tier = ? WHERE phone_number = ?";
            try (Connection connection = Database.connectDB();
                    PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, customerPoint);
                preparedStatement.setString(2, Customer.getTierByPoint(customerPoint));
                preparedStatement.setString(3, phone);
                int rowsAffected = preparedStatement.executeUpdate();

            }
        }
    }

    public boolean add(Customer c) throws SQLException {
        String query = "INSERT INTO customer (last_name, first_name, gender, birthdate, address, phone_number, customer_point, customer_tier) VALUES (?, ?, ?, ?, ?, ?, 0, 'Không')";
        try (Connection connection = Database.connectDB();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, c.getLastName());
            preparedStatement.setString(2, c.getFirstName());
            preparedStatement.setString(3, c.getGender());
            preparedStatement.setString(4, c.getBirthdate());
            preparedStatement.setString(5, c.getAddress());
            preparedStatement.setString(6, c.getPhoneNumber());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    
    public Customer getCustomerByPhoneNumber(String phoneNumber) {
        String query = "SELECT * FROM customer WHERE phone_number = ? AND deleted = 0";
        
        try (Connection connection = Database.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, phoneNumber);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Customer customer = new Customer();
                    customer.setFirstName(resultSet.getString("first_name"));
                    customer.setLastName(resultSet.getString("last_name"));
                    customer.setCustomerPoint(resultSet.getInt("customer_point"));
                    customer.setCustomerTier(resultSet.getString("customer_tier"));
                    customer.setPhoneNumber(resultSet.getString("phone_number"));
                    
                    return customer;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null; // Trả về null nếu không tìm thấy nhân viên với ID cụ thể
    }
    public int getCustomerIdByPhoneNumber(String phoneNumber) {
        String query = "SELECT * FROM customer WHERE phone_number = ? AND deleted = 0";
        
        try (Connection connection = Database.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, phoneNumber);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("customer_id");
                    return id;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 1; // Trả về null nếu không tìm thấy nhân viên với ID cụ thể
    }
    public Customer getCustomerById(int id) {
        String query = "SELECT * FROM customer WHERE customer_id = ? AND deleted = 0";
        
        try (Connection connection = Database.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, id);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Customer customer = new Customer(resultSet.getInt("customer_id"), resultSet.getString("last_name"), resultSet.getString("first_name"), 
                        resultSet.getString("gender"), resultSet.getString("birthdate"), resultSet.getString("address"),
                        resultSet.getString("phone_number"), resultSet.getInt("customer_point"), resultSet.getString("customer_tier"));
                    return customer;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null; // Trả về null nếu không tìm thấy nhân viên với ID cụ thể
    }
    public boolean deleteCustomerById(int id) {
        String query = "UPDATE customer SET deleted = 1 WHERE customer_id = ?";
        
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
    public ObservableList<Customer> getCustomerList(String input) throws SQLException {
        ObservableList<Customer> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM customer WHERE deleted = 0 AND (first_name LIKE ? OR last_name LIKE ? OR phone_number LIKE ?)";
        try (Connection connection = Database.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + input + "%");
            preparedStatement.setString(2, "%" + input + "%");
            preparedStatement.setString(3, "%" + input + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Customer(resultSet.getInt("customer_id"), resultSet.getString("last_name"), resultSet.getString("first_name"), 
                        resultSet.getString("gender"), resultSet.getString("birthdate"), resultSet.getString("address"),
                        resultSet.getString("phone_number"), resultSet.getInt("customer_point"), resultSet.getString("customer_tier")));
            }
            // Đóng kết nối
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
