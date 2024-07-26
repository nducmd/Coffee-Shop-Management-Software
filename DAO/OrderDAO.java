/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Controller.InvoiceReportController;
import static Controller.OrderFormController.orderList;
import Model.OrderData;
import Model.Product;
import Model.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author nducmd
 */
public class OrderDAO {

    public OrderDAO() {
    }
    
    public ObservableList<Product> getMenuData(String name) throws SQLException {
        ObservableList<Product> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM product WHERE status = ? AND product_name LIKE ? AND deleted = 0";
        try (Connection connection = Database.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "Hoạt động");
            preparedStatement.setString(2, "%" + name + "%");
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
    
    public void saveInvoice() {
        String query = "INSERT INTO invoice (sales_history_id, product_id, quantity, price, product_name, unit, unit_price) VALUES (?, ?, ?, ?,?, ?, ?)";
        SalesHistoryDAO shd = new SalesHistoryDAO();
        int salesId = shd.getLastestSalesHistoryId();
        try (Connection connection = Database.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (OrderData order : orderList) {
                preparedStatement.setInt(1, salesId);
                preparedStatement.setInt(2, order.getId());
                preparedStatement.setInt(3, order.getQuantity());
                preparedStatement.setInt(4, order.getPrice()*order.getQuantity());
                preparedStatement.setString(5, order.getName());
                preparedStatement.setString(6, order.getUnit());
                preparedStatement.setInt(7, order.getPrice());
                preparedStatement.executeUpdate();
            }
            // Đóng kết nối
            preparedStatement.close();
            connection.close();
            // in hoa don
            InvoiceReportController irc = new InvoiceReportController();
            irc.showInvoice(salesId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
