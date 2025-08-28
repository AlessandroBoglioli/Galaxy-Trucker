package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SmugglersCardTest {

    private SmugglersCard smugglersCard;
    private List<CargoColor> rewards;
    private String image;

    @BeforeEach
    void setUp() {
        image = "/cards/GT-cards_1_smugglers_1.png";
        rewards = Arrays.asList(CargoColor.RED, CargoColor.GREEN, CargoColor.BLUE);
        smugglersCard = new SmugglersCard(1, image, 5, 2, rewards, 3);
    }

    @Test
    void testConstructor() {
        assertNotNull(smugglersCard);
        assertEquals("Smugglers", smugglersCard.getName());
        assertEquals(1, smugglersCard.getLevel());
        assertEquals(image, smugglersCard.getImage());
        assertEquals(5, smugglersCard.getFirePowerNeeded());
        assertEquals(2, smugglersCard.getCargoPenalty());
        assertEquals(rewards, smugglersCard.getRewards());
        assertEquals(3, smugglersCard.getFlightDaysReduction());
    }

    @Test
    void testGetLevel() {
        assertEquals(1, smugglersCard.getLevel());
    }

    @Test
    void testGetImage() {
        assertEquals(image, smugglersCard.getImage());
    }

    @Test
    void testGetFirePowerNeeded() {
        assertEquals(5, smugglersCard.getFirePowerNeeded());
    }

    @Test
    void testGetCargoPenalty() {
        assertEquals(2, smugglersCard.getCargoPenalty());
    }

    @Test
    void testGetRewards() {
        assertEquals(rewards, smugglersCard.getRewards());
    }

    @Test
    void testGetFlightDaysReduction() {
        assertEquals(3, smugglersCard.getFlightDaysReduction());
    }

    // TODO: apply effect

}