package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StellarSpaceDustCardTest {

    private StellarSpaceDustCard stellarSpacedustCard;
    private String image;

    @BeforeEach
    void setUp() {
        image = "/cards/GT-cards_1_stellar_spacedust_1.png";
        stellarSpacedustCard = new StellarSpaceDustCard(1,image);
    }

    @Test
    void testConstructor() {
        assertNotNull(stellarSpacedustCard);
        assertEquals("Stellar space dust", stellarSpacedustCard.getName());
        assertEquals(1, stellarSpacedustCard.getLevel());
    }

    @Test
    void testGetLevel() {
        assertEquals(1, stellarSpacedustCard.getLevel());
    }

    @Test
    void testGetImage() {
        assertEquals(image, stellarSpacedustCard.getImage());
    }

    // TODO: apply effect
}