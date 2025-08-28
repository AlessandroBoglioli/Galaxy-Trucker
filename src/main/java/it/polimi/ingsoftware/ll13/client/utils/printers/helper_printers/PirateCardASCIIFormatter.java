package it.polimi.ingsoftware.ll13.client.utils.printers.helper_printers;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.PirateCard;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.FireShot;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.ProblemType;
import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;


import java.util.List;
import java.util.Collections;


/**
 * Formats a PirateCard into a multi-line ASCII string representation.
 */
public class PirateCardASCIIFormatter {

    private static final int CARD_WIDTH = 48;
    private static final int MAX_SHOTS_ON_FIRST_LINE = 2;

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

    // Helper to get a short representation of direction (e.g., initial)
    private static String formatDirectionInitial(Direction direction) {
        if (direction == null) return "?";
        return switch (direction) {
            case TOP -> "T";
            case BOTTOM -> "B";
            case LEFT -> "L";
            case RIGHT -> "R";
            default -> "?";
        };
    }

    /**
     * Formats the given PirateCard into a compact multi-line string.
     * Produces 11 lines total output from the formatter, including its borders.
     * (9 lines of actual card content between the top/bottom borders of the formatter).
     *
     * @param card The PirateCard to format.
     * @return A string representing the card.
     */
    public static String format(PirateCard card) {
        if (card == null) {
            // Basic error display, could be enhanced
            return "+" + "-".repeat(CARD_WIDTH - 2) + "+\n" +
                    "|" + centerText("Error: Card data missing.", CARD_WIDTH - 2) + "|\n" +
                    "+" + "-".repeat(CARD_WIDTH - 2) + "+\n";
        }

        String name = card.getName().toUpperCase();
        int level = card.getLevel();
        int firePowerNeeded = card.getFirePowerNeeded();
        List<FireShot> fireShots = card.getFireShots() == null ? Collections.emptyList() : card.getFireShots();
        int creditsReward = card.getCreditsReward();
        int flightDaysPenalty = card.getFlightDaysReduction();

        StringBuilder sb = new StringBuilder();
        String hb = "+" + "-".repeat(CARD_WIDTH - 2) + "+\n"; // hb for horizontalBorder
        String emptyContentLine = "|" + " ".repeat(CARD_WIDTH - 2) + "|\n";

        // Line 1: Top border
        sb.append(hb);
        // Line 2: Title & Level (Combined)
        sb.append(String.format("|%s|\n", centerText(name + " (Lvl: " + level + ")", CARD_WIDTH - 2)));
        // Line 3: Separator
        sb.append(hb);

        // Line 4: Minimal Art & Short Flavor (Combined on one line)
        sb.append(String.format("|%s|\n", centerText("💀 \"Yield your cargo, space dogs!\" 💀", CARD_WIDTH - 2)));

        // Line 5: Separator
        sb.append(hb);

        // Line 6: Challenge: Firepower Needed
        sb.append(String.format("|%s|\n", centerText("Threat: " + firePowerNeeded + "FP to WIN!", CARD_WIDTH - 2)));
        // Line 7: Win Outcome
        sb.append(String.format("|%s|\n", centerText("Win: +" + creditsReward + "¢ (Cost: " + flightDaysPenalty + " flight days)", CARD_WIDTH - 2)));
        // Line 8: Lose Outcome Intro
        sb.append(String.format("|%s|\n", centerText("If you LOSE or SURRENDER, they fire:", CARD_WIDTH - 2)));

        // Line 9: Fire Shot Details (Compact display)
        if (fireShots.isEmpty()) {
            sb.append(String.format("|%s|\n", centerText("(No direct fire listed for this encounter)", CARD_WIDTH - 2)));
        } else {
            StringBuilder shotsDisplay = new StringBuilder("Shots: ");
            for (int i = 0; i < Math.min(fireShots.size(), MAX_SHOTS_ON_FIRST_LINE); i++) {
                FireShot shot = fireShots.get(i);
                String typeInitial = String.valueOf(shot.getFireShotType().toString().charAt(0)); // S or B
                String dirInitial = formatDirectionInitial(shot.getFireShotDirection());       // T, B, L, R
                shotsDisplay.append(String.format("%s/%s", typeInitial, dirInitial));
                if (i < Math.min(fireShots.size(), MAX_SHOTS_ON_FIRST_LINE) - 1) {
                    shotsDisplay.append(" | "); // Separator if more shots on this line
                }
            }
            if (fireShots.size() > MAX_SHOTS_ON_FIRST_LINE) {
                shotsDisplay.append(String.format(" (...+%d more)", fireShots.size() - MAX_SHOTS_ON_FIRST_LINE));
            }
            sb.append(String.format("|%s|\n", centerText(shotsDisplay.toString(), CARD_WIDTH - 2)));
        }

        // Line 10: Final advice / Call to action
        sb.append(String.format("|%s|\n", centerText("(Choose: Fight bravely or Brace for impact!)", CARD_WIDTH - 2)));
        // Line 11: Bottom border
        sb.append(hb);

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("Testing Compact PirateCardASCIIFormatter:");
        System.out.println("----------------------------------------");

        List<FireShot> shots1 = List.of(
                new FireShot(ProblemType.SMALL, Direction.TOP),
                new FireShot(ProblemType.BIG, Direction.LEFT)
        );
        PirateCard card1 = new PirateCard(1, "pirati_lvl1.png", 5, shots1, 4, 1);
        System.out.println(format(card1));

        List<FireShot> shots2 = List.of(
                new FireShot(ProblemType.SMALL, Direction.RIGHT),
                new FireShot(ProblemType.SMALL, Direction.BOTTOM),
                new FireShot(ProblemType.BIG, Direction.TOP), // This will be part of "...+X more"
                new FireShot(ProblemType.SMALL, Direction.LEFT)  // This too
        );
        PirateCard card2 = new PirateCard(2, "pirati_lvl2.png", 8, shots2, 10, 2);
        System.out.println(format(card2));

        PirateCard cardNoShots = new PirateCard(1, "cowardly_pirates.png", 3, Collections.emptyList(), 5, 0);
        System.out.println(format(cardNoShots));

        List<FireShot> oneShot = List.of(new FireShot(ProblemType.BIG, Direction.TOP));
        PirateCard cardOneShot = new PirateCard(3, "one_shot_pirate.png", 6, oneShot, 7, 1);
        System.out.println(format(cardOneShot));
    }
}

