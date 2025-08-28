package it.polimi.ingsoftware.ll13.client.utils.printers.helper_printers;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.PlanetsCard;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Planet;
import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor;

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Formats a PlanetsCard into a compact multi-line ASCII string representation.
 */
public class PlanetsCardASCIIFormatter {

    private static final int CARD_WIDTH = 48;
    private static final int MAX_PLANETS_TO_DISPLAY = 3; // Max planets to list individually

    private static String centerText(String text, int width) {
        if (text == null) text = "";
        if (text.length() > width) {
            text = text.substring(0, width - 3) + "..."; // Truncate if too long
        }
        if (text.length() == width) return text;

        int padding = width - text.length();
        int padLeft = padding / 2;
        int padRight = padding - padLeft;
        return " ".repeat(padLeft) + text + " ".repeat(padRight);
    }

    private static String formatCargoList(List<CargoColor> cargos) {
        if (cargos == null || cargos.isEmpty()) {
            return "No Cargo";
        }
        return "Cargo: " + cargos.stream()
                .map(c -> String.valueOf(c.toString().charAt(0))) // Y, G, B, R
                .collect(Collectors.joining("")); // YGBR
    }

    /**
     * Formats the given PlanetsCard into a compact multi-line string.
     * Produces 12 lines total output from the formatter, including its borders.
     * (10 lines of actual card content between the top/bottom borders of the formatter).
     *
     * @param card The PlanetsCard to format.
     * @return A string representing the card.
     */
    public static String format(PlanetsCard card) {
        if (card == null) {
            return "+" + "-".repeat(CARD_WIDTH - 2) + "+\n" +
                    "|" + centerText("Error: Card data missing.", CARD_WIDTH - 2) + "|\n" +
                    "+" + "-".repeat(CARD_WIDTH - 2) + "+\n";
        }

        String name = card.getName().toUpperCase();
        int level = card.getLevel();
        List<Planet> planets = card.getPlanets() == null ? Collections.emptyList() : card.getPlanets();
        int flightDaysPenalty = card.getFlightDaysReduction();

        StringBuilder sb = new StringBuilder();
        String hb = "+" + "-".repeat(CARD_WIDTH - 2) + "+\n"; // Horizontal Border
        String emptyContentForLine = " ".repeat(CARD_WIDTH - 2);

        // Line 1: Top border
        sb.append(hb);
        // Line 2: Title & Level
        sb.append(String.format("|%s|\n", centerText(name + " (Lvl: " + level + ")", CARD_WIDTH - 2)));
        // Line 3: Separator
        sb.append(hb);
        // Line 4: Art & Short Flavor
        sb.append(String.format("|%s|\n", centerText("🪐 Explore new worlds! Riches await... 🪐", CARD_WIDTH - 2)));
        // Line 5: Separator
        sb.append(hb);

        // Line 6: Header for planets
        sb.append(String.format("|%s|\n", centerText("Planets (Select by #):", CARD_WIDTH - 2)));

        // Lines 7-9: Displaying up to MAX_PLANETS_TO_DISPLAY
        for (int i = 0; i < MAX_PLANETS_TO_DISPLAY; i++) {
            if (i < planets.size()) {
                Planet planet = planets.get(i);
                String planetStatus = planet.isChosen() ? "Visited" : formatCargoList(planet.getCargos());
                String planetLine = String.format("%d: %s", i + 1, planetStatus);
                sb.append(String.format("| %-44s |\n", planetLine.substring(0, Math.min(planetLine.length(), 44))));
            } else {
                sb.append(String.format("|%s|\n", emptyContentForLine)); // Empty line if fewer than MAX_PLANETS
            }
        }


        // Line 10: Landing Cost
        sb.append(String.format("|%s|\n", centerText("Landing Cost: Extends flight by " + flightDaysPenalty + " days!", CARD_WIDTH - 2)));
        // Line 11: Skip option / Advice
        sb.append(String.format("|%s|\n", centerText("(Choose #, or 0 to Skip)", CARD_WIDTH - 2)));
        // Line 12: Bottom border
        sb.append(hb);

        return sb.toString(); // Total 12 lines
    }

    public static void main(String[] args) {
        System.out.println("Testing Compact PlanetsCardASCIIFormatter:");
        System.out.println("----------------------------------------");

        Planet p1 = new Planet(List.of(CargoColor.YELLOW, CargoColor.GREEN, CargoColor.BLUE, CargoColor.BLUE));
        Planet p2 = new Planet(List.of(CargoColor.YELLOW, CargoColor.YELLOW));
        Planet p3_visited = new Planet(List.of(CargoColor.RED));
        p3_visited.occupyPlanet(); // Mark as chosen for test
        Planet p4_empty = new Planet(Collections.emptyList());


        List<Planet> planetList1 = List.of(p1, p2);
        PlanetsCard card1 = new PlanetsCard(3, "planets_lvl3_img1.png", planetList1, 2); // Level 3, FDR 2
        System.out.println("Card 1 (2 Planets):");
        System.out.println(format(card1));

        List<Planet> planetList2 = List.of(p1, p2, p3_visited, p4_empty); // 4 planets, 1 visited, 1 empty cargo
        PlanetsCard card2 = new PlanetsCard(3, "planets_lvl3_img2.png", planetList2, 1);
        System.out.println("Card 2 (4 Planets, 1 visited, 1 no cargo - shows first 3):");
        System.out.println(format(card2));

        PlanetsCard cardNoPlanets = new PlanetsCard(1, "empty_sector.png", Collections.emptyList(), 0);
        System.out.println("Card 3 (No Planets):");
        System.out.println(format(cardNoPlanets));
    }
}
