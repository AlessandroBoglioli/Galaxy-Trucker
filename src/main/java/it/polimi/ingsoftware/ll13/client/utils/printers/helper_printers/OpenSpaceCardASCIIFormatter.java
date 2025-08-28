package it.polimi.ingsoftware.ll13.client.utils.printers.helper_printers;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.OpenSpaceCard;

/**
 * Formats an OpenSpaceCard into a multi-line ASCII string representation.
 */
public class OpenSpaceCardASCIIFormatter {

    private static final int CARD_WIDTH = 48;

    private static String centerText(String text, int width) {
        if (text == null) text = "";
        if (text.length() >= width) return text.substring(0, width);
        int padding = width - text.length();
        int padLeft = padding / 2;
        int padRight = padding - padLeft;
        return " ".repeat(padLeft) + text + " ".repeat(padRight);
    }

    /**
     * Formats the given OpenSpaceCard into a multi-line string (13 lines of card content).
     * The total output including top/bottom borders created by this method is 15 lines.
     *
     * @param card The OpenSpaceCard to format.
     * @return A string representing the card.
     */
    public static String format(OpenSpaceCard card) {
        if (card == null) {
            return centerText("Error: Card data is missing.", CARD_WIDTH - 2) + "\n";
        }

        String name = card.getName().toUpperCase();
        int level = card.getLevel();

        StringBuilder sb = new StringBuilder();
        String horizontalBorder = "+" + "-".repeat(CARD_WIDTH - 2) + "+\n";
        String emptyLineContent = " ".repeat(CARD_WIDTH - 2);

        // Line 1: Top border
        sb.append(horizontalBorder);
        // Line 2: Title
        sb.append(String.format("|%s|\n", centerText(name, CARD_WIDTH - 2)));
        // Line 3: Level
        sb.append(String.format("|%s|\n", centerText("(Level: " + level + ")", CARD_WIDTH - 2)));
        // Line 4: Separator border
        sb.append(horizontalBorder);
        // Line 5: Empty line
        sb.append(String.format("|%s|\n", emptyLineContent));

        // Lines 6-8: Abstract "art" for open space/speed (3 lines)
        sb.append(String.format("|%s|\n", centerText("  * --->--->--->     * ", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText(".   ACCELERATE THROUGH THE VOID   .", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("  `     <---<---<---     `  ", CARD_WIDTH - 2)));

        // Line 9: Flavor text
        sb.append(String.format("|%s|\n", centerText("\"Full throttle! Let's make some distance!\"", CARD_WIDTH - 2)));
        // Line 10: Empty line
        sb.append(String.format("|%s|\n", emptyLineContent));
        // Line 11: Separator border
        sb.append(horizontalBorder);

        // Lines 12-14: Effect description (3 lines of content here for the card display)
        sb.append(String.format("|%s|\n", centerText("OPEN SPACE AHEAD!", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("A chance to engage engines and surge forward.", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("Movement based on your total thrust power.", CARD_WIDTH - 2)));

        // Line 15: Bottom border
        sb.append(horizontalBorder);

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("Testing OpenSpaceCardASCIIFormatter:");
        System.out.println("------------------------------------");

        OpenSpaceCard card1 = new OpenSpaceCard(1, "open_space_lvl1.png");
        String formattedCard1 = format(card1);
        System.out.println("Card Level 1 (" + card1.getName() + "):");
        System.out.println(formattedCard1);

        OpenSpaceCard card2 = new OpenSpaceCard(2, "deep_space_lvl2.png");
        String formattedCard2 = format(card2);
        System.out.println("Card Level 2 (" + card2.getName() + "):");
        System.out.println(formattedCard2);
    }
}