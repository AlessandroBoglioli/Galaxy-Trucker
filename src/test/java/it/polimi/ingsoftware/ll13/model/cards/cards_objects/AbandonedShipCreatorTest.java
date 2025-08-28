package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedShipCreatorTest {

    private final AbandonedShipCreator creator = new AbandonedShipCreator();

    @Test
    void createCard_shouldCreateValidCardWithCorrectValues() throws Exception {
        JSONObject validData = new JSONObject()
                .put("level", 1)
                .put("image", "/cards/GT-cards_1_abandoned_ship.png")
                .put("crew_sacrifice", 2)
                .put("credits_reward", 3)
                .put("flight_days_reduction", 1);

        Card card = creator.createCard(validData);

        assertInstanceOf(AbandonedShipCard.class, card);
        AbandonedShipCard abandonedShipCard = (AbandonedShipCard) card;
        assertEquals(1, abandonedShipCard.getLevel());
        assertEquals("/cards/GT-cards_1_abandoned_ship.png", card.getImage());
        assertEquals(2, abandonedShipCard.getCrewSacrifice());
        assertEquals(3, abandonedShipCard.getCreditsReward());
        assertEquals(1, abandonedShipCard.getFlightDaysReduction());
    }

    @Test
    void createCard_shouldThrowIllegalArgumentExceptionForNullData() {
        assertThrows(IllegalArgumentException.class,
                () -> creator.createCard(null),
                "Should throw IllegalArgumentException for null data");
    }

    @ParameterizedTest
    @MethodSource("provideMissingFieldData")
    void createCard_shouldThrowJSONExceptionForMissingFields(JSONObject invalidData) {
        assertThrows(JSONException.class,
                () -> creator.createCard(invalidData),
                "Should throw JSONException for missing required fields");
    }

    private static Stream<JSONObject> provideMissingFieldData() {
        return Stream.of(
                new JSONObject() // Missing all fields
                        .put("image", "/cards/GT-cards_1_abandoned_ship.png")
                        .put("credits_reward", 3)
                        .put("flight_days_reduction", 1),
                new JSONObject() // Missing credits_reward
                        .put("level", 1)
                        .put("crew_sacrifice", 2)
                        .put("flight_days_reduction", 1),
                new JSONObject() // Missing flight_days_reduction
                        .put("level", 1)
                        .put("image", "/cards/GT-cards_1_abandoned_ship.png")
                        .put("crew_sacrifice", 2)
                        .put("credits_reward", 3)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTypeData")
    void createCard_shouldThrowJSONExceptionForInvalidFieldTypes(JSONObject invalidData) {
        assertThrows(JSONException.class,
                () -> creator.createCard(invalidData),
                "Should throw JSONException for invalid field types");
    }

    private static Stream<JSONObject> provideInvalidTypeData() {
        return Stream.of(
                new JSONObject() // level as string
                        .put("level", "one")
                        .put("crew_sacrifice", 2)
                        .put("credits_reward", 3)
                        .put("flight_days_reduction", 1),
                new JSONObject() // crew_sacrifice as string
                        .put("level", 1)
                        .put("crew_sacrifice", "two")
                        .put("credits_reward", 3)
                        .put("flight_days_reduction", 1),
                new JSONObject() // credits_reward as boolean
                        .put("level", 1)
                        .put("crew_sacrifice", 2)
                        .put("credits_reward", true)
                        .put("flight_days_reduction", 1),
                new JSONObject() // flight_days_reduction as array
                        .put("level", 1)
                        .put("crew_sacrifice", 2)
                        .put("credits_reward", 3)
                        .put("flight_days_reduction", new JSONArray())
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -10})
    void createCard_shouldThrowIllegalArgumentExceptionForNegativeValues(int negativeValue) {
        JSONObject data = new JSONObject()
                .put("level", negativeValue)
                .put("crew_sacrifice", negativeValue)
                .put("credits_reward", 3)
                .put("flight_days_reduction", 1);

        assertThrows(IllegalArgumentException.class,
                () -> creator.createCard(data),
                "Should throw IllegalArgumentException for negative values");
    }

    @Test
    void createCard_shouldAcceptZeroValues() throws Exception {
        JSONObject dataWithZeros = new JSONObject()
                .put("level", 0)
                .put("image", "/cards/GT-cards_1_abandoned_ship.png")
                .put("crew_sacrifice", 0)
                .put("credits_reward", 0)
                .put("flight_days_reduction", 0);

        Card card = assertDoesNotThrow(() -> creator.createCard(dataWithZeros),
                "Should accept zero values");

        assertInstanceOf(AbandonedShipCard.class, card);
        AbandonedShipCard abandonedShipCard = (AbandonedShipCard) card;
        assertEquals(0, abandonedShipCard.getLevel());
        assertEquals(0, abandonedShipCard.getCrewSacrifice());
        assertEquals(0, abandonedShipCard.getCreditsReward());
        assertEquals(0, abandonedShipCard.getFlightDaysReduction());
    }

    @ParameterizedTest
    @MethodSource("provideValidPositiveValues")
    void createCard_shouldAcceptValidPositiveValues(int level, int crew, int credits, int reduction) throws Exception {
        JSONObject validData = new JSONObject()
                .put("level", level)
                .put("image", "/cards/GT-cards_1_abandoned_ship.png")
                .put("crew_sacrifice", crew)
                .put("credits_reward", credits)
                .put("flight_days_reduction", reduction);

        Card card = creator.createCard(validData);

        assertInstanceOf(AbandonedShipCard.class, card);
        AbandonedShipCard abandonedShipCard = (AbandonedShipCard) card;
        assertEquals(level, abandonedShipCard.getLevel());
        assertEquals(crew, abandonedShipCard.getCrewSacrifice());
        assertEquals(credits, abandonedShipCard.getCreditsReward());
        assertEquals(reduction, abandonedShipCard.getFlightDaysReduction());
    }

    private static Stream<Arguments> provideValidPositiveValues() {
        return Stream.of(
                Arguments.of(1, 1, 1, 1),
                Arguments.of(5, 5, 10, 2),
                Arguments.of(100, 100, 200, 50),
                Arguments.of(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE)
        );
    }
}