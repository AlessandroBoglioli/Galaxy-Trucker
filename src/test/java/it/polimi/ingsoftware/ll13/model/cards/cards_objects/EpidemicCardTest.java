package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpidemicCardTest {

    private EpidemicCard epidemicCard;

    @BeforeEach
    void setUp() {
        String image = "/cards/GT-cards_2_epidemic_1.png";
        epidemicCard = new EpidemicCard(1, image);
    }


    @Test
    void testConstructor() {
        assertNotNull(epidemicCard);
        assertEquals("Epidemic", epidemicCard.getName());
        assertEquals(1, epidemicCard.getLevel());
    }

    @Test
    void testGetLevel() {
        assertEquals(1, epidemicCard.getLevel());
    }

    // TODO: apply effect
}