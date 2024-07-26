/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author nducmd
 */
public class QRcodeFormController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ImageView qrImage;

    @FXML
    private Button saveButton;

    @FXML
    private ComboBox<String> statusBox;

    private String link = "";

    private final ObservableList<String> statusList = FXCollections.observableArrayList("Chưa thanh toán", "Đã thanh toán");

    public void setLink(String link) {
        this.link = link;
        Image image = new Image(link) {
        };
        qrImage.setImage(image);
    }

    public void pay() {
        if (statusBox.getValue().equals("Đã thanh toán")) {
            OrderFormController.isPaid = true;
            saveButton.getScene().getWindow().hide();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText("Chưa thanh toán!");
            alert.showAndWait();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        statusBox.setItems(statusList);
        statusBox.setValue("Chưa thanh toán");
    }

}
