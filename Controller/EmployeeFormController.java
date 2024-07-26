/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.EmployeeDAO;
import Model.Employee;
import Model.ExportExcel;
import Model.MySence;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author nducmd
 */
public class EmployeeFormController implements Initializable {

    @FXML
    private TableColumn<Employee, Integer> IdColumn;

    @FXML
    private Button addEmployeeButton;

    @FXML
    private TableColumn<Employee, String> addressColumn;

    @FXML
    private TableColumn<Employee, String> birthdateColumn;

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private Button exportButton;

    @FXML
    private TableColumn<Employee, String> firstNameColumn;

    @FXML
    private TableColumn<Employee, String> genderColumn;

    @FXML
    private TableColumn<Employee, Integer> hoursWorkedColumn;

    @FXML
    private TableColumn<Employee, String> lastNameColumn;

    @FXML
    private TableColumn<Employee, String> phoneNumberColumn;

    @FXML
    private TableColumn<Employee, String> positionColumn;

    @FXML
    private TableColumn<Employee, Integer> salaryColumn;

    @FXML
    private TextField searchEmployeeField;

    @FXML
    private TableColumn<Employee, Button> viewColumn;

    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    @FXML
    void addEmployee(ActionEvent event) throws IOException, SQLException {
        MySence ms = new MySence("/View/RegisterEmployeeForm.fxml", "Đăng ký", false, false);
        ms.showAndWait();
        searchEmployee();
    }

    @FXML
    void exportToExcel(ActionEvent event) {
        ExportExcel ex = new ExportExcel();
        ex.exportEmployeeList(employeeTable);
    }

    public void initEmployeeTable() {
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        birthdateColumn.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        hoursWorkedColumn.setCellValueFactory(new PropertyValueFactory<>("hoursWorked"));
        salaryColumn.setCellValueFactory(cellData -> {
            int hoursWorked = cellData.getValue().getHoursWorked();
            int salary = hoursWorked * 25000;
            return new SimpleObjectProperty<>(salary);
        });
        viewColumn.setCellValueFactory(param -> {
            Employee employee = param.getValue();
            Button viewButton = new Button("Xem");
            viewButton.setOnAction(event -> {
                // Tạo một cửa sổ mới
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EmployeeInfoForm.fxml"));
                Parent root;
                try {
                    root = loader.load();
                    Scene scene = new Scene(root);
                    // Lấy controller
                    EmployeeInfoFormController controller = loader.getController();
                    controller.displayEmployeeInfo(employee);
                    // Hiển thị
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Thông tin nhân viên");
                    stage.setResizable(false);
                    stage.showAndWait();
                    searchEmployee();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
            return new SimpleObjectProperty<>(viewButton);
        });
        employeeTable.setItems(employeeList);
    }

    public void searchEmployee() throws SQLException {
        String name = searchEmployeeField.getText();
        EmployeeDAO ed = new EmployeeDAO();
        employeeList.clear();
        employeeList.addAll(ed.getEmployeeList(name));
        employeeTable.refresh();
    }

    public void resetSalary() throws IOException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Bạn chắc chắn muốn chốt lương không?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)) {
            EmployeeDAO ed = new EmployeeDAO();
            if (!ed.resetHoursWorkedEmployee()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setContentText("Chốt lương không thành công!");
                alert.showAndWait();
            } else {
                ExportExcel ex = new ExportExcel();
                ex.exportEmployeeList(employeeTable);
            }
            searchEmployee();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        initEmployeeTable();
        try {
            searchEmployee();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
