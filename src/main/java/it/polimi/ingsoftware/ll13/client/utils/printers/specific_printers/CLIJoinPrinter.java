package it.polimi.ingsoftware.ll13.client.utils.printers.specific_printers;

import it.polimi.ingsoftware.ll13.client.utils.ansi.AnsiColor;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIInterface;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIState;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIString;
import org.jetbrains.annotations.NotNull;

public class CLIJoinPrinter implements CLIInterface {
    private static final CLIString isHosting = new CLIString(">> Choose: [start] to host a match, [join] to join one.\n>> ", AnsiColor.YELLOW, 0, 30);
    private static final CLIString invalidInput = new CLIString(">> Invalid input!\n", AnsiColor.RED, 0, 29);
    private static final CLIString username = new CLIString(">> Please insert your username:\n>> ", AnsiColor.YELLOW, 0, 30);
    private static final CLIString usernameTaken = new CLIString(">> Username is already been taken. Try another one.\n", AnsiColor.RED, 0, 29);
    private static final CLIString players = new CLIString(">> Please insert the number of player [2-4]:\n>> ", AnsiColor.YELLOW, 0, 30);
    private static final CLIString gameMode = new CLIString(">> Please insert the game mode [try] or [level2]:\n>> ", AnsiColor.YELLOW, 0, 30);
    private static final CLIString matchAlreadyCreated = new CLIString(">> ⚠️ Match already created!\nReturning to menu...\n", AnsiColor.RED, 0, 29);
    private static final CLIString matchNotExists = new CLIString(">> ⚠️ Match doesn't exists!\nReturning to menu...\n", AnsiColor.RED, 0, 29);

    private CLIState state = new Default();

    @Override
    public void changeState(@NotNull CLIState state) {
        this.state = state;
    }

    @Override
    public void print(Object[] args) {
        state.apply(args);
    }

    public static class Default implements CLIState {
        @Override
        public void apply(Object[] args) {
            CLICommands.initialize();

            CLIBanners.displayLogin();

            isHosting.print();
            CLICommands.saveCursorPosition();
        }
    }

    public static class InvalidInputCauseHosting implements CLIState {
        @Override
        public void apply(Object @NotNull [] args) {
            CLICommands.restoreCursorPosition();
            CLICommands.clearUserInput((String) args[0]);

            if(matchAlreadyCreated.isVisible())
                matchAlreadyCreated.deleteString();
            if(matchNotExists.isVisible())
                matchNotExists.deleteString();
            if(usernameTaken.isVisible())
                usernameTaken.deleteString();

            if(!invalidInput.isVisible())
                invalidInput.print();
            isHosting.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class InsertUsername implements CLIState {
        @Override
        public void apply(Object @NotNull [] args) {
            CLICommands.restoreCursorPosition();
            CLICommands.clearUserInput((String) args[0]);

            if(invalidInput.isVisible())
                invalidInput.deleteString();

            if(isHosting.isVisible())
                CLIString.replace(isHosting, username);
            else
                username.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class InsertPlayers implements CLIState {
        @Override
        public void apply(Object[] args) {
            CLICommands.restoreCursorPosition();
            CLICommands.clearUserInput((String) args[0]);

            if(matchAlreadyCreated.isVisible())
                matchAlreadyCreated.deleteString();
            if(matchNotExists.isVisible())
                matchNotExists.deleteString();
            if(usernameTaken.isVisible())
                usernameTaken.deleteString();

            CLIString.replace(username, players);

            CLICommands.restoreCursorPosition();
        }
    }

    public static class InvalidInputCausePlayers implements CLIState {
        @Override
        public void apply(Object @NotNull [] args) {
            CLICommands.restoreCursorPosition();
            CLICommands.clearUserInput((String) args[0]);

            if(matchAlreadyCreated.isVisible())
                matchAlreadyCreated.deleteString();
            if(matchNotExists.isVisible())
                matchNotExists.deleteString();
            if(usernameTaken.isVisible())
                usernameTaken.deleteString();

            if(!invalidInput.isVisible())
                invalidInput.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class InsertLevelType implements CLIState {
        @Override
        public void apply(Object @NotNull [] args) {
            CLICommands.restoreCursorPosition();
            CLICommands.clearUserInput((String) args[0]);

            if(invalidInput.isVisible())
                invalidInput.deleteString();

            if(players.isVisible())
                CLIString.replace(players, gameMode);
            else
                gameMode.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class InvalidInputCauseLevel implements CLIState {
        @Override
        public void apply(Object @NotNull [] args) {
            CLICommands.restoreCursorPosition();
            CLICommands.clearUserInput((String) args[0]);

            if(!invalidInput.isVisible())
                invalidInput.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class InvalidInputCauseLobbyAlreadyCreated implements CLIState {
        @Override
        public void apply(Object @NotNull [] args) {
            CLICommands.restoreCursorPosition();
            CLICommands.clearUserInput((String) args[0]);

            if(invalidInput.isVisible())
                invalidInput.deleteString();
            if(!matchAlreadyCreated.isVisible())
                matchAlreadyCreated.print();

            if(gameMode.isVisible())
                CLIString.replace(gameMode, isHosting);
            else
                isHosting.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class UsernameAlreadyTaken implements CLIState {
        @Override
        public void apply(Object @NotNull [] args) {
            CLICommands.restoreCursorPosition();
            CLICommands.clearUserInput((String) args[0]);

            if(invalidInput.isVisible())
                invalidInput.deleteString();
            if(!usernameTaken.isVisible())
                usernameTaken.print();

            if(gameMode.isVisible())
                CLIString.replace(gameMode, isHosting);
            else
                isHosting.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class MatchNotExisting implements CLIState {
        @Override
        public void apply(Object @NotNull [] args) {
            CLICommands.restoreCursorPosition();
            CLICommands.clearUserInput((String) args[0]);

            if(invalidInput.isVisible())
                invalidInput.deleteString();
            if(!matchNotExists.isVisible())
                matchNotExists.print();

            if(gameMode.isVisible())
                CLIString.replace(gameMode, isHosting);
            else
                isHosting.print();

            CLICommands.restoreCursorPosition();
        }
    }
}
