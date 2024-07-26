/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.CustomerDAO;
import DAO.SalesHistoryDAO;
import Model.Customer;
import Model.People;
import Model.PurchaseHistory;
import Model.Session;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author nducmd
 */
public class CustomerInfoFormController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private final ObservableList<String> genderList = FXCollections.observableArrayList("Nam", "Nữ");
    private ObservableList<PurchaseHistory> purchaseHistoryList = FXCollections.observableArrayList();
    
    @FXML
    private TextField addressField;

    @FXML
    private DatePicker birthdatePicker;

    @FXML
    private Button changeButton;
    
    @FXML
    private Button deleteCustomerButton;

    @FXML
    private TableColumn<PurchaseHistory, String> employeeColumn;

    @FXML
    private TextField firstNameField;

    @FXML
    private ComboBox<String> genderBox;

    @FXML
    private TableColumn<PurchaseHistory, Integer> idColumn;

    @FXML
    private TextField idField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField pointField;

    @FXML
    private TableView<PurchaseHistory> purchaseHistoryTable;

    @FXML
    private Button saveButton;

    @FXML
    private TableColumn<PurchaseHistory, String> tableColumn;

    @FXML
    private TextField tierField;

    @FXML
    private TableColumn<PurchaseHistory, String> timeColumn;

    @FXML
    private TableColumn<PurchaseHistory, String> totalMoneyColumn;
    
    @FXML
    private TableColumn<PurchaseHistory, Button> viewColumn;
    
    private Alert alert;

    @FXML
    void changeInfo(ActionEvent event) {
        firstNameField.setEditable(true);
        lastNameField.setEditable(true);
        genderBox.setEditable(true);
        genderBox.setMouseTransparent(false);
        genderBox.setValue(currCustomer.getGender());
        addressField.setEditable(true);
        phoneNumberField.setEditable(true);
        birthdatePicker.setEditable(true);
        birthdatePicker.setMouseTransparent(false);
        changeButton.setVisible(false);
        saveButton.setVisible(true);
    }

    @FXML
    void saveInfo(ActionEvent event) throws SQLException {
        if (firstNameField.getText().equals("")
                || lastNameField.getText().equals("")
                || birthdatePicker.getValue().toString().equals("")
                || genderBox.getValue().equals("")
                || addressField.getText().equals("")
                || phoneNumberField.getText().equals("")) {

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

//            Customer c = new Customer();
//            c.setAddress(addressField.getText());
//            c.setBirthdate(birthdatePicker.getValue().toString());
//            c.setCustomerId(Integer.parseInt(idField.getText()));
//            c.setFirstName(firstName);
//            c.setGender(genderBox.getValue());
//            c.setLastName(lastName);
//            c.setPhoneNumber(phoneNumberField.getText());
//            c.setCustomerPoint(Integer.parseInt(pointField.getText()));
//            c.setCustomerTier(tierField.getText());
            
            Customer c = new Customer(Integer.parseInt(idField.getText()), Integer.parseInt(pointField.getText()), tierField.getText(),
                    lastName, firstName, genderBox.getValue(),
                    birthdatePicker.getValue().toString(), addressField.getText(), phoneNumberField.getText());
            CustomerDAO cd = new CustomerDAO();

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Bạn chắc chắn muốn lưu thông tin không?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                // nếu thay số điện thoại thì kiểm tra trùng
                if (!phoneNumberField.getText().equals(currCustomer.getPhoneNumber()) && cd.checkPhoneNumberExists(c.getPhoneNumber())) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setContentText("Số điện thoại đã tồn tại!");
                    alert.showAndWait();
                } else if (Session.checkAge(birthdatePicker.getValue())) {
                    if (cd.updateCustomer(c)) {
                        firstNameField.setEditable(false);
                        lastNameField.setEditable(false);
                        genderBox.setEditable(false);
                        genderBox.setMouseTransparent(true);
                        addressField.setEditable(false);
                        phoneNumberField.setEditable(false);
                        birthdatePicker.setEditable(false);
                        birthdatePicker.setMouseTransparent(true);
                        changeButton.setVisible(true);
                        saveButton.setVisible(false);

                        currCustomer = c;
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Cập nhật thông tin thành công!");
                        alert.showAndWait();
                        initInfo();
                    }
                }
            }

        }

    }

    @FXML
    void deleteCustomer(ActionEvent event) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Bạn chắc chắn muốn xoá khách hàng không?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)) {
            CustomerDAO cd = new CustomerDAO();
            if (cd.deleteCustomerById(currCustomer.getCustomerId())) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setContentText("Xoá khách hàng thành công!");
                alert.showAndWait();
                deleteCustomerButton.getScene().getWindow().hide();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setContentText("Xoá khách hàng không thành công!");
                alert.showAndWait();
            }
        }

    }

    private Customer currCustomer;

    public void displayCustomerInfo(Customer customer) throws SQLException {
        this.currCustomer = customer;
        initInfo();
        initPurchaseHistoryTable();
        purchaseHistoryList.clear();
        SalesHistoryDAO shd = new SalesHistoryDAO();
        purchaseHistoryList.addAll(shd.getPurchaseHistoryList(currCustomer.getCustomerId()));
    }

    public void initInfo() {
        firstNameField.setText(currCustomer.getFirstName());
        lastNameField.setText(currCustomer.getLastName());
        genderBox.setValue(currCustomer.getGender());
        addressField.setText(currCustomer.getAddress());
        phoneNumberField.setText(currCustomer.getPhoneNumber());
        idField.setText(Integer.toString(currCustomer.getCustomerId()));
        birthdatePicker.setValue(LocalDate.parse(currCustomer.getBirthdate()));
        pointField.setText(Integer.toString(currCustomer.getCustomerPoint()));
        tierField.setText(currCustomer.getCustomerTier());
    }

    public void initPurchaseHistoryTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseHistoryId"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        tableColumn.setCellValueFactory(new PropertyValueFactory<>("table"));
        totalMoneyColumn.setCellValueFactory(new PropertyValueFactory<>("totalMoney"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        genderBox.setItems(genderList);
        genderBox.setEditable(false);
        genderBox.setMouseTransparent(true);
        birthdatePicker.setMouseTransparent(true);
        // TODO
    }

}
