package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SlaversCreatorTest {

    private final SlaversCreator creator = new SlaversCreator();
    private final String image = "/cards/GT-cards_1_slavers_1.png";

    // Helper method to create valid test data
    private JSONObject createValidSlaversData(int level, int power, int sacrifice, int reward, int reduction) {
        return new JSONObject()
                .put("level", level)
                .put("image", image)
                .put("fire_power_needed", power)
                .put("crew_sacrifice", sacrifice)
                .put("credits_reward", reward)
                .put("flight_days_reduction", reduction);
    }

    @Test
    void createCard_shouldCreateValidSlaversCard() throws Exception {
        JSONObject validData = createValidSlaversData(1,6, 3, 5, 1);

        Card card = creator.createCard(validData);

        assertInstanceOf(SlaversCard.class, card);
        SlaversCard slaversCard = (SlaversCard) card;
        assertEquals(1, slaversCard.getLevel());
        assertEquals(image, slaversCard.getImage());
        assertEquals(6, slaversCard.getFirePowerNeeded());
        assertEquals(3, slaversCard.getCrewSacrifice());
        assertEquals(5, slaversCard.getCreditsReward());
        assertEquals(1, slaversCard.getFlightDaysReduction());
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
        return Stream.of(
                // Missing fire_power_needed
                new JSONObject()
                        .put("crew_sacrifice", 3)
                        .put("credits_reward", 5)
                        .put("flight_days_reduction", 1),
                // Missing crew_sacrifice
                new JSONObject()
                        .put("fire_power_needed", 6)
                        .put("credits_reward", 5)
                        .put("flight_days_reduction", 1),
                // Missing credits_reward
                new JSONObject()
                        .put("fire_power_needed", 6)
                        .put("crew_sacrifice", 3)
                        .put("flight_days_reduction", 1),
                // Missing flight_days_reduction
                new JSONObject()
                        .put("fire_power_needed", 6)
                        .put("crew_sacrifice", 3)
                        .put("credits_reward", 5)
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
        return Stream.of(
                // Negative fire power
                Arguments.of(new JSONObject()
                        .put("fire_power_needed", -1)
                        .put("crew_sacrifice", 3)
                        .put("credits_reward", 5)
                        .put("flight_days_reduction", 1)),
                // Negative crew sacrifice
                Arguments.of(new JSONObject()
                        .put("fire_power_needed", 6)
                        .put("crew_sacrifice", -3)
                        .put("credits_reward", 5)
                        .put("flight_days_reduction", 1)),
                // Negative credits reward
                Arguments.of(new JSONObject()
                        .put("fire_power_needed", 6)
                        .put("crew_sacrifice", 3)
                        .put("credits_reward", -5)
                        .put("flight_days_reduction", 1)),
                // Negative flight days reduction
                Arguments.of(new JSONObject()
                        .put("fire_power_needed", 6)
                        .put("crew_sacrifice", 3)
                        .put("credits_reward", 5)
                        .put("flight_days_reduction", -1)),
                // Non-integer fire power
                Arguments.of(new JSONObject()
                        .put("fire_power_needed", "six")
                        .put("crew_sacrifice", 3)
                        .put("credits_reward", 5)
                        .put("flight_days_reduction", 1))
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 10, 100})
    void createCard_shouldAcceptValidPositiveValues(int value) throws Exception {
        JSONObject data = createValidSlaversData(value, value, value, value, value);

        assertDoesNotThrow(() -> {
            Card card = creator.createCard(data);
            SlaversCard slaversCard = (SlaversCard) card;
            assertEquals(value, slaversCard.getLevel());
            assertEquals(value, slaversCard.getFirePowerNeeded());
            assertEquals(value, slaversCard.getCrewSacrifice());
            assertEquals(value, slaversCard.getCreditsReward());
            assertEquals(value, slaversCard.getFlightDaysReduction());
        }, "Should accept valid positive values");
    }

    @Test
    void createCard_shouldHandleMaximumIntegerValues() throws Exception {
        int maxValue = Integer.MAX_VALUE;
        JSONObject data = createValidSlaversData(maxValue, maxValue, maxValue, maxValue, maxValue);

        Card card = creator.createCard(data);

        SlaversCard slaversCard = (SlaversCard) card;
        assertEquals(maxValue, slaversCard.getLevel());
        assertEquals(maxValue, slaversCard.getFirePowerNeeded());
        assertEquals(maxValue, slaversCard.getCrewSacrifice());
        assertEquals(maxValue, slaversCard.getCreditsReward());
        assertEquals(maxValue, slaversCard.getFlightDaysReduction());
    }

    @Test
    void createCard_shouldHandleZeroValues() throws Exception {
        JSONObject data = createValidSlaversData(0,0, 0, 0, 0);

        Card card = creator.createCard(data);

        SlaversCard slaversCard = (SlaversCard) card;
        assertEquals(0, slaversCard.getLevel());
        assertEquals(0, slaversCard.getFirePowerNeeded());
        assertEquals(0, slaversCard.getCrewSacrifice());
        assertEquals(0, slaversCard.getCreditsReward());
        assertEquals(0, slaversCard.getFlightDaysReduction());
    }

    @Test
    void createCard_shouldReturnNewInstanceEachTime() throws Exception {
        JSONObject data1 = createValidSlaversData(1,5, 2, 4, 1);
        JSONObject data2 = createValidSlaversData(1,6, 3, 5, 2);

        Card card1 = creator.createCard(data1);
        Card card2 = creator.createCard(data2);

        assertNotSame(card1, card2, "Should return new instance for everyone");
    }
}