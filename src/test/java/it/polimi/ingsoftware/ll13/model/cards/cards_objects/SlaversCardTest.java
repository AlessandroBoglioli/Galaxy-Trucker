package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SlaversCardTest {

    private SlaversCard slaversCard;
    private final String image = "/cards/GT-cards_1_slavers_1.png";

    @BeforeEach
    void setUp() {
        slaversCard = new SlaversCard(1, image,5, 2, 100, 3);
    }

    @Test
    void testConstructor() {
        assertNotNull(slaversCard);
        assertEquals("Slavers", slaversCard.getName());
        assertEquals(1, slaversCard.getLevel());
        assertEquals(image, slaversCard.getImage());
        assertEquals(5, slaversCard.getFirePowerNeeded());
        assertEquals(2, slaversCard.getCrewSacrifice());
        assertEquals(100, slaversCard.getCreditsReward());
        assertEquals(3, slaversCard.getFlightDaysReduction());
    }

    @Test
    void testGetLevel() {
        assertEquals(1, slaversCard.getLevel());
    }

    @Test
    void testGetImage() {
        assertEquals(image, slaversCard.getImage());
    }

    @Test
    void testGetFirePowerNeeded() {
        assertEquals(5, slaversCard.getFirePowerNeeded());
    }

    @Test
    void testGetCrewSacrifice() {
        assertEquals(2, slaversCard.getCrewSacrifice());
    }

    @Test
    void testGetCreditsReward() {
        assertEquals(100, slaversCard.getCreditsReward());
    }

    @Test
    void testGetFlightDaysReduction() {
        assertEquals(3, slaversCard.getFlightDaysReduction());
    }
}