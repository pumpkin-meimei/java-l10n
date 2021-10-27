import javafx.fxml.FXML;

import java.io.IOException;

public class Failures extends SceneController {

    @FXML
    public void initialize() {
    }

    public void goBack() throws IOException {
        appController.goToHome();
    }
}
