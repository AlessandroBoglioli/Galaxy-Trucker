package it.polimi.ingsoftware.ll13.client.utils.printers.specific_printers;

import it.polimi.ingsoftware.ll13.client.utils.ansi.AnsiColor;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIInterface;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIState;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIString;
import it.polimi.ingsoftware.ll13.client.utils.printers.helper_printers.TechnicalTerminalPrinter;
import it.polimi.ingsoftware.ll13.model.cards.decks.CardStack;
import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CLIGamePrinter implements CLIInterface {
    private static final CLIString playerColor = new CLIString(
            ">> Let's begin the trip! The color of the label is your color.\n",
            AnsiColor.YELLOW, 0, 20);
    private static final CLIString wantsToStartGame = new CLIString(
            ">> Ready to begin [start]?\n>> ",
            AnsiColor.YELLOW, 0, 21);
    private static final CLIString chooseAction = new CLIString(
            ">> Choose your action [draw], [save], [draw:saved_tiles] [rotate:number_of_rotations], [place], [show], [show:stack_number], [showship], [finish]:\n>> ",
            AnsiColor.DEFAULT, 0, 21);

    // TILES ACTIONS
    private static CLIString drawnTile;
    private static CLIString discardedTileStack;
    public static CLIString shipDisplayOutput;

    private static final CLIString placementPrompt = new CLIString(
            ">> Place tile [row]:[col]\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString placedTile = new CLIString(
            "Tile correctly placed!\n",
            AnsiColor.GREEN, 0, 20);
    private static final CLIString invalidPositionPlacedTile = new CLIString(
            "The tile wasn't placed! You can replace it if you want!",
            AnsiColor.RED, 0, 20);
    private static final CLIString invalidFormatError = new CLIString(
            ">> Invalid format! Please use row:col\n",
            AnsiColor.RED, 0, 20);
    private static final CLIString invalidCoordinatesError = new CLIString(
            ">> Invalid coordinates! Please enter valid row and column numbers\n",
            AnsiColor.RED, 0, 20);
    private static final CLIString invalidRotationError = new CLIString(
            ">> Invalid rotation! Rotation must be 0-3 (0=0°, 1=90°, 2=180°, 3=270°)\n",
            AnsiColor.RED, 0, 20);
    private static final CLIString thereIsNotATileDrew = new CLIString(
            ">> There's not a tile drew. Draw it first!\n",
            AnsiColor.RED, 0, 20);
    private static final CLIString emptyFlippedTiles = new CLIString(
            ">> There are no tiles in the flipped tiles!\n",
            AnsiColor.RED, 0, 20);
    private static final CLIString tileAlreadySaved = new CLIString(
            ">> The tile was already saved!\n",
            AnsiColor.PURPLE, 0, 20);

    private static final CLIString chooseDiscardedTile = new CLIString(
            ">> Choose the number of the tile that you want to pick up [1-num_of_tiles_displayed]:\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString invalidDiscardedTile = new CLIString(
            ">> This tile doesn't exists!\n",
            AnsiColor.RED, 0, 20);

    private static final CLIString savedTile = new CLIString(
            ">> Saved the tile! Choose [draw:number] to pick it\n",
            AnsiColor.GREEN, 0, 20);
    private static final CLIString invalidSavedTile = new CLIString(
            ">> Could not save tile!\n",
            AnsiColor.RED, 0, 20);
    private static final CLIString drewFromSavedTiles = new CLIString(
            ">> Drew the tile from the saved tiles!\n",
            AnsiColor.RED, 0, 20);
    private static final CLIString couldNotDrawFromSavedTiles = new CLIString(
            ">> Could not pick the tile from the saved tiles!\n",
            AnsiColor.RED, 0, 20);

    //
     // CARDS
    //
    private static final CLIString invalidCardStackError = new CLIString(
            ">> Invalid card stack number! Please enter the correct stack's number\n",
            AnsiColor.RED, 0, 20);
    private static final CLIString noCardInTryLevel = new CLIString(
            ">> No card can be showed in the try level!\n",
            AnsiColor.RED, 0, 20);

    //
     // General
    //
    private static final CLIString unknownInput = new CLIString(
            ">> Wrong input! WHAT WAS THAT!!!\n",
            AnsiColor.RED, 0, 20);



    // End game phase
    private static final CLIString chooseStartingPosition = new CLIString(
            ">> Choose your starting position on the map [1-num_of_players]:\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString invalidStartingPosition = new CLIString(
            ">> The starting position you have chosen is invalid!\n",
            AnsiColor.RED, 0, 20);
    private static final CLIString positionAlreadyTaken = new CLIString(
            ">> The starting position you have chosen is already taken!\n",
            AnsiColor.RED, 0, 20);
    private static final CLIString waitingHourglassFinish = new CLIString (
            ">> Waiting the hourglass to finish the timer!\n",
            AnsiColor.YELLOW, 0, 20);
    private static final CLIString flipHourGlass = new CLIString (
            ">> Type [flip] to flip the hour glass:\n>> ",
            AnsiColor.YELLOW, 0, 21);
    private static final CLIString invalidHourGlassCommand = new CLIString(
            ">> Invalid hourglass command!\n",
            AnsiColor.RED, 0, 20);
    private static final CLIString finishTurnPrompt = new CLIString(
            ">> Turn finished! Waiting for other players...\n",
            AnsiColor.YELLOW, 0, 21);

    // Define fixed grid dimensions and cabin location
    private static final int SHIP_GRID_MIN_ROW = 5;
    private static final int SHIP_GRID_MAX_ROW = 9;
    private static final int SHIP_GRID_MIN_COL = 4;
    private static final int SHIP_GRID_MAX_COL = 10;

    // This is the DISPLAY coordinate where the "CABIN" placeholder appears if the slot is empty.
    private static final int CABIN_ROW = 7;
    private static final int CABIN_COL = 7;

    private static final int ROW_OFFSET_FOR_DISPLAY = 5; // Add this to internal row to get display row
    private static final int COL_OFFSET_FOR_DISPLAY = 4; // Add this to internal col to get display col
    private static final int TILE_RENDER_HEIGHT = 2;
    private static final int TILE_RENDER_WIDTH = 7;  // Width of one tile's output from renderCompactTile
    private static final String GRID_H_SPACER = " "; // Space between tile columns

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
         * Applies the CLI state actions based on the provided arguments.
         * Clears the screen, moves the cursor home, displays the "Galaxy Trucker" banner in the player's
         * corresponding color, and saves the cursor position. If the player's color is AnsiColor.DEFAULT,
         * no actions are performed.
         *
         * @param args an array of objects containing the required parameters for the action.
         *             The first element must be of type {@link PlayerColors} representing the player's color.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            if(translatePlayerColor((PlayerColors) args[0]) == AnsiColor.DEFAULT) return;

            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.displayGalaxyTrucker(5, translatePlayerColor((PlayerColors) args[0]));

            playerColor.print();
            CLICommands.saveCursorPosition();
        }

        private AnsiColor translatePlayerColor(PlayerColors playerColor) {
            return switch (playerColor) {
                case RED -> AnsiColor.RED;
                case BLUE -> AnsiColor.CYAN;
                case GREEN -> AnsiColor.GREEN;
                case YELLOW -> AnsiColor.YELLOW;
                default -> AnsiColor.DEFAULT;
            };
        }
    }

    public static class StartGameAdmin implements CLIState {
        /**
         * Applies the logic to manage the current CLI state based on the provided arguments.
         * This method restores the cursor position, checks the visibility of specific CLI elements,
         * and performs actions accordingly (deleting strings or printing prompts).
         *
         * @param args an array of objects representing optional parameters or values to process
         */
        @Override
        public void apply(Object[] args) {
            CLICommands.restoreCursorPosition();

            if(unknownInput.isVisible())
                unknownInput.deleteString();

            if(!wantsToStartGame.isVisible())
                wantsToStartGame.print();
            else
                wantsToStartGame.print();
            CLICommands.saveCursorPosition();
        }
    }

    public static class StartGameAdminError implements CLIState {
        /**
         * Applies changes to the Command-Line Interface (CLI) based on provided arguments.
         * This method restores the cursor position, clears user input, and displays specific
         * components in the terminal interface when necessary.
         *
         * @param args an array of arguments where the first element is expected to be a String
         *             representing the input to be cleared from the terminal. Additional elements
         *             in the array are not utilized in this implementation.
         */
        @Override
        public void apply(@NotNull Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(wantsToStartGame.isVisible())
                CLIString.replace(wantsToStartGame, chooseAction);

            CLICommands.restoreCursorPosition();
        }
    }

    public static class GameStarted implements CLIState {
        /**
         * Updates the state of the Command-Line Interface (CLI)
         * by managing the cursor position, clearing user input, deleting or
         * replacing specific strings based on their visibility, and saving
         * the updated cursor position.
         *
         * @param args an array of arguments where the first element (index 0)
         *        is expected to be a String representing the previous user input,
         *        used for clearing or modifying terminal output.
         */
        @Override
        public void apply(@NotNull Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(discardedTileStack != null && discardedTileStack.isVisible())
                discardedTileStack.deleteString();

            if(wantsToStartGame.isVisible())
                CLIString.replace(wantsToStartGame, chooseAction);
            else
                if(drawnTile != null && drawnTile.isVisible())
                    drawnTile.deleteString();
                else
                    chooseAction.print();
            CLICommands.saveCursorPosition();
        }
    }

    public static class DrawnActionState implements CLIState {
        /**
         * Executes the apply logic to update the CLI state, manage error messages, display the drawn tile
         * using the TechnicalTerminalPrinter, and update prompts based on provided arguments.
         *
         * @param args an array of {@link Object} where
         * args[0] is a {@link String} representing the necessary input for state transition,
         * args[1] is an instance of {@link Tile} used to generate the drawn tile display.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(drawnTile != null && drawnTile.isVisible()) {
                drawnTile.deleteString();
            }
            if(discardedTileStack != null && discardedTileStack.isVisible()) {
                discardedTileStack.deleteString();
            }

            if (!(args[1] instanceof Tile)) {
                System.err.println(TechnicalTerminalPrinter.PREFIX_ERROR + "Expected Tile object as argument [1] in DrawnActionState.");
                return;
            }
            Tile drawnTileObject = (Tile) args[1];

            int terminalWidth;
            try {
                terminalWidth = 160;
            } catch (NoClassDefFoundError | Exception e) {
                terminalWidth = 80; // Fallback width
                System.err.println(TechnicalTerminalPrinter.PREFIX_ERROR + "Could not determine terminal width, using default 80.");
            }

            List<Tile> singleTileList;
            try {
                singleTileList = List.of(drawnTileObject);
            } catch (NoClassDefFoundError | NoSuchMethodError e) {
                singleTileList = new ArrayList<>();
                singleTileList.add(drawnTileObject);
            }
            String tileBlockString = TechnicalTerminalPrinter.generateTileGrid(singleTileList, terminalWidth);
            CLIString drawnTileDisplay = new CLIString(tileBlockString, AnsiColor.DEFAULT, 0, 15);
            drawnTileDisplay.centerPrint();
            drawnTile = drawnTileDisplay;

            chooseAction.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class SaveTileState implements  CLIState {
        /**
         * Applies a series of state transitions and updates to the Command-Line Interface (CLI)
         * based on the provided arguments. This method ensures the proper rendering
         * and updating of CLI elements by clearing errors, toggling visibility states, and restoring
         * the cursor position.
         *
         * @param args an array of {@code Object} where the first element should be a {@code String}
         *             representing user input. This input will be used to initiate a state transition.
         *             The {@code args} array must not be {@code null} and must contain at least one element.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(drawnTile.isVisible()) {
                drawnTile.deleteString();
            }

            if(!savedTile.isVisible())
                savedTile.print();
            if(!chooseAction.isVisible())
                chooseAction.print();
            else
                chooseAction.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class SaveTileErrorState implements CLIState {
        /**
         * Applies the current CLI state transition logic.
         * This method transitions to a new state based on the provided arguments,
         * clears all error messages, handles the visibility of certain CLI elements,
         * and restores the CLI cursor position at the end.
         *
         * @param args an array of Objects where the first element is expected to be a
         *             String representing the user's input for processing the state
         *             transition
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(drawnTile.isVisible()) {
                drawnTile.deleteString();
            }

            if(!invalidSavedTile.isVisible())
                invalidSavedTile.print();
            if(!chooseAction.isVisible())
                chooseAction.print();
            else
                chooseAction.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class DrawFromSavedTilesState implements CLIState {
        /**
         * Applies the given state transition and updates the CLI interface based on the provided arguments.
         *
         * This method performs the following steps:
         * - Prepares a state transition using the first argument as input.
         * - Clears all existing error messages on the CLI.
         * - Updates the visibility of certain CLI elements: deletes the current `drawnTile` string if visible,
         *   and ensures the appropriate CLI strings such as `drewFromChosenTiles` and `chooseAction` are printed
         *   based on their visibility status.
         * - Restores the cursor position to its previous location.
         *
         * @param args An array of objects where the first element is expected to be a string used for
         *             state preparation. Additional elements in the array are not used in this implementation.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(drawnTile.isVisible()) {
                drawnTile.deleteString();
            }

            if (!(args[1] instanceof Tile)) {
                System.err.println(TechnicalTerminalPrinter.PREFIX_ERROR + "Expected Tile object as argument [1] in DrawnActionState.");
                return;
            }
            Tile drawnTileObject = (Tile) args[1];

            int terminalWidth;
            try {
                terminalWidth = 160;
            } catch (NoClassDefFoundError | Exception e) {
                terminalWidth = 80; // Fallback width
                System.err.println(TechnicalTerminalPrinter.PREFIX_ERROR + "Could not determine terminal width, using default 80.");
            }

            List<Tile> singleTileList;
            try {
                singleTileList = List.of(drawnTileObject);
            } catch (NoClassDefFoundError | NoSuchMethodError e) {
                singleTileList = new ArrayList<>();
                singleTileList.add(drawnTileObject);
            }
            String tileBlockString = TechnicalTerminalPrinter.generateTileGrid(singleTileList, terminalWidth);
            CLIString drawnTileDisplay = new CLIString(tileBlockString, AnsiColor.DEFAULT, 0, 15);
            drawnTileDisplay.centerPrint();
            drawnTile = drawnTileDisplay;

            chooseAction.print();
            CLICommands.restoreCursorPosition();
        }
    }
    public static class DrawFromSavedTilesErrorState implements CLIState {
        /**
         * Applies the state transition and manages the visibility and printing of specific tiles
         * in the command-line interface (CLI).
         *
         * This method first prepares the state transition using the provided argument and clears
         * all error messages. It then performs visibility checks and prints or deletes strings
         * from the CLI as required. Finally, it restores the cursor position.
         *
         * @param args an array of objects where the first element is expected to be a String
         *             representing the input for state transition.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(drawnTile.isVisible()) {
                drawnTile.deleteString();
            }

            if(!couldNotDrawFromSavedTiles.isVisible())
                couldNotDrawFromSavedTiles.print();
            if(!chooseAction.isVisible())
                chooseAction.print();
            else
                chooseAction.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class PlacedTileState implements CLIState {
        /**
         * Applies the current state logic in the CLI context.
         * Clears all error messages from the interface, ensures necessary UI components are visible and printed,
         * and saves the current cursor position for further interactions.
         *
         * @param args an array of objects passed to the method, representing additional data or parameters required
         *             to apply the logic of the current CLI state
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(drawnTile != null && drawnTile.isVisible())
                drawnTile.deleteString();

            if(!placedTile.isVisible())
                placedTile.print();
            else
                placedTile.print();

            if(!chooseAction.isVisible())
                chooseAction.print();
            else
                chooseAction.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class PlacedTileErrorState implements CLIState {
        /**
         * Applies the actions specific to the current CLI state. This method performs the following:
         * - Clears all error messages from the command-line interface.
         * - Displays the placed tile and action prompts if they are not already visible.
         * - Saves the current cursor position in the command-line interface for subsequent updates.
         *
         * @param args an array of objects containing arguments for the method. The arguments
         *             may be used to customize the behavior of the method, although this
         *             implementation does not directly utilize them.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(drawnTile != null && drawnTile.isVisible())
                CLIString.replace(drawnTile, invalidPositionPlacedTile);
            else
                invalidPositionPlacedTile.print();

            if(!chooseAction.isVisible())
                chooseAction.print();
            else
                chooseAction.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class AlreadySavedTileState implements CLIState {
        /**
         * Applies the state-specific logic for transitioning to or preparing the currently saved tile state
         * in the CLI context.
         *
         * @param args an array of objects where the first element is expected to be a {@link String}
         *             representing the input used to prepare the state transition
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(drawnTile != null && drawnTile.isVisible()) {
                drawnTile.deleteString();
            }

            if(!tileAlreadySaved.isVisible())
                tileAlreadySaved.print();
            else
                tileAlreadySaved.print();

            if(!chooseAction.isVisible())
                chooseAction.print();
            else
                chooseAction.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class PlacingTileState implements CLIState {
        /**
         * Executes the apply operation for the current CLI state, clearing any existing error messages
         * and handling the display of the placement prompt or replacing a visible action prompt.
         *
         * @param args an array of objects that may be used as parameters for the apply operation
         */
        @Override
        public void apply(Object [] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if (chooseAction.isVisible())
                CLIString.replace(chooseAction, placementPrompt);
            else
                placementPrompt.print();
            CLICommands.saveCursorPosition();
        }
    }

    public static class InvalidFormatState implements CLIState {
        /**
         * Applies the state-specific behavior defined for invalid format errors in the command-line interface.
         * The method performs the following operations:
         * - Restores the terminal cursor position to the last saved location.
         * - Clears the previously entered user input based on the provided arguments.
         * - Deletes visible error messages for invalid coordinates and rotation errors.
         * - Displays the error message for invalid format if it is not already visible.
         * - Prints the placement prompt for the next user input.
         * - Restores the cursor position again to ensure proper alignment for user input.
         *
         * @param args an array of objects where the first element is expected to be a {@code String} containing
         *             the user-input text to be cleared from the terminal display
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(!drawnTile.isVisible())
                drawnTile.deleteString();

            if (!invalidFormatError.isVisible())
                invalidFormatError.print();
            if(!placementPrompt.isVisible())
                placementPrompt.print();
            else
                placementPrompt.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class InvalidCoordinatesState implements CLIState {
        /**
         * Performs various terminal interface actions in response to the input arguments.
         * This method restores the cursor position, clears user input, and handles the visibility
         * of error messages and prompts in the terminal interface.
         *
         * The method is used to manage the dynamic display of a Command-Line Interface (CLI)
         * by interacting with multiple components such as error messages and input prompts.
         *
         * @param args an array of arguments, where:
         *             - args[0] is expected to be a String representing the user input to be cleared.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if (!invalidCoordinatesError.isVisible())
                invalidCoordinatesError.print();
            if(!placementPrompt.isVisible())
                placementPrompt.print();
            else
                placementPrompt.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class InvalidRotationState implements CLIState {
        /**
         * Applies state-specific behavior for handling CLI actions such as restoring the cursor position,
         * clearing user input, managing error messages, and printing the placement prompt.
         *
         * @param args an array of objects containing parameters required for the state logic.
         *             The first element of the array is expected to be a String representing the
         *             user input that needs to be cleared from the terminal.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if (!invalidRotationError.isVisible())
                invalidRotationError.print();

            if (!chooseAction.isVisible())
                chooseAction.print();
            else
                chooseAction.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class InvalidGeneralInputState implements CLIState {
        /**
         * Applies the specified arguments to transition the CLI state and handle display updates.
         * The method initiates a state transition based on the provided input, clears any existing
         * error messages on the CLI, and ensures that any unknown input is displayed if necessary.
         * The cursor position in the CLI is also restored after operations.
         *
         * @param args an array of {@link Object} containing the input parameters.
         *             The first argument (args[0]) must be a {@link String} representing the input to manage state transition.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(drawnTile != null && drawnTile.isVisible())
                drawnTile.deleteString();
            if (!unknownInput.isVisible())
                unknownInput.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class ShowDrewTilesState implements CLIState {
        /**
         * Applies the state transition and updates the command line interface (CLI)
         * to reflect the changes based on the provided arguments. This includes
         * clearing any previous error messages, handling the discarded tiles display,
         * and updating relevant state transitions.
         *
         * @param args an array of objects where:
         *             - args[0] is a {@link String} representing the current input to prepare the state transition.
         *             - args[1] is a {@link List} of {@link Tile} objects representing the discarded tiles
         *               to be displayed on the CLI.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(shipDisplayOutput != null && shipDisplayOutput.isVisible())
                shipDisplayOutput.deleteString();

            discardedTileStack = new CLIString(generateDiscardedTiles((List<Tile>)args[1]), AnsiColor.DEFAULT, 0, 25);
            if(!discardedTileStack.isVisible())
                discardedTileStack.print();
            if(chooseAction.isVisible())
                CLIString.replace(chooseAction, chooseDiscardedTile);
            else
                chooseDiscardedTile.print();

            CLICommands.restoreCursorPosition();
        }

        private String generateDiscardedTiles(List<Tile> discardedTileStack) {
            StringBuilder output = new StringBuilder();
            int terminalWidth = 160;

            output.append(TechnicalTerminalPrinter.generateHeader("DISCARDED TILES", terminalWidth));

            if (discardedTileStack == null || discardedTileStack.isEmpty()) {
                output.append(TechnicalTerminalPrinter.PREFIX_PROMPT)
                        .append("No tile discarded available.\n");
            } else {
                output.append(TechnicalTerminalPrinter.PREFIX_PROMPT)
                        .append("Tiles discarded are available (choose by index):\n");
                output.append(TechnicalTerminalPrinter.generateTileGrid(discardedTileStack, terminalWidth));
            }

            output.append(TechnicalTerminalPrinter.generateFooter(terminalWidth));

            return output.toString();
        }
    }

    public static class InvalidChooseDrewTilesState implements CLIState {
        /**
         * Handles state transitions, clears any error messages, and updates the CLI
         * to reflect the current state of discarded tiles and available actions.
         *
         * @param args an array of arguments where:
         *             args[0] is a String representing the input used for the state transition,
         *             args[1] is a List of Tile objects representing the discarded tiles.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(drawnTile != null && drawnTile.isVisible())
                CLIString.replace(drawnTile, unknownInput);
            else
                unknownInput.print();

            discardedTileStack = new CLIString(generateDiscardedTiles((List<Tile>)args[1]), AnsiColor.DEFAULT, 0, 25);
            if(!discardedTileStack.isVisible())
                discardedTileStack.print();
            else
                discardedTileStack.print();
            if(chooseAction.isVisible())
                CLIString.replace(chooseAction, chooseDiscardedTile);
            else
                chooseDiscardedTile.print();

            CLICommands.restoreCursorPosition();
        }

        private String generateDiscardedTiles(List<Tile> discardedTileStack) {
            StringBuilder output = new StringBuilder();
            int terminalWidth = 160;

            output.append(TechnicalTerminalPrinter.generateHeader("DISCARDED TILES", terminalWidth));

            if (discardedTileStack == null || discardedTileStack.isEmpty()) {
                output.append(TechnicalTerminalPrinter.PREFIX_PROMPT)
                        .append("No tile discarded available.\n");
            } else {
                output.append(TechnicalTerminalPrinter.PREFIX_PROMPT)
                        .append("Tiles discarded are available (choose by index):\n");
                output.append(TechnicalTerminalPrinter.generateTileGrid(discardedTileStack, terminalWidth));
            }

            output.append(TechnicalTerminalPrinter.generateFooter(terminalWidth));

            return output.toString();
        }
    }

    public static class NoTileInDrawingStageState implements CLIState {
        /**
         * Applies the logic for the current CLI state by transitioning state, clearing error messages,
         * and handling the visibility and printing of error messages or notifications on the CLI.
         *
         * @param args an array of objects where the first element (index 0) is expected to be a
         *             string input required for state transition.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(!thereIsNotATileDrew.isVisible())
                thereIsNotATileDrew.print();
            else
                thereIsNotATileDrew.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class EmptyFlippedTilesState implements CLIState {
        /**
         * Applies the given arguments to transition the CLI state and update the
         * user interface accordingly. Handles error message clearing, visibility checks,
         * and printing for specific CLI components.
         *
         * @param args an array of objects where the first element is expected to be a
         *             {@code String} for state transition preparation.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(!emptyFlippedTiles.isVisible())
                emptyFlippedTiles.print();
            else
                emptyFlippedTiles.print();
            if(!chooseAction.isVisible())
                chooseAction.print();
            CLICommands.restoreCursorPosition();
        }
    }



    //
     // CARD STACK
    //
    public static class ShowCardStackState implements CLIState {

        private CLIString currentCardStackDisplay = null;

        /**
         * Processes the application of the current CLI state to clear errors, display a card stack,
         * and re-show the action prompt. This method handles user inputs and updates the CLI view
         * appropriately based on the provided arguments.
         *
         * @param args an array of objects passed as arguments:
         * - args[0]: a String representing the user's input to be cleared.
         * - args[1]: a CardStack object containing the cards to be displayed in the CLI.
         */
        @Override
        public void apply(@NotNull Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            CardStack cardStackToDisplay = (CardStack) args[1];

            int terminalWidth;
            try {
                terminalWidth = 40;
            } catch (NoClassDefFoundError | Exception e) {
                terminalWidth = 80; // Fallback width
                System.err.println("Warning: Could not determine terminal width, using default 80.");
            }

            if(shipDisplayOutput != null && shipDisplayOutput.isVisible()) {
                shipDisplayOutput.deleteString();
            }

            String formattedCards = TechnicalTerminalPrinter.generateCardGrid(cardStackToDisplay.getCards(), terminalWidth);
            CLIString cardStackDisplay = new CLIString(formattedCards, AnsiColor.DEFAULT, 0, 25); // Create CLIString
            if (currentCardStackDisplay != null && currentCardStackDisplay.isVisible()) {
                CLIString.replace(currentCardStackDisplay, cardStackDisplay);
            } else {
                cardStackDisplay.print();
            }
            // Update the reference to the current display
            currentCardStackDisplay = cardStackDisplay;
            chooseAction.print();


            CLICommands.restoreCursorPosition();
        }
    }

    public static class InvalidCardStackState implements CLIState {
        /**
         * Applies the current CLI state's behavior and handles various error and feedback messages,
         * guiding the user towards corrective actions for invalid or incorrect inputs.
         *
         * This method interacts with the terminal display to:
         * - Restore the cursor position.
         * - Clear the user's current input.
         * - Manage the display of error messages based on their visibility.
         * - Prompt the user to choose an appropriate action.
         *
         * @param args an array of objects containing the arguments for the method,
         *             where the first element in the array is expected to be a String
         *             representing the user input to be cleared.
         */
        @Override
        public void apply(@NotNull Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(drawnTile != null && drawnTile.isVisible())
                drawnTile.deleteString();
            if (!invalidCardStackError.isVisible())
                invalidCardStackError.print();
            chooseAction.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class NoShowingCardInTryLevelState implements CLIState {
        /**
         * Applies the current state changes and updates the Command-Line Interface (CLI)
         * based on the provided arguments. The method manages state transitions, clears
         * error messages, and prints necessary CLI elements only if they are not already
         * visible. It also restores the cursor position to ensure a consistent user interface.
         *
         * @param args an array of objects containing arguments required for state changes.
         *             The first element (args[0]) must be a String representing user input
         *             for the state transition process.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(!noCardInTryLevel.isVisible())
                noCardInTryLevel.print();
            if(!chooseAction.isVisible())
                chooseAction.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class ChooseStartingPositionState implements CLIState {
        /**
         * Applies a sequence of terminal commands to clear user input, handle any visible error messages,
         * manage the cursor position, and display a starting position prompt.
         *
         * @param args an array of arguments; the first element is expected to be a string representing user input
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            // Show starting position prompt
            if(chooseAction.isVisible())
                CLIString.replace(chooseAction, chooseStartingPosition);
            else
                if(!chooseStartingPosition.isVisible())
                    chooseStartingPosition.print();
                else
                    chooseStartingPosition.print();
            CLICommands.saveCursorPosition();
        }
    }

    public static class InvalidStartingPositionState implements CLIState {
        /**
         * Executes the logic for handling the invalid starting position state in a CLI-based application.
         * This method clears user input and certain error messages, displays an error message indicating
         * the invalid position, and re-displays the prompt for the user to choose a starting position.
         *
         * @param args an array of objects containing input data, where the first element is expected
         *             to be a string representing the user's previous input
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            // Show invalid position error
            if (!invalidStartingPosition.isVisible())
                invalidStartingPosition.print();
            else
                invalidStartingPosition.print();

            // Re-show prompt
            chooseStartingPosition.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class PositionAlreadyTakenState implements CLIState {
        /**
         * Applies the behavior associated with the current state of the application.
         * The method clears existing messages, updates the user interface, and displays
         * relevant prompts or error messages, ensuring proper terminal behavior.
         *
         * @param args an array of Objects containing necessary context or arguments for
         *             the current state. The first element is expected to be a String
         *             representing the user input to be cleared.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            // Show position taken error
            if (!positionAlreadyTaken.isVisible())
                positionAlreadyTaken.print();

            // Re-show prompt
            chooseStartingPosition.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class FlipHourGlassState implements CLIState {
        /**
         * Handles the application of state transitions for the CLI interface.
         * Performs a series of operations including preparing the state transition
         * based on input arguments, clearing error messages, rendering UI components,
         * and restoring cursor position.
         *
         * @param args an array of objects where the first element is expected to be
         *             a string representing the input for state transition.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(!flipHourGlass.isVisible())
                flipHourGlass.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class InvalidInputForFlipState implements CLIState {
        /**
         * Applies the state transition logic for the current CLIState. The method processes
         * input arguments, clears any existing error messages, and ensures the proper rendering
         * of hourglass commands in the CLI. Cursor position is restored at the end of the execution.
         *
         * @param args The input arguments for transitioning the state. The first argument is
         *             expected to be a String representing user input, which is processed
         *             during state transition preparation.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(!invalidHourGlassCommand.isVisible())
                invalidHourGlassCommand.print();
            if(!flipHourGlass.isVisible())
                flipHourGlass.print();
            else
                flipHourGlass.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class WaitingForHourglassToFinishState implements CLIState {
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(chooseStartingPosition.isVisible())
                chooseStartingPosition.deleteString();

            if(!waitingHourglassFinish.isVisible())
                waitingHourglassFinish.print();
            CLICommands.restoreCursorPosition();
        }
    }

    // --- Static Helper Methods for Tile Rendering ---
    private static String getTileDisplayName(Tile tile) {
        if (tile == null) return "";
        if (tile instanceof BatteryStorageTile) return "Battery";
        if (tile instanceof CabinTile) return "Cabin";
        if (tile instanceof CannonTile) return "Cannon";
        if (tile instanceof CargoHoldTile) return "Cargo";
        if (tile instanceof DoubleCannonTile) return "DblCannon";
        if (tile instanceof DoubleMotorTile) return "DblMotor";
        if (tile instanceof ShieldTile) return "Shield";
        if (tile instanceof StructuralModuleTile) return "Struct";
        if (tile instanceof VitalSupportTile) return "Vital";
        if (tile instanceof MotorTile) return "Motor";
        return ""; // Default for unknown tile types
    }

    private static char getConnectorSymbol(ConnectorType type, Direction onTileFace) {
        if (type == null) return ' ';
        switch (type) {
            case SMOOTH: return '#';
            case SINGLE: return (onTileFace == Direction.TOP || onTileFace == Direction.BOTTOM) ? '│' : '/';
            case DOUBLE: return (onTileFace == Direction.TOP || onTileFace == Direction.BOTTOM) ? '║' : '═';
            case UNIVERSAL: return '*';
            default: return '?';
        }
    }

    private static List<String> renderCompactTile(Tile tile, int tileNumberForID, boolean isCabinSlot) {
        final int TILE_HEIGHT = 2;
        final int TILE_WIDTH = 7;
        final String EMPTY_SLOT_SYMBOL_TEXT = isCabinSlot ? "CABIN" : "EMPTY";

        if (tile == null) {
            List<String> emptyRender = new ArrayList<>();
            emptyRender.add(String.format("┌%s┐", String.join("", Collections.nCopies(TILE_WIDTH - 2, "─"))));
            emptyRender.add(String.format("│%-" + (TILE_WIDTH - 2) + "." + (TILE_WIDTH - 2) + "s│", EMPTY_SLOT_SYMBOL_TEXT));
            return emptyRender;
        }

        char topSym = getConnectorSymbol(tile.getTopConnector().getType(), Direction.TOP);
        char rightSym = getConnectorSymbol(tile.getRightConnector().getType(), Direction.RIGHT);
        char bottomSym = getConnectorSymbol(tile.getBottomConnector().getType(), Direction.BOTTOM);
        char leftSym = getConnectorSymbol(tile.getLeftConnector().getType(), Direction.LEFT);

        String idStr = null;
        String imageFromTile = tile.getImage();

        if (imageFromTile != null && imageFromTile.length() == 1) {
            char c = imageFromTile.charAt(0);
            if (!Character.isISOControl(c) && !Character.isWhitespace(c)) {
                idStr = String.valueOf(c).toUpperCase();
            }
        }

        if (idStr == null) {
            String displayNameFromType = getTileDisplayName(tile);
            if (displayNameFromType != null && !displayNameFromType.isEmpty()) {
                idStr = displayNameFromType.substring(0, 1).toUpperCase();
            }
        }

        if (idStr == null) {
            idStr = String.valueOf(tileNumberForID % 10);
        }

        List<String> lines = new ArrayList<>();
        lines.add(String.format("┌─%c%c%c─┐", leftSym, topSym, rightSym));
        lines.add(String.format("│ %s %c │", idStr, bottomSym));
        return lines;
    }

    private static String buildShipOutputString(List<TileCoordinates> shipCoordinatesList) {
        StringBuilder sb = new StringBuilder();

        // --- 1. Prepare Tile Data with Display Coordinates ---
        Map<String, Tile> placedTilesMap = new HashMap<>();
        if (shipCoordinatesList != null) {
            for (TileCoordinates tc : shipCoordinatesList) {
                if (tc.getTile() != null) {
                    // Ensure ROW_OFFSET_FOR_DISPLAY and COL_OFFSET_FOR_DISPLAY are defined if you use them
                    int displayRow = tc.getRow() + ROW_OFFSET_FOR_DISPLAY;
                    int displayCol = tc.getCol() + COL_OFFSET_FOR_DISPLAY;
                    placedTilesMap.put(displayRow + "," + displayCol, tc.getTile());
                }
            }
        }

        // --- 2. Calculate Grid Dimensions for Display ---
        int numDisplayGridRows = SHIP_GRID_MAX_ROW - SHIP_GRID_MIN_ROW + 1;
        int numDisplayGridCols = SHIP_GRID_MAX_COL - SHIP_GRID_MIN_COL + 1;

        // --- 3. Pre-render all visible tiles ---
        Map<Integer, Map<Integer, List<String>>> gridRenderings = new HashMap<>();
        int displayTileCounter = 0;
        for (int r_disp = SHIP_GRID_MIN_ROW; r_disp <= SHIP_GRID_MAX_ROW; r_disp++) {
            for (int c_disp = SHIP_GRID_MIN_COL; c_disp <= SHIP_GRID_MAX_COL; c_disp++) {
                Tile currentTile = placedTilesMap.get(r_disp + "," + c_disp);
                boolean isCabinSlotForRender = (r_disp == CABIN_ROW && c_disp == CABIN_COL);
                if (currentTile != null) displayTileCounter++;

                List<String> tileStrList = renderCompactTile(currentTile, displayTileCounter, isCabinSlotForRender);
                gridRenderings.computeIfAbsent(r_disp - SHIP_GRID_MIN_ROW, k -> new HashMap<>())
                        .put(c_disp - SHIP_GRID_MIN_COL, tileStrList);
            }
        }

        // --- 4. Build Fancy Output ---
        String shipName = "YOUR INTERSTELLAR TRUCK"; // Or your preferred title
        // Calculate width needed for tiles and the spaces *between* them
        int tilesAndSpacersWidth = (numDisplayGridCols * TILE_RENDER_WIDTH) + Math.max(0, numDisplayGridCols - 1) * GRID_H_SPACER.length();

        int titleBarContentWidth = tilesAndSpacersWidth;

        String titlePadding = String.join("", Collections.nCopies(
                Math.max(0, (titleBarContentWidth - shipName.length()) / 2), " "));

        sb.append("\n");
        // Fancy Header
        sb.append(AnsiColor.YELLOW.getCode());
        sb.append("╔").append(String.join("", Collections.nCopies(titleBarContentWidth + 2 * 1, "═"))).append("╗\n"); // Adjusted padding
        sb.append("║ ").append(titlePadding).append(AnsiColor.YELLOW.getCode()).append(shipName).append(AnsiColor.YELLOW.getCode())
                .append(titlePadding).append(shipName.length() % 2 != 0 ? " " : "").append(" ║\n"); // Ensure enough spaces for the ║
        sb.append("╠").append(String.join("", Collections.nCopies(titleBarContentWidth + 2 * 1, "═"))).append("╣\n");
        sb.append(AnsiColor.YELLOW.getCode());

        // Column Headers
        String rowLabelIndent = "    ";
        sb.append(AnsiColor.DEFAULT.getCode()).append(rowLabelIndent);
        for (int cIdx = 0; cIdx < numDisplayGridCols; cIdx++) {
            sb.append(String.format("%-" + TILE_RENDER_WIDTH + "s", "C" + (cIdx + SHIP_GRID_MIN_COL)));
            if (cIdx < numDisplayGridCols - 1) sb.append(GRID_H_SPACER);
        }
        sb.append("\n").append(AnsiColor.DEFAULT.getCode());

        // Helper string for a single cell's horizontal border segment
        String singleCellHBorderSegment = String.join("", Collections.nCopies(TILE_RENDER_WIDTH, "═"));

        // Top Grid Border
        sb.append(AnsiColor.DEFAULT.getCode()).append("  ╔"); // Indent for row labels
        for (int cIdx = 0; cIdx < numDisplayGridCols; cIdx++) {
            sb.append(singleCellHBorderSegment);
            if (cIdx < numDisplayGridCols - 1) {
                sb.append("╦");
            } else {
                sb.append("╗");
            }
        }
        sb.append("\n").append(AnsiColor.DEFAULT.getCode());

        // Grid Content
        for (int rIdx = 0; rIdx < numDisplayGridRows; rIdx++) {
            String rowLabel = String.format("R%d", (rIdx + SHIP_GRID_MIN_ROW));

            for (int lineNum = 0; lineNum < TILE_RENDER_HEIGHT; lineNum++) {
                if (lineNum == 0) {
                    sb.append(AnsiColor.DEFAULT.getCode()).append(String.format("%-2s", rowLabel)).append("║").append(AnsiColor.DEFAULT.getCode());
                } else {
                    sb.append(AnsiColor.DEFAULT.getCode()).append("  ").append("║").append(AnsiColor.DEFAULT.getCode());
                }

                for (int cIdx = 0; cIdx < numDisplayGridCols; cIdx++) {
                    List<String> tileLines = gridRenderings.get(rIdx).get(cIdx);
                    sb.append(tileLines.get(lineNum));
                    if (cIdx < numDisplayGridCols - 1) {
                        sb.append(AnsiColor.DEFAULT.getCode()).append(GRID_H_SPACER).append(AnsiColor.DEFAULT.getCode());
                    }
                }
                sb.append(AnsiColor.DEFAULT.getCode()).append("║").append(AnsiColor.DEFAULT.getCode());
                sb.append("\n");
            }

            // Inter-row border
            if (rIdx < numDisplayGridRows - 1) {
                sb.append(AnsiColor.DEFAULT.getCode()).append("  ╠");
                for (int cIdx = 0; cIdx < numDisplayGridCols; cIdx++) {
                    sb.append(singleCellHBorderSegment);
                    if (cIdx < numDisplayGridCols - 1) {
                        sb.append("╬");
                    } else {
                        sb.append("╣");
                    }
                }
                sb.append("\n").append(AnsiColor.DEFAULT.getCode());
            }
        }

        // Bottom Grid Border
        sb.append(AnsiColor.DEFAULT.getCode()).append("  ╚");
        for (int cIdx = 0; cIdx < numDisplayGridCols; cIdx++) {
            sb.append(singleCellHBorderSegment);
            if (cIdx < numDisplayGridCols - 1) {
                sb.append("╩");
            } else {
                sb.append("╝");
            }
        }
        sb.append("\n").append(AnsiColor.DEFAULT.getCode());

        // Footer
        sb.append(AnsiColor.YELLOW.getCode());
        sb.append("╚").append(String.join("", Collections.nCopies(titleBarContentWidth + 2 * 1, "═"))).append("╝\n");
        sb.append(AnsiColor.DEFAULT.getCode());

        return sb.toString();
    }

    // ---------------------------
    public static class ShowShipState implements CLIState {
        private static final int TILE_RENDER_HEIGHT = 2; // Matches renderCompactTile
        private static final int TILE_RENDER_WIDTH = 7;  // Matches renderCompactTile
        private static final String TILE_H_SPACER = " "; // Space between tiles
        private static final String GRID_INDENT = "  ";

        /**
         * Applies the transformation or operation specified for the current state.
         * Handles the visualization of a ship based on the provided data, including
         * deleting previous ship visuals, building new ship strings, and updating the CLI display.
         *
         * @param DTO An object array containing data required for this operation.
         *            The second element of the array is expected to be a List of TileCoordinates objects
         *            representing the positions and details of the ship tiles to be displayed.
         */
        @Override
        @SuppressWarnings("unchecked")
        public void apply(Object[] DTO) {
            prepareStateTransition((String) DTO[0]);
            clearAllErrorMessages();

            // 1. Delete previous ship display if it was visible
            if (shipDisplayOutput != null && shipDisplayOutput.isVisible()) {
                shipDisplayOutput.deleteString();
            }
            List<TileCoordinates> shipCoordinatesList = (List<TileCoordinates>) DTO[1];

            // 2. Build the new complete ship string
            String fullShipViewString = buildShipOutputString(shipCoordinatesList);

            // 3. Create the global CLIString for the ship
            shipDisplayOutput = new CLIString(fullShipViewString, AnsiColor.DEFAULT, 0, 25);
            shipDisplayOutput.print();

            if(!chooseAction.isVisible())
                chooseAction.print();
            else
                chooseAction.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class EmptyShipState implements CLIState {
        /**
         * Updates the command-line interface (CLI) to display the current status of an empty ship.
         * Provides instructions and prompts for the user to take action, ensuring previous output is cleared before displaying.
         *
         * @param DTO an array of objects containing any data necessary for the method, though not directly utilized in this implementation.
         *            This parameter is included to conform to interface requirements.
         */
        @Override
        public void apply(Object[] DTO) {
            prepareStateTransition((String) DTO[0]);
            clearAllErrorMessages();

            if (shipDisplayOutput != null && shipDisplayOutput.isVisible()) {
                shipDisplayOutput.deleteString();
            }

            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            sb.append(AnsiColor.YELLOW.getCode())
                    .append("+---------- EMPTY SHIP STATUS ----------+")
                    .append(AnsiColor.DEFAULT.getCode())
                    .append("\n");
            sb.append(ShowShipState.GRID_INDENT).append("Your ship is currently empty. Place tiles to build it!\n");
            sb.append(ShowShipState.GRID_INDENT).append("The central cabin is expected at (Row 7, Col 7).\n");
            sb.append(ShowShipState.GRID_INDENT).append("Display: Rows 5-9 and Columns 4-10.\n\n");

            shipDisplayOutput = new CLIString(sb.toString(), AnsiColor.DEFAULT, 0, 25);
            shipDisplayOutput.print();

            if(!chooseAction.isVisible())
                chooseAction.print();
            else
                chooseAction.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class FinishTurnState implements CLIState {
        /**
         * Applies the "Finish Turn" state-related actions, including resetting cursor position,
         * clearing user input, clearing various error messages, displaying the finish turn prompt,
         * and saving the cursor position.
         *
         * @param args an array of arguments where the first element represents the user input string
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllMessages();

            // Show finish turn message
            if(chooseAction.isVisible())
                CLIString.replace(chooseAction, finishTurnPrompt);
            else if (placementPrompt.isVisible())
                CLIString.replace(placementPrompt, finishTurnPrompt);
            else if (!finishTurnPrompt.isVisible())
                finishTurnPrompt.print();
            else if (chooseDiscardedTile.isVisible())
                CLIString.replace(chooseDiscardedTile, finishTurnPrompt);
            else
                finishTurnPrompt.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class ReturnToActionSelectionState implements CLIState {
        /**
         * Applies changes to the current CLI state, clears user input, removes any visible error messages,
         * and resets prompts to the default action selection state.
         *
         * @param args An array of objects containing arguments required for processing the state.
         *             The first element is expected to be a String representing the user input to be cleared.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);

            // Return to action selection
            if (placementPrompt.isVisible())
                CLIString.replace(placementPrompt, chooseAction);
            else if (chooseStartingPosition.isVisible())
                CLIString.replace(chooseStartingPosition, chooseAction);
            else if (finishTurnPrompt.isVisible())
                CLIString.replace(finishTurnPrompt, chooseAction);
            else if (chooseDiscardedTile.isVisible())
                CLIString.replace(chooseDiscardedTile, chooseAction);
            else
                chooseAction.print();

            CLICommands.saveCursorPosition();
        }
    }

    public static class DeleteAllMessages implements CLIState {
        /**
         * Applies the given arguments to perform state transition and clears all messages.
         *
         * @param args an array of arguments, where the first element is expected to be a string input
         *             used for state transition preparation.
         */
        @Override
        public void apply(Object[] args) {
            clearAllMessages();
        }
    }

    private static void clearAllErrorMessages() {
        if (playerColor.isVisible()) playerColor.deleteString();
        if (placedTile.isVisible()) placedTile.deleteString();
        if (invalidPositionPlacedTile.isVisible()) invalidPositionPlacedTile.deleteString();
        if (invalidFormatError.isVisible()) invalidFormatError.deleteString();
        if (invalidCoordinatesError.isVisible()) invalidCoordinatesError.deleteString();
        if (invalidRotationError.isVisible()) invalidRotationError.deleteString();
        if (invalidCardStackError.isVisible()) invalidCardStackError.deleteString();
        if (invalidStartingPosition.isVisible()) invalidStartingPosition.deleteString();
        if (thereIsNotATileDrew.isVisible()) thereIsNotATileDrew.deleteString();
        if (positionAlreadyTaken.isVisible()) positionAlreadyTaken.deleteString();
        if (unknownInput.isVisible()) unknownInput.deleteString();
        if (emptyFlippedTiles.isVisible()) emptyFlippedTiles.deleteString();
        if (savedTile.isVisible()) savedTile.deleteString();
        if (invalidSavedTile.isVisible()) invalidSavedTile.deleteString();
        if (tileAlreadySaved.isVisible()) tileAlreadySaved.deleteString();
    }

    private static void clearAllMessages() {
        clearAllErrorMessages();
        if(drawnTile != null && drawnTile.isVisible()) drawnTile.deleteString();
        if(discardedTileStack != null && discardedTileStack.isVisible()) discardedTileStack.deleteString();
        if(shipDisplayOutput != null && shipDisplayOutput.isVisible()) shipDisplayOutput.deleteString();
        if(placementPrompt.isVisible()) placementPrompt.deleteString();
        if(finishTurnPrompt.isVisible()) finishTurnPrompt.deleteString();
        if(flipHourGlass.isVisible()) flipHourGlass.deleteString();
        if(finishTurnPrompt.isVisible()) finishTurnPrompt.deleteString();
    }

    private static void prepareStateTransition(String input) {
        CLICommands.restoreCursorPosition();
        CLICommands.clearUserInput(input);
    }

}