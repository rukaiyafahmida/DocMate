package docmate;

import docmate.controller.FXMLDocMateController;
import docmate.controller.FXMLLoginController;
import docmate.controller.FXMLRegisterController;
import docmate.model.Doctor;
import docmate.util.LoginConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Launcher extends Application {

    public static final String TAG = "DOCMATE";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.getIcons().add(new Image("docmate/res/docmate_logo_96px.png"));
        primaryStage.setTitle("DocMate");

        // Finding login config
        Doctor doctor = LoginConfig.getInstance().readLoginConfig();

        if (doctor != null) {
            Doctor.getInstance().setDoctor(doctor);

            // Opening docmate window
            openDocMate(primaryStage);
        } else {
            // Opening login register window
            openLoginRegister(primaryStage);
        }
    }

    private void openLoginRegister(Stage primaryStage) throws Exception {
        // Loading login scene
        FXMLLoader loginFXMLLoader = new FXMLLoader(getClass().getResource("view/FXMLLogin.fxml"));
        Parent loginRoot = loginFXMLLoader.load();
        Scene loginScene = new Scene(loginRoot);

        // Loading register scene
        FXMLLoader registerFXMLLoader = new FXMLLoader(getClass().getResource("view/FXMLRegister.fxml"));
        Parent registerRoot = registerFXMLLoader.load();
        Scene registerScene = new Scene(registerRoot);

        // Injecting stage and register scene into login controller
        FXMLLoginController fxmlLoginController = loginFXMLLoader.getController();
        fxmlLoginController.setPrimaryStage(primaryStage);
        fxmlLoginController.setRegisterScene(registerScene);

        // Injecting stage and login scene into register controller
        FXMLRegisterController fxmlRegisterController = registerFXMLLoader.getController();
        fxmlRegisterController.setPrimaryStage(primaryStage);
        fxmlRegisterController.setLoginScene(loginScene);

        primaryStage.setScene(loginScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void openDocMate(Stage primaryStage) throws Exception {
        // Loading docmate scene
        FXMLLoader docmateFXMLLoader = new FXMLLoader(getClass().getResource("view/FXMLDocMate.fxml"));
        Parent docmateRoot = docmateFXMLLoader.load();
        Scene docmateScene = new Scene(docmateRoot);

        // Injecting stage into docmate controller
        FXMLDocMateController fxmlDocMateController = docmateFXMLLoader.getController();
        fxmlDocMateController.setPrimaryStage(primaryStage);

        primaryStage.setScene(docmateScene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
