/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Employee;
import Model.Session;
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
public class EmployeeDAO {

    public EmployeeDAO() {
    }
    public int getNumberOfEmployee() {
        String query = "SELECT COUNT(*) FROM employee WHERE deleted = 0";
        try (Connection connection = Database.connectDB();
                Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                int employeeCount = resultSet.getInt(1);
                return employeeCount;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public boolean add(Employee e) throws SQLException {
        String query = "INSERT INTO employee (last_name, first_name, gender, birthdate, phone_number, address, position, hours_worked, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.connectDB();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, e.getLastName());
            preparedStatement.setString(2, e.getFirstName());
            preparedStatement.setString(3, e.getGender());
            preparedStatement.setString(4, e.getBirthdate());
            preparedStatement.setString(5, e.getPhoneNumber());
            preparedStatement.setString(6, e.getAddress());
            preparedStatement.setString(7, e.getPosition());
            preparedStatement.setInt(8, e.getHoursWorked());
            preparedStatement.setString(9, e.getPassword());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }
    public Employee getEmployeeByPhoneNumber(String phoneNumber) {
        String query = "SELECT * FROM employee WHERE phone_number = ? AND deleted = 0";

        try (Connection connection = Database.connectDB();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, phoneNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
//                    Employee employee = new Employee();
//                    employee.setEmployeeId(resultSet.getInt("employee_id"));
//                    employee.setLastName(resultSet.getString("last_name"));
//                    employee.setFirstName(resultSet.getString("first_name"));
//                    employee.setGender(resultSet.getString("gender"));
//                    employee.setBirthdate(resultSet.getString("birthdate"));
//                    employee.setAddress(resultSet.getString("address"));
//                    employee.setHoursWorked(resultSet.getInt("hours_worked"));
//                    employee.setPhoneNumber(resultSet.getString("phone_number"));
//                    employee.setPassword(resultSet.getString("password"));
//                    employee.setPosition(resultSet.getString("position"));
                    
                    Employee employee = new Employee(resultSet.getInt("employee_id"), resultSet.getString("password"), resultSet.getInt("hours_worked"), 
                            resultSet.getString("position"), resultSet.getString("last_name"), resultSet.getString("first_name"), resultSet.getString("gender"), 
                            resultSet.getString("birthdate"), resultSet.getString("address"), resultSet.getString("phone_number"));
                    return employee;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Trả về null nếu không tìm thấy nhân viên với ID cụ thể
    }

    public boolean authenticate(String phoneNumber, String password) throws SQLException {

        String query = "SELECT * FROM employee WHERE phone_number=? AND password=? AND deleted = 0";
        try (Connection connection = Database.connectDB();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            };
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public boolean checkPhoneNumberExists(String phone) throws SQLException {
        // Kết nối với cơ sở dữ liệu
        Connection connection = Database.connectDB();

        try {
            // Kiểm tra xem số điện thoại đã tồn tại trong cơ sở dữ liệu chưa
            String query = "SELECT 1 FROM employee WHERE phone_number = ? AND deleted = 0";
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
    public boolean updateEmployee(Employee e) throws SQLException {
        String query = "UPDATE employee SET last_name = ?, first_name = ?, gender = ?, birthdate = ?, phone_number = ?, address = ?, password = ?, position = ? WHERE employee_id = ?";
        try (Connection connection = Database.connectDB();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, e.getLastName());
            preparedStatement.setString(2, e.getFirstName());
            preparedStatement.setString(3, e.getGender());
            preparedStatement.setString(4, e.getBirthdate());
            preparedStatement.setString(5, e.getPhoneNumber());
            preparedStatement.setString(6, e.getAddress());
            preparedStatement.setString(7, e.getPassword());
            preparedStatement.setString(8, e.getPosition());
            preparedStatement.setInt(9, e.getEmployeeId());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    public void updateHoursWorkedEmployee() throws SQLException {
        String query = "UPDATE employee SET  hours_worked = ? WHERE employee_id = ?";
        try (Connection connection = Database.connectDB();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, Session.currEmployee.getHoursWorked() + Session.getDurationTime());
            preparedStatement.setInt(2, Session.currEmployee.getEmployeeId());
            int rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public ObservableList<Employee> getEmployeeList(String input) throws SQLException {
        ObservableList<Employee> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM employee WHERE deleted = 0 AND (first_name LIKE ? OR last_name LIKE ? OR phone_number LIKE ?)";
        try (Connection connection = Database.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + input + "%");
            preparedStatement.setString(2, "%" + input + "%");
            preparedStatement.setString(3, "%" + input + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Employee(resultSet.getInt("employee_id"), resultSet.getString("password"), resultSet.getInt("hours_worked"), 
                        resultSet.getString("position"), resultSet.getString("last_name"), resultSet.getString("first_name"),
                        resultSet.getString("gender"), resultSet.getString("birthdate"), resultSet.getString("address"), resultSet.getString("phone_number")));
            }
            // Đóng kết nối
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean deleteEmployeeById(int id) {
        String query = "UPDATE employee SET deleted = 1 WHERE employee_id = ?";
        
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
    public boolean resetHoursWorkedEmployee() {
        String query = "UPDATE employee SET hours_worked = 0";
        
        try (Connection connection = Database.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
               
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        } 
    }
}
