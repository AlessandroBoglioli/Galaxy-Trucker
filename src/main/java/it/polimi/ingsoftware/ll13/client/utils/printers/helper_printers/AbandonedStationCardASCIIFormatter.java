package it.polimi.ingsoftware.ll13.client.utils.printers.helper_printers;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.AbandonedStationCard;
import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor; // Necessary for the main method

import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

/**
 * The AbandonedStationCardASCIIFormatter class provides functionality to format an
 * AbandonedStationCard into an ASCII art representation that fits within a 48-character wide
 * and 15-line high textual display. This formatter is designed to visually represent
 * the card's details along with a simple aesthetic structure.
 */
public class AbandonedStationCardASCIIFormatter {

    private static final int CARD_WIDTH = 48;

    private static String centerText(String text, int width) {
        if (text == null) text = "";
        if (text.length() > width) {
            text = text.substring(0, width);
        }
        int padding = width - text.length();
        int padLeft = padding / 2;
        int padRight = padding - padLeft;
        return " ".repeat(padLeft) + text + " ".repeat(padRight);
    }

    /**
     * Formats the information of an {@code AbandonedStationCard} object into a
     * visually represented ASCII art-based card layout.
     *
     * The output includes the card's name, level, required crew, cargo rewards, and
     * additional effects such as flight day reductions. It also features an ASCII art
     * depiction and a flavor text for thematic representation.
     *
     * @param card The {@code AbandonedStationCard} object containing the data to be formatted.
     *             If {@code null}, an error message is displayed.
     * @return A formatted string representing the information in a styled ASCII card layout.
     */
    public static String format(AbandonedStationCard card) {
        if (card == null) {
            return centerText("Error: Card data is missing.", CARD_WIDTH - 2) + "\n";
        }

        String name = card.getName().toUpperCase();
        int level = card.getLevel();
        int requiredCrew = card.getRequiredCrew();
        List<CargoColor> rewards = card.getRewards();
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

        // Lines 5-9: Abstract station art (5 lines)
        sb.append(String.format("|%s|\n", centerText("                  .-''-.                  ", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("                 /  ||  \\                 ", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("                | ====== |                ", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("               /= SpaceSt =\\               ", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("                `------'                ", CARD_WIDTH - 2)));

        // Line 10: Flavor text
        sb.append(String.format("|%s|\n", centerText("\"A silent station... what's inside?\"", CARD_WIDTH - 2)));
        // Line 11: Empty line for spacing
        sb.append(String.format("|%s|\n", emptyLineContent));
        // Line 12: Separator border
        sb.append(horizontalBorder);

        // Line 13: Stats line (Level, Required Crew, Cargo Rewards)
        String levelStr = String.format("Lvl %d▼", level);
        String reqCrewStr = String.format("Req: %d Crew", requiredCrew);

        String cargoInitials;
        if (rewards == null || rewards.isEmpty()) {
            cargoInitials = "None";
        } else {
            cargoInitials = rewards.stream()
                    .map(c -> String.valueOf(c.toString().charAt(0)))
                    .collect(Collectors.joining("|"));
        }
        String cargoDisplay = "Cargo: " + cargoInitials; // e.g., "Cargo: YGB" or "Cargo: None"

        String part1Stats = centerText(levelStr, 10);
        String part2Stats = centerText(reqCrewStr, 12);
        String part3Stats = centerText(cargoDisplay, 22);
        sb.append(String.format("|%s|%s|%s|\n", part1Stats, part2Stats, part3Stats));

        // Line 14: Additional effect (Flight Days Reduction)
        String effectText = String.format("(Reduces flight by %d Day(s) if chosen)", flightDaysReduction);
        sb.append(String.format("|%s|\n", centerText(effectText, CARD_WIDTH - 2)));

        // Line 15: Bottom border
        sb.append(horizontalBorder);

        return sb.toString();
    }

    /**
     * The main method serves as the entry point for testing the functionality of the
     * {@code AbandonedStationCard} class and its associated methods, including the
     * ASCII formatting utility provided by {@code AbandonedStationCardASCIIFormatter}.
     *
     * Various test cases are created to demonstrate the formatting of cards with different
     * configurations, such as varying levels, cargo rewards, and other properties essential
     * to the game's logic.
     *
     * @param args Command-line arguments passed to the program. These are not
     *             utilized in this implementation.
     */
    public static void main(String[] args) {
        AbandonedStationCard testCardGT = new AbandonedStationCard(
                1,
                "stazione_abbandonata.png",
                5,
                Arrays.asList(CargoColor.YELLOW, CargoColor.GREEN),
                2
        );

        System.out.println("Testing Galaxy Trucker Style Card Formatting (Abandoned Station):");
        System.out.println("--------------------------------------------------------------");
        System.out.println("Card: " + testCardGT.getName() + " (Level " + testCardGT.getLevel() + ")");
        String formattedCardGT = AbandonedStationCardASCIIFormatter.format(testCardGT);
        System.out.println(formattedCardGT);

        // Example with more cargo items
        AbandonedStationCard testCard2 = new AbandonedStationCard(
                2,
                "large_station.png",
                8,
                Arrays.asList(CargoColor.RED, CargoColor.BLUE, CargoColor.GREEN, CargoColor.YELLOW, CargoColor.GREEN),
                3
        );
        System.out.println("Card: " + testCard2.getName() + " (Level " + testCard2.getLevel() + ")");
        String formattedCard2 = AbandonedStationCardASCIIFormatter.format(testCard2);
        System.out.println(formattedCard2);

        // Example with an even larger number of cargo items to test the limits of the cargo field
        AbandonedStationCard testCard_manyCargo = new AbandonedStationCard(
                3,
                "mega_station_depot.png",
                10,
                Arrays.asList(
                        CargoColor.RED, CargoColor.BLUE, CargoColor.GREEN, CargoColor.YELLOW, CargoColor.GREEN,
                        CargoColor.RED, CargoColor.BLUE, CargoColor.GREEN, CargoColor.YELLOW, CargoColor.GREEN,
                        CargoColor.RED, CargoColor.BLUE, CargoColor.GREEN, CargoColor.YELLOW, CargoColor.GREEN // 15 items
                ),
                1
        );
        System.out.println("Card: " + testCard_manyCargo.getName() + " (Level " + testCard_manyCargo.getLevel() + ")");
        String formatted_manyCargo = AbandonedStationCardASCIIFormatter.format(testCard_manyCargo);
        System.out.println(formatted_manyCargo);


        AbandonedStationCard testCard3 = new AbandonedStationCard(
                1,
                "empty_station.png",
                3,
                Arrays.asList(), // Empty list for rewards
                1
        );
        System.out.println("Card: " + testCard3.getName() + " (Level " + testCard3.getLevel() + ")");
        String formattedCard3 = AbandonedStationCardASCIIFormatter.format(testCard3);
        System.out.println(formattedCard3);
    }
}