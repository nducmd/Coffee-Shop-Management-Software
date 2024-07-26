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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
public class AddProductFormController implements Initializable {

    @FXML
    private ImageView imageProduct;

    @FXML
    private Button importImageButton;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private ComboBox<String> statusBox;
    @FXML
    private Button addProductButton;

    @FXML
    private TextField unitField;
    private String tempImagePath = "/Image/trasua.png";
    private final ObservableList<String> statusList = FXCollections.observableArrayList("Hoạt động", "Dừng");

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
    public void addProduct() {
        if (nameField.getText().equals("")
                || unitField.getText().equals("")
                || statusBox.getValue().equals("")
                || priceField.getText().equals("")) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText("Vui lòng điền đủ thông tin!");
            alert.showAndWait();
        } else {

            try {
                if (priceField.getText().startsWith("-")) {
                    Integer price = Integer.parseInt("a" + priceField.getText());
                }
                Integer price = Integer.parseInt(priceField.getText());
                Product product = new Product(nameField.getText(), unitField.getText(), price, tempImagePath, statusBox.getValue());
                ProductDAO pd = new ProductDAO();
                if (pd.add(product)) {

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setContentText("Thêm sản phẩm thành công!");
                    alert.showAndWait();
                    addProductButton.getScene().getWindow().hide();
                }
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setContentText("Vui lòng nhập đúng thông tin!");
                alert.showAndWait();
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        statusBox.setItems(statusList);
        // TODO
    }

}
