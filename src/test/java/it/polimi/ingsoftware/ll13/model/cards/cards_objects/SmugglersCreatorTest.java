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

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SmugglersCreatorTest {

    private final SmugglersCreator creator = new SmugglersCreator();
    private final String image = "/cards/GT-cards_1_slavers_1.png";

    // Helper method to create valid test data
    private JSONObject createValidSmugglersData(int level, int power, int penalty, JSONArray rewards, int reduction) {
        return new JSONObject()
                .put("level", level)
                .put("image", image)
                .put("fire_power_needed", power)
                .put("cargo_penalty", penalty)
                .put("rewards", rewards)
                .put("flight_days_reduction", reduction);
    }

    @Test
    void createCard_shouldCreateValidSmugglersCard() throws Exception {
        JSONArray rewards = new JSONArray().put("RED").put("BLUE").put("GREEN");
        JSONObject validData = createValidSmugglersData(1, 4, 2, rewards, 1);

        Card card = creator.createCard(validData);

        assertInstanceOf(SmugglersCard.class, card);
        SmugglersCard smugglersCard = (SmugglersCard) card;

        assertEquals(1, smugglersCard.getLevel());
        assertEquals(4, smugglersCard.getFirePowerNeeded());
        assertEquals(2, smugglersCard.getCargoPenalty());
        assertEquals(1, smugglersCard.getFlightDaysReduction());

        List<CargoColor> rewardColors = smugglersCard.getRewards();
        assertEquals(3, rewardColors.size());
        assertEquals(CargoColor.RED, rewardColors.get(0));
        assertEquals(CargoColor.BLUE, rewardColors.get(1));
        assertEquals(CargoColor.GREEN, rewardColors.get(2));
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
        JSONArray sampleRewards = new JSONArray().put("YELLOW");

        return Stream.of(
                // Missing level
                new JSONObject()
                        .put("image", "/cards/GT-cards_1_slavers_1.png")
                        .put("fire_power_needed", 4)
                        .put("cargo_penalty", 2)
                        .put("rewards", sampleRewards)
                        .put("flight_days_reduction", 1),
                // Missing fire_power_needed
                new JSONObject()
                        .put("level", 1)
                        .put("image", "/cards/GT-cards_1_slavers_1.png")
                        .put("cargo_penalty", 2)
                        .put("rewards", sampleRewards)
                        .put("flight_days_reduction", 1),
                // Missing cargo_penalty
                new JSONObject()
                        .put("level", 1)
                        .put("image", "/cards/GT-cards_1_slavers_1.png")
                        .put("fire_power_needed", 4)
                        .put("rewards", sampleRewards)
                        .put("flight_days_reduction", 1),
                // Missing rewards
                new JSONObject()
                        .put("level", 1)
                        .put("image", "/cards/GT-cards_1_slavers_1.png")
                        .put("fire_power_needed", 4)
                        .put("cargo_penalty", 2)
                        .put("flight_days_reduction", 1),
                // Missing flight_days_reduction
                new JSONObject()
                        .put("level", 1)
                        .put("image", "/cards/GT-cards_1_slavers_1.png")
                        .put("fire_power_needed", 4)
                        .put("cargo_penalty", 2)
                        .put("rewards", sampleRewards)
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
        JSONArray validRewards = new JSONArray().put("RED");

        return Stream.of(
                // Negative level
                Arguments.of(new JSONObject()
                        .put("level", -1)
                        .put("image", "/cards/GT-cards_1_slavers_1.png")
                        .put("fire_power_needed", 4)
                        .put("cargo_penalty", 2)
                        .put("rewards", validRewards)
                        .put("flight_days_reduction", 1)),
                // Negative fire power
                Arguments.of(new JSONObject()
                        .put("level", 1)
                        .put("image", "/cards/GT-cards_1_slavers_1.png")
                        .put("fire_power_needed", -4)
                        .put("cargo_penalty", 2)
                        .put("rewards", validRewards)
                        .put("flight_days_reduction", 1)),
                // Negative cargo penalty
                Arguments.of(new JSONObject()
                        .put("level", 1)
                        .put("image", "/cards/GT-cards_1_slavers_1.png")
                        .put("fire_power_needed", 4)
                        .put("cargo_penalty", -2)
                        .put("rewards", validRewards)
                        .put("flight_days_reduction", 1)),
                // Negative flight days reduction
                Arguments.of(new JSONObject()
                        .put("level", 1)
                        .put("image", "/cards/GT-cards_1_slavers_1.png")
                        .put("fire_power_needed", 4)
                        .put("cargo_penalty", 2)
                        .put("rewards", validRewards)
                        .put("flight_days_reduction", -1)),
                // Invalid rewards color
                Arguments.of(new JSONObject()
                        .put("level", 1)
                        .put("image", "/cards/GT-cards_1_slavers_1.png")
                        .put("fire_power_needed", 4)
                        .put("cargo_penalty", 2)
                        .put("rewards", new JSONArray().put("INVALID"))
                        .put("flight_days_reduction", 1)),
                // Non-integer fire power
                Arguments.of(new JSONObject()
                        .put("level", 1)
                        .put("image", "/cards/GT-cards_1_slavers_1.png")
                        .put("fire_power_needed", "four")
                        .put("cargo_penalty", 2)
                        .put("rewards", validRewards)
                        .put("flight_days_reduction", 1))
        );
    }

    @Test
    void createCard_shouldHandleEmptyRewardsArray() {
        JSONObject data = createValidSmugglersData(1, 4, 2, new JSONArray(), 1);

        assertThrows(JSONException.class,
                () -> creator.createCard(data),
                "Should throw JSONException for empty rewards array");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 10, 100})
    void createCard_shouldAcceptValidPositiveValues(int value) throws Exception {
        JSONArray rewards = new JSONArray().put("YELLOW");
        JSONObject data = createValidSmugglersData(value, value, value, rewards, value);

        assertDoesNotThrow(() -> {
            Card card = creator.createCard(data);
            SmugglersCard smugglersCard = (SmugglersCard) card;
            assertEquals(value, smugglersCard.getFirePowerNeeded());
            assertEquals(value, smugglersCard.getCargoPenalty());
            assertEquals(value, smugglersCard.getFlightDaysReduction());
        }, "Should accept valid positive values");
    }

    @Test
    void createCard_shouldHandleMultipleRewardColors() throws Exception {
        JSONArray rewards = new JSONArray()
                .put("RED")
                .put("YELLOW")
                .put("GREEN")
                .put("BLUE");
        JSONObject data = createValidSmugglersData(1, 4, 2, rewards, 1);

        Card card = creator.createCard(data);

        SmugglersCard smugglersCard = (SmugglersCard) card;
        List<CargoColor> rewardColors = smugglersCard.getRewards();
        assertEquals(4, rewardColors.size());
        assertEquals(CargoColor.RED, rewardColors.get(0));
        assertEquals(CargoColor.YELLOW, rewardColors.get(1));
        assertEquals(CargoColor.GREEN, rewardColors.get(2));
        assertEquals(CargoColor.BLUE, rewardColors.get(3));
    }

    @Test
    void createCard_shouldPreserveRewardsOrder() throws Exception {
        JSONArray rewards = new JSONArray()
                .put("BLUE")
                .put("RED")
                .put("GREEN");
        JSONObject data = createValidSmugglersData(1, 4, 2, rewards, 1);

        Card card = creator.createCard(data);

        SmugglersCard smugglersCard = (SmugglersCard) card;
        List<CargoColor> rewardColors = smugglersCard.getRewards();
        assertEquals(CargoColor.BLUE, rewardColors.get(0));
        assertEquals(CargoColor.RED, rewardColors.get(1));
        assertEquals(CargoColor.GREEN, rewardColors.get(2));
    }

    @Test
    void createCard_shouldReturnNewInstanceEachTime() throws Exception {
        JSONArray rewards1 = new JSONArray().put("RED");
        JSONArray rewards2 = new JSONArray().put("BLUE");
        JSONObject data1 = createValidSmugglersData(1, 4, 2, rewards1, 1);
        JSONObject data2 = createValidSmugglersData(1, 5, 3, rewards2, 2);

        Card card1 = creator.createCard(data1);
        Card card2 = creator.createCard(data2);

        assertNotSame(card1, card2, "Should return new instance for each call");
    }
}