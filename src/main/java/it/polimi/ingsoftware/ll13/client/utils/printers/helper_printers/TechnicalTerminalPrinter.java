package it.polimi.ingsoftware.ll13.client.utils.printers.helper_printers;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.*;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.ProblemType;
import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor;
import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.*;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.function.BiFunction; // Use BiFunction for rendering

public class TechnicalTerminalPrinter {

    // --- Configurable constants for tile rendering ---
    private static final int TILE_VISUAL_WIDTH = 26; // Characters wide for one tile block
    private static final int TILE_VISUAL_HEIGHT = 4; // Lines high for one tile block
    private static final int ITEM_HORIZONTAL_SPACING = 2; // Characters between items (tiles/cards) in a row
    private static final int ITEM_VERTICAL_SPACING = 1;   // Lines between rows of items

    // --- Configurable constants for card rendering ---
    // Let's make card blocks a bit smaller than tiles but similar style
    private static final int CARD_VISUAL_WIDTH = 19; // Width for the card name content + borders/padding
    private static final int CARD_VISUAL_HEIGHT = 3; // Lines high (top border, name, bottom border)

    // --- Border Elements (Technical Theme) ---
    private static final char BOX_TL = '╔';
    private static final char BOX_TR = '╗';
    private static final char BOX_BL = '╚';
    private static final char BOX_BR = '╝';
    private static final char BOX_H = '═';
    private static final char BOX_V = '║';
    // Using rounded corners for cards for a slight visual difference
    private static final char CARD_BOX_TL = '┌';
    private static final char CARD_BOX_TR = '┐';
    private static final char CARD_BOX_BL = '└';
    private static final char CARD_BOX_BR = '┘';
    private static final char CARD_BOX_H = '─';
    private static final char CARD_BOX_V = '│';


    // --- Prefixes & Styles ---
    public static final String PREFIX_PROMPT = ">> ";
    public static final String PREFIX_ITEM = "[+] ";
    public static final String PREFIX_ERROR = "[!] ";
    private static final char HEADER_LINE_CHAR = '=';
    private static final char FOOTER_LINE_CHAR = '-';

    // --- Connector Symbols (Technical Theme) ---
    // Using slightly more abstract/technical symbols
    private static final String CONN_UNIVERSAL_SYMBOL = "|||"; // Made 3 chars for alignment
    private static final String CONN_DOUBLE_SYMBOL = "||";    // Need 3 chars for alignment
    private static final String CONN_SINGLE_SYMBOL = "|";    // Need 3 chars for alignment
    private static final String CONN_NONE_SYMBOL = "_";      // Need 3 chars for alignment (e.g., "XX ")


    private static String repeatChar(char c, int times) {
        if (times <= 0) return "";
        // Use String.repeat if Java 11+, otherwise use array fill (or the provided code)
        if (System.getProperty("java.version").compareTo("11") >= 0) {
            return String.valueOf(c).repeat(times);
        } else {
            char[] repeat = new char[times];
            Arrays.fill(repeat, c);
            return new String(repeat);
        }
    }

    /**
     * Centers text within a given width using a specified padding character.
     * @param text The text to center.
     * @param width The total width for the centered text.
     * @param padChar The character to use for padding.
     * @return The centered text string, padded to the specified width.
     */
    private static String centerText(String text, int width, char padChar) {
        if (text == null) text = "";
        // Strip ANSI codes before calculating length for centering
        String cleanText = text.replaceAll("\u001B\\[[;\\d]*m", "");
        int cleanTextLength = cleanText.length();

        if (cleanTextLength >= width) {
            // Truncate if too long (keep ANSI codes)
            // This truncation might break ANSI sequences mid-code. A more robust solution
            // would parse ANSI, truncate content, and reapply codes. For simplicity,
            // we'll do a basic substring which might look weird if truncated inside a code.
            // A safer simple approach for fixed-width display is often to just truncate
            // and maybe add '...' without trying to preserve color.
            // Let's stick to the simple substring for now, assuming ANSI is applied *after* formatting.
            // Or, better, remove ANSI before calculating length and before truncating/padding.
            if (cleanTextLength > width) {
                // Simple truncation after stripping ANSI, then re-center the truncated clean text.
                String truncatedCleanText = cleanText.substring(0, width - (width > 3 ? 3 : 0)) + (width > 3 ? "..." : "");
                return centerText(truncatedCleanText, width, padChar); // Recurse with clean, truncated text
            }
            return text; // If exactly width, return as is
        }
        int padding = width - cleanTextLength;
        int padLeft = padding / 2;
        int padRight = padding - padLeft;
        return repeatChar(padChar, padLeft) + text + repeatChar(padChar, padRight);
    }


    /**
     * Pads text to the right within a given width using a specified padding character.
     * @param text The text to pad.
     * @param width The total width.
     * @param padChar The character to use for padding.
     * @return The right-padded text string.
     */
    private static String padRight(String text, int width, char padChar) {
        if (text == null) text = "";
        // Strip ANSI codes before calculating length
        String cleanText = text.replaceAll("\u001B\\[[;\\d]*m", "");
        int cleanTextLength = cleanText.length();

        if (cleanTextLength >= width) {
            return text; // Don't truncate here, let the caller handle width limits
        }
        return text + repeatChar(padChar, width - cleanTextLength);
    }


    /**
     * Generates a technical-themed header.
     * Example:
     * =================== TITLE ====================
     */
    public static String generateHeader(String title, int totalWidth) {
        String headerBar = repeatChar(HEADER_LINE_CHAR, totalWidth);
        String titledBarContent = " " + (title == null ? "" : title.toUpperCase()) + " ";
        String titledBar = centerText(titledBarContent, totalWidth, HEADER_LINE_CHAR);
        return headerBar + "\n" + titledBar + "\n" + headerBar + "\n";
    }

    /**
     * Generates a technical-themed footer.
     */
    public static String generateFooter(int totalWidth) {
        return repeatChar(FOOTER_LINE_CHAR, totalWidth) + "\n";
    }

    /**
     * Gets a display-friendly name for a Tile object.
     */
    private static String getTileTypeName(Tile tile) {
        if (tile == null) return "Unknown Tile";
        // Use the logic from your example
        if (tile instanceof BatteryStorageTile) return "Battery Storage";
        if (tile instanceof CabinTile) return "Cabin";
        if (tile instanceof CannonTile) return "Cannon";
        if (tile instanceof CargoHoldTile) return "Cargo Hold";
        if (tile instanceof DoubleCannonTile) return "Double Cannon";
        if (tile instanceof DoubleMotorTile) return "Double Motor";
        if (tile instanceof ShieldTile) return "Shield";
        if (tile instanceof StructuralModuleTile) return "Structural Module";
        if (tile instanceof VitalSupportTile) return "Vital Support";

        String simpleName = tile.getClass().getSimpleName();
        if (simpleName.endsWith("Tile")) {
            return simpleName.substring(0, simpleName.length() - "Tile".length()).trim();
        }
        return simpleName.trim();
    }

    /**
     * Gets a display-friendly name for a Card object.
     */
    private static String getCardName(Card card) {
        if (card == null) return "Unknown Card";
        // Use the card's getName method
        return card.getName();
    }


    /**
     * Formats a ConnectorType into its technical string representation (fixed 3 chars).
     */
    private static String formatConnectorTechnical(ConnectorType type) {
        if (type == null) return padRight(CONN_NONE_SYMBOL, 3, ' ');
        return switch (type) {
            case UNIVERSAL -> CONN_UNIVERSAL_SYMBOL;
            case DOUBLE -> padRight(CONN_DOUBLE_SYMBOL, 3, ' ');
            case SINGLE -> padRight(CONN_SINGLE_SYMBOL, 3, ' ');
            case SMOOTH -> padRight(CONN_NONE_SYMBOL, 3, ' '); // Assuming SMOOTH maps to NONE visually
            default -> padRight(CONN_NONE_SYMBOL, 3, ' ');
        };
    }

    // --- Renderer Interfaces and Implementations ---
    /**
     * Functional interface for rendering an item (like a Tile or Card) into a block of strings.
     */
    private interface ItemBlockRenderer<T> extends BiFunction<T, Integer, String[]> {
        // BiFunction: (Item, Index) -> String[] (the visual block)

        /**
         * Gets the fixed visual width of the rendered block.
         */
        int getVisualWidth();

        /**
         * Gets the fixed visual height (number of lines) of the rendered block.
         */
        int getVisualHeight();
    }


    /**
     * Renderer for Tile items.
     */
    private static class TileBlockRenderer implements ItemBlockRenderer<Tile> {
        @Override
        public int getVisualWidth() { return TILE_VISUAL_WIDTH; }
        @Override
        public int getVisualHeight() { return TILE_VISUAL_HEIGHT; }

        /**
         * Renders a single tile into a fixed-size block of text (String array).
         * Each string in the array is a line of the tile's visual representation.
         * All lines will have length TILE_VISUAL_WIDTH.
         * The array will have length TILE_VISUAL_HEIGHT.
         *
         * Tile Block Layout (TILE_VISUAL_HEIGHT = 4, TILE_VISUAL_WIDTH = 26):
         * ╔════#00: Tile Name ════╗ (Total width TILE_VISUAL_WIDTH)
         * ║ N:+++    E:==        ║ (Inner width TILE_VISUAL_WIDTH - 2)
         * ║ S:--     W:xx        ║
         * ╚══════════════════════╝
         */
        @Override
        public String[] apply(Tile tile, Integer index) {
            String[] block = new String[TILE_VISUAL_HEIGHT];

            String tileTypeName = getTileTypeName(tile);
            String titleContent = String.format("#%02d: %s", index, tileTypeName);

            // Title Line: ╔ + PADDING_H + CONTENT + PADDING_H + ╗
            int innerTitleWidth = getVisualWidth() - 2; // Space between ╔ and ╗
            // Center the title content within the inner width using BOX_H as padding
            String paddedTitleContent = centerText(titleContent, innerTitleWidth, BOX_H);
            block[0] = BOX_TL + paddedTitleContent + BOX_TR;

            // Connector Lines (Lines 1 and 2)
            Connector[] connectors = tile != null ? tile.getConnectors() : new Connector[0];
            // Assuming connectors are ordered N, E, S, W (0 to 3)
            String connN = formatConnectorTechnical(connectors.length > 0 && connectors[0] != null ? connectors[0].getType() : null);
            String connE = formatConnectorTechnical(connectors.length > 1 && connectors[1] != null ? connectors[1].getType() : null);
            String connS = formatConnectorTechnical(connectors.length > 2 && connectors[2] != null ? connectors[2].getType() : null);
            String connW = formatConnectorTechnical(connectors.length > 3 && connectors[3] != null ? connectors[3].getType() : null);

            int innerConnectorWidth = getVisualWidth() - 2; // Space between ║ and ║

            // N and E connectors on line 1
            String connLine1Content = String.format(" N:%s    E:%s", connN, connE);
            // Pad the content to fill the inner width
            block[1] = BOX_V + padRight(connLine1Content, innerConnectorWidth, ' ') + BOX_V;


            // S and W connectors on line 2
            String connLine2Content = String.format(" S:%s    W:%s", connS, connW);
            // Pad the content to fill the inner width
            block[2] = BOX_V + padRight(connLine2Content, innerConnectorWidth, ' ') + BOX_V;


            // Bottom Line: ╚ + PADDING_H + ╝
            block[3] = BOX_BL + repeatChar(BOX_H, getVisualWidth() - 2) + BOX_BR;

            // Safety check: Ensure all lines are the exact width
            for(int i = 0; i < getVisualHeight(); i++) {
                if(block[i] == null) block[i] = repeatChar(' ', getVisualWidth());
                else if (block[i].length() > getVisualWidth()) block[i] = block[i].substring(0, getVisualWidth());
                else if (block[i].length() < getVisualWidth()) block[i] = padRight(block[i], getVisualWidth(), ' ');
            }


            return block;
        }
    }

    /**
     * Renderer for Card items.
     */
    private static class CardBlockRenderer implements ItemBlockRenderer<Card> {
        @Override
        public int getVisualWidth() { return CARD_VISUAL_WIDTH; }
        @Override
        public int getVisualHeight() { return CARD_VISUAL_HEIGHT; }

        /**
         * Formats a card name to fit within the available space in the card block,
         * including truncation with "..." if necessary.
         * @param name The card name.
         * @param targetWidth The width available for the name content (excluding inner padding/borders).
         * @return The formatted and potentially truncated card name.
         */
        private String formatCardNameForBlock(String name, int targetWidth) {
            if (name == null) name = "";
            if (name.length() <= targetWidth) {
                return name; // No padding or truncation needed here, padding is done later
            } else {
                // Truncate and add ellipsis if it fits
                if (targetWidth > 3) {
                    return name.substring(0, targetWidth - 3) + "...";
                } else {
                    // If targetWidth is too small for ellipsis, just truncate hard
                    return name.substring(0, targetWidth);
                }
            }
        }


        /**
         * Renders a single card into a fixed-size block of text (String array).
         * Each string in the array is a line of the card's visual representation.
         * All lines will have length CARD_VISUAL_WIDTH.
         * The array will have length CARD_VISUAL_HEIGHT.
         *
         * Card Block Layout (CARD_VISUAL_HEIGHT = 3, CARD_VISUAL_WIDTH = 19):
         * ┌─────────────────┐
         * │ Card Name       │ (Inner width CARD_VISUAL_WIDTH - 4 for " Name ")
         * └─────────────────┘
         */
        @Override
        public String[] apply(Card card, Integer index) {
            String[] block = new String[CARD_VISUAL_HEIGHT];

            String cardName = getCardName(card);

            // Top Line: ┌ + PADDING_H + ┐
            block[0] = CARD_BOX_TL + repeatChar(CARD_BOX_H, getVisualWidth() - 2) + CARD_BOX_TR;

            // Name Line: │ + PADDING + NAME + PADDING + │
            int innerNameContentWidth = getVisualWidth() - 4; // Space for " Name " inside │ borders
            String formattedName = formatCardNameForBlock(cardName, innerNameContentWidth);
            String nameLineContent = " " + formattedName + " "; // Add inner spaces
            // Pad the content to fill the inner width between borders
            block[1] = CARD_BOX_V + padRight(nameLineContent, getVisualWidth() - 2, ' ') + CARD_BOX_V;

            // Bottom Line: └ + PADDING_H + ┘
            block[2] = CARD_BOX_BL + repeatChar(CARD_BOX_H, getVisualWidth() - 2) + CARD_BOX_BR;

            // Safety check: Ensure all lines are the exact width
            for(int i = 0; i < getVisualHeight(); i++) {
                if(block[i] == null) block[i] = repeatChar(' ', getVisualWidth());
                else if (block[i].length() > getVisualWidth()) block[i] = block[i].substring(0, getVisualWidth());
                else if (block[i].length() < getVisualWidth()) block[i] = padRight(block[i], getVisualWidth(), ' ');
            }

            return block;
        }
    }


    /**
     * Generates a grid of items, displaying them side-by-side with wrapping.
     * Uses a provided renderer to format each item block.
     * @param items List of items to display.
     * @param terminalWidth The total available width of the terminal.
     * @param renderer The renderer to use for formatting each item block.
     * @param <T> The type of items in the list.
     * @return A string representing the grid of items.
     */
    private static <T> String generateItemGrid(List<T> items, int terminalWidth, ItemBlockRenderer<T> renderer) {
        if (items == null || items.isEmpty()) {
            return PREFIX_PROMPT + "No items to display in grid.\n";
        }

        int itemWidth = renderer.getVisualWidth();
        int itemHeight = renderer.getVisualHeight();
        int spacingH = ITEM_HORIZONTAL_SPACING;
        int spacingV = ITEM_VERTICAL_SPACING;

        // Calculate how many items fit horizontally, ensuring at least one if possible
        int itemsPerRow = Math.max(1, (terminalWidth + spacingH) / (itemWidth + spacingH));
        if (itemsPerRow == 0 && terminalWidth >= itemWidth) itemsPerRow = 1;
        else if (itemsPerRow == 0) {
            // Terminal is too narrow for even one item block
            return PREFIX_ERROR + "Terminal too narrow to display items (requires at least " + itemWidth + " chars).\n";
        }


        List<String[]> renderedItemBlocks = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            renderedItemBlocks.add(renderer.apply(items.get(i), i));
        }

        StringBuilder gridOutput = new StringBuilder();
        int totalItemRows = (int) Math.ceil((double) renderedItemBlocks.size() / itemsPerRow);

        for (int visualRow = 0; visualRow < totalItemRows; visualRow++) {
            int startItemIndex = visualRow * itemsPerRow;

            // Build each line for the current visual row
            for (int lineNum = 0; lineNum < itemHeight; lineNum++) {
                StringBuilder currentDisplayLine = new StringBuilder();
                for (int j = 0; j < itemsPerRow; j++) {
                    int currentItemIndex = startItemIndex + j;
                    if (currentItemIndex < renderedItemBlocks.size()) {
                        currentDisplayLine.append(renderedItemBlocks.get(currentItemIndex)[lineNum]);
                        if (j < itemsPerRow - 1) { // If not the last item block in this visual row
                            currentDisplayLine.append(repeatChar(' ', spacingH));
                        }
                    } else {
                        // Add empty space if this position in the grid row is empty
                        currentDisplayLine.append(repeatChar(' ', itemWidth));
                        if (j < itemsPerRow - 1) {
                            currentDisplayLine.append(repeatChar(' ', spacingH));
                        }
                    }
                }
                gridOutput.append(currentDisplayLine.toString()).append("\n");
            }

            // Add vertical spacing between visual rows
            if (visualRow < totalItemRows - 1) {
                for(int k = 0; k < spacingV; k++) {
                    gridOutput.append("\n");
                }
            }
        }
        return gridOutput.toString();
    }

    /**
     * Generates a grid representation of Tile objects.
     * @param tiles List of Tile objects to display.
     * @param terminalWidth The total available width of the terminal.
     * @return A string representing the grid of tiles.
     */
    public static String generateTileGrid(List<Tile> tiles, int terminalWidth) {
        return generateItemGrid(tiles, terminalWidth, new TileBlockRenderer());
    }

    /**
     * Generates a grid representation of Card objects.
     * @param cards List of Card objects to display.
     * @param terminalWidth The total available width of the terminal.
     * @return A string representing the grid of cards.
     */
    public static String generateCardGrid(List<Card> cards, int terminalWidth) {
        return generateItemGrid(cards, terminalWidth, new CardBlockRenderer());
    }


    /**
     * Generates a simple formatted list of items.
     * @param items List of strings to list.
     * @param listTitle An optional title for the list.
     * @return A string representing the formatted list.
     */
    public static String generateSimpleList(List<String> items, String listTitle) {
        StringBuilder sb = new StringBuilder();
        if (listTitle != null && !listTitle.isEmpty()) {
            sb.append(PREFIX_PROMPT).append(listTitle.toUpperCase()).append(":\n");
        }
        if (items == null || items.isEmpty()) {
            sb.append(PREFIX_PROMPT).append("  (No items)\n");
            return sb.toString();
        }
        for (String item : items) {
            sb.append(PREFIX_ITEM).append(item).append("\n");
        }
        return sb.toString();
    }

    // --- Main method for testing the printer ---
    public static void main(String[] args) {
        System.out.println(generateHeader("System Diagnostics", 80));

        // --- Test Tile Grid ---
        // Create some sample connectors
        ConnectorType cUni = ConnectorType.UNIVERSAL;
        ConnectorType cDbl = ConnectorType.DOUBLE;
        ConnectorType cSgl = ConnectorType.SINGLE;
        ConnectorType cNon = ConnectorType.SMOOTH; // Use SMOOTH for non-connector

        // Create some sample tiles
        List<Tile> testTiles = new ArrayList<>();
        testTiles.add(new BatteryStorageTile("id1", cUni, cDbl, cSgl, cNon, 1));
        testTiles.add(new CabinTile("id2", cSgl, cSgl, cNon, cUni));
        testTiles.add(new BatteryStorageTile("id3", cDbl, cNon, cDbl, cUni, 2));
        testTiles.add(new CabinTile("id4", cUni, cUni, cUni, cUni));
        testTiles.add(new StructuralModuleTile("id5", cNon, cNon, cNon, cNon)); // No connectors
        testTiles.add(new CannonTile("id6", cSgl, cNon, cSgl, cNon, Direction.TOP));
        testTiles.add(new DoubleCannonTile("id7", cDbl, cDbl, cNon, cNon, Direction.TOP));


        System.out.println(PREFIX_PROMPT + "Available Blueprints (Tile Grid) @ 120px:");
        System.out.print(generateTileGrid(testTiles, 120));
        System.out.println("\n" + repeatChar(HEADER_LINE_CHAR, 80) + "\n"); // Separator

        System.out.println(PREFIX_PROMPT + "Available Blueprints (Tile Grid) @ 60px:");
        System.out.print(generateTileGrid(testTiles, 60));
        System.out.println("\n" + repeatChar(HEADER_LINE_CHAR, 80) + "\n"); // Separator

        System.out.println(PREFIX_PROMPT + "Available Blueprints (Tile Grid) @ 30px:");
        System.out.print(generateTileGrid(testTiles, 30));
        System.out.println("\n" + repeatChar(HEADER_LINE_CHAR, 80) + "\n"); // Separator


        // --- Test Card Grid ---
        List<Card> testCards = new ArrayList<>();

        List<Meteor> meteors = new ArrayList<>();
        meteors.add(new Meteor(ProblemType.BIG, Direction.TOP));
        meteors.add(new Meteor(ProblemType.BIG, Direction.LEFT));
        meteors.add(new Meteor(ProblemType.SMALL, Direction.RIGHT));
        meteors.add(new Meteor(ProblemType.SMALL, Direction.RIGHT));
        testCards.add(new MeteorShowerCard(2,"Meteoroid Shower", meteors));


        List<CargoColor> cargoColors = new ArrayList<>();
        cargoColors.add(CargoColor.RED);
        cargoColors.add(CargoColor.BLUE);
        cargoColors.add(CargoColor.GREEN);
        testCards.add(new SmugglersCard(2, "Smugglers Encounter", 2, 3, cargoColors, 2));
        testCards.add(new OpenSpaceCard(1, "Deep Space Boredom"));
        testCards.add(new OpenSpaceCard(1, "Deep Space Boredom"));
        testCards.add(new OpenSpaceCard(1, "Deep Space Boredom"));
        testCards.add(new EpidemicCard(1, "Cargo Inspection Random Event With A Long Name")); // test truncation


        System.out.println(PREFIX_PROMPT + "Current Events Deck (Card Grid) @ 120px:");
        System.out.print(generateCardGrid(testCards, 120));
        System.out.println("\n" + repeatChar(HEADER_LINE_CHAR, 80) + "\n"); // Separator

        System.out.println(PREFIX_PROMPT + "Current Events Deck (Card Grid) @ 60px:");
        System.out.print(generateCardGrid(testCards, 60));
        System.out.println("\n" + repeatChar(HEADER_LINE_CHAR, 80) + "\n"); // Separator

        System.out.println(PREFIX_PROMPT + "Current Events Deck (Card Grid) @ 30px:");
        System.out.print(generateCardGrid(testCards, 30));
        System.out.println("\n" + repeatChar(HEADER_LINE_CHAR, 80) + "\n"); // Separator


        // --- Test Simple List ---
        List<String> logMessages = Arrays.asList(
                "System boot complete.",
                "Navigation online.",
                "Life support stable.",
                "Communication link established."
        );
        System.out.print(generateSimpleList(logMessages, "Event Log"));


        System.out.println(generateFooter(80));
    }
}