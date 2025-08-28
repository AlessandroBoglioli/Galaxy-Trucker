package it.polimi.ingsoftware.ll13.client.utils.printers.specific_printers;

import it.polimi.ingsoftware.ll13.client.utils.ansi.AnsiColor;
import it.polimi.ingsoftware.ll13.client.utils.ansi.AnsiFont;
import it.polimi.ingsoftware.ll13.client.utils.ansi.AnsiString;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIInterface;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIState;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIString;
import org.jetbrains.annotations.NotNull;

public class CLIConnectionPrinter implements CLIInterface {
    private static final CLIString welcome = new CLIString(">> Hello new astronauts! Welcome to the fantastic Galaxy Truck experience!", AnsiColor.YELLOW, 0, 36);
    private static final CLIString invalidInput = new CLIString(">> Invalid input.", AnsiColor.RED, 0, 36);
    private static final CLIString invalidIp = new CLIString(">> Invalid server ip.", AnsiColor.RED, 0, 36);
    private static final CLIString invalidPort = new CLIString(">> Invalid port number.", AnsiColor.RED, 0, 36);
    private static final CLIString options = new CLIString(">> Insert your server IP and PORT as IP:PORT\n>> ", AnsiColor.DEFAULT, 0, 37);

    private CLIState state = new Default();

    @Override
    public void changeState(@NotNull CLIState state) {
        this.state = state;
    }

    @Override
    public void print(Object[] args) {
        this.state.apply(args);
    }

    public static class Default implements CLIState {
        /**
         * Applies the current CLI state by executing a series of commands to configure
         * the terminal display, display banners, and set the cursor position for relevant
         * details. This method prepares the terminal screen for interaction in the specified
         * state.
         *
         * @param args an array of objects containing arguments that may influence how the
         *             CLI state is applied. This parameter can include contextual information
         *             relevant to the CLI state implementation.
         */
        @Override
        public void apply(Object[] args) {
            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.displayConnection();

            welcome.print();
            options.print();

            CLICommands.saveCursorPosition();
            AnsiString.print("127.0.0.1:1234", AnsiColor.GREY, AnsiFont.LIGHT);
            CLICommands.restoreCursorPosition();
        }
    }

    public static class InvalidInput implements CLIState {
        /**
         * Applies the specified operations to handle invalid input in the CLI.
         * This method restores the cursor position, clears the user input,
         * replaces the welcome message with the invalid input message, and
         * finally restores the cursor position again.
         *
         * @param args an array of objects, where the first element is expected
         *             to be a {@link String} representing the user input to be cleared
         */
        @Override
        public void apply(Object @NotNull [] args) {
            CLICommands.restoreCursorPosition();
            CLICommands.clearUserInput((String) args[0]);
            CLIString.replace(welcome, invalidInput);

            CLICommands.restoreCursorPosition();
        }
    }

    public static class InvalidIP implements CLIState {
        /**
         * Applies the operations for handling invalid IP input in the CLI context.
         * This includes restoring the cursor position, clearing user input, and updating
         * displayed text related to invalid IP messages.
         *
         * @param args an array of Objects, where the first element is expected to be
         *             a String representing the user*/
        @Override
        public void apply(Object @NotNull [] args) {
            CLICommands.restoreCursorPosition();
            CLICommands.clearUserInput((String) args[0]);
            CLIString.replace(welcome, invalidIp);

            CLICommands.restoreCursorPosition();
        }
    }

    public static class InvalidPort implements CLIState {
        /**
         * Applies a series of command-line interface operations including restoring
         * the cursor position, clearing user input, and replacing specific text.
         *
         * @param args an array of objects representing arguments for the method.
         *             The first element is expected to be a String containing the
         *             user input to be cleared.
         */
        @Override
        public void apply(Object[] args) {
            CLICommands.restoreCursorPosition();
            CLICommands.clearUserInput((String) args[0]);
            CLIString.replace(welcome, invalidPort);

            CLICommands.restoreCursorPosition();
        }
    }


}
