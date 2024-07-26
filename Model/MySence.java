/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import DAO.EmployeeDAO;
import java.io.IOException;
import java.util.Optional;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author nducmd
 */
public class MySence {

    private String source;
    private String title;
    private boolean resizable;
    private boolean maximized;

    public MySence(String source, String title, boolean resizable, boolean maximized) {
        this.source = source;
        this.title = title;
        this.resizable = resizable;
        this.maximized = maximized;
    }

    public void show() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(source));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        scene.getRoot().requestFocus();
        stage.setTitle(title);
        stage.setResizable(resizable);
        stage.setMaximized(maximized);
        stage.setScene(scene);
        stage.show();
    }

    public void showAndWait() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(source));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        scene.getRoot().requestFocus();
        stage.setTitle(title);
        stage.setResizable(resizable);
        stage.setMaximized(maximized);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void showAndNotClose() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(source));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        scene.getRoot().requestFocus();
        stage.setTitle(title);
        stage.setResizable(resizable);
        stage.setMaximized(maximized);
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //event.consume(); // Ngăn chặn sự kiện đóng cửa sổ mặc định
                try {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Bạn có muốn đăng xuất không?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get().equals(ButtonType.OK)) {
                        Session.setEndTime();
                        EmployeeDAO ed = new EmployeeDAO();
                        ed.updateHoursWorkedEmployee();
                        stage.hide();
                        MySence ms = new MySence("/View/LoginForm.fxml", "Quản lí cửa hàng", false, false);
                        ms.show();
                    } else event.consume();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        stage.show();
    }
}
