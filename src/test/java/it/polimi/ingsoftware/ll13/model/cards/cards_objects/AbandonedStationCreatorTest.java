package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor;

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

class AbandonedStationCreatorTest {

    private final AbandonedStationCreator creator = new AbandonedStationCreator();

    // Helper method to create valid test data
    private JSONObject createValidTestData(int level, String image, int crew, JSONArray rewards, int reduction) {
        return new JSONObject()
                .put("level", level)
                .put("image", image)
                .put("required_crew", crew)
                .put("rewards", rewards)
                .put("flight_days_reduction", reduction);
    }

    @Test
    void createCard_shouldCreateValidCardWithCorrectValues() throws Exception {
        JSONArray rewards = new JSONArray().put("RED").put("BLUE");
        JSONObject validData = createValidTestData(1, "/cards/GT-cards_1_abandoned_station_1.png", 3, rewards, 1);

        Card card = creator.createCard(validData);

        assertInstanceOf(AbandonedStationCard.class, card);
        AbandonedStationCard stationCard = (AbandonedStationCard) card;
        assertEquals(1, stationCard.getLevel());
        assertEquals("/cards/GT-cards_1_abandoned_station_1.png", stationCard.getImage());
        assertEquals(3, stationCard.getRequiredCrew());
        assertEquals(2, stationCard.getRewards().size());
        assertEquals(CargoColor.RED, stationCard.getRewards().get(0));
        assertEquals(CargoColor.BLUE, stationCard.getRewards().get(1));
        assertEquals(1, stationCard.getFlightDaysReduction());
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
                        .put("rewards", new JSONArray().put("RED"))
                        .put("flight_days_reduction", 1),
                new JSONObject() // Missing rewards
                        .put("required_crew", 2)
                        .put("flight_days_reduction", 1),
                new JSONObject() // Missing flight_days_reduction
                        .put("required_crew", 2)
                        .put("rewards", new JSONArray().put("GREEN"))
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
                new JSONObject() // required_crew as string
                        .put("required_crew", "three")
                        .put("rewards", new JSONArray().put("RED"))
                        .put("flight_days_reduction", 1),
                new JSONObject() // rewards as object
                        .put("required_crew", 2)
                        .put("rewards", new JSONObject())
                        .put("flight_days_reduction", 1),
                new JSONObject() // flight_days_reduction as array
                        .put("required_crew", 2)
                        .put("rewards", new JSONArray().put("YELLOW"))
                        .put("flight_days_reduction", new JSONArray())
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -10})
    void createCard_shouldThrowIllegalArgumentExceptionForNegativeValues(int negativeValue) {
        JSONObject data = createValidTestData(negativeValue, "/cards/GT-cards_1_abandoned_station_1.png", negativeValue, new JSONArray().put("GREEN"), 1);

        assertThrows(IllegalArgumentException.class,
                () -> creator.createCard(data),
                "Should throw IllegalArgumentException for negative crew value");
    }

    @Test
    void createCard_shouldThrowForNegativeFlightDaysReduction() {
        JSONObject data = createValidTestData(1, "/cards/GT-cards_1_abandoned_station_1.png", 2, new JSONArray().put("BLUE"), -1);

        assertThrows(IllegalArgumentException.class,
                () -> creator.createCard(data),
                "Should throw IllegalArgumentException for negative flight days reduction");
    }

    @Test
    void createCard_shouldAcceptZeroValues() throws Exception {
        JSONArray emptyRewards = new JSONArray().put("RED");
        JSONObject dataWithZeros = createValidTestData(1, "/cards/GT-cards_1_abandoned_station_1.png",0, emptyRewards, 0);

        Card card = assertDoesNotThrow(() -> creator.createCard(dataWithZeros),
                "Should accept zero values");

        assertInstanceOf(AbandonedStationCard.class, card);
        AbandonedStationCard stationCard = (AbandonedStationCard) card;
        assertEquals(0, stationCard.getRequiredCrew());
        assertEquals(0, stationCard.getFlightDaysReduction());
    }

    @ParameterizedTest
    @MethodSource("provideValidPositiveValues")
    void createCard_shouldAcceptValidPositiveValues(int level, int crew, int reduction) throws Exception {
        JSONArray rewards = new JSONArray().put("YELLOW").put("GREEN");
        JSONObject validData = createValidTestData(level, "/cards/GT-cards_1_abandoned_station_1.png", crew, rewards, reduction);

        Card card = creator.createCard(validData);

        assertInstanceOf(AbandonedStationCard.class, card);
        AbandonedStationCard stationCard = (AbandonedStationCard) card;
        assertEquals(level, stationCard.getLevel());
        assertEquals(crew, stationCard.getRequiredCrew());
        assertEquals(reduction, stationCard.getFlightDaysReduction());
        assertEquals(2, stationCard.getRewards().size());
    }

    private static Stream<Arguments> provideValidPositiveValues() {
        return Stream.of(
                Arguments.of(1, 1, 1),
                Arguments.of(2, 5, 2),
                Arguments.of(100, 100, 50),
                Arguments.of(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE)
        );
    }

    @Test
    void createCard_shouldThrowForEmptyRewardsArray() {
        JSONObject data = createValidTestData(1, "/cards/GT-cards_1_abandoned_station_1.png", 2, new JSONArray(), 1);

        assertThrows(JSONException.class,
                () -> creator.createCard(data),
                "Should throw JSONException for empty rewards array");
    }

    @Test
    void createCard_shouldThrowForInvalidRewardColor() {
        JSONArray invalidRewards = new JSONArray().put("RED").put("INVALID");
        JSONObject data = createValidTestData(1, "/cards/GT-cards_1_abandoned_station_1.png", 3, invalidRewards, 1);

        assertThrows(IllegalArgumentException.class,
                () -> creator.createCard(data),
                "Should throw IllegalArgumentException for invalid reward color");
    }
}