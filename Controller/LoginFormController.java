/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Session;
import DAO.EmployeeDAO;
import Model.MySence;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author nducmd
 */
public class LoginFormController implements Initializable {

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    private Alert alert;

    @FXML
    public void loginButton(ActionEvent event) throws SQLException, IOException {

        String phoneNumber = usernameField.getText();
        String password = passwordField.getText();
        EmployeeDAO ed = new EmployeeDAO();
        if (ed.authenticate(phoneNumber, password)) {
            //System.out.println("Dang nhap thanh cong");
            
            Session.currEmployee = ed.getEmployeeByPhoneNumber(phoneNumber);
           
            if (Session.currEmployee.getPosition().toLowerCase().equals("nhân viên")) {
                MySence ms = new MySence("/View/OrderForm.fxml", "Quản lí cửa hàng", false, true);
                ms.showAndNotClose();
            } else {
                MySence ms = new MySence("/View/ManagementForm.fxml", "Quản lí cửa hàng", false, true);
                ms.showAndNotClose();
            }
  
            Session.setStartTime();
            loginButton.getScene().getWindow().hide();
            
            
        } else {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Sai tài khoản/mật khẩu");
            alert.showAndWait();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
