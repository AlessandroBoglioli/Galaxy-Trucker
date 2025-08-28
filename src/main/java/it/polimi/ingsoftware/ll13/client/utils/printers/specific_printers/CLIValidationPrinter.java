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

public class CLIValidationPrinter implements CLIInterface {

    private static final CLIString validatePhase = new CLIString(
            ">> Here is the validation phase! Press [validate] to validate the ship:\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString invalidValidatePhase = new CLIString(
            ">> Invalid [validation] command!\n",
            AnsiColor.RED, 0, 20);
    private static final CLIString deletePrompt = new CLIString(
            ">> Select a tile that you want to delete [row]:[col]:\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString deletePromptError = new CLIString(
            ">> Invalid [row]:[col] format!\n>> ",
            AnsiColor.RED, 0, 20);
    private static final CLIString correctlyDeleted = new CLIString(
            ">> Tile removed correctly!\n>> ",
            AnsiColor.GREEN, 0, 20);

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

    public static class Default implements CLIState {
        /**
         * Executes a series of command-line interface (CLI) operations including
         * terminal initialization, rendering a player color, and*/
        @Override
        public void apply(Object[] args) {
            if(translatePlayerColor((PlayerColors) args[0]) == AnsiColor.DEFAULT) return;

            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.displayGalaxyTrucker(5, translatePlayerColor((PlayerColors) args[0]));

            validatePhase.print();
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

    public static class ValidateCommandErrorState implements CLIState {
        /**
         * Applies the state transition and updates the CLI interface.
         * This method processes the given arguments, performs a state transition,
         * clears error messages, and updates the visibility and content of specific
         * CLI elements based on their current state.
         *
         * @param args an array of objects where:
         *             - The first element is expected to be a {@code String} used to
         *               determine the state transition input.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            shipDisplayOutput = new CLIString(buildShipOutputString((List<TileCoordinates>) args[1]), AnsiColor.DEFAULT, 0, 25);
            if(!shipDisplayOutput.isVisible())
                shipDisplayOutput.print();

            if(!invalidValidatePhase.isVisible())
                invalidValidatePhase.print();

            if(!validatePhase.isVisible())
                validatePhase.print();
            else
                validatePhase.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class NeedToDeleteTilesState implements CLIState {
        /**
         * Applies the current state logic for handling the deletion of tiles in the CLI.
         * This method initializes the CLI environment, updates the display to prompt
         * for tile deletion if needed, and restores the terminal's cursor position.
         *
         * @param args an array of arguments that may provide additional context or data
         *             for processing during the state application.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            shipDisplayOutput = new CLIString(buildShipOutputString((List<TileCoordinates>) args[1]), AnsiColor.DEFAULT, 0, 25);
            if(!shipDisplayOutput.isVisible())
                shipDisplayOutput.print();

            if(validatePhase.isVisible())
                CLIString.replace(validatePhase, deletePrompt);
            CLICommands.restoreCursorPosition();
        }
    }

    public static class NeedToDeleteTilesErrorState implements CLIState {
        /**
         * Applies the given arguments to transition the CLI state, clear error messages,
         * and manage the visibility and rendering of CLI string objects related
         * to delete prompts.
         *
         * @param args an array of arguments where the first argument is expected to be
         *             a {@code String} that triggers the state transition. Other arguments
         *             are currently unused.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(!deletePromptError.isVisible())
                deletePromptError.print();

            if(!deletePrompt.isVisible())
                validatePhase.print();
            else
                deletePrompt.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class CorrectlyDeletedTile implements CLIState {
        /**
         * Applies a series of state transition actions and updates the CLI display
         * based on the provided arguments. This method prepares for a state transition,
         * clears error messages, displays ship-related output, and ensures all CLI
         * components are appropriately updated and visible.
         *
         * @param args an array of objects where:
         *             args[0] is a String representing the input for state transition preparation.
         *             args[1] is a List of TileCoordinates used to construct the ship output string.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            shipDisplayOutput = new CLIString(buildShipOutputString((List<TileCoordinates>) args[1]), AnsiColor.DEFAULT, 0, 25);
            if(!shipDisplayOutput.isVisible())
                shipDisplayOutput.print();
            else
                shipDisplayOutput.print();

            if(!correctlyDeleted.isVisible())
                correctlyDeleted.print();
            else
                correctlyDeleted.print();
            validatePhase.print();
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

    protected static String buildShipOutputString(List<TileCoordinates> shipCoordinatesList) {
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

    private static void prepareStateTransition(String input) {
        CLICommands.restoreCursorPosition();
        CLICommands.clearUserInput(input);
    }

    private static void clearAllErrorMessages() {
        if(deletePromptError.isVisible()) deletePromptError.deleteString();
        if(invalidValidatePhase.isVisible()) invalidValidatePhase.deleteString();
        if(correctlyDeleted.isVisible()) correctlyDeleted.deleteString();
    }
}
