package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.cards.enumerations.WarZonePenaltyEffect;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.WarZonePenaltyType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class WarZoneCreatorTest {

    private final WarZoneCreator creator = new WarZoneCreator();

    // Helper method to create valid penalty data
    private JSONObject createValidWarZoneData(JSONArray penalties) {
        String image = "/cards/GT-cards_1_war_zone_1.png";
        return new JSONObject()
                .put("level", 1)
                .put("image", image)
                .put("penalties", penalties);
    }

    @Test
    void createCard_shouldCreateValidWarZoneCard() throws Exception {
        JSONArray penalties = new JSONArray()
                .put(new JSONObject()
                        .put("target", "LOWEST_CREW")
                        .put("effect", "MOVE_BACK")
                        .put("value", 3))
                .put(new JSONObject()
                        .put("target", "LOWEST_FIREPOWER")
                        .put("effect", "HIT_BY_PROJECTILES")
                        .put("fire_shots", new JSONArray()
                                .put(new JSONObject()
                                        .put("type", "BIG")
                                        .put("direction", "TOP"))));

        JSONObject data = createValidWarZoneData(penalties);

        Card card = creator.createCard(data);

        assertInstanceOf(WarZoneCard.class, card);
        WarZoneCard warZoneCard = (WarZoneCard) card;
        assertEquals(1, warZoneCard.getLevel());

        List<WarZonePenalty> penaltyList = warZoneCard.getPenalties();
        assertEquals(2, penaltyList.size());

        // Verify first penalty (numeric)
        assertEquals(WarZonePenaltyType.LOWEST_CREW, penaltyList.getFirst().getWarZonePenaltyType());
        assertEquals(WarZonePenaltyEffect.MOVE_BACK, penaltyList.getFirst().getWarZonePenaltyEffect());
        assertEquals(3, penaltyList.get(0).getValue());
        assertNull(penaltyList.get(0).getFireShots());

        // Verify second penalty (fire shots)
        assertEquals(WarZonePenaltyType.LOWEST_FIREPOWER, penaltyList.get(1).getWarZonePenaltyType());
        assertEquals(WarZonePenaltyEffect.HIT_BY_PROJECTILES, penaltyList.get(1).getWarZonePenaltyEffect());
        assertEquals(1, penaltyList.get(1).getFireShots().size());
    }

    @Test
    void createCard_shouldThrowForNullData() {
        assertThrows(IllegalArgumentException.class,
                () -> creator.createCard(null),
                "Should throw IllegalArgumentException for null data");
    }

    @Test
    void createCard_shouldThrowForMissingPenaltiesField() {
        JSONObject data = new JSONObject(); // No penalties field
        assertThrows(JSONException.class,
                () -> creator.createCard(data),
                "Should throw JSONException for missing penalties field");
    }

    @Test
    void createCard_shouldThrowForEmptyPenaltiesArray() {
        JSONObject data = createValidWarZoneData(new JSONArray());
        assertThrows(JSONException.class,
                () -> creator.createCard(data),
                "Should throw for empty penalties array");
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPenaltyData")
    void createCard_shouldThrowForInvalidPenaltyData(JSONObject invalidPenalty) {
        JSONArray penalties = new JSONArray().put(invalidPenalty);
        JSONObject data = createValidWarZoneData(penalties);

        assertThrows(Exception.class,
                () -> creator.createCard(data),
                "Should throw exception for invalid penalty data");
    }

    private static Stream<Arguments> provideInvalidPenaltyData() {
        return Stream.of(
                // Missing target
                Arguments.of(new JSONObject()
                        .put("effect", "MOVE_BACK")
                        .put("value", 3)),

                // Missing effect
                Arguments.of(new JSONObject()
                        .put("target", "LOWEST_CREW")
                        .put("value", 3)),

                // Invalid target
                Arguments.of(new JSONObject()
                        .put("target", "INVALID_TARGET")
                        .put("effect", "MOVE_BACK")
                        .put("value", 3)),

                // Invalid effect
                Arguments.of(new JSONObject()
                        .put("target", "LOWEST_CREW")
                        .put("effect", "INVALID_EFFECT")
                        .put("value", 3)),

                // Missing both value and fire_shots
                Arguments.of(new JSONObject()
                        .put("target", "LOWEST_CREW")
                        .put("effect", "MOVE_BACK"))
        );
    }

    @Test
    void createCard_shouldHandleMixedPenaltyTypes() throws Exception {
        JSONArray penalties = new JSONArray()
                .put(new JSONObject()
                        .put("target", "LOWEST_THRUST")
                        .put("effect", "LOSE_CREW")
                        .put("value", 2))
                .put(new JSONObject()
                        .put("target", "LOWEST_FIREPOWER")
                        .put("effect", "HIT_BY_PROJECTILES")
                        .put("fire_shots", new JSONArray()
                                .put(new JSONObject()
                                        .put("type", "SMALL")
                                        .put("direction", "LEFT"))));

        JSONObject data = createValidWarZoneData(penalties);

        Card card = creator.createCard(data);

        WarZoneCard warZoneCard = (WarZoneCard) card;
        assertEquals(2, warZoneCard.getPenalties().size());
        assertNotNull(warZoneCard.getPenalties().get(1).getFireShots());
    }

    @Test
    void createCard_shouldPreservePenaltyOrder() throws Exception {
        JSONArray penalties = new JSONArray()
                .put(new JSONObject()
                        .put("target", "LOWEST_THRUST")
                        .put("effect", "MOVE_BACK")
                        .put("value", 1))
                .put(new JSONObject()
                        .put("target", "LOWEST_CREW")
                        .put("effect", "LOSE_CREW")
                        .put("value", 2));

        JSONObject data = createValidWarZoneData(penalties);

        Card card = creator.createCard(data);

        WarZoneCard warZoneCard = (WarZoneCard) card;
        List<WarZonePenalty> penaltyList = warZoneCard.getPenalties();
        assertEquals("LOWEST_THRUST", penaltyList.get(0).getWarZonePenaltyType().name());
        assertEquals("LOWEST_CREW", penaltyList.get(1).getWarZonePenaltyType().name());
    }

    @Test
    void createCard_shouldReturnNewInstanceEachTime() throws Exception {
        JSONArray penalties1 = new JSONArray()
                .put(new JSONObject()
                        .put("target", "LOWEST_CREW")
                        .put("effect", "MOVE_BACK")
                        .put("value", 3));

        JSONArray penalties2 = new JSONArray()
                .put(new JSONObject()
                        .put("target", "LOWEST_FIREPOWER")
                        .put("effect", "HIT_BY_PROJECTILES")
                        .put("fire_shots", new JSONArray()
                                .put(new JSONObject()
                                        .put("type", "BIG")
                                        .put("direction", "TOP"))));

        JSONObject data1 = createValidWarZoneData(penalties1);
        JSONObject data2 = createValidWarZoneData(penalties2);

        Card card1 = creator.createCard(data1);
        Card card2 = creator.createCard(data2);

        assertNotSame(card1, card2, "Should return new instance for each call");
    }
}