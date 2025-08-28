package it.polimi.ingsoftware.ll13.client.utils.printers.helper_printers;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.WarZoneCard;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.WarZonePenalty;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.FireShot;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.WarZonePenaltyType;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.WarZonePenaltyEffect;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.ProblemType;
import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Formats a WarZoneCard into a multi-line ASCII string representation
 * with enhanced graphics, detailing its penalties.
 */
public class WarZoneCardASCIIFormatter {

    private static final int CARD_WIDTH = 48;
    private static final int MAX_PENALTIES_TO_DISPLAY = 3; // Usually 3 penalties

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

    private static String formatDirection(Direction direction) {
        if (direction == null) return "???";
        return switch (direction) {
            case TOP -> "Top(↑)";
            case BOTTOM -> "Bot(↓)";
            case LEFT -> "Left(←)";
            case RIGHT -> "Right(→)";
            default -> direction.toString().substring(0, Math.min(direction.toString().length(), 3));
        };
    }

    private static String formatWarZonePenaltyType(WarZonePenaltyType type) {
        if (type == null) return "Unknown Target";
        return switch (type) {
            case LOWEST_CREW -> "Lowest Crew";
            case LOWEST_FIREPOWER -> "Lowest Firepower";
            case LOWEST_THRUST -> "Lowest Thrust";
            default -> "All Players"; // Or some other default
        };
    }

    private static String formatWarZonePenalty(WarZonePenalty penalty) {
        if (penalty == null) return "Unknown Penalty";
        String target = formatWarZonePenaltyType(penalty.getWarZonePenaltyType());
        String effectStr = "";

        switch (penalty.getWarZonePenaltyEffect()) {
            case MOVE_BACK:
                effectStr = "Move Back " + penalty.getValue() + " spaces";
                break;
            case LOSE_CREW:
                effectStr = "Lose " + penalty.getValue() + " Crew";
                break;
            case LOSE_CARGOS:
                effectStr = "Lose " + penalty.getValue() + " Cargo(s)";
                break;
            case HIT_BY_PROJECTILES:
                List<FireShot> shots = penalty.getFireShots();
                if (shots == null || shots.isEmpty()) {
                    effectStr = "Hit by unspecified projectiles";
                } else {
                    effectStr = "Hit by: " + shots.stream()
                            .map(fs -> (fs.getFireShotType() == ProblemType.SMALL ? "Sml" : "Big") + "/" + formatDirection(fs.getFireShotDirection()))
                            .collect(Collectors.joining(", "));
                    if (effectStr.length() > 30) { // Keep it somewhat concise for one line
                        effectStr = "Hit by " + shots.size() + " shot(s) (mixed)";
                    }
                }
                break;
            default:
                effectStr = "Unknown effect";
                break;
        }
        return String.format("%s: %s.", target, effectStr);
    }


    /**
     * Formats the given WarZoneCard into a multi-line string.
     * Produces 15 lines total output from the formatter.
     */
    public static String format(WarZoneCard card) {
        if (card == null) {
            // Error display
            return "+" + "-".repeat(CARD_WIDTH - 2) + "+\n" +
                    "|" + centerText("Error: Card data missing.", CARD_WIDTH - 2) + "|\n" +
                    "+" + "-".repeat(CARD_WIDTH - 2) + "+\n";
        }

        String name = card.getName().toUpperCase(); // "WAR ZONE"
        int level = card.getLevel();
        List<WarZonePenalty> penalties = card.getPenalties() == null ? Collections.emptyList() : card.getPenalties();

        StringBuilder sb = new StringBuilder();
        String hb = "+" + "-".repeat(CARD_WIDTH - 2) + "+\n";
        String emptyContentForLine = " ".repeat(CARD_WIDTH - 2);

        // Lines 1-4 (Header, Empty)
        sb.append(hb);
        sb.append(String.format("|%s|\n", centerText(name + " (Lvl: " + level + ")", CARD_WIDTH - 2)));
        sb.append(hb);
        sb.append(String.format("|%s|\n", emptyContentForLine));

        // Lines 5-8: ASCII Art for War Zone (4 lines)
        sb.append(String.format("|%s|\n", centerText("   💥 🔥 KA-BOOM! 🔥 💥   ", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("  <-- LASERS --- ZAP! -->  ", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("   🔥 <EXPLOSIONS!> 🔥   ", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("  * Total Chaos Reigns! * ", CARD_WIDTH - 2)));

        // Line 9: Flavor Text
        sb.append(String.format("|%s|\n", centerText("\"The sector is a raging inferno of conflict!\"", CARD_WIDTH - 2)));
        // Line 10: Separator
        sb.append(hb);

        // Lines 11-14: Penalties (Displaying up to MAX_PENALTIES_TO_DISPLAY)
        sb.append(String.format("|%s|\n", centerText("WAR ZONE PENALTIES (Resolve in order):", CARD_WIDTH - 2)));
        for (int i = 0; i < MAX_PENALTIES_TO_DISPLAY; i++) {
            if (i < penalties.size()) {
                String penaltyText = String.format("%d. %s", i + 1, formatWarZonePenalty(penalties.get(i)));
                // Ensure penaltyText fits within the available space
                String lineContent = penaltyText.substring(0, Math.min(penaltyText.length(), CARD_WIDTH - 4));
                sb.append(String.format("| %-44s |\n", lineContent));
            } else {
                sb.append(String.format("|%s|\n", emptyContentForLine));
            }
        }

        // Line 15: Bottom Border
        sb.append(hb);

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("Testing WarZoneCardASCIIFormatter (Enhanced Graphics):");
        System.out.println("---------------------------------------------------");

        // Example Penalties
        WarZonePenalty p1 = new WarZonePenalty(WarZonePenaltyType.LOWEST_FIREPOWER, WarZonePenaltyEffect.LOSE_CREW, 3);

        List<FireShot> shotsForP2 = List.of(
                new FireShot(ProblemType.SMALL, Direction.TOP),
                new FireShot(ProblemType.BIG, Direction.LEFT)
        );
        WarZonePenalty p2 = new WarZonePenalty(WarZonePenaltyType.LOWEST_THRUST, WarZonePenaltyEffect.HIT_BY_PROJECTILES, shotsForP2);

        WarZonePenalty p3 = new WarZonePenalty(WarZonePenaltyType.LOWEST_CREW, WarZonePenaltyEffect.MOVE_BACK, 2);

        List<WarZonePenalty> penaltiesList = List.of(p1, p2, p3);
        WarZoneCard card1 = new WarZoneCard(1, "warzone_lvl1.png", penaltiesList);
        System.out.println(format(card1));

        List<WarZonePenalty> penaltiesList2 = List.of(
                new WarZonePenalty(WarZonePenaltyType.LOWEST_FIREPOWER, WarZonePenaltyEffect.LOSE_CARGOS, 2)
        );
        WarZoneCard card2 = new WarZoneCard(2, "warzone_light.png", penaltiesList2);
        System.out.println(format(card2));
    }
}
