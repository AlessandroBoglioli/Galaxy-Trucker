package it.polimi.ingsoftware.ll13.client.utils.printers.specific_printers;

import it.polimi.ingsoftware.ll13.client.utils.ansi.AnsiColor;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIInterface;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIState;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIString;
import org.jetbrains.annotations.NotNull;

public class CLIWaitingPrinter implements CLIInterface {
    private static final CLIString waiting = new CLIString(">> Good job, astronauts! Now we wait for other players!\n", AnsiColor.YELLOW, 0, 36);

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
         * Applies the current state of the CLI by performing a series of terminal commands
         * and display updates. This method clears the screen, moves the cursor to the home
         * position, displays the waiting room banner, prints the waiting room content,
         * and saves the current cursor position.
         *
         * @param args an array of objects containing inputs or arguments required for the state application;
         *             may be ignored by this implementation.
         */
        @Override
        public void apply(Object[] args) {
            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.displayWaitingRoom();

            waiting.print();

            CLICommands.saveCursorPosition();
        }
    }
}
