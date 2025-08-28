package it.polimi.ingsoftware.ll13.client.utils.printers.helper_printers;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Meteor;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.MeteorShowerCard;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.ProblemType;
import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeteorShowerCardASCIIFormatter {

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
     * Formats the given MeteorShowerCard into a multi-line string (14 lines total structure).
     * The actual content formatted is 12 lines between top/bottom borders.
     * @param card The MeteorShowerCard to format.
     * @return A string representing the card.
     */
    public static String format(MeteorShowerCard card) {
        if (card == null) {
            return centerText("Error: Card data is missing.", CARD_WIDTH - 2) + "\n";
        }

        String name = card.getName().toUpperCase();
        int level = card.getLevel();
        List<Meteor> meteors = card.getMeteors() == null ? Collections.emptyList() : card.getMeteors();

        long smallMeteors = meteors.stream().filter(m -> m.getMeteorType() == ProblemType.SMALL).count();
        long bigMeteors = meteors.stream().filter(m -> m.getMeteorType() == ProblemType.BIG).count();
        int totalMeteors = meteors.size();

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

        // Lines 6-8: Abstract "art" for meteors (3 lines)
        sb.append(String.format("|%s|\n", centerText("    * o O     .   ", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText("  O .  * METEORS!  o ", CARD_WIDTH - 2)));
        sb.append(String.format("|%s|\n", centerText(" .      o . * O . ", CARD_WIDTH - 2)));

        // Line 9: Flavor text
        sb.append(String.format("|%s|\n", centerText("\"Incoming! Brace for multiple impacts!\"", CARD_WIDTH - 2)));
        // Line 10: Empty line
        sb.append(String.format("|%s|\n", emptyLineContent));
        // Line 11: Separator border
        sb.append(horizontalBorder);

        // Line 12: Meteor details
        String details = String.format("TOTAL: %d (%dS, %dB)", totalMeteors, smallMeteors, bigMeteors);
        sb.append(String.format("|%s|\n", centerText("METEOR SHOWER! " + details, CARD_WIDTH - 2)));
        // Line 13: Effect description
        sb.append(String.format("|%s|\n", centerText("Warning! Exposed components at risk!", CARD_WIDTH - 2)));

        // Line 14: Bottom border
        sb.append(horizontalBorder);

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("Testing MeteorShowerCardASCIIFormatter with actual Meteor class:");
        System.out.println("-------------------------------------------------------------");

        List<Meteor> testMeteors1 = new ArrayList<>();
        testMeteors1.add(new Meteor(ProblemType.SMALL, Direction.TOP));
        testMeteors1.add(new Meteor(ProblemType.SMALL, Direction.LEFT));
        testMeteors1.add(new Meteor(ProblemType.BIG, Direction.BOTTOM));

        MeteorShowerCard card1 = new MeteorShowerCard(1, "pioggia_lvl1.png", testMeteors1);
        String formattedCard1 = format(card1);
        System.out.println("Card Level 1 (" + card1.getName() + "):");
        System.out.println(formattedCard1);

        List<Meteor> testMeteors2 = new ArrayList<>();
        testMeteors2.add(new Meteor(ProblemType.SMALL, Direction.RIGHT));
        testMeteors2.add(new Meteor(ProblemType.BIG, Direction.TOP));
        testMeteors2.add(new Meteor(ProblemType.BIG, Direction.LEFT));
        testMeteors2.add(new Meteor(ProblemType.SMALL, Direction.BOTTOM));

        MeteorShowerCard card2 = new MeteorShowerCard(2, "pioggia_lvl2.png", testMeteors2);
        String formattedCard2 = format(card2);
        System.out.println("Card Level 2 (" + card2.getName() + "):");
        System.out.println(formattedCard2);

        MeteorShowerCard cardNoMeteors = new MeteorShowerCard(1, "calm_space.png", Collections.emptyList());
        String formattedCardNoMeteors = format(cardNoMeteors);
        System.out.println("Card Level 1 (" + cardNoMeteors.getName() + " - No Meteors):");
        System.out.println(formattedCardNoMeteors);
    }
}

