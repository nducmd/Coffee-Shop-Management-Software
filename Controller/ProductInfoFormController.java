/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.ProductDAO;
import Model.Product;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author nducmd
 */
public class ProductInfoFormController implements Initializable {

    @FXML
    private ImageView imageProduct;
    @FXML
    private Button changeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button importImageButton;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private Button saveButton;

    @FXML
    private ComboBox<String> statusBox;

    @FXML
    private TextField unitField;

    private final ObservableList<String> statusList = FXCollections.observableArrayList("Hoạt động", "Dừng");
    private Product currProduct;
    private String tempImagePath = "";

    public void displayProductInfo(Product product) {
        this.currProduct = product;
        initInfo();
    }

    public void initInfo() {
        imageProduct.setImage(new Image(currProduct.getImage(), 85, 100, false, true));
        nameField.setText(currProduct.getProductName());
        unitField.setText(currProduct.getUnit());
        statusBox.setValue(currProduct.getStatus());
        priceField.setText(Integer.toString(currProduct.getPrice()));
        tempImagePath = currProduct.getImage();
    }

    public void importImage() throws IOException {
        String projectDirectory = System.getProperty("user.dir");
        String imageDirectoryPath = projectDirectory + File.separator + "src" + File.separator + "Image";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(imageDirectoryPath));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Ảnh", "*.png")
        );

        // Hiển thị hộp thoại chọn tệp
        File selectedFile = fileChooser.showOpenDialog(imageProduct.getScene().getWindow());
        if (selectedFile != null) {
            if (selectedFile.getParent().equals(imageDirectoryPath)) {
                String imagePath = "/Image/" + selectedFile.getName();
                imageProduct.setImage(new Image(imagePath, 85, 100, false, true));
                tempImagePath = imagePath;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setContentText("Ảnh phải trong thư mục Image!");
                alert.showAndWait();
            }
        }
    }

    public void changeInfo() {
        statusBox.setEditable(true);
        statusBox.setMouseTransparent(false);
        statusBox.setValue(currProduct.getStatus());
        nameField.setEditable(true);
        unitField.setEditable(true);
        priceField.setEditable(true);
        importImageButton.setVisible(true);
        changeButton.setVisible(false);
        saveButton.setVisible(true);
    }

    public void saveInfo() {
        if (nameField.getText().equals("")
                || unitField.getText().equals("")
                || statusBox.getValue().equals("")
                || priceField.getText().equals("")) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText("Vui lòng điền đủ thông tin!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Bạn chắc chắn muốn lưu thông tin không?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                try {
                    if (priceField.getText().startsWith("-")) {
                        Integer price = Integer.parseInt("a" + priceField.getText());
                    }
                    Integer price = Integer.parseInt(priceField.getText());
                    Product product = new Product(currProduct.getId(), nameField.getText(), unitField.getText(), price, tempImagePath, statusBox.getValue());
                    ProductDAO pd = new ProductDAO();
                    if (pd.updateProduct(product)) {

                        currProduct = product;
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Cập nhật thông tin thành công!");
                        alert.showAndWait();
                        statusBox.setEditable(false);
                        statusBox.setMouseTransparent(true);
                        statusBox.setValue(currProduct.getStatus());
                        nameField.setEditable(false);
                        unitField.setEditable(false);
                        priceField.setEditable(false);
                        importImageButton.setVisible(false);
                        changeButton.setVisible(true);
                        saveButton.setVisible(false);
                        initInfo();
                    }
                } catch (Exception ex) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setContentText("Vui lòng nhập đúng thông tin!");
                    alert.showAndWait();
                }
            }

        }
    }

    public void deleteProduct() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Bạn chắc chắn muốn xoá sản phẩm không?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)) {
            ProductDAO pd = new ProductDAO();
            if (pd.deleteProductById(currProduct.getId())) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setContentText("Xoá sản phẩm thành công!");
                alert.showAndWait();
                deleteButton.getScene().getWindow().hide();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setContentText("Xoá sản phẩm không thành công!");
                alert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        statusBox.setItems(statusList);
        statusBox.setEditable(false);
        statusBox.setMouseTransparent(true);
        importImageButton.setVisible(false);
    }

}
