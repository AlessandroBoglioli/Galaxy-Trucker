package it.polimi.ingsoftware.ll13.client.utils.printers.helper_printers;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.EpidemicCard;

/**
 * Formats an EpidemicCard into a multi-line ASCII string representation.
 */
public class EpidemicCardASCIIFormatter {

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
     * Formats the given EpidemicCard into a multi-line string (14 lines of card content).
     *
     * @param card The EpidemicCard to format.
     * @return A string representing the card.
     */
    public static String format(EpidemicCard card) {
        if (card == null) {
            return centerText("Error: Card data is missing.", CARD_WIDTH - 2) + "\n";
        }

        String name = card.getName().toUpperCase(); // Will be "EPIDEMIC" from constructor
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

        // Lines 6-8: Abstract "art" for epidemic (3 lines)
        sb.append(String.format("|%s|\n", centerText("********************", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("*** BIOHAZARD ALERT ***", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("********************", CARD_WIDTH - 2)));

        // Line 9: Flavor text
        sb.append(String.format("|%s|\n", centerText("\"A deadly contagion sweeps the ship!\"", CARD_WIDTH - 2)));
        // Line 10: Empty line
        sb.append(String.format("|%s|\n", emptyLineContent));
        // Line 11: Separator border
        sb.append(horizontalBorder);

        // Lines 12-13: Effect description (2 lines)
        sb.append(String.format("|%s|\n", centerText("EPIDEMIC OUTBREAK! All crew are at risk.", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("The consequences are immediate.", CARD_WIDTH - 2)));

        // Line 14: Bottom border
        sb.append(horizontalBorder); // This makes 14 lines for the card content

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("Testing EpidemicCardASCIIFormatter:");
        System.out.println("-----------------------------------");

        EpidemicCard card1 = new EpidemicCard(1, "epidemia_lvl1.png");
        String formattedCard1 = format(card1);
        System.out.println("Card Level 1 (" + card1.getName() + "):");
        System.out.println(formattedCard1);

        EpidemicCard card2 = new EpidemicCard(3, "epidemia_lvl3_severe.png");
        String formattedCard2 = format(card2);
        System.out.println("Card Level 3 (" + card2.getName() + "):");
        System.out.println(formattedCard2);
    }
}
