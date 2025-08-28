package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AbandonedStationCardTest {

    private AbandonedStationCard abandonedStationCard;
    private List<CargoColor> rewards;

    @BeforeEach
    void setUp() {
        String image = "/cards/GT-cards_1_abandoned_station_1.png";
        rewards = Arrays.asList(CargoColor.RED, CargoColor.BLUE);
        abandonedStationCard = new AbandonedStationCard(1, image, 3, rewards, 2);
    }

    @Test
    void testConstructor() {
        assertNotNull(abandonedStationCard);
        assertEquals("Abandoned Station", abandonedStationCard.getName());
        assertEquals(1, abandonedStationCard.getLevel());
        assertEquals("/cards/GT-cards_1_abandoned_station_1.png", abandonedStationCard.getImage());
        assertEquals(3, abandonedStationCard.getRequiredCrew());
        assertEquals(rewards, abandonedStationCard.getRewards());
        assertEquals(2, abandonedStationCard.getFlightDaysReduction());
    }

    @Test
    void testGetLevel() {
        assertEquals(1, abandonedStationCard.getLevel());
    }

    @Test
    void testGetRequiredCrew() {
        assertEquals(3, abandonedStationCard.getRequiredCrew());
    }

    @Test
    void testGetRewards() {
        assertEquals(rewards, abandonedStationCard.getRewards());
    }

    @Test
    void testGetFlightDaysReduction() {
        assertEquals(2, abandonedStationCard.getFlightDaysReduction());
    }

    // TODO: apply effect
}