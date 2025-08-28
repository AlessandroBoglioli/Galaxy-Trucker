package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpidemicCreatorTest {

    private final EpidemicCreator creator = new EpidemicCreator();

    @Test
    void createCard_shouldCreateValidCardWithCorrectValues() throws Exception {
        JSONObject validData = new JSONObject()
                .put("level", 1)
                .put("image", "/cards/GT-cards_2_epidemic_1.png");

        Card card = creator.createCard(validData);

        assertInstanceOf(EpidemicCard.class, card);
        EpidemicCard epidemicCard = (EpidemicCard) card;
        assertEquals(1, epidemicCard.getLevel());
    }

    @Test
    void createCard_shouldReturnEpidemicCardInstance() {
        JSONObject jsonObject = new JSONObject()
                .put("level", 1)
                .put("image", "/cards/GT-cards_2_epidemic_1.png");

        Card card = creator.createCard(jsonObject);

        assertInstanceOf(EpidemicCard.class, card,
                "Should return an instance of EpidemicCard");
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
                .put("image", "/cards/GT-cards_2_epidemic_1.png");
        JSONObject data2 = new JSONObject()
                .put("level", 1)
                .put("image", "/cards/GT-cards_2_epidemic_1.png");

        Card card1 = creator.createCard(data1);
        Card card2 = creator.createCard(data2);

        assertNotSame(card1, card2,
                "Should return new instance for each call");
    }
}