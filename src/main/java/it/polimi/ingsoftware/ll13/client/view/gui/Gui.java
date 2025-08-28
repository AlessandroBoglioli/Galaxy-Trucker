package it.polimi.ingsoftware.ll13.client.view.gui;

import it.polimi.ingsoftware.ll13.client.controller.GuiController;
import it.polimi.ingsoftware.ll13.network.requests.QuitRequest;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;

public class Gui extends Application {
    private final GuiController controller = GuiController.getInstance();
    @Override
    public void start(Stage stage) {
        controller.initialize(stage);
        stage.setOnCloseRequest(event -> {
            event.consume();
            controller.send(new QuitRequest(controller.getId()));
            Platform.exit();
            System.exit(0);
        });
    }
    public static void main(String[] args) {
        launch(args);
    }
}
