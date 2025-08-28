package it.polimi.ingsoftware.ll13.client.exceptions;

import it.polimi.ingsoftware.ll13.client.utils.printers.specific_printers.CLIErrorPrinter;
import it.polimi.ingsoftware.ll13.client.view.cli.CliView;

import java.io.IOException;
import java.net.SocketException;

/**
 * This class is a singleton implementation of the {@code ExceptionHandler} interface
 * for handling exceptions in a Command-Line Interface (CLI) context.
 */
public class CliExceptionHandler implements ExceptionHandler{
    private static CliExceptionHandler instance;

    public static CliExceptionHandler getInstance(){
        if(instance == null){
            instance = new CliExceptionHandler();
        }
        return instance;
    }

    // --> Getters <--
    public CliView getCliView() {
        return CliView.getInstance();
    }

    @Override
    public void handle(IOException e) {
        getCliView().setCliInterface(new CLIErrorPrinter());
        getCliView().updatePageState(new CLIErrorPrinter.IOException());
        getCliView().displayPage(null);
    }

    @Override
    public void handle(ServerCrashError e) {
        getCliView().setCliInterface(new CLIErrorPrinter());
        getCliView().updatePageState(new CLIErrorPrinter.ServerCrash());
        getCliView().displayPage(null);
    }

    @Override
    public void handle(SocketConnectionError e) {
        getCliView().setCliInterface(new CLIErrorPrinter());
        getCliView().updatePageState(new CLIErrorPrinter.ServerUnreachable());
        getCliView().displayPage(null);
    }

    @Override
    public void handle(Exception e) {
        getCliView().setCliInterface(new CLIErrorPrinter());
        getCliView().updatePageState(new CLIErrorPrinter.GeneralException());
        getCliView().displayPage(null);
    }

    @Override
    public void handle(SocketException e) {
        System.out.println("fuhaoufa");
    }
}
