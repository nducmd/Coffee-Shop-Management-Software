package Controller;

import Model.MySence;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author nducmd
 */
public class CafeShop extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        MySence ms = new MySence("/View/LoginForm.fxml", "2.11 Coffee", false, false);
        ms.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
