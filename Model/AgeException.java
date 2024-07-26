/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.scene.control.Alert;

/**
 *
 * @author nducmd
 */
public class AgeException extends RuntimeException {

    public AgeException() {
        super("Tuổi không thể nhỏ hơn 18 hoặc lớn hơn 100!");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText("Tuổi không thể nhỏ hơn 18 hoặc lớn hơn 100!");
        alert.showAndWait();
    }

}
