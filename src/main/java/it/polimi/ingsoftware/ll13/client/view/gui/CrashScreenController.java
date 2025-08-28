package it.polimi.ingsoftware.ll13.client.view.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
@SuppressWarnings("ALL")
public class CrashScreenController implements GuiSceneController{

    @FXML
    public Button exitButton;
    @FXML
    public Text errorMessageText;


    @Override
    public String getFxml() {
        return "/gui/fxml/CrashScreen.fxml";
    }


    @Override

    public List<String> getCss() {
        return List.of(
                "/gui/css/Style.css"
        );

    }
    @FXML
    public void onExitClicked() {
        Platform.exit();
        System.exit(0);
    }
    @FXML
    public void setMessageLabel(String message){
        errorMessageText.setText(message);
    }
}
