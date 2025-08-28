package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StellarSpaceDustCreatorTest {

    private final StellarSpaceDustCreator creator = new StellarSpaceDustCreator();
    private final String image = "/cards/GT-cards_1_stellar_spacedust_1.png";

    @Test
    void createCard_shouldCreateValidCardWithCorrectValues() throws Exception {
        JSONObject validData = new JSONObject()
                .put("level", 1)
                .put("image", image);

        Card card = creator.createCard(validData);

        assertInstanceOf(StellarSpaceDustCard.class, card);
        StellarSpaceDustCard stellarSpacedustCard = (StellarSpaceDustCard) card;
        assertEquals(1, stellarSpacedustCard.getLevel());
    }

    @Test
    void createCard_shouldReturnStellarSpacedustInstance() {
        JSONObject data = new JSONObject()
                .put("level", 1)
                .put("image", image);

        Card card = creator.createCard(data);

        assertInstanceOf(StellarSpaceDustCard.class, card,
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
                .put("image", "/cards/GT-cards_1_slavers_1.png");
        JSONObject data2 = new JSONObject()
                .put("level", 1)
                .put("image", "/cards/GT-cards_1_slavers_1.png");

        Card card1 = creator.createCard(data1);
        Card card2 = creator.createCard(data2);

        assertNotSame(card1, card2,
                "Should return new instance for each call");
    }
}