package it.polimi.ingsoftware.ll13.client.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.awt.event.ActionListener;
import java.util.List;
@SuppressWarnings("ALL")
public class EndGameController implements GuiSceneController {

    @FXML
    public Label firstPlayerName;
    @FXML
    public Label firstPlayerPoints;
    @FXML
    public Label secondPlayerName;
    @FXML
    public Label secondPlayerPoints;
    @FXML
    public Label thirdPlayerName;
    @FXML
    public Label thirdPlayerPoints;
    @FXML
    public Label fourthPlayerName;
    @FXML
    public Label fourthPlayerPoints;

    @Override
    public String getFxml() {
        return "/gui/fxml/EndGame.fxml";
    }

    @Override
    public List<String> getCss() {
        return List.of();
    }

    public void setLeaderboard(List<String> playersName, List<Float> points) {
        int size = Math.min(playersName.size(), points.size());
        if (size > 0) {
            firstPlayerName.setText(playersName.get(0));
            firstPlayerPoints.setText(String.valueOf(points.get(0)));
        } else {
            firstPlayerName.setText("");
            firstPlayerPoints.setText("");
        }
        if (size > 1) {
            secondPlayerName.setText(playersName.get(1));
            secondPlayerPoints.setText(String.valueOf(points.get(1)));
        } else {
            secondPlayerName.setText("");
            secondPlayerPoints.setText("");
        }

        if (size > 2) {
            thirdPlayerName.setText(playersName.get(2));
            thirdPlayerPoints.setText(String.valueOf(points.get(2)));
        } else {
            thirdPlayerName.setText("");
            thirdPlayerPoints.setText("");
        }

        if (size > 3) {
            fourthPlayerName.setText(playersName.get(3));
            fourthPlayerPoints.setText(String.valueOf(points.get(3)));
        } else {
            fourthPlayerName.setText("");
            fourthPlayerPoints.setText("");
        }
    }


}
