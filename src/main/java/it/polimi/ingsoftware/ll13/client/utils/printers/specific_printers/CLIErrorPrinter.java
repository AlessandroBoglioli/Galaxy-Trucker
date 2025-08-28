package it.polimi.ingsoftware.ll13.client.utils.printers.specific_printers;

import it.polimi.ingsoftware.ll13.client.utils.ansi.AnsiColor;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIInterface;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIState;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIString;
import org.jetbrains.annotations.NotNull;

public class CLIErrorPrinter implements CLIInterface {
    private static final CLIString ioException = new CLIString(">> Huston, we got a problem! Input or output exception.\n", AnsiColor.RED, 0, 36);
    private static final CLIString serverCrash = new CLIString(">> Huston, we got a problem! Server crashed.\n", AnsiColor.RED, 0, 36);
    private static final CLIString serverUnreachable = new CLIString(">> Huston, we got a problem! Server is unreachable.\n", AnsiColor.RED, 0, 36);
    private static final CLIString generalException = new CLIString(">> Huston, we got a problem! We don't know the problem either.\n", AnsiColor.RED, 0, 36);

    private CLIState state = new CLIErrorPrinter.IOException();

    @Override
    public void changeState(@NotNull CLIState state) {
        this.state = state;
    }

    @Override
    public void print(Object[] args) {
        this.state.apply(args);
    }

    public static class IOException implements CLIState {
        /**
         * Applies a series of terminal operations to configure the state of the Command-Line Interface (CLI).
         * This method clears the terminal screen, moves the cursor*/
        @Override
        public void apply(Object[] args) {
            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.display404();

            ioException.print();

            CLICommands.saveCursorPosition();
        }
    }

    public static class ServerCrash implements CLIState {
        /**
         * Executes a sequence of CLI commands to reset the terminal state, display an error banner,
         * print server crash information, and save the cursor position.
         *
         * @param args an array of objects passed to the method; currently unused in the implementation
         */
        @Override
        public void apply(Object[] args) {
            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.display404();

            serverCrash.print();

            CLICommands.saveCursorPosition();
        }
    }

    public static class ServerUnreachable implements CLIState {
        /**
         * Applies the actions defined for when the server is unreachable.
         * This method interacts with the command-line interface to handle
         * visual updates and displays an error banner regarding server unreachability.
         *
         * @param args an array of objects representing any arguments passed to the method,
         *             though not specifically utilized in this implementation
         */
        @Override
        public void apply(Object[] args) {
            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.displayError();

            serverUnreachable.print();

            CLICommands.saveCursorPosition();
        }
    }

    public static class GeneralException implements CLIState {
        /**
         * Applies the specified behavior by executing a series of terminal manipulation
         * commands and visual display functions.
         *
         * The method performs the following actions:
         * - Clears the terminal screen.
         * - Moves the cursor to the home position.
         * - Displays an error banner in the terminal.
         * - Prints a general exception message.
         * - Saves the current cursor position for potential restoration.
         *
         * @param args an array of arguments (unused in this implementation) that can be provided
         *             for extensibility or additional contextual data.
         */
        @Override
        public void apply(Object[] args) {
            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.displayError();

            generalException.print();

            CLICommands.saveCursorPosition();
        }
    }
}
