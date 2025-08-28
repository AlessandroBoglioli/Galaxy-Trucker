package it.polimi.ingsoftware.ll13.client.exceptions;

import it.polimi.ingsoftware.ll13.client.controller.GuiController;
import it.polimi.ingsoftware.ll13.client.view.gui.CrashScreenController;
import it.polimi.ingsoftware.ll13.server.Server;
import javafx.application.Platform;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * This class is a singleton implementation of the {@code ExceptionHandler} interface
 * for handling exceptions in a Graphical User Interface (GUI) context.
 */
public class GuiExceptionHandler implements ExceptionHandler{
    private final GuiController controller;
    public GuiExceptionHandler(GuiController controller){
        this.controller = controller;
    }
    @Override
    public void handle(IOException e) {
        e.printStackTrace();
        if(controller.getClient().isActive()) {
            Platform.runLater(() -> {
                CrashScreenController errorScreen = new CrashScreenController();
                controller.changeScene(errorScreen);
                ((CrashScreenController)controller.getPage()).setMessageLabel("SERVER ERROR");
            });
        }else Platform.exit();
    }

    @Override
    public void handle(ServerCrashError e) {
        e.printStackTrace();
        Platform.runLater(() -> {
            CrashScreenController  errorScreen = new CrashScreenController();
            controller.changeScene(errorScreen);
            ((CrashScreenController)controller.getPage()).setMessageLabel("CONNECTION TIMED OUT");
        });
    }

    @Override
    public void handle(SocketConnectionError e) {
        Platform.runLater(() -> {
            CrashScreenController  errorScreen = new CrashScreenController();
            controller.changeScene(errorScreen);
            ((CrashScreenController)controller.getPage()).setMessageLabel("SOCKET ERROR");
        });
    }


    @Override
    public void handle(SocketException e) {
        Platform.runLater(() -> {
            CrashScreenController  errorScreen = new CrashScreenController();
            controller.changeScene(errorScreen);
            ((CrashScreenController)controller.getPage()).setMessageLabel("SOCKET ERROR");
        });
    }

    @Override
    public void handle(Exception e) {

        if (e instanceof SocketException) {
            Platform.runLater(() -> {
                CrashScreenController  errorScreen = new CrashScreenController();
//                try {
//                    Thread.sleep(7000);
//                } catch (InterruptedException ex) {
//                    throw new RuntimeException(ex);
//                }
                controller.changeScene(errorScreen);
                ((CrashScreenController)controller.getPage()).setMessageLabel("CONNECTION TIMED OUT");
            });
        } else {
            CrashScreenController errorScreen = new CrashScreenController();
            Platform.runLater(() -> {
                controller.changeScene(errorScreen);
                ((CrashScreenController)controller.getPage()).setMessageLabel("UNKNOWN ERROR");
                System.out.println(controller.getPage());
            });
        }


    }
}
