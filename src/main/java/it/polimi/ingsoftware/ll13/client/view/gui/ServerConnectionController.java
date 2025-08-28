package it.polimi.ingsoftware.ll13.client.view.gui;

import it.polimi.ingsoftware.ll13.client.controller.GuiController;
import it.polimi.ingsoftware.ll13.network.connection.ClientHearthBeat;
import it.polimi.ingsoftware.ll13.utils.input.InputChecker;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
@SuppressWarnings("ALL")
public class ServerConnectionController implements GuiSceneController {
    private final GuiController controller = GuiController.getInstance();
    @FXML
    public TextField ServerPortTextField;
    @FXML
    public TextField ServerAddressTextField;
    @FXML
    public Button ConnectionToServerButton;
    @FXML
    public Label PortErrorLabel;
    @FXML
    public Label AddressErrorLabel;
    @FXML
    public Label ConnectionErrorLabel;

    @FXML
    public void SaveServerData(ActionEvent event) throws IOException {
        String serverPort = ServerPortTextField.getText();
        String serverAddress = ServerAddressTextField.getText();
        AddressErrorLabel.setVisible(false);
        PortErrorLabel.setVisible(false);
        ConnectionErrorLabel.setVisible(false);


        boolean isValidIp = InputChecker.isValidIP(serverAddress);
        boolean isValidPort = InputChecker.isValidPort(serverPort);
        if(!isValidIp){
            showErrorLabel(AddressErrorLabel);
        }
        if(!isValidPort){
            showErrorLabel(PortErrorLabel);
        }
        if(isValidIp && isValidPort){
            try{
                int port = Integer.parseInt(serverPort);
                Socket socket = new Socket(serverAddress, port);
                controller.setSocket(socket);
                controller.setUp();
                controller.setId();
                setUpHearthBeat();
                controller.changeScene(new LoginController());
            } catch (Exception e) {
                showErrorLabel(ConnectionErrorLabel);
            }
        }
    }
    private void showErrorLabel(Label label) {
        label.setVisible(true);
        label.getStyleClass().add("pulsing");

    }
    private void setUpHearthBeat(){
        ClientHearthBeat.setController(GuiController.getInstance());
        ClientHearthBeat.heartBeat();
    }
    @Override
    public String getFxml() {
        return "/gui/fxml/ServerConnection.fxml";
    }

    @Override
    public List<String> getCss() {
        return List.of(
                "/gui/css/Style.css",
                "/gui/css/ErrorStyling.css"
        );
    }
}
