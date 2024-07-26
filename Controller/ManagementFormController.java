/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.EmployeeDAO;
import Model.MySence;
import Model.Session;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author nducmd
 */
public class ManagementFormController implements Initializable {

    @FXML
    private Label employeeName;
    
    @FXML
    private Button customersButton;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button employeeButton;

    @FXML
    private Label currentDateLable;
    
    @FXML
    private Label employeePositon;

    @FXML
    private Button exchangeViewButton;

    @FXML
    private Button inventoryButton;

    @FXML
    private Button logoutButton;

    @FXML
    private BorderPane managementPane;

    @FXML
    private Button salesHistoryButton;

    @FXML
    private Button showEmployeeInfoButton;

    public void switchForm(ActionEvent event) throws IOException {

        if (event.getSource() == dashboardButton) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/DashboardForm.fxml"));
            AnchorPane tmp = loader.load();
            managementPane.setCenter(tmp);

        } else if (event.getSource() == inventoryButton) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/InventoryForm.fxml"));
            AnchorPane tmp = loader.load();
            managementPane.setCenter(tmp);

        } else if (event.getSource() == employeeButton) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EmployeeForm.fxml"));
            AnchorPane tmp = loader.load();
            managementPane.setCenter(tmp);

        } else if (event.getSource() == customersButton) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/CustomersForm.fxml"));
            AnchorPane tmp = loader.load();

            managementPane.setCenter(tmp);
        } else if (event.getSource() == salesHistoryButton) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/SalesHistoryForm.fxml"));
            AnchorPane tmp = loader.load();

            managementPane.setCenter(tmp);
        }

    }

    public void logout() {

        try {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có muốn đăng xuất không?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                Session.setEndTime();
                EmployeeDAO ed = new EmployeeDAO();
                ed.updateHoursWorkedEmployee();
                logoutButton.getScene().getWindow().hide();
                MySence ms = new MySence("/View/LoginForm.fxml", "Quản lí cửa hàng", false, false);
                ms.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void exchangeView() throws IOException {

        MySence ms = new MySence("/View/OrderForm.fxml", "Quản lí cửa hàng", false, true);
        ms.showAndNotClose();
        exchangeViewButton.getScene().getWindow().hide();
    }

    public void displayName() {
        employeePositon.setText(Session.currEmployee.getPosition());
        employeeName.setText(Session.currEmployee.getLastName() + " " + Session.currEmployee.getFirstName());
    }
    private void displayCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd-MM-yyyy", new Locale("vi", "VN"));
        String currentTime = dateFormat.format(new Date());

        currentDateLable.setText(currentTime);
    }
    public void showEmployeeInfo() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/EmployeeInfoForm.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.setTitle("Thông tin nhân viên");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayName();        
        displayCurrentDate();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/DashboardForm.fxml"));
        AnchorPane tmp;
        try {
            tmp = loader.load();
            managementPane.setCenter(tmp);
        } catch (IOException ex) {
            Logger.getLogger(ManagementFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }

}
