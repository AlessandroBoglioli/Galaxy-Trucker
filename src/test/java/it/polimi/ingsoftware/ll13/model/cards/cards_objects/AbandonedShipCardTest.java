package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AbandonedShipCardTest {

    private AbandonedShipCard abandonedShipCard;

    @BeforeEach
    void setUp() {
        String image = "/cards/GT-cards_1_abandoned_ship_1.png";
        abandonedShipCard = new AbandonedShipCard(1, image,2, 100, 3);
    }

    @Test
    void testConstructor() {
        assertNotNull(abandonedShipCard);
        assertEquals("Abandoned Ship", abandonedShipCard.getName());
        assertEquals(1, abandonedShipCard.getLevel());
        assertEquals(2, abandonedShipCard.getCrewSacrifice());
        assertEquals(100, abandonedShipCard.getCreditsReward());
        assertEquals(3, abandonedShipCard.getFlightDaysReduction());
    }

    @Test
    void testGetCardLevel() {
        assertEquals(1, abandonedShipCard.getLevel());
    }

    @Test
    void testGetCrewSacrifice() {
        assertEquals(2, abandonedShipCard.getCrewSacrifice());
    }

    @Test
    void testGetCreditsReward() {
        assertEquals(100, abandonedShipCard.getCreditsReward());
    }

    @Test
    void testGetFlightDaysReduction() {
        assertEquals(3, abandonedShipCard.getFlightDaysReduction());
    }

    // TODO: apply effect
}