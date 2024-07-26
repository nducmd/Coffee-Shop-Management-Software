/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.OrderData;
import Model.Product;
import static Controller.OrderFormController.orderList;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author nducmd
 */
public class CardProductController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ImageView imageProduct;

    @FXML
    private Label nameLable;

    @FXML
    private Label priceLable;
    
    @FXML
    private Label unitLable;
    
    @FXML
    private Label idLable;

    public void setData(Product product) {
        idLable.setText(String.valueOf(product.getId()));
        nameLable.setText(product.getProductName());
        priceLable.setText(String.valueOf(product.getPrice()));
        imageProduct.setImage(new Image(product.getImage(), 175, 125, false, true));
        imageProduct.setPreserveRatio(true);
        imageProduct.setSmooth(true);
        unitLable.setText(product.getUnit());
    }
    
    // Thêm sản phẩm vào danh sách
    public void addProductToOrderList(OrderData newOrder) {

        for (OrderData orderData : orderList) {
            if (orderData.getId() == newOrder.getId()) {
                orderData.setQuantity(orderData.getQuantity() + newOrder.getQuantity());
                orderList.add(newOrder);
                orderList.remove(orderList.size() - 1);
                return;
            }
        }
        orderList.add(newOrder);
    }

    public void addToOrderList() {
        OrderData order = new OrderData(Integer.parseInt(idLable.getText()), nameLable.getText(), unitLable.getText(), 1, Integer.parseInt(priceLable.getText()));
        addProductToOrderList(order);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
