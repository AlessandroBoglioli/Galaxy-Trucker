package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OpenSpaceCardTest {

    private OpenSpaceCard openSpaceCard;
    private String image;

    @BeforeEach
    void setUp() {
        image = "/cards/GT-cards_1_open_space_1.png";
        openSpaceCard = new OpenSpaceCard(1, image);
    }

    @Test
    void testConstructor() {
        assertNotNull(openSpaceCard);
        assertEquals("Open space", openSpaceCard.getName());
        assertEquals(1, openSpaceCard.getLevel());
        assertEquals(image, openSpaceCard.getImage());
    }

    @Test
    void testGetLevel() {
        assertEquals(1, openSpaceCard.getLevel());
    }

    @Test
    void testGetImage() {
        assertEquals(image, openSpaceCard.getImage());
    }

    // TODO: apply effect

}