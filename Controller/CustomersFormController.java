/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import DAO.CustomerDAO;
import Model.ExportExcel;
import Model.Customer;
import Model.MySence;
import java.io.IOException;
import java.sql.SQLException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author nducmd
 */
public class CustomersFormController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button exportButton;

    @FXML
    private TableColumn<Customer, Integer> IdColumn;

    @FXML
    private TableColumn<Customer, String> addressColumn;

    @FXML
    private TableColumn<Customer, String> birthdateColumn;

    @FXML
    private TableView<Customer> customersTable;

    @FXML
    private TableColumn<Customer, String> firstNameColumn;

    @FXML
    private TableColumn<Customer, String> genderColumn;

    @FXML
    private TableColumn<Customer, String> lastNameColumn;

    @FXML
    private TableColumn<Customer, String> phoneNumberColumn;

    @FXML
    private TableColumn<Customer, Integer> pointColumn;

    @FXML
    private TableColumn<Customer, String> tierColumn;

    @FXML
    private TableColumn<Customer, Button> viewColumn;
    @FXML
    private TextField searchCustomer;

    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    public void exportToExcel() {
        ExportExcel ex = new ExportExcel();
        ex.exportCustomerList(customersTable);
    }

    public void addCustomer() throws IOException, SQLException {
        MySence ms = new MySence("/View/RegisterCustomerForm.fxml", "Đăng ký", false, false);
        ms.showAndWait();
        searchCustomer();
    }

    public void initCustomersTable() {
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        birthdateColumn.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        pointColumn.setCellValueFactory(new PropertyValueFactory<>("customerPoint"));
        tierColumn.setCellValueFactory(new PropertyValueFactory<>("customerTier"));
        viewColumn.setCellValueFactory(param -> {
            Customer customer = param.getValue();
            Button viewButton = new Button("Xem");
            viewButton.setOnAction(event -> {
                // Tạo một cửa sổ mới
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/CustomerInfoForm.fxml"));
                Parent root;
                try {
                    root = loader.load();
                    Scene scene = new Scene(root);

                    // Lấy controller
                    CustomerInfoFormController controller = loader.getController();
                    controller.displayCustomerInfo(customer);

                    // Hiển thị
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Thông tin khách hàng");
                    stage.setResizable(false);
                    stage.showAndWait();
                    searchCustomer();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
            return new SimpleObjectProperty<>(viewButton);
        });
        customersTable.setItems(customerList);
    }

    public void searchCustomer() throws SQLException {
        String name = searchCustomer.getText();
        CustomerDAO cd = new CustomerDAO();
        customerList.clear();
        customerList.addAll(cd.getCustomerList(name));
        customersTable.refresh();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initCustomersTable();
        try {
            searchCustomer();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

}
