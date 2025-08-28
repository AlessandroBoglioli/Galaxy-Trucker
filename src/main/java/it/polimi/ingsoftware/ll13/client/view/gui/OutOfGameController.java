package it.polimi.ingsoftware.ll13.client.view.gui;

import it.polimi.ingsoftware.ll13.client.controller.GuiController;
import it.polimi.ingsoftware.ll13.network.requests.QuitRequest;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
@SuppressWarnings("ALL")
public class OutOfGameController implements GuiSceneController {

    private final GuiController controller = GuiController.getInstance();

    @FXML
    public StackPane stackPane;
    @FXML
    public Label pointsLabel;
    @FXML
    public VBox container;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public Button disconnectButton;


    @Override
    public String getFxml() {
        return "/gui/fxml/OutOfGame.fxml";
    }

    @Override
    public List<String> getCss() {
        return List.of();
    }

    public void setPoints(float points) {
        pointsLabel.setText("Sei stato eliminato, hai ottenuto un punteggio di : " + points);
    }

    public void switchEndGameScreen (List<String> playersName, List<Float> points) {
        FXMLLoader loader = new FXMLLoader(GuiController.class.getResource("/gui/fxml/EndGame.fxml"));
        try {
            Parent root = loader.load();
            EndGameController endGameScene = loader.getController();

            endGameScene.setLeaderboard(playersName, points);

            controller.setPage(endGameScene);
            controller.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void disconnect (ActionEvent event) {
        System.out.println("Ciao adios");
        controller.send(new QuitRequest(controller.getId()));
        Platform.exit();
        System.exit(0);
    }



}
