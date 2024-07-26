/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.CustomerDAO;
import Model.People;
import Model.Customer;
import Model.Session;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author nducmd
 */
public class RegisterCustomerFormController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button addButton;

    @FXML
    private TextField addressField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private ComboBox<String> genderBox;

    @FXML
    private DatePicker birthdateField;

    @FXML
    private TextField phoneField;
    private Alert alert;
   

    private final ObservableList<String> genderList = FXCollections.observableArrayList("Nam", "Nữ");

    public void addCustomer() throws SQLException {

        String phone = phoneField.getText();
        CustomerDAO cd = new CustomerDAO();
        if (genderBox.getValue().equals("") || birthdateField.getValue() == null || 
                phone.equals("") || addressField.getText().equals("") || 
                lastNameField.getText().equals("") || firstNameField.getText().equals("")) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText("Vui lòng nhập đủ thông tin!");
            alert.showAndWait();
        } else if (cd.checkPhoneNumberExists(phone)) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText("Số điện thoại đã tồn tại!");
            alert.showAndWait();
        } else {
            try {
                String[] tmpName = People.standardName(lastNameField.getText() + " " + firstNameField.getText()).split("\\s+");

                String lastName = "";
                for (int i = 0; i < tmpName.length - 1; i++) {
                    lastName += tmpName[i] + " ";
                }
                lastName = lastName.trim();
                String firstName = tmpName[tmpName.length - 1];
                String address = People.standardName(addressField.getText());
                String gender = genderBox.getValue();
                String birthdate = birthdateField.getValue().toString();
                // Thêm khách hàng mới vào cơ sở dữ liệu
                Customer c = new Customer(lastName, firstName, gender, birthdate, address, phone);
                if (Session.checkAge(birthdateField.getValue())) {
                    if (cd.add(c)) {
                        // Thông báo đăng ký thành công
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Đăng ký khách hàng thành công!");
                        alert.showAndWait();
                        addButton.getScene().getWindow().hide();
                    } else {
                        // Thông báo đăng ký không thành công
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setContentText("Đăng ký khách hàng không thành công!");
                        alert.showAndWait();
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        genderBox.setItems(genderList);
    }

}
