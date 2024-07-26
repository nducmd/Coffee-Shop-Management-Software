/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.EmployeeDAO;
import DAO.SalesHistoryDAO;
import Model.Employee;
import Model.People;
import Model.PurchaseHistory;
import Model.Session;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author nducmd
 */
public class EmployeeInfoFormController implements Initializable {

    private final ObservableList<String> genderList = FXCollections.observableArrayList("Nam", "Nữ");
    private final ObservableList<String> positionList = FXCollections.observableArrayList("Quản lí", "Nhân viên");
    private ObservableList<PurchaseHistory> purchaseHistoryList = FXCollections.observableArrayList();

    @FXML
    private TextField addressField;

    @FXML
    private DatePicker birthdatePicker;

    @FXML
    private Button changeButton;

    @FXML
    private TableColumn<PurchaseHistory, String> customerColumn;

    @FXML
    private Button deleteEmployeeButton;

    @FXML
    private TextField firstNameField;

    @FXML
    private ComboBox<String> genderBox;

    @FXML
    private ComboBox<String> positionBox;

    @FXML
    private TableColumn<PurchaseHistory, Integer> idColumn;

    @FXML
    private TextField idField;

    @FXML
    private TextField lastNameField;

    @FXML
    private PasswordField passField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField hoursWorkedField;

    @FXML
    private PasswordField rePassField;

    @FXML
    private TableView<PurchaseHistory> purchaseHistoryTable;

    @FXML
    private Button saveButton;

    @FXML
    private TableColumn<PurchaseHistory, String> tableColumn;

    @FXML
    private TextField tempSalaryField;

    @FXML
    private TableColumn<PurchaseHistory, String> timeColumn;

    @FXML
    private TableColumn<PurchaseHistory, String> totalMoneyColumn;

    @FXML
    private TableColumn<PurchaseHistory, Button> viewColumn;

    private Alert alert;

    @FXML
    void changeInfo(ActionEvent event) {
        if (!Session.currEmployee.getPosition().toLowerCase().equals("nhân viên")) {
            positionBox.setEditable(true);
            positionBox.setMouseTransparent(false);
        }
        firstNameField.setEditable(true);
        lastNameField.setEditable(true);
        genderBox.setEditable(true);
        genderBox.setMouseTransparent(false);
        birthdatePicker.setMouseTransparent(false);
        genderBox.setValue(currEmployee.getGender());
        positionBox.setValue(currEmployee.getPosition());
        addressField.setEditable(true);
        phoneNumberField.setEditable(true);
        passField.setEditable(true);
        birthdatePicker.setEditable(true);
        rePassField.setEditable(true);
        changeButton.setVisible(false);
        saveButton.setVisible(true);
        
    }

    @FXML
    void deleteEmployee(ActionEvent event) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Bạn chắc chắn muốn xoá nhân viên không?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)) {
            EmployeeDAO ed = new EmployeeDAO();
            if (ed.deleteEmployeeById(currEmployee.getEmployeeId())) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setContentText("Xoá nhân viên thành công!");
                alert.showAndWait();
                deleteEmployeeButton.getScene().getWindow().hide();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Xoá nhân viên không thành công!");
                alert.showAndWait();
            }
        }

    }

    @FXML
    void saveInfo(ActionEvent event) throws SQLException {
        if (!passField.getText().equals(rePassField.getText())) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText("Mật khẩu không khớp!");
            alert.showAndWait();

        } else if (firstNameField.getText().equals("")
                || lastNameField.getText().equals("")
                || birthdatePicker.getValue().toString().equals("")
                || genderBox.getValue().equals("")
                || addressField.getText().equals("")
                || phoneNumberField.getText().equals("")
                || passField.getText().equals("")
                || positionBox.getValue().equals("")) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText("Vui lòng điền đủ thông tin!");
            alert.showAndWait();

        } else {
            String[] tmpName = People.standardName(lastNameField.getText() + " " + firstNameField.getText()).split("\\s+");

            String lastName = "";
            for (int i = 0; i < tmpName.length - 1; i++) {
                lastName += tmpName[i] + " ";
            }
            lastName = lastName.trim();
            String firstName = tmpName[tmpName.length - 1];

//            Employee e = new Employee();
//            e.setAddress(addressField.getText());
//            e.setBirthdate(birthdatePicker.getValue().toString());
//            e.setEmployeeId(Integer.parseInt(idField.getText()));
//            e.setFirstName(firstName);
//            e.setGender(genderBox.getValue());
//            e.setHoursWorked(Integer.parseInt(hoursWorkedField.getText()));
//            e.setLastName(lastName);
//            e.setPassword(passField.getText());
//            e.setPhoneNumber(phoneNumberField.getText());
//            e.setPosition(positionBox.getValue());
            Employee e = new Employee(Integer.parseInt(idField.getText()), passField.getText(), Integer.parseInt(hoursWorkedField.getText()),
                    positionBox.getValue(), lastName, firstName, genderBox.getValue(),
                    birthdatePicker.getValue().toString(), addressField.getText(), phoneNumberField.getText());
            EmployeeDAO ed = new EmployeeDAO();

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Bạn chắc chắn muốn lưu thông tin không?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                // nếu thay số điện thoại thì kiểm tra trùng
                if (!phoneNumberField.getText().equals(currEmployee.getPhoneNumber()) && ed.checkPhoneNumberExists(e.getPhoneNumber())) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setContentText("Số điện thoại đã tồn tại!");
                    alert.showAndWait();
                } else if (ed.updateEmployee(e)) {
                    firstNameField.setEditable(false);
                    lastNameField.setEditable(false);
                    genderBox.setEditable(false);
                    positionBox.setEditable(false);
                    addressField.setEditable(false);
                    phoneNumberField.setEditable(false);
                    passField.setEditable(false);
                    rePassField.setEditable(false);
                    birthdatePicker.setEditable(false);
                    changeButton.setVisible(true);
                    saveButton.setVisible(false);
                    genderBox.setMouseTransparent(true);
                    positionBox.setMouseTransparent(true);
                    birthdatePicker.setMouseTransparent(true);

                    currEmployee = e;
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setContentText("Cập nhật thông tin thành công!");
                    alert.showAndWait();
                    saveButton.getScene().getWindow().hide();
                }
            }

        }
    }

    public void displayEmployeeInfo(Employee employee) throws SQLException {
        this.currEmployee = employee;
        initInfo();
        initPurchaseHistoryTable();
        purchaseHistoryList.clear();
        SalesHistoryDAO shd = new SalesHistoryDAO();
        purchaseHistoryList.addAll(shd.getSalesHistoryList(currEmployee.getEmployeeId()));
    }

    public void initPurchaseHistoryTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseHistoryId"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        tableColumn.setCellValueFactory(new PropertyValueFactory<>("table"));
        totalMoneyColumn.setCellValueFactory(new PropertyValueFactory<>("totalMoney"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        viewColumn.setCellValueFactory(param -> {
            PurchaseHistory ph = param.getValue();
            Button viewButton = new Button("Xem");
            viewButton.setOnAction(event -> {
                InvoiceReportController irc = new InvoiceReportController();
                irc.showInvoice(ph.getPurchaseHistoryId());
            });
            return new SimpleObjectProperty<>(viewButton);
        });
        purchaseHistoryTable.setItems(purchaseHistoryList);
    }

    public void initInfo() {
        firstNameField.setText(currEmployee.getFirstName());
        lastNameField.setText(currEmployee.getLastName());
        genderBox.setValue(currEmployee.getGender());
        positionBox.setValue(currEmployee.getPosition());

        addressField.setText(currEmployee.getAddress());
        phoneNumberField.setText(currEmployee.getPhoneNumber());
        passField.setText(currEmployee.getPassword());
        idField.setText(Integer.toString(currEmployee.getEmployeeId()));
        hoursWorkedField.setText(Integer.toString(currEmployee.getHoursWorked()));
        tempSalaryField.setText(Integer.toString(25000 * currEmployee.getHoursWorked()));
        birthdatePicker.setValue(LocalDate.parse(currEmployee.getBirthdate()));
        if (!Session.currEmployee.getPosition().toLowerCase().equals("nhân viên")) {
            rePassField.setPromptText(currEmployee.getPassword());
            if (currEmployee.getEmployeeId() != Session.currEmployee.getEmployeeId()) {
                deleteEmployeeButton.setVisible(true);
            }
        }
    }
    private Employee currEmployee;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        genderBox.setItems(genderList);
        genderBox.setEditable(false);
        genderBox.setMouseTransparent(true);
        positionBox.setItems(positionList);
        positionBox.setEditable(false);
        positionBox.setMouseTransparent(true);
        birthdatePicker.setMouseTransparent(true);
        try {
            displayEmployeeInfo(Session.currEmployee);
            // TODO
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeInfoFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
