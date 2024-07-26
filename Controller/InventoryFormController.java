/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.ProductDAO;
import Model.MySence;
import Model.Product;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.Button;
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
public class InventoryFormController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private Button exportButton;

    @FXML
    private TableColumn<Product, Integer> idColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Integer> priceColumn;

    @FXML
    private TextField searchProductField;

    @FXML
    private TableColumn<Product, String> statusColumn;

    @FXML
    private TableColumn<Product, String> unitColumn;

    @FXML
    private TableColumn<Product, Button> viewColumn;
    
    private ObservableList<Product> productList = FXCollections.observableArrayList();
    
    @FXML
    void addProduct(ActionEvent event) throws IOException, SQLException {
        MySence ms = new MySence("/View/AddProductForm.fxml", "Thêm sản phẩm", false, false);
        ms.showAndWait();
        searchProduct();
    }
    
    @FXML
    void exportToExcel(ActionEvent event) {

    }
    public void initProductTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        viewColumn.setCellValueFactory(param -> {
            
            Product product = param.getValue();
            Button viewButton = new Button("Xem");
            viewButton.setOnAction(event -> {
                // Tạo một cửa sổ mới
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ProductInfoForm.fxml"));
                Parent root;
                try {
                    root = loader.load();
                    Scene scene = new Scene(root);
                    scene.getRoot().requestFocus();
                    // Lấy controller
                    ProductInfoFormController controller = loader.getController();
                    controller.displayProductInfo(product);

                    // Hiển thị
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Thông tin sản phẩm");
                    stage.setResizable(false);
                    stage.showAndWait();
                    searchProduct();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
            return new SimpleObjectProperty<>(viewButton);
        });
        productTable.setItems(productList);
    }
    
    public void searchProduct() throws SQLException {
        String name = searchProductField.getText();
        ProductDAO pd = new ProductDAO();
        productList.clear();
        productList.addAll(pd.getProductList(name));
        productTable.refresh();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initProductTable();
        try {
            searchProduct();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }    
    
}
