package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.ProblemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PirateCardTest {

    private PirateCard pirateCard;
    private List<FireShot> fireShots;
    private String image;

    @BeforeEach
    void setUp() {
        image = "/cards/GT-cards_1_pirate_1.png";
        fireShots = Arrays.asList(
                new FireShot(ProblemType.BIG, Direction.TOP),
                new FireShot(ProblemType.SMALL, Direction.RIGHT)
        );
        pirateCard = new PirateCard(1, image, 5, fireShots, 100, 2);
    }

    @Test
    void testConstructor() {
        assertNotNull(pirateCard);
        assertEquals("Pirate", pirateCard.getName());
        assertEquals(1, pirateCard.getLevel());
        assertEquals("/cards/GT-cards_1_pirate_1.png", pirateCard.getImage());
        assertEquals(5, pirateCard.getFirePowerNeeded());
        assertEquals(fireShots, pirateCard.getFireShots());
        assertEquals(100, pirateCard.getCreditsReward());
        assertEquals(2, pirateCard.getFlightDaysReduction());
    }

    @Test
    void testGetLevel() {
        assertEquals(1, pirateCard.getLevel());
    }

    @Test
    void testGetImage() {
        assertEquals(image, pirateCard.getImage());
    }

    @Test
    void testGetFirePowerNeeded() {
        assertEquals(5, pirateCard.getFirePowerNeeded());
    }

    @Test
    void testGetFireShots() {
        assertEquals(fireShots, pirateCard.getFireShots());
    }

    @Test
    void testGetCreditsReward() {
        assertEquals(100, pirateCard.getCreditsReward());
    }

    @Test
    void testGetFlightDaysReduction() {
        assertEquals(2, pirateCard.getFlightDaysReduction());
    }

    // TODO: apply effect

}