package it.polimi.ingsoftware.ll13.client.utils.printers.helper_printers;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.SlaversCard;

/**
 * Formats a SlaversCard into a multi-line ASCII string representation
 * with more detailed ASCII art.
 */
public class SlaversCardASCIIFormatter {

    private static final int CARD_WIDTH = 48;

    private static String centerText(String text, int width) {
        if (text == null) text = "";
        // Truncate if text is longer than width to prevent breaking layout
        if (text.length() > width) {
            text = text.substring(0, width - 3) + "...";
        }
        // If text is already exact width, no padding needed
        if (text.length() == width) return text;

        int padding = width - text.length();
        int padLeft = padding / 2;
        int padRight = padding - padLeft;
        return " ".repeat(padLeft) + text + " ".repeat(padRight);
    }

    /**
     * Formats the given SlaversCard into a multi-line string with enhanced graphics.
     * Produces 15 lines total output from the formatter, including its borders.
     * (13 lines of actual card content between the top/bottom borders of the formatter).
     *
     * @param card The SlaversCard to format.
     * @return A string representing the card.
     */
    public static String format(SlaversCard card) {
        if (card == null) {
            // Basic error display
            return "+" + "-".repeat(CARD_WIDTH - 2) + "+\n" +
                    "|" + centerText("Error: Card data missing.", CARD_WIDTH - 2) + "|\n" +
                    "+" + "-".repeat(CARD_WIDTH - 2) + "+\n";
        }

        String name = card.getName().toUpperCase(); // "SLAVERS"
        int level = card.getLevel();
        int firePowerNeeded = card.getFirePowerNeeded();
        int crewSacrifice = card.getCrewSacrifice();
        int creditsReward = card.getCreditsReward();
        int flightDaysPenalty = card.getFlightDaysReduction();

        StringBuilder sb = new StringBuilder();
        String hb = "+" + "-".repeat(CARD_WIDTH - 2) + "+\n"; // Horizontal Border
        String emptyContentForLine = " ".repeat(CARD_WIDTH - 2);

        // Line 1: Top border
        sb.append(hb);
        // Line 2: Title & Level (Combined)
        sb.append(String.format("|%s|\n", centerText(name + " (Lvl: " + level + ")", CARD_WIDTH - 2)));
        // Line 3: Separator
        sb.append(hb);
        // Line 4: Empty line above art
        sb.append(String.format("|%s|\n", emptyContentForLine));

        // Lines 5-8: ASCII Art for Slaver Ship (4 lines)
        sb.append(String.format("|%s|\n", centerText("      .--\"\"\"--.      ", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("     /  <⊙ menacing⊙>  \\     ", CARD_WIDTH - 2))); // <⊙⊙> as 'eyes' or cockpit
        sb.append(String.format("|%s|\n", centerText("    <==[SLAVER'S GRASP]==>", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("      \\   `----'   /      ", CARD_WIDTH - 2)));

        // Line 9: Flavor Text
        sb.append(String.format("|%s|\n", centerText("\"They patrol these sectors... for chattel!\"", CARD_WIDTH - 2)));
        // Line 10: Empty line below flavor
        sb.append(String.format("|%s|\n", emptyContentForLine));
        // Line 11: Separator
        sb.append(hb);

        // Line 12: Challenge
        sb.append(String.format("|%s|\n", centerText("SLAVERS! To fight, you need " + firePowerNeeded + " Firepower.", CARD_WIDTH - 2)));
        // Line 13: Clear Win Outcome (> FP)
        sb.append(String.format("|%s|\n", centerText("Clear Win (>"+firePowerNeeded+"FP): +" + creditsReward + "¢, Cost: " + flightDaysPenalty + " flight days.", CARD_WIDTH - 2)));
        // Line 14: Exact FP Outcome / Lose or Surrender (Combined for brevity, but distinct in consequence)
        String loseSurrenderText = "Exact "+firePowerNeeded+"FP: Stalemate. Lose (<"+firePowerNeeded+"FP) or Surrender:";
        sb.append(String.format("|%s|\n", centerText(loseSurrenderText, CARD_WIDTH - 2)));
        // Line 15: Consequence of loss/surrender (Sacrifice)
        sb.append(String.format("|%s|\n", centerText("SACRIFICE " + crewSacrifice + " CREW MEMBERS!", CARD_WIDTH - 2)));

        // Rebuilding with 15 lines from formatter:
        StringBuilder sb15 = new StringBuilder();
        sb15.append(hb); // L1
        sb15.append(String.format("|%s|\n", centerText(name + " (Lvl: " + level + ")", CARD_WIDTH - 2))); // L2
        sb15.append(hb); // L3

        // Art (4 lines) -  New L4-L7
        sb15.append(String.format("|%s|\n", centerText("      .--\"\"\"--.      ", CARD_WIDTH - 2)));
        sb15.append(String.format("|%s|\n", centerText("     /  <⊙⊙>  \\     ", CARD_WIDTH - 2))); // <⊙⊙> as 'eyes'
        sb15.append(String.format("|%s|\n", centerText("    <==[SLAVER SHIP]==>", CARD_WIDTH - 2)));
        sb15.append(String.format("|%s|\n", centerText("      \\   `----'   /      ", CARD_WIDTH - 2)));

        // Flavor Text - New L8
        sb15.append(String.format("|%s|\n", centerText("\"Their reputation for cruelty is widespread.\"", CARD_WIDTH - 2)));
        // Separator - New L9
        sb15.append(hb);

        // Info section (4 lines) - New L10-L13
        sb15.append(String.format("|%s|\n", centerText("SLAVERS! To fight, you need " + firePowerNeeded + " Firepower.", CARD_WIDTH - 2)));
        sb15.append(String.format("|%s|\n", centerText("Clear Win (>"+firePowerNeeded+"FP): +" + creditsReward + "¢, Cost: " + flightDaysPenalty + " flight days.", CARD_WIDTH - 2)));
        sb15.append(String.format("|%s|\n", centerText("Exact "+firePowerNeeded+"FP Stalemate (no reward/loss if fought).", CARD_WIDTH - 2)));
        sb15.append(String.format("|%s|\n", centerText("Lose fight (<"+firePowerNeeded+"FP) or Surrender: Sacrifice " + crewSacrifice + " Crew!", CARD_WIDTH - 2)));

        // Bottom Border - New L14 (This makes it 14 lines from formatter, 12 content)

        // Final attempt for 15 lines from formatter:
        StringBuilder sbFinal = new StringBuilder();
        sbFinal.append(hb); // L1
        sbFinal.append(String.format("|%s|\n", centerText(name + " (Lvl: " + level + ")", CARD_WIDTH - 2))); // L2
        sbFinal.append(hb); // L3
        sbFinal.append(String.format("|%s|\n", emptyContentForLine)); // L4 - Empty above art

        // Art (4 lines) - L5-L8
        sbFinal.append(String.format("|%s|\n", centerText("      .--\"\"\"--.      ", CARD_WIDTH - 2)));
        sbFinal.append(String.format("|%s|\n", centerText("     /  <⊙⊙>  \\     ", CARD_WIDTH - 2)));
        sbFinal.append(String.format("|%s|\n", centerText("    <==[SLAVER SHIP]==>", CARD_WIDTH - 2)));
        sbFinal.append(String.format("|%s|\n", centerText("      \\   `----'   /      ", CARD_WIDTH - 2)));

        // Flavor Text - L9
        sbFinal.append(String.format("|%s|\n", centerText("\"Their reputation for cruelty is widespread.\"", CARD_WIDTH - 2)));
        // Empty line after flavor - L10
        sbFinal.append(String.format("|%s|\n", emptyContentForLine));
        // Separator - L11
        sbFinal.append(hb);

        // Info section (3 lines) - L12-L14. Condense info slightly.
        sbFinal.append(String.format("|%s|\n", centerText("Fight: Need " + firePowerNeeded + "FP. Win (>"+firePowerNeeded+"FP): +" + creditsReward + "¢, Cost " + flightDaysPenalty + " days.", CARD_WIDTH - 2)));
        sbFinal.append(String.format("|%s|\n", centerText("Exact "+firePowerNeeded+"FP: Stalemate. Lose/Surrender:", CARD_WIDTH - 2)));
        sbFinal.append(String.format("|%s|\n", centerText("Sacrifice " + crewSacrifice + " Crew!", CARD_WIDTH - 2)));

        // Bottom Border - L15
        sbFinal.append(hb);


        return sbFinal.toString(); // Total 15 lines
    }

    public static void main(String[] args) {
        System.out.println("Testing SlaversCardASCIIFormatter (Enhanced Graphics):");
        System.out.println("----------------------------------------------------");

        // Values from card image: Level 1, FP Needed 6, Crew Sacrifice 3, Credits Reward 5
        SlaversCard card1 = new SlaversCard(
                1, "schiavisti_lvl1.png", 3, 5, 1, 1
        );
        // The above constructor is wrong based on the one I have. It should be:
        // public SlaversCard(int level, String image, int firePowerNeeded, int crewSacrifice, int creditsReward, int flightDaysReduction)
        SlaversCard card1_fixed = new SlaversCard(1, "schiavisti_lvl1.png", 6, 3, 5, 1);

        String formattedCard1 = format(card1_fixed);
        System.out.println("Card 1 (Lvl " + card1_fixed.getLevel() + ", " + card1_fixed.getName() + "):");
        System.out.println(formattedCard1);

        SlaversCard card2 = new SlaversCard(2, "slavers_strong.png", 10, 5, 15, 2);
        String formattedCard2 = format(card2);
        System.out.println("Card 2 (Lvl " + card2.getLevel() + ", " + card2.getName() + "):");
        System.out.println(formattedCard2);
    }
}
