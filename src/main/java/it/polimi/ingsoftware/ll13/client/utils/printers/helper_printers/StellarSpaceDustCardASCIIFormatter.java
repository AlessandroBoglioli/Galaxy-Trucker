package it.polimi.ingsoftware.ll13.client.utils.printers.helper_printers;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.StellarSpaceDustCard;

/**
 * Formats a StellarSpaceDustCard into a multi-line ASCII string representation
 * with enhanced graphics.
 */
public class StellarSpaceDustCardASCIIFormatter {

    private static final int CARD_WIDTH = 48;

    private static String centerText(String text, int width) {
        if (text == null) text = "";
        if (text.length() > width) {
            text = text.substring(0, width - 3) + "...";
        }
        if (text.length() == width) return text;

        int padding = width - text.length();
        int padLeft = padding / 2;
        int padRight = padding - padLeft;
        return " ".repeat(padLeft) + text + " ".repeat(padRight);
    }

    /**
     * Formats the given StellarSpaceDustCard into a multi-line string.
     * Produces 15 lines total output from the formatter, including its borders
     * (13 lines of actual card content).
     *
     * @param card The StellarSpaceDustCard to format.
     * @return A string representing the card.
     */
    public static String format(StellarSpaceDustCard card) {
        if (card == null) {
            return "+" + "-".repeat(CARD_WIDTH - 2) + "+\n" +
                    "|" + centerText("Error: Card data missing.", CARD_WIDTH - 2) + "|\n" +
                    "+" + "-".repeat(CARD_WIDTH - 2) + "+\n";
        }

        String name = card.getName().toUpperCase();
        int level = card.getLevel();

        StringBuilder sb = new StringBuilder();
        String hb = "+" + "-".repeat(CARD_WIDTH - 2) + "+\n";
        String emptyContentForLine = " ".repeat(CARD_WIDTH - 2);

        // Line 1: Top border
        sb.append(hb);
        // Line 2: Title & Level
        sb.append(String.format("|%s|\n", centerText(name + " (Lvl: " + level + ")", CARD_WIDTH - 2)));
        // Line 3: Separator
        sb.append(hb);
        // Line 4: Empty line above art
        sb.append(String.format("|%s|\n", emptyContentForLine));

        // Lines 5-8: ASCII Art for Stellar Dust / Clogged Pipes (4 lines)
        sb.append(String.format("|%s|\n", centerText("      .--~~~~--.      ", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("     / * DUST * \\     ", CARD_WIDTH - 2))); // Dust cloud
        sb.append(String.format("|%s|\n", centerText("    <XX[PIPES]XX>    ", CARD_WIDTH - 2))); // XX for clogged
        sb.append(String.format("|%s|\n", centerText("      \\_CLOGGED_/      ", CARD_WIDTH - 2)));

        // Line 9: Flavor Text
        sb.append(String.format("|%s|\n", centerText("\"Tiny particles, major mechanical grief...\"", CARD_WIDTH - 2)));
        // Line 10: Empty line below flavor
        sb.append(String.format("|%s|\n", emptyContentForLine));
        // Line 11: Separator
        sb.append(hb);

        // Info section (3 lines) - L12-L14
        sb.append(String.format("|%s|\n", centerText("STELLAR DUST CLOUD ENCOUNTERED!", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("Exposed connectors on your ship get clogged.", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("Flight extended by 1 day PER exposed connector!", CARD_WIDTH - 2)));

        // Line 15: Bottom Border
        sb.append(hb);

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("Testing StellarSpaceDustCardASCIIFormatter (Enhanced Graphics):");
        System.out.println("-----------------------------------------------------------");

        StellarSpaceDustCard card1 = new StellarSpaceDustCard(1, "polvere_stellare_lvl1.png");
        String formattedCard1 = format(card1);
        System.out.println("Card 1 (Lvl " + card1.getLevel() + ", " + card1.getName() + "):");
        System.out.println(formattedCard1);

        StellarSpaceDustCard card2 = new StellarSpaceDustCard(2, "dense_dust_cloud.png");
        String formattedCard2 = format(card2);
        System.out.println("Card 2 (Lvl " + card2.getLevel() + ", " + card2.getName() + "):");
        System.out.println(formattedCard2);
    }
}
