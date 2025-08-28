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
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PirateCreatorTest {

    private final PirateCreator creator = new PirateCreator();
    private final String image = "/cards/GT-cards_1_pirate_1.png";

    // Helper method to create valid test data
    private JSONObject createValidPirateData(int level, String image, int power, JSONArray shots, int reward, int reduction) {
        return new JSONObject()
                .put("level", level)
                .put("image", image)
                .put("fire_power_needed", power)
                .put("fire_shots", shots)
                .put("credits_reward", reward)
                .put("flight_days_reduction", reduction);
    }

    @Test
    void createCard_shouldCreateValidPirateCard() throws Exception {
        JSONArray fireShots = new JSONArray()
                .put(new JSONObject().put("type", "BIG").put("direction", "TOP"))
                .put(new JSONObject().put("type", "SMALL").put("direction", "LEFT"));

        JSONObject validData = createValidPirateData(1, image, 5, fireShots, 4, 1);

        Card card = creator.createCard(validData);

        assertInstanceOf(PirateCard.class, card);
        PirateCard pirateCard = (PirateCard) card;

        assertEquals(1, pirateCard.getLevel());
        assertEquals(image, pirateCard.getImage());
        assertEquals(5, pirateCard.getFirePowerNeeded());
        assertEquals(4, pirateCard.getCreditsReward());
        assertEquals(1, pirateCard.getFlightDaysReduction());

        List<FireShot> shots = pirateCard.getFireShots();
        assertEquals(2, shots.size());
        assertEquals(ProblemType.BIG, shots.get(0).getFireShotType());
        assertEquals(Direction.TOP, shots.get(0).getFireShotDirection());
        assertEquals(ProblemType.SMALL, shots.get(1).getFireShotType());
        assertEquals(Direction.LEFT, shots.get(1).getFireShotDirection());
    }

    @Test
    void createCard_shouldThrowForNullData() {
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
        JSONArray sampleShots = new JSONArray()
                .put(new JSONObject().put("type", "BIG").put("direction", "TOP"));

        return Stream.of(
                new JSONObject() // Missing level field
                        .put("fire_power_needed", 5)
                        .put("fire_shots", sampleShots)
                        .put("credits_reward", 4)
                        .put("flight_days_reduction", 1),
                new JSONObject() // Missing fire power field
                        .put("level", 1)
                        .put("fire_shots", sampleShots)
                        .put("credits_reward", 4)
                        .put("flight_days_reduction", 1),
                new JSONObject() // Missing fire_shots
                        .put("level", 1)
                        .put("fire_power_needed", 5)
                        .put("credits_reward", 4)
                        .put("flight_days_reduction", 1),
                new JSONObject() // Missing credits_reward
                        .put("level", 1)
                        .put("fire_power_needed", 5)
                        .put("fire_shots", sampleShots)
                        .put("flight_days_reduction", 1),
                new JSONObject() // Missing flight_days_reduction
                        .put("level", 1)
                        .put("fire_power_needed", 5)
                        .put("fire_shots", sampleShots)
                        .put("credits_reward", 4)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidFieldData")
    void createCard_shouldThrowForInvalidFieldValues(JSONObject invalidData) {
        assertThrows(Exception.class,
                () -> creator.createCard(invalidData),
                "Should throw exception for invalid field values");
    }

    private static Stream<Arguments> provideInvalidFieldData() {
        JSONArray validShots = new JSONArray()
                .put(new JSONObject().put("type", "BIG").put("direction", "TOP"));

        return Stream.of(
                // Negative level power
                Arguments.of(new JSONObject()
                        .put("level", -1)
                        .put("fire_power_needed", 1)
                        .put("fire_shots", validShots)
                        .put("credits_reward", 4)
                        .put("flight_days_reduction", 1)),

                // Negative fire power
                Arguments.of(new JSONObject()
                        .put("level", 1)
                        .put("fire_power_needed", -1)
                        .put("fire_shots", validShots)
                        .put("credits_reward", 4)
                        .put("flight_days_reduction", 1)),

                // Negative credits reward
                Arguments.of(new JSONObject()
                        .put("level", 1)
                        .put("fire_power_needed", 5)
                        .put("fire_shots", validShots)
                        .put("credits_reward", -4)
                        .put("flight_days_reduction", 1)),

                // Negative flight days reduction
                Arguments.of(new JSONObject()
                        .put("level", 1)
                        .put("fire_power_needed", 5)
                        .put("fire_shots", validShots)
                        .put("credits_reward", 4)
                        .put("flight_days_reduction", -1)),

                // Invalid fire shots data
                Arguments.of(new JSONObject()
                        .put("level", 1)
                        .put("fire_power_needed", 5)
                        .put("fire_shots", new JSONArray()
                                .put(new JSONObject().put("type", "INVALID").put("direction", "TOP")))
                        .put("credits_reward", 4)
                        .put("flight_days_reduction", 1))
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 100})
    void createCard_shouldAcceptValidPositiveValues(int value) throws Exception {
        JSONArray shots = new JSONArray()
                .put(new JSONObject().put("type", "SMALL").put("direction", "BOTTOM"));

        JSONObject data = createValidPirateData(value, image, value, shots, value, value);

        assertDoesNotThrow(() -> {
            Card card = creator.createCard(data);
            PirateCard pirateCard = (PirateCard) card;
            assertEquals(value, pirateCard.getFirePowerNeeded());
            assertEquals(value, pirateCard.getCreditsReward());
            assertEquals(value, pirateCard.getFlightDaysReduction());
        }, "Should accept valid positive values");
    }

    @Test
    void createCard_shouldHandleMultipleFireShots() throws Exception {
        JSONArray fireShots = new JSONArray()
                .put(new JSONObject().put("type", "BIG").put("direction", "TOP"))
                .put(new JSONObject().put("type", "SMALL").put("direction", "LEFT"))
                .put(new JSONObject().put("type", "BIG").put("direction", "RIGHT"))
                .put(new JSONObject().put("type", "SMALL").put("direction", "BOTTOM"));

        JSONObject data = createValidPirateData(1, image, 5, fireShots, 4, 1);

        Card card = creator.createCard(data);

        PirateCard pirateCard = (PirateCard) card;
        assertEquals(4, pirateCard.getFireShots().size());
    }

    @Test
    void createCard_shouldPreserveFireShotsOrder() throws Exception {
        JSONArray fireShots = new JSONArray()
                .put(new JSONObject().put("type", "SMALL").put("direction", "LEFT"))
                .put(new JSONObject().put("type", "BIG").put("direction", "TOP"))
                .put(new JSONObject().put("type", "SMALL").put("direction", "RIGHT"));

        JSONObject data = createValidPirateData(1, image, 5, fireShots, 4, 1);

        Card card = creator.createCard(data);

        PirateCard pirateCard = (PirateCard) card;
        List<FireShot> shots = pirateCard.getFireShots();
        assertEquals(ProblemType.SMALL, shots.get(0).getFireShotType());
        assertEquals(ProblemType.BIG, shots.get(1).getFireShotType());
        assertEquals(ProblemType.SMALL, shots.get(2).getFireShotType());
    }
}