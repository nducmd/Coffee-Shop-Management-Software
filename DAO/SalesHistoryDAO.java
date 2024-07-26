/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.PurchaseHistory;
import Model.SalesHistory;
import Model.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author nducmd
 */
public class SalesHistoryDAO {

    public SalesHistoryDAO() {
    }

    public boolean add(SalesHistory s) throws SQLException {
        String query = "INSERT INTO sales_history (temp_money, discount, vat, total_money, received_money, return_money, sales_date, employee_id, [table], customer_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.connectDB();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, s.getTemporarilyMoney());
            preparedStatement.setString(2, s.getDiscount());
            preparedStatement.setString(3, s.getVat());
            preparedStatement.setString(4, s.getTotalMoney());
            preparedStatement.setString(5, s.getReceivedMoney());
            preparedStatement.setString(6, s.getReturnMoney());
            preparedStatement.setString(7, s.getSaleDate());
            preparedStatement.setInt(8, s.getEmployeeId());
            preparedStatement.setInt(9, s.getTable());
            preparedStatement.setInt(10, s.getCustomerId());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public int getLastestSalesHistoryId() {
        String query = "SELECT TOP 1 sales_history_id FROM sales_history ORDER BY sales_history_id DESC";
        try (Connection connection = Database.connectDB();
                Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                int lastestSalesHistoryId = resultSet.getInt("sales_history_id");
                return lastestSalesHistoryId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public ObservableList<PurchaseHistory> getPurchaseHistoryList(int customerId) throws SQLException {
        ObservableList<PurchaseHistory> list = FXCollections.observableArrayList();
        //String query = "SELECT * FROM sales_history WHERE customer_id = ?";
        String query = "SELECT dbo.customer.*, dbo.employee.*, dbo.sales_history.*,\n" +
                        "CONCAT(dbo.customer.last_name ,' ' ,dbo.customer.first_name) AS customer_name,\n" +
                        "CONCAT(dbo.employee.last_name ,' ' ,dbo.employee.first_name) AS employee_name,\n" +
                        "dbo.customer.phone_number AS customer_phone\n" +
                        "FROM     dbo.customer INNER JOIN\n" +
                        "dbo.sales_history ON dbo.customer.customer_id = dbo.sales_history.customer_id INNER JOIN\n" +
                        "dbo.employee ON dbo.sales_history.employee_id = dbo.employee.employee_id\n" +
                        "WHERE dbo.customer.customer_id = ?";
        try (Connection connection = Database.connectDB();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new PurchaseHistory(resultSet.getInt("sales_history_id"), resultSet.getString("total_money"),
                        resultSet.getString("sales_date"), resultSet.getString("employee_name"), resultSet.getString("table"),
                        resultSet.getString("customer_name"), resultSet.getString("customer_phone")));
            }
            // Đóng kết nối
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<PurchaseHistory> getSalesHistoryList(int employeeId) throws SQLException {
        ObservableList<PurchaseHistory> list = FXCollections.observableArrayList();
        //String query = "SELECT * FROM sales_history WHERE employee_id = ?";
        String query = "SELECT dbo.customer.*, dbo.employee.*, dbo.sales_history.*,\n" +
                        "CONCAT(dbo.customer.last_name ,' ' ,dbo.customer.first_name) AS customer_name,\n" +
                        "CONCAT(dbo.employee.last_name ,' ' ,dbo.employee.first_name) AS employee_name,\n" +
                        "dbo.customer.phone_number AS customer_phone\n" +
                        "FROM     dbo.customer INNER JOIN\n" +
                        "dbo.sales_history ON dbo.customer.customer_id = dbo.sales_history.customer_id INNER JOIN\n" +
                        "dbo.employee ON dbo.sales_history.employee_id = dbo.employee.employee_id\n" +
                        "WHERE dbo.employee.employee_id = ?";
        try (Connection connection = Database.connectDB();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new PurchaseHistory(resultSet.getInt("sales_history_id"), resultSet.getString("total_money"),
                        resultSet.getString("sales_date"), resultSet.getString("employee_name"), resultSet.getString("table"),
                        resultSet.getString("customer_name"), resultSet.getString("customer_phone")));
            }
            // Đóng kết nối
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<PurchaseHistory> getHistoryList(String input, String startDate, String endDate) throws SQLException {
        ObservableList<PurchaseHistory> list = FXCollections.observableArrayList();
        //String query = "SELECT * FROM sales_history WHERE (customer_name LIKE ? OR customer_phone LIKE ? OR employee_name LIKE ?) AND sales_date >= ? AND sales_date <= ?";
        String query = "SELECT dbo.customer.*, dbo.employee.*, dbo.sales_history.*,\n" +
                        "CONCAT(dbo.customer.last_name ,' ' ,dbo.customer.first_name) AS customer_name,\n" +
                        "CONCAT(dbo.employee.last_name ,' ' ,dbo.employee.first_name) AS employee_name,\n" +
                        "dbo.customer.phone_number AS customer_phone\n" +
                        "FROM     dbo.customer INNER JOIN\n" +
                        "dbo.sales_history ON dbo.customer.customer_id = dbo.sales_history.customer_id INNER JOIN\n" +
                        "dbo.employee ON dbo.sales_history.employee_id = dbo.employee.employee_id\n" +
                        "WHERE (dbo.customer.last_name LIKE ? OR dbo.customer.phone_number LIKE ? OR dbo.employee.last_name LIKE ?) AND sales_date >= ? AND sales_date <= ?";
        try (Connection connection = Database.connectDB();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + input + "%");
            preparedStatement.setString(2, "%" + input + "%");
            preparedStatement.setString(3, "%" + input + "%");
            preparedStatement.setString(4, startDate + "T00:00:00.000");
            preparedStatement.setString(5, endDate + "T23:59:59.999");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new PurchaseHistory(resultSet.getInt("sales_history_id"), resultSet.getString("total_money"),
                        resultSet.getString("sales_date"), resultSet.getString("employee_name"), resultSet.getString("table"),
                        resultSet.getString("customer_name"), resultSet.getString("customer_phone")));
            }
            // Đóng kết nối
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int[] getRevenueByDay(int month) {
        int[] revenue = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };
        String startDate = Integer.toString(LocalDate.now().getYear()) + "-" + String.format("%02d", month) + "-01T00:00:00.000";
        String endDate = Integer.toString(LocalDate.now().getYear()) + "-" + String.format("%02d", month) + "-31T23:59:59.999";
        String query = "SELECT * FROM sales_history WHERE sales_date >= ? AND sales_date <= ?";
        try (Connection connection = Database.connectDB();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String dateTimeString = resultSet.getString("sales_date");
                if (dateTimeString.length() == 22) {
                    dateTimeString = dateTimeString + "0";
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
                int ngay = dateTime.getDayOfMonth();
                revenue[ngay-1] += Integer.parseInt(resultSet.getString("total_money"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return revenue;
    }
    public int[] getTrafficByDay(int month) {
        int[] customer = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };
        String startDate = Integer.toString(LocalDate.now().getYear()) + "-" + String.format("%02d", month) + "-01T00:00:00.000";
        String endDate = Integer.toString(LocalDate.now().getYear()) + "-" + String.format("%02d", month) + "-31T23:59:59.999";
        String query = "SELECT * FROM sales_history WHERE sales_date >= ? AND sales_date <= ?";
        try (Connection connection = Database.connectDB();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String dateTimeString = resultSet.getString("sales_date");
                if (dateTimeString.length() == 22) {
                    dateTimeString = dateTimeString + "0";
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
                int ngay = dateTime.getDayOfMonth();
                customer[ngay-1] += 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }
    public int getTrafficToday() {
        int today = LocalDate.now().getDayOfMonth();
        String year = Integer.toString(LocalDate.now().getYear());
        int month = LocalDate.now().getMonthValue();
        String startDate = year + "-" + String.format("%02d", month) + "-" +String.format("%02d", today)+ "T00:00:00.000";
        String endDate = year + "-" + String.format("%02d", month) + "-" +String.format("%02d", today)+ "T23:59:59.999";
        
        String query = "SELECT COUNT(*) FROM sales_history WHERE sales_date >= ? AND sales_date <= ?";
        try (Connection connection = Database.connectDB();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int customerCount = resultSet.getInt(1);
                return customerCount;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public int getRevenueToday() {
        int today = LocalDate.now().getDayOfMonth();
        String year = Integer.toString(LocalDate.now().getYear());
        int month = LocalDate.now().getMonthValue();
        String startDate = year + "-" + String.format("%02d", month) + "-" +String.format("%02d", today)+ "T00:00:00.000";
        String endDate = year + "-" + String.format("%02d", month) + "-" +String.format("%02d", today)+ "T23:59:59.999";
        int revenue = 0;
        String query = "SELECT * FROM sales_history WHERE sales_date >= ? AND sales_date <= ?";
        try (Connection connection = Database.connectDB();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                revenue += Integer.parseInt(resultSet.getString("total_money"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return revenue;
    }
}
