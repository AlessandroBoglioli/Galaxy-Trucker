package it.polimi.ingsoftware.ll13.client.utils.printers.helper_printers;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.AbandonedShipCard;

/**
 * The AbandonedShipCardASCIIFormatter class provides functionality to format
 * an AbandonedShipCard as an ASCII art representation. The formatted output
 * includes card details such as name, level, crew sacrifice cost, rewards,
 * and additional effects in a visually appealing ASCII art format.
 */
public class AbandonedShipCardASCIIFormatter {

    private static final int CARD_WIDTH = 48; // Adjusted width

    /**
     * Helper method to center text within a given width.
     */
    private static String centerText(String text, int width) {
        if (text == null) text = "";
        if (text.length() >= width) return text.substring(0, width);
        int padding = width - text.length();
        int padLeft = padding / 2;
        int padRight = padding - padLeft;
        return " ".repeat(padLeft) + text + " ".repeat(padRight);
    }

    /**
     * Formats the details of an AbandonedShipCard into an ASCII art
     * representation with card details and additional information.
     *
     * @param card the AbandonedShipCard to be formatted. Must not be null.
     * @return the formatted ASCII art string representation of the card. If the card is null, returns an error message.
     */
    public static String format(AbandonedShipCard card) {
        if (card == null) {
            return centerText("Error: Card data is missing.", CARD_WIDTH - 2) + "\n";
        }

        // Card details
        String name = card.getName().toUpperCase();
        int level = card.getLevel();
        int crewSacrifice = card.getCrewSacrifice();
        int creditsReward = card.getCreditsReward();
        int flightDaysReduction = card.getFlightDaysReduction();

        StringBuilder sb = new StringBuilder();
        String horizontalBorder = "+" + "-".repeat(CARD_WIDTH - 2) + "+\n";
        String emptyLineContent = " ".repeat(CARD_WIDTH - 2);

        // Line 1: Top border
        sb.append(horizontalBorder);

        // Line 2: Title
        sb.append(String.format("|%s|\n", centerText(name, CARD_WIDTH - 2)));

        // Line 3: Separator border
        sb.append(horizontalBorder);

        // Line 4: Empty line for spacing
        sb.append(String.format("|%s|\n", emptyLineContent));

        // Lines 5-9: Abstract ship art (5 lines)
        sb.append(String.format("|%s|\n", centerText("                 .--\"\"--.                 ", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("                /________\\                ", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("               |[°] [_] [°]|               ", CARD_WIDTH - 2))); // Windows or lights
        sb.append(String.format("|%s|\n", centerText("              |   Derelict   |              ", CARD_WIDTH - 2))); // Ship body
        sb.append(String.format("|%s|\n", centerText("               `----------'               ", CARD_WIDTH - 2)));

        // Line 10: Flavor text
        sb.append(String.format("|%s|\n", centerText("\"An abandoned ship... what secrets?\"", CARD_WIDTH - 2)));

        // Line 11: Empty line for spacing
        sb.append(String.format("|%s|\n", emptyLineContent));

        // Line 12: Separator border
        sb.append(horizontalBorder);

        // Line 13: Stats line (Level, Cost, Reward)
        String levelStr = String.format("Lvl %d▼", level);
        String costStr = String.format("Cost: %d Crew X", crewSacrifice);
        String rewardStr = String.format("Reward: %d¢", creditsReward);
        String statsLine = String.format("%s | %s | %s",
                centerText(levelStr, (CARD_WIDTH - 2) / 3 -2),
                centerText(costStr, (CARD_WIDTH - 2) / 3 +2),
                centerText(rewardStr, (CARD_WIDTH - 2) / 3 )
        );
        statsLine = String.format(" %s | %s | %s ",
                centerText(levelStr, 10),
                centerText(costStr, 18),
                centerText(rewardStr, 12)
        );

        int padTotal = CARD_WIDTH - 2 - (levelStr.length() + costStr.length() + rewardStr.length() + 6); // 6 for " | " times 2
        int pad1 = padTotal / 4;
        int pad2 = padTotal / 4;
        int pad3 = padTotal / 4;
        int pad4 = padTotal - pad1 - pad2 - pad3;

        statsLine = " ".repeat(pad1) + levelStr + " ".repeat(pad2) + "|" +
                " ".repeat(pad3) + costStr + " ".repeat(pad4) + "|" +
                String.format("%-12s %-20s %-12s", levelStr, costStr, rewardStr);
        String part1 = centerText(levelStr, 13); // Lvl 1v -> 6 chars. 13 gives padding.
        String part2 = centerText(costStr, 17);  // Cost: 2 Crew X -> 14 chars.
        String part3 = centerText(rewardStr, 13);// Reward: 3c -> 10 chars.
        sb.append(String.format("|%s|%s|%s|\n", part1, part2, part3));


        // Line 14: Additional effect (Flight Days Reduction)
        String effectText = String.format("(Also reduces flight by %d Day(s))", flightDaysReduction);
        sb.append(String.format("|%s|\n", centerText(effectText, CARD_WIDTH - 2)));

        // Line 15: Bottom border
        sb.append(horizontalBorder);

        return sb.toString();
    }

    /**
     * The main entry point for the application. This method demonstrates the usage of
     * AbandonedShipCard objects by creating instances of the card with sample data,
     * formatting the cards using the functionality provided by the AbandonedShipCardASCIIFormatter,
     * and printing the results to the console.
     *
     * @param args an array of command-line arguments passed to the program. These arguments
     *             are not utilized in this implementation.
     */
    public static void main(String[] args) {
        // Sample card data inspired by the "NAVE ABBANDONATA" (Level 1) card image
        AbandonedShipCard testCardGT = new AbandonedShipCard(
                1,                          // level (from the triangle on the card)
                "nave_abbandonata.png",     // image placeholder
                2,                          // crewSacrifice
                3,                          // creditsReward
                1                           // flightDaysReduction (example value)
        );

        System.out.println("Testing Galaxy Trucker Style Card Formatting:");
        System.out.println("-------------------------------------------");

        System.out.println("Card: " + testCardGT.getName() + " (Level " + testCardGT.getLevel() + ")");
        String formattedCardGT = AbandonedShipCardASCIIFormatter.format(testCardGT);
        System.out.println(formattedCardGT);

        // Another example with different values
        AbandonedShipCard testCard2 = new AbandonedShipCard(
                2,
                "derelict_freighter.png",
                3,
                5,
                2
        );
        System.out.println("Card: " + testCard2.getName() + " (Level " + testCard2.getLevel() + ")");
        String formattedCard2 = AbandonedShipCardASCIIFormatter.format(testCard2);
        System.out.println(formattedCard2);

    }
}