import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationApp extends Application {

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        if (primaryStage.getScene() != null) {
            throw new Exception("Primary stage already has a scene before application start");
        }

        goToHome();
        primaryStage.show();
    }

    public void goToHome() throws IOException {
        ResourceBundle messages = ResourceBundle.getBundle("home", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/Home.fxml"), messages);
        Parent homeView = loader.load();
        Home homeController = loader.getController();
        homeController.setAppController(this);
        primaryStage.setTitle("Home");

        if (primaryStage.getScene() == null) {
            primaryStage.setScene(new Scene(homeView));
        }
        else {
            primaryStage.getScene().setRoot(homeView);
        }
    }

    public void goToDetails() throws IOException {
        ResourceBundle messages = ResourceBundle.getBundle("details", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/Details.fxml"), messages);
        Parent detailsView = loader.load();
        Details detailsController = loader.getController();
        detailsController.setAppController(this);
        primaryStage.setTitle("Details");

        if (primaryStage.getScene() == null) {
            primaryStage.setScene(new Scene(detailsView));
        }
        else {
            primaryStage.getScene().setRoot(detailsView);
        }
    }

    public void goToDatesAndNumbers() throws IOException {
        ResourceBundle messages = ResourceBundle.getBundle("datesAndNumbers", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/DatesAndNumbers.fxml"), messages);
        Parent datesAndNumbersView = loader.load();
        DatesAndNumbers datesAndNumbersController = loader.getController();
        datesAndNumbersController.setAppController(this);
        primaryStage.setTitle("Dates and numbers");

        if (primaryStage.getScene() == null) {
            primaryStage.setScene(new Scene(datesAndNumbersView));
        }
        else {
            primaryStage.getScene().setRoot(datesAndNumbersView);
        }
    }

    public void goToFailures() throws IOException {
        ResourceBundle messages = ResourceBundle.getBundle("failures", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/Failures.fxml"), messages);
        Parent failuresView = loader.load();
        Failures failuresController = loader.getController();
        failuresController.setAppController(this);
        primaryStage.setTitle("Failures");

        if (primaryStage.getScene() == null) {
            primaryStage.setScene(new Scene(failuresView));
        }
        else {
            primaryStage.getScene().setRoot(failuresView);
        }
    }
}
