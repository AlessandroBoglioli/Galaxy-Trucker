package it.polimi.ingsoftware.ll13.client.view.gui;

import java.util.List;

public class WaitingGameController implements GuiSceneController {
    @Override
    public String getFxml() {
        return "/gui/fxml/WaitingGame.fxml";
    }

    @Override
    public List<String> getCss() {
        return List.of(
                "/gui/css/Style.css"
        );
    }
}

