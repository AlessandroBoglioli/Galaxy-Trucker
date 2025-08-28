package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenSpaceCreatorTest {

    private final OpenSpaceCreator creator = new OpenSpaceCreator();

    @Test
    void createCard_shouldCreateValidCardWithCorrectValues() throws Exception {
        JSONObject validData = new JSONObject()
                .put("level", 1)
                .put("image", "/cards/GT-cards_1_open_space_1.png");

        Card card = creator.createCard(validData);

        assertInstanceOf(OpenSpaceCard.class, card);
        OpenSpaceCard openSpaceCard = (OpenSpaceCard) card;
        assertEquals(1, openSpaceCard.getLevel());
        assertEquals("/cards/GT-cards_1_open_space_1.png", openSpaceCard.getImage());
    }

    @Test
    void createCard_shouldReturnOpenSpaceCardInstance() {
        JSONObject data = new JSONObject()
                .put("level", 1)
                .put("image", "/cards/GT-cards_1_open_space_1.png");

        Card card = creator.createCard(data);

        assertInstanceOf(OpenSpaceCard.class, card,
                "Should return an instance of OpenSpaceCard");
    }

    @Test
    void createCard_shouldThrowIllegalArgumentExceptionForNullData() {
        assertThrows(IllegalArgumentException.class,
                () -> creator.createCard(null),
                "Should throw IllegalArgumentException for null data");
    }

    @Test
    void createCard_shouldAlwaysReturnNewInstance() {
        JSONObject data1 = new JSONObject()
                .put("level", 1)
                .put("image", "/cards/GT-cards_1_open_space_1.png");
        JSONObject data2 = new JSONObject()
                .put("level", 1)
                .put("image", "/cards/GT-cards_1_open_space_1.png");

        Card card1 = creator.createCard(data1);
        Card card2 = creator.createCard(data2);

        assertNotSame(card1, card2,
                "Should return new instance for each call");
    }
}