package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.ProblemType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MeteorShowerCreatorTest {

    private final MeteorShowerCreator creator = new MeteorShowerCreator();

    @Test
    void createCard_shouldCreateValidCardWithMeteors() throws Exception {
        JSONArray meteors = new JSONArray()
                .put(new JSONObject().put("type", "BIG").put("direction", "TOP"))
                .put(new JSONObject().put("type", "SMALL").put("direction", "LEFT"));
        JSONObject data = new JSONObject()
                .put("level", 1)
                .put("image", "/cards/GT-cards_1_meteor_shower_1.png")
                .put("meteors", meteors);

        Card card = creator.createCard(data);

        assertInstanceOf(MeteorShowerCard.class, card);
        MeteorShowerCard meteorCard = (MeteorShowerCard) card;
        assertEquals(1, meteorCard.getLevel());
        assertEquals("/cards/GT-cards_1_meteor_shower_1.png", meteorCard.getImage());
        assertEquals(2, meteorCard.getMeteors().size());

        Meteor bigMeteor = meteorCard.getMeteors().getFirst();
        assertEquals(ProblemType.BIG, bigMeteor.getMeteorType());
        assertEquals(Direction.TOP, bigMeteor.getMeteorDirection());

        Meteor smallMeteor = meteorCard.getMeteors().get(1);
        assertEquals(ProblemType.SMALL, smallMeteor.getMeteorType());
        assertEquals(Direction.LEFT, smallMeteor.getMeteorDirection());
    }

    @Test
    void createCard_shouldThrowForNullData() {
        assertThrows(IllegalArgumentException.class,
                () -> creator.createCard(null),
                "Should throw IllegalArgumentException for null data");
    }

    @Test
    void createCard_shouldThrowForMissingLevelField() {
        JSONArray meteors = new JSONArray()
                .put(new JSONObject().put("type", "BIG").put("direction", "TOP"))
                .put(new JSONObject().put("type", "SMALL").put("direction", "LEFT"));
        JSONObject data = new JSONObject() // Non level field
                .put("meteors", meteors);
        assertThrows(JSONException.class,
                () -> creator.createCard(data),
                "Should throw JSONException for missing meteors field");
    }

    @Test
    void createCard_shouldThrowForMissingMeteorsField() {
        JSONObject data = new JSONObject()
                .put("level", 1); // No meteors field
        assertThrows(JSONException.class,
                () -> creator.createCard(data),
                "Should throw JSONException for missing meteors field");
    }

    @Test
    void createCard_shouldThrowForEmptyMeteorsArray() {
        JSONObject data = new JSONObject()
                .put("level", 1)
                .put("image", "/cards/GT-cards_1_meteor_shower_1.png")
                .put("meteors", new JSONArray());
        assertThrows(JSONException.class,
                () -> creator.createCard(data),
                "Should throw JSONException for empty meteors array");
    }

    @ParameterizedTest
    @MethodSource("provideInvalidMeteorData")
    void createCard_shouldThrowForInvalidMeteorData(JSONObject invalidMeteorData) {
        JSONArray meteors = new JSONArray().put(invalidMeteorData);
        JSONObject data = new JSONObject()
                .put("level", 1)
                .put("image", "/cards/GT-cards_1_meteor_shower_1.png")
                .put("meteors", meteors);

        assertThrows(Exception.class,
                () -> creator.createCard(data),
                "Should throw exception for invalid meteor data");
    }

    private static Stream<Arguments> provideInvalidMeteorData() {
        return Stream.of(
                // Missing type field
                Arguments.of(new JSONObject().put("direction", "TOP")),

                // Missing direction field
                Arguments.of(new JSONObject().put("type", "BIG")),

                // Invalid type value
                Arguments.of(new JSONObject().put("type", "HUGE").put("direction", "TOP")),

                // Invalid direction value
                Arguments.of(new JSONObject().put("type", "SMALL").put("direction", "UP"))
        );
    }

    @Test
    void createCard_shouldHandleMixedMeteorTypes() throws Exception {
        JSONArray meteors = new JSONArray()
                .put(new JSONObject().put("type", "BIG").put("direction", "BOTTOM"))
                .put(new JSONObject().put("type", "SMALL").put("direction", "RIGHT"))
                .put(new JSONObject().put("type", "BIG").put("direction", "LEFT"))
                .put(new JSONObject().put("type", "SMALL").put("direction", "TOP"));
        JSONObject data = new JSONObject()
                .put("level", 1)
                .put("image", "/cards/GT-cards_1_meteor_shower_1.png")
                .put("meteors", meteors);

        Card card = creator.createCard(data);

        assertInstanceOf(MeteorShowerCard.class, card);
        MeteorShowerCard meteorCard = (MeteorShowerCard) card;
        assertEquals(1, meteorCard.getLevel());
        assertEquals("/cards/GT-cards_1_meteor_shower_1.png", meteorCard.getImage());
        assertEquals(4, meteorCard.getMeteors().size());
    }

    @Test
    void createCard_shouldPreserveMeteorOrder() throws Exception {
        JSONArray meteors = new JSONArray()
                .put(new JSONObject().put("type", "SMALL").put("direction", "LEFT"))
                .put(new JSONObject().put("type", "BIG").put("direction", "TOP"))
                .put(new JSONObject().put("type", "SMALL").put("direction", "RIGHT"));
        JSONObject data = new JSONObject()
                .put("level", 1)
                .put("image", "/cards/GT-cards_1_meteor_shower_1.png")
                .put("meteors", meteors);

        Card card = creator.createCard(data);

        MeteorShowerCard meteorCard = (MeteorShowerCard) card;
        assertEquals(ProblemType.SMALL, meteorCard.getMeteors().get(0).getMeteorType());
        assertEquals(ProblemType.BIG, meteorCard.getMeteors().get(1).getMeteorType());
        assertEquals(ProblemType.SMALL, meteorCard.getMeteors().get(2).getMeteorType());
    }
}