package docmate.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AlertMessage extends Alert {

    public AlertMessage(String contentText) {
        super(AlertType.NONE, contentText, ButtonType.OK);
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("docmate/res/box_important_96px.png"));
        setTitle("Message");
    }
}
