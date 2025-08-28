package it.polimi.ingsoftware.ll13.client.utils.printers.specific_printers;

import it.polimi.ingsoftware.ll13.client.utils.ansi.AnsiColor;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIInterface;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIState;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIString;
import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

    public class CLISetEquipePrinter implements CLIInterface {

        public static final CLIString initialPrompt = new CLIString(
                ">> The ship is now CORRECTLY BUILT. Set up the ship if you need to!\n",
                AnsiColor.YELLOW, 0, 20
        );

        public static final CLIString insertEquipe = new CLIString(
                ">> Where do you want to insert crew members? [row]:[col]\n>> ",
                AnsiColor.DEFAULT, 0, 21
        );
        public static final CLIString insertEquipeType = new CLIString(
                ">> What do you want to insert? [crew]/[alienp]/[alieny]\n>> ",
                AnsiColor.DEFAULT, 0, 21
        );

        public static final CLIString insertEquipeError = new CLIString(
                ">> Wrong input! Insert coordinates in this format: [row]:[col].\n",
                AnsiColor.RED, 0, 20
        );
        public static final CLIString insertEquipeTypeError = new CLIString(
                ">> Wrong input! Insert the right type of crew members.\n",
                AnsiColor.RED, 0, 20
        );
        public static final CLIString invalidEquipeError = new CLIString(
                ">> Wrong placement! We had a problem with that!.\n",
                AnsiColor.RED, 0, 20
        );

        public static final CLIString waitingForNextStage = new CLIString (
                ">> Waiting that all other players finish the equipping phase!.\n",
                AnsiColor.YELLOW, 0, 20
        );


        public static CLIString shipDisplayOutput;

        private CLIState state = new Default();

        @Override
        public void changeState(@NotNull CLIState state) {
            this.state = state;
        }

        @Override
        public void print(Object[] args) {
            state.apply(args);
        }

        // --- Ship Rendering Constants (as in CLIValidationPrinter) ---
        private static final int SHIP_GRID_MIN_ROW = 5;
        private static final int SHIP_GRID_MAX_ROW = 9;
        private static final int SHIP_GRID_MIN_COL = 4;
        private static final int SHIP_GRID_MAX_COL = 10;
        private static final int CABIN_ROW = 7; // This is a DISPLAY coordinate
        private static final int CABIN_COL = 7; // This is a DISPLAY coordinate
        private static final int ROW_OFFSET_FOR_DISPLAY = 5;
        private static final int COL_OFFSET_FOR_DISPLAY = 4;
        private static final int TILE_RENDER_HEIGHT = 2;
        private static final int TILE_RENDER_WIDTH = 7;
        private static final String GRID_H_SPACER = " ";
        // ---------------------------

        // --- Static Helper Methods for CLI State Management ---
        private static void prepareStateTransition(String input) {
            CLICommands.restoreCursorPosition();
            CLICommands.clearUserInput(input);
        }

        private static void clearAllErrorMessages() {
            if(insertEquipeError.isVisible()) insertEquipeError.deleteString();
            if(insertEquipeTypeError.isVisible()) insertEquipeTypeError.deleteString();
            if(invalidEquipeError.isVisible()) invalidEquipeError.deleteString();
        }
        // ---------------------------

        // --- Tile Rendering Methods (duplicated from CLIValidationPrinter) ---
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
            final int RENDER_TILE_HEIGHT = 2; // Using TILE_RENDER_HEIGHT constant
            final int RENDER_TILE_WIDTH = 7;  // Using TILE_RENDER_WIDTH constant
            final String EMPTY_SLOT_SYMBOL_TEXT = isCabinSlot ? "CABIN" : "EMPTY";

            if (tile == null) {
                List<String> emptyRender = new ArrayList<>();
                emptyRender.add(String.format("┌%s┐", String.join("", Collections.nCopies(RENDER_TILE_WIDTH - 2, "─"))));
                emptyRender.add(String.format("│%-" + (RENDER_TILE_WIDTH - 2) + "." + (RENDER_TILE_WIDTH - 2) + "s│", EMPTY_SLOT_SYMBOL_TEXT));
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
            lines.add(String.format("┌─%c%c%c─┐", leftSym, topSym, rightSym)); // Assuming connector order is L, T, R for top line
            lines.add(String.format("│ %s %c │", idStr, bottomSym)); // Assuming bottom connector is displayed with ID
            return lines;
        }

        private static String buildShipOutputString(List<TileCoordinates> shipCoordinatesList) {
            StringBuilder sb = new StringBuilder();

            Map<String, Tile> placedTilesMap = new HashMap<>();
            if (shipCoordinatesList != null) {
                for (TileCoordinates tc : shipCoordinatesList) {
                    if (tc.getTile() != null) {
                        int displayRow = tc.getRow() + ROW_OFFSET_FOR_DISPLAY;
                        int displayCol = tc.getCol() + COL_OFFSET_FOR_DISPLAY;
                        placedTilesMap.put(displayRow + "," + displayCol, tc.getTile());
                    }
                }
            }

            int numDisplayGridRows = SHIP_GRID_MAX_ROW - SHIP_GRID_MIN_ROW + 1;
            int numDisplayGridCols = SHIP_GRID_MAX_COL - SHIP_GRID_MIN_COL + 1;

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

            String shipName = "YOUR INTERSTELLAR TRUCK";
            int tilesAndSpacersWidth = (numDisplayGridCols * TILE_RENDER_WIDTH) + Math.max(0, numDisplayGridCols - 1) * GRID_H_SPACER.length();
            int titleBarContentWidth = tilesAndSpacersWidth;
            String titlePadding = String.join("", Collections.nCopies(
                    Math.max(0, (titleBarContentWidth - shipName.length()) / 2), " "));

            sb.append("\n");
            sb.append(AnsiColor.YELLOW.getCode());
            sb.append("╔").append(String.join("", Collections.nCopies(titleBarContentWidth + 2, "═"))).append("╗\n");
            sb.append("║ ").append(titlePadding).append(shipName)
                    .append(titlePadding).append(shipName.length() % 2 != 0 ? " " : "").append(" ║\n");
            sb.append("╠").append(String.join("", Collections.nCopies(titleBarContentWidth + 2, "═"))).append("╣\n");
            sb.append(AnsiColor.DEFAULT.getCode()); // Reset color before content

            String rowLabelIndent = "    ";
            sb.append(rowLabelIndent);
            for (int cIdx = 0; cIdx < numDisplayGridCols; cIdx++) {
                sb.append(String.format("%-" + TILE_RENDER_WIDTH + "s", "C" + (cIdx + SHIP_GRID_MIN_COL)));
                if (cIdx < numDisplayGridCols - 1) sb.append(GRID_H_SPACER);
            }
            sb.append("\n");

            String singleCellHBorderSegment = String.join("", Collections.nCopies(TILE_RENDER_WIDTH, "═"));
            sb.append("  ╔");
            for (int cIdx = 0; cIdx < numDisplayGridCols; cIdx++) {
                sb.append(singleCellHBorderSegment);
                sb.append(cIdx < numDisplayGridCols - 1 ? "╦" : "╗");
            }
            sb.append("\n");

            for (int rIdx = 0; rIdx < numDisplayGridRows; rIdx++) {
                String rowLabel = String.format("R%d", (rIdx + SHIP_GRID_MIN_ROW));
                for (int lineNum = 0; lineNum < TILE_RENDER_HEIGHT; lineNum++) {
                    sb.append(lineNum == 0 ? String.format("%-2s", rowLabel) : "  ").append("║");
                    for (int cIdx = 0; cIdx < numDisplayGridCols; cIdx++) {
                        List<String> tileLines = gridRenderings.get(rIdx).get(cIdx);
                        sb.append(tileLines.get(lineNum));
                        if (cIdx < numDisplayGridCols - 1) sb.append(GRID_H_SPACER);
                    }
                    sb.append("║\n");
                }
                if (rIdx < numDisplayGridRows - 1) {
                    sb.append("  ╠");
                    for (int cIdx = 0; cIdx < numDisplayGridCols; cIdx++) {
                        sb.append(singleCellHBorderSegment);
                        sb.append(cIdx < numDisplayGridCols - 1 ? "╬" : "╣");
                    }
                    sb.append("\n");
                }
            }

            sb.append("  ╚");
            for (int cIdx = 0; cIdx < numDisplayGridCols; cIdx++) {
                sb.append(singleCellHBorderSegment);
                sb.append(cIdx < numDisplayGridCols - 1 ? "╩" : "╝");
            }
            sb.append("\n");

            sb.append(AnsiColor.YELLOW.getCode());
            sb.append("╚").append(String.join("", Collections.nCopies(titleBarContentWidth + 2, "═"))).append("╝\n");
            sb.append(AnsiColor.DEFAULT.getCode());

            return sb.toString();
        }
        // ---------------------------

    public static class Default implements CLIState {
        /**
         * Applies the default state for setting equipe.
         * Clears the screen, displays the game banner and the player's ship.
         * Then, it shows the initial prompt for equipe setup.
         *
         * @param args an array of objects where:
         * - args[0] is the {@link PlayerColors}
         * - args[1] is a {@link List} of {@link TileCoordinates} representing the ship.
         */
        @Override
        @SuppressWarnings("unchecked")
        public void apply(Object[] args) {
            if (args.length < 2) {
                System.err.println("CLISetEquipePrinter.Default state requires PlayerColors and List<TileCoordinates>.");
                return;
            }
            PlayerColors playerColor = (PlayerColors) args[0];
            List<TileCoordinates> shipCoordinates = (List<TileCoordinates>) args[1];

            AnsiColor color = translatePlayerColor(playerColor);
            if(color == AnsiColor.DEFAULT) return;

            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.displayGalaxyTrucker(5, color);

            // Display the ship
            shipDisplayOutput = new CLIString(
                    buildShipOutputString(shipCoordinates),
                    AnsiColor.DEFAULT, 0, 25
            );
            shipDisplayOutput.print();

            clearAllErrorMessages();

            initialPrompt.print();
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

    public static class AskEquipeLocationState implements CLIState {
        /**
         * Applies the current state transition based on the provided arguments and updates
         * the command-line interface (CLI) display accordingly. This method performs the
         * following actions:
         * - Prepares the state transition using the first argument.
         * - Clears all currently displayed error messages.
         * - Based on the visibility of the initial prompt, either deletes it or proceeds.
         * - Prints the "insertEquipe" prompt to the CLI.
         * - Saves the cursor position after printing to the CLI.
         *
         * @param args an array of objects where the first element is expected to be a string
         *             representing the user input for state transition. The array may also
         *             contain additional elements, though they are not processed by this method.
         */
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(initialPrompt.isVisible()) {
                initialPrompt.deleteString();
                insertEquipe.print();
            } else {
                insertEquipe.print();
            }
            CLICommands.saveCursorPosition();
        }
    }

    public static class AskEquipeLocationErrorState implements CLIState {
        /**
         * Handles errors related to invalid equipe placement coordinates.
         * Displays an error message and re-prompts for coordinates.
         *
         * @param args an array of objects where:
         * - args[0] is the previous user input string (for clearing).
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(!insertEquipeError.isVisible()) {
                insertEquipeError.print();
            }

            // Re-display the prompt for coordinates
            if(!insertEquipe.isVisible()) {
                insertEquipe.print();
            } else {
                insertEquipe.print();
            }
            CLICommands.restoreCursorPosition();
        }
    }

    public static class AskEquipeTypeState implements CLIState {
        /**
         * Prompts the user for the type of equipe member to place.
         * This state is typically entered after valid coordinates have been provided.
         *
         * @param args an array of objects where:
         * - args[0] is the previous user input string (for clearing, likely the coordinates).
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            // Replace the coordinate prompt with the type prompt
            if(insertEquipe.isVisible()) {
                CLIString.replace(insertEquipe, insertEquipeType);
            } else {
                insertEquipeType.print(); // Fallback
            }
            CLICommands.restoreCursorPosition();
        }
    }

    public static class AskEquipeTypeErrorState implements CLIState {
        /**
         * Handles errors related to invalid equipe type selection.
         * Displays an error message and re-prompts for the equipe type.
         *
         * @param args an array of objects where:
         * - args[0] is the previous user input string (for clearing).
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(!insertEquipeTypeError.isVisible()) {
                insertEquipeTypeError.print();
            }

            // Re-display the prompt for type
            if(!insertEquipeType.isVisible()){
                insertEquipeType.print();
            } else {
                insertEquipeType.print();
            }
            CLICommands.restoreCursorPosition();
        }
    }

    public static class EquipeSuccessfullySetState implements CLIState {
        /**
         * Handles the screen update after an equipe member has been successfully set.
         * It refreshes the ship display and returns to the initial prompt.
         * No specific success message CLIString was provided, so it transitions smoothly.
         *
         * @param args an array of objects where:
         * - args[0] is the previous user input string (for clearing, likely the type).
         * - args[1] is a {@link List} of {@link TileCoordinates} representing the updated ship.
         */
        @Override
        @SuppressWarnings("unchecked")
        public void apply(Object[] args) {
            if (args.length < 2) {
                System.err.println("CLISetEquipePrinter.EquipeSuccessfullySetState requires input string and List<TileCoordinates>.");
                return;
            }
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            List<TileCoordinates> shipCoordinates = (List<TileCoordinates>) args[1];
            shipDisplayOutput = new CLIString(
                    buildShipOutputString(shipCoordinates),
                    AnsiColor.DEFAULT,
                    0, 25
            );
            shipDisplayOutput.print();

            if(insertEquipeType.isVisible()) {
                CLIString.replace(insertEquipeType, insertEquipe);
            } else {
                insertEquipe.print();
            }
            CLICommands.restoreCursorPosition();
        }
    }

    public static class EquipeWronglySetState implements CLIState {
        /**
         * Applies the state transition logic by preparing the state, clearing errors, and
         * managing the display output based on the supplied arguments.
         *
         * @param args an array of objects where:
         *             - args[0] is expected to be a String representing input required for the state transition.
         *             - args[1] is expected to be a List of TileCoordinates representing coordinates to be processed
         *               and used for constructing the display output.
         */
        @Override
        @SuppressWarnings("unchecked")
        public void apply(Object[] args) {
            if (args.length < 2) {
                System.err.println("CLISetEquipePrinter.EquipeSuccessfullySetState requires input string and List<TileCoordinates>.");
                return;
            }
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            List<TileCoordinates> shipCoordinates = (List<TileCoordinates>) args[1];
            shipDisplayOutput = new CLIString(
                    buildShipOutputString(shipCoordinates),
                    AnsiColor.DEFAULT,
                    0, 25
            );
            shipDisplayOutput.print();

            if(!invalidEquipeError.isVisible()) {
                invalidEquipeError.print();
            } else {
                invalidEquipeError.print();
            }

            if(insertEquipeType.isVisible()) {
                CLIString.replace(insertEquipeType, insertEquipe);
            } else {
                insertEquipe.print();
            }
            CLICommands.restoreCursorPosition();
        }
    }

        public static class EquipeWaitingFinishState implements CLIState {
            /**
             * Applies the state transition logic by preparing the state, clearing errors, and
             * managing the display output based on the supplied arguments.
             *
             * @param args an array of objects where:
             *             - args[0] is expected to be a String representing input required for the state transition.
             *             - args[1] is expected to be a List of TileCoordinates representing coordinates to be processed
             *               and used for constructing the display output.
             */
            @Override
            @SuppressWarnings("unchecked")
            public void apply(Object[] args) {
                if (args.length < 2) {
                    System.err.println("CLISetEquipePrinter.EquipeSuccessfullySetState requires input string and List<TileCoordinates>.");
                    return;
                }
                prepareStateTransition((String) args[0]);
                clearAllErrorMessages();

                List<TileCoordinates> shipCoordinates = (List<TileCoordinates>) args[1];
                shipDisplayOutput = new CLIString(
                        buildShipOutputString(shipCoordinates),
                        AnsiColor.DEFAULT,
                        0, 25
                );
                shipDisplayOutput.print();

                if(insertEquipe.isVisible()) {
                    insertEquipe.deleteString();
                }
                if(insertEquipeType.isVisible()) {
                    insertEquipeType.deleteString();
                }

                if(!waitingForNextStage.isVisible()) {
                    waitingForNextStage.print();
                } else {
                    waitingForNextStage.print();
                }
            }
        }
}