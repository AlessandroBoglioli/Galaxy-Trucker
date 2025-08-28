package it.polimi.ingsoftware.ll13.client.utils.printers.helper_printers;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.SmugglersCard;
import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor; // For rewards and main method

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Formats a SmugglersCard into a multi-line ASCII string representation
 * with enhanced graphics.
 */
public class SmugglersCardASCIIFormatter {

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

    private static String formatCargoRewards(List<CargoColor> cargos) {
        if (cargos == null || cargos.isEmpty()) {
            return "No Cargo";
        }
        // Compact representation: e.g., "YGB" for Yellow, Green, Blue
        return cargos.stream()
                .map(c -> String.valueOf(c.toString().charAt(0)))
                .collect(Collectors.joining(""));
    }

    /**
     * Formats the given SmugglersCard into a multi-line string.
     * Produces 15 lines total output from the formatter, including its borders
     * (13 lines of actual card content).
     *
     * @param card The SmugglersCard to format.
     * @return A string representing the card.
     */
    public static String format(SmugglersCard card) {
        if (card == null) {
            return "+" + "-".repeat(CARD_WIDTH - 2) + "+\n" +
                    "|" + centerText("Error: Card data missing.", CARD_WIDTH - 2) + "|\n" +
                    "+" + "-".repeat(CARD_WIDTH - 2) + "+\n";
        }

        String name = card.getName().toUpperCase(); // "SMUGGLERS"
        int level = card.getLevel();
        int firePowerNeeded = card.getFirePowerNeeded();
        int cargoPenalty = card.getCargoPenalty();
        List<CargoColor> rewards = card.getRewards() == null ? Collections.emptyList() : card.getRewards();
        int flightDaysPenalty = card.getFlightDaysReduction();

        String cargoRewardStr = formatCargoRewards(rewards);

        StringBuilder sb = new StringBuilder();
        String hb = "+" + "-".repeat(CARD_WIDTH - 2) + "+\n"; // Horizontal Border
        String emptyContentForLine = " ".repeat(CARD_WIDTH - 2);

        // Line 1: Top border
        sb.append(hb);
        // Line 2: Title & Level
        sb.append(String.format("|%s|\n", centerText(name + " (Lvl: " + level + ")", CARD_WIDTH - 2)));
        // Line 3: Separator
        sb.append(hb);
        // Line 4: Empty line above art
        sb.append(String.format("|%s|\n", emptyContentForLine));

        // Lines 5-8: ASCII Art for Smuggler Ship (4 lines) - Freighter-like
        sb.append(String.format("|%s|\n", centerText("    .-------------.    ", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("   | [o] [|||||] |__`)  ", CARD_WIDTH - 2))); // [o] cockpit, [|||||] cargo bay
        sb.append(String.format("|%s|\n", centerText("   |   [|||||] |___\\\\ ", CARD_WIDTH - 2))); // Another cargo bay, different angle
        sb.append(String.format("|%s|\n", centerText("    `--(oo)-(oo)--'    ", CARD_WIDTH - 2))); // (oo) engines

        // Line 9: Flavor Text
        sb.append(String.format("|%s|\n", centerText("\"Shady deals or outright theft... their choice.\"", CARD_WIDTH - 2)));
        // Line 10: Empty line below flavor
        sb.append(String.format("|%s|\n", emptyContentForLine));
        // Line 11: Separator
        sb.append(hb);

        // Info section (3 lines) - L12-L14
        sb.append(String.format("|%s|\n", centerText("SMUGGLERS! Fight: " + firePowerNeeded + "FP. Win(>" + firePowerNeeded + "FP): Get " + cargoRewardStr + "!", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("Win Cost: " + flightDaysPenalty + " flight days. Exact " + firePowerNeeded + "FP: Stalemate.", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("Lose Fight or Surrender: Lose " + cargoPenalty + " Cargo items!", CARD_WIDTH - 2)));

        // Line 15: Bottom Border
        sb.append(hb);

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("Testing SmugglersCardASCIIFormatter (Enhanced Graphics):");
        System.out.println("-----------------------------------------------------");

        // Values from card image: Level 1, FP Needed 4, Cargo Penalty 2,
        // Reward: YGB, Flight Penalty (assume 1)
        List<CargoColor> rewards1 = List.of(CargoColor.YELLOW, CargoColor.GREEN, CargoColor.BLUE);
        SmugglersCard card1 = new SmugglersCard(
                1, "smugglers_lvl1.png", 4, 2, rewards1, 1
        );
        String formattedCard1 = format(card1);
        System.out.println("Card 1 (Lvl " + card1.getLevel() + ", " + card1.getName() + "):");
        System.out.println(formattedCard1);

        List<CargoColor> rewards2 = List.of(CargoColor.RED, CargoColor.RED);
        SmugglersCard card2 = new SmugglersCard(2, "smugglers_heavy.png", 6, 3, rewards2, 2);
        System.out.println("Card 2 (Lvl " + card2.getLevel() + ", " + card2.getName() + "):");
        System.out.println(format(card2));
    }
}