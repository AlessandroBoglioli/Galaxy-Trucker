package it.polimi.ingsoftware.ll13.client.view.gui;

import it.polimi.ingsoftware.ll13.client.controller.GuiController;
import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;
import it.polimi.ingsoftware.ll13.network.requests.menu_requests.LoginRequest;
import it.polimi.ingsoftware.ll13.network.requests.menu_requests.NewMatchRequest;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
@SuppressWarnings("ALL")
public class LoginController implements GuiSceneController, Initializable {

    private GuiController controller = GuiController.getInstance();
    private String currentNickname;

    @FXML
    public VBox SelectionPane;
    @FXML
    public Button NewMatchButton;
    @FXML
    public Button JoinGameButton;
    @FXML
    public GridPane NewMatchPane;
    @FXML
    public TextField NicknameTextField;
    @FXML
    public ChoiceBox<String> NumberOfPlayersChoiceBox;
    @FXML
    public ChoiceBox<String> GameLevelChoiceBox;
    @FXML
    public VBox JoinGamePane;
    @FXML
    public TextField JoinNicknameTextField;
    @FXML
    public Label usernameTakenLabel;
    @FXML
    public Label usernameErrorLabel;
    @FXML
    public Label usernameErrorLabel2;



    @FXML
    public void showNewMatchOptions(){
        SelectionPane.setVisible(false);
        NewMatchPane.setVisible(true);
    }
    @FXML
    public void showJoinOptions(){
        SelectionPane.setVisible(false);
        JoinGamePane.setVisible(true);
    }
    @FXML
    public void createNewMatch(){
        String nickname = NicknameTextField.getText().trim();
        String players = NumberOfPlayersChoiceBox.getValue();
        String level = GameLevelChoiceBox.getValue();
        if (nickname.isEmpty() || players == null || level == null) {
            return;
        }

        if (nickname.length() < 2 || nickname.length() > 15) {
            showUsernameError2("Nickname must be 2–15 characters");
            return;
        }
        int numPlayers = Integer.parseInt(players);
        GameLevel gameLevel = GameLevel.fromString(level);
        this.currentNickname = nickname;
        controller.send(new NewMatchRequest(controller.getId(), numPlayers, gameLevel, nickname));


    }
    @FXML
    public void goBackToSelection(){
        NewMatchPane.setVisible(false);
        JoinGamePane.setVisible(false);
        SelectionPane.setVisible(true);
    }
    @FXML
    public void joinGame(){
        String nickname = JoinNicknameTextField.getText().trim();
        if (usernameErrorLabel != null) {
            usernameErrorLabel.setVisible(false);
            usernameErrorLabel.getStyleClass().remove("pulsing");
        }
        if (nickname.length() < 2 || nickname.length() > 15) {
            showUsernameError("Nickname must be 2–15 characters");
            return;
        }
        this.currentNickname = nickname;
        LoginRequest request = new LoginRequest(controller.getId(), nickname);
        controller.send(request);

    }
    @Override
    public String getFxml() {
        return "/gui/fxml/Login.fxml";
    }

    @Override
    public List<String> getCss() {
        return List.of(
                "/gui/css/ErrorStyling.css",
                "/gui/css/Style.css",
                "/gui/css/NeonStyle.cc"
        );
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NumberOfPlayersChoiceBox.getItems().addAll("2", "3", "4");
        GameLevelChoiceBox.getItems().addAll("TRY LEVEL", "LEVEL 2");
        Thread responseReader = controller.responseReaderThread();
        responseReader.start();
    }

    public void handleLoginSuccess(){
        controller.getClient().setUsername(currentNickname);
        controller.getClient().setLogged(true);
        controller.changeScene(new WaitingGameController());
    }
    public void handleUsernameTaken(){
        goBackToSelection();
        showJoinOptions();
        showUsernameTaken();
    }
    public void showUsernameTaken(){
        usernameErrorLabel.setVisible(false);
        if(usernameTakenLabel != null){
            usernameTakenLabel.setVisible(true);
            usernameTakenLabel.getStyleClass().add("pulsing");
        }
    }
    public void showUsernameError(String message){
        usernameTakenLabel.setVisible(false);
        if (usernameErrorLabel != null) {
            usernameErrorLabel.setText(message);
            usernameErrorLabel.setVisible(true);
            if (!usernameErrorLabel.getStyleClass().contains("pulsing")) {
                usernameErrorLabel.getStyleClass().add("pulsing");
            }
        }

    }
    public void showUsernameError2(String message){
        if (usernameErrorLabel2 != null) {
            usernameErrorLabel2.setText(message);
            usernameErrorLabel2.setVisible(true);
            if (!usernameErrorLabel2.getStyleClass().contains("pulsing")) {
                usernameErrorLabel2.getStyleClass().add("pulsing");
            }
        }

    }
    public void handleMatchStarted(){
        goBackToSelection();
    }
    public void noMatchStarted(){
        goBackToSelection();
    }
}

