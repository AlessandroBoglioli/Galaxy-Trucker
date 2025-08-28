package it.polimi.ingsoftware.ll13;


import it.polimi.ingsoftware.ll13.client.Client;
import it.polimi.ingsoftware.ll13.client.controller.CliController;
import it.polimi.ingsoftware.ll13.client.exceptions.CliExceptionHandler;
import it.polimi.ingsoftware.ll13.client.handlers.CliHandler;
import it.polimi.ingsoftware.ll13.client.view.cli.CliView;
import it.polimi.ingsoftware.ll13.client.view.gui.Gui;
import javafx.application.Application;

public class ClientLauncher {
    public static void main(String[] args) {
        String mode;
        if (args.length > 0) {
            mode = args[0].trim().toLowerCase();
        } else {
            System.out.println("Choose client mode: [cli/gui] (default: cli)");
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            String input = scanner.nextLine().trim().toLowerCase();
            mode = input.isEmpty() ? "cli" : input;
        }

        switch (mode) {
            case "cli" -> {
                Client client = Client.getInstance();
                CliController controller = CliController.getInstance();
                controller.setResponseHandler(CliHandler.getInstance());
                controller.setExceptionHandler(CliExceptionHandler.getInstance());
                // System.out.println(">>> Exception handler set");
                client.setController(controller);
                client.setExceptionHandler(new CliExceptionHandler());
                new Thread(client).start(); // Launch client
            }
            case "gui" -> {
                Application.launch(Gui.class);
            }
            default -> System.out.println("Unknown mode: use 'cli' or 'gui'");
        }
    }


}
