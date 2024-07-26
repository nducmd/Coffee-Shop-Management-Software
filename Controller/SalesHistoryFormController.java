/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.SalesHistoryDAO;
import Model.ExportExcel;
import Model.PurchaseHistory;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
public class SalesHistoryFormController implements Initializable {

    @FXML
    private TableColumn<PurchaseHistory, PurchaseHistory> idColumn;

    @FXML
    private TableColumn<PurchaseHistory, String> customerColumn;

    @FXML
    private TableView<PurchaseHistory> purchaseHistoryTable;

    @FXML
    private TableColumn<PurchaseHistory, String> dateColumn;

    @FXML
    private TableColumn<PurchaseHistory, String> employeeColumn;

    @FXML
    private Button exportButton;

    @FXML
    private TableColumn<PurchaseHistory, String> phoneColumn;

    @FXML
    private TextField searchHistoryField;

    @FXML
    private TableColumn<PurchaseHistory, String> tableColumn;

    @FXML
    private TableColumn<PurchaseHistory, String> totalColumn;

    @FXML
    private TableColumn<PurchaseHistory, Button> viewColumn;
    
    
    @FXML
    private DatePicker startDate;
    
    @FXML
    private DatePicker endDate;
    
    private ObservableList<PurchaseHistory> purchaseHistoryList = FXCollections.observableArrayList();
    
    @FXML
    void exportToExcel(ActionEvent event) {
        ExportExcel ex = new ExportExcel();
        ex.exportHistoryList(purchaseHistoryTable, startDate.getValue().toString(), endDate.getValue().toString());
    }

    
    public void searchHistory() throws SQLException {
        String input = searchHistoryField.getText();
        purchaseHistoryList.clear();
        SalesHistoryDAO shd = new SalesHistoryDAO();
        purchaseHistoryList.addAll(shd.getHistoryList(input, startDate.getValue().toString(), endDate.getValue().toString()));
    }
    public void initPurchaseHistoryTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseHistoryId"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        tableColumn.setCellValueFactory(new PropertyValueFactory<>("table"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalMoney"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
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
        // TODO
        initPurchaseHistoryTable();
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        startDate.setValue(firstDayOfMonth);
        LocalDate today = LocalDate.now();
        endDate.setValue(today);
        try {
            searchHistory();
        } catch (SQLException ex) {
            Logger.getLogger(SalesHistoryFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
