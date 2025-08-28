package it.polimi.ingsoftware.ll13.client.controller;

import it.polimi.ingsoftware.ll13.client.Client;
import it.polimi.ingsoftware.ll13.client.exceptions.ExceptionHandler;
import it.polimi.ingsoftware.ll13.client.exceptions.GuiExceptionHandler;
import it.polimi.ingsoftware.ll13.client.handlers.GuiHandler;
import it.polimi.ingsoftware.ll13.client.view.gui.GuiSceneController;
import it.polimi.ingsoftware.ll13.client.view.gui.ServerConnectionController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.Objects;


public class GuiController extends ClientController{
    private static GuiController instance;
    private Stage stage;
    private Scene scene;
    private GuiSceneController page;
    private final ExceptionHandler exceptionHandler = new GuiExceptionHandler(this);
    private final Object pageLock = new Object();
    public static GuiController getInstance(){
        if(instance == null){
            instance = new GuiController();
        }
        return instance;
    }

    public Stage getStage() {
        return stage;
    }

    public Scene getScene() {
        return scene;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public synchronized void setPage(GuiSceneController page){
        this.page = page;
    }
    public synchronized GuiSceneController getPage(){
        return page;
    }

    @Override
    public void close() {

    }

    @Override
    public void waitingRoom() {

    }

    @Override
    public void join() {

    }

    @Override
    public void connectToServer() {

    }



    public void changeScene(GuiSceneController sceneController){
        FXMLLoader loader = new FXMLLoader(GuiController.class.getResource(sceneController.getFxml()));
        try{
            Parent root = loader.load();
            scene.setRoot(root);
            scene.getStylesheets().clear();
            stage.show();
        } catch (IOException e) {
                exceptionHandler.handle(e);
        }
        setPage(loader.getController());
    }
    public synchronized void initialize(Stage stage){
        setClient( new Client());
        setExceptionHandler(new GuiExceptionHandler(this));
        setResponseHandler(GuiHandler.getInstance());
        this.stage = stage;
        stage.setTitle("Galaxy Trucker");
         //Window Icon
        Image icon = new Image(getClass().getResourceAsStream("/gui/images/icon.png"));
        stage.getIcons().add(icon);
        GuiSceneController initialPage = new ServerConnectionController();
        setPage(initialPage);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(initialPage.getFxml()));
            Parent root = loader.load();
            scene = new Scene(root);
            Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
            stage.setWidth(screenBounds.getWidth() * 1);
            stage.setHeight(screenBounds.getHeight() * 1);

            // Center the window
            stage.setX(screenBounds.getMinX() + (screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY(screenBounds.getMinY() + (screenBounds.getHeight() - stage.getHeight()) / 2);

            // Optional: prevent resizing below a certain size
            stage.setMinWidth(900);
            stage.setMinHeight(700);

            stage.setResizable(false);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            exceptionHandler.handle(e);
        }
    }

    @Override
    public void waitForHostOk() {
        ;
    }

    @Override
    public void buildingShip() {
        ;
    }

    @Override
    public void validationPhase() {
        ;
    }

    @Override
    public void setEquipe() {
        ;
    }

    @Override
    public void adventureShip() {
        ;
    }
}
