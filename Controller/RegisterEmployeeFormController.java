/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.EmployeeDAO;
import Model.Employee;
import Model.People;
import Model.Session;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
public class RegisterEmployeeFormController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private TextField addressField;

    @FXML
    private DatePicker birthdateField;

    @FXML
    private TextField firstNameField;

    @FXML
    private ComboBox<String> genderBox;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneField;

    @FXML
    private ComboBox<String> positionBox;

    private final ObservableList<String> genderList = FXCollections.observableArrayList("Nam", "Nữ");
    private final ObservableList<String> positionList = FXCollections.observableArrayList("Quản lí", "Nhân viên");
    private Alert alert;
    @FXML
    void addEmployee(ActionEvent event) throws SQLException {
        String phone = phoneField.getText();
        EmployeeDAO ed = new EmployeeDAO();
        if (positionBox.getValue().equals("") || birthdateField.getValue() == null || 
                phone.equals("") || addressField.getText().equals("") || 
                lastNameField.getText().equals("") || firstNameField.getText().equals("") || 
                genderBox.getValue().equals("")) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText("Vui lòng nhập đủ thông tin!");
            alert.showAndWait();
        } else if (ed.checkPhoneNumberExists(phone)) {
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
                String position = positionBox.getValue();
         
                // Thêm nhân viên mới vào cơ sở dữ liệu
                // Password mặc định khi tạo nhân viên 123456
                Employee e = new Employee(0, "123456", 0, position, lastName, firstName, gender, birthdate, address, phone);
                if (Session.checkAge(birthdateField.getValue())) {
                    if (ed.add(e)) {
                        // Thông báo đăng ký thành công
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Đăng ký nhân viên thành công!");
                        alert.showAndWait();
                        addButton.getScene().getWindow().hide();
                    } else {
                        // Thông báo đăng ký không thành công
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setContentText("Đăng ký nhân viên không thành công!");
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
        positionBox.setItems(positionList);
    }

}
