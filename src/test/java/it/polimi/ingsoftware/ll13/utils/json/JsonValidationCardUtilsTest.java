package it.polimi.ingsoftware.ll13.utils.json;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.FireShot;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Meteor;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Planet;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.WarZonePenalty;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.*;

import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor;
import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonValidationCardUtilsTest {
    // ~~~~~~~~~~~~~~~~~~~~~~~~ extract_and_validate_cargo_colors ~~~~~~~~~~~~~~~~~~~~~~~~
    @Test
    void extractAndValidateCargoColors_shouldThrowForEmptyArray() {
        assertThrows(JSONException.class,
                () -> JsonValidationCardUtils.extractAndValidateCargoColors(new JSONArray()),
                "Should throw JSONException for empty array");
    }

    @Test
    void extractAndValidateCargoColors_shouldThrowForInvalidColor() {
        JSONArray invalidColors = new JSONArray().put("RED").put("INVALID");
        assertThrows(IllegalArgumentException.class,
                () -> JsonValidationCardUtils.extractAndValidateCargoColors(invalidColors),
                "Should throw IllegalArgumentException for invalid color");
    }

    @Test
    void extractAndValidateCargoColors_shouldReturnCorrectList() {
        JSONArray colors = new JSONArray()
                .put("RED")
                .put("BLUE")
                .put("GREEN")
                .put("YELLOW");

        List<CargoColor> result = assertDoesNotThrow(
                () -> JsonValidationCardUtils.extractAndValidateCargoColors(colors),
                "Should not throw for valid input");

        assertEquals(4, result.size());
        assertEquals(CargoColor.RED, result.get(0));
        assertEquals(CargoColor.BLUE, result.get(1));
        assertEquals(CargoColor.GREEN, result.get(2));
        assertEquals(CargoColor.YELLOW, result.get(3));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ extract_and_validate_meteors ~~~~~~~~~~~~~~~~~~~~~~~~
    @Test
    void extractAndValidateMeteors_shouldThrowForEmptyArray() {
        assertThrows(JSONException.class,
                () -> JsonValidationCardUtils.extractAndValidateMeteors(new JSONArray()),
                "Should throw JSONException for empty array");
    }

    @Test
    void extractAndValidateMeteors_shouldThrowForInvalidType() {
        JSONArray meteors = new JSONArray()
                .put(new JSONObject()
                        .put("type", "INVALID")
                        .put("direction", "TOP"));

        assertThrows(JSONException.class,
                () -> JsonValidationCardUtils.extractAndValidateMeteors(meteors),
                "Should throw IllegalStateException for invalid meteor type");
    }

    @Test
    void extractAndValidateMeteors_shouldReturnCorrectList() {
        JSONArray meteors = new JSONArray()
                .put(new JSONObject().put("type", "BIG").put("direction", "TOP"))
                .put(new JSONObject().put("type", "SMALL").put("direction", "LEFT"));

        List<Meteor> result = assertDoesNotThrow(
                () -> JsonValidationCardUtils.extractAndValidateMeteors(meteors),
                "Should not throw for valid input");

        assertEquals(2, result.size());
        assertEquals(ProblemType.BIG, result.get(0).getMeteorType());
        assertEquals(Direction.TOP, result.get(0).getMeteorDirection());
        assertEquals(ProblemType.SMALL, result.get(1).getMeteorType());
        assertEquals(Direction.LEFT, result.get(1).getMeteorDirection());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ extract_and_validate_fire_shots ~~~~~~~~~~~~~~~~~~~~~~~~
    @Test
    void extractAndValidateFireShots_shouldThrowForEmptyArray() {
        assertThrows(JSONException.class,
                () -> JsonValidationCardUtils.extractAndValidateFireShots(new JSONArray()),
                "Should throw JSONException for empty array");
    }

    @Test
    void extractAndValidateFireShots_shouldReturnCorrectList() {
        JSONArray fireShots = new JSONArray()
                .put(new JSONObject().put("type", "BIG").put("direction", "BOTTOM"))
                .put(new JSONObject().put("type", "SMALL").put("direction", "RIGHT"));

        List<FireShot> result = assertDoesNotThrow(
                () -> JsonValidationCardUtils.extractAndValidateFireShots(fireShots),
                "Should not throw for valid input");

        assertEquals(2, result.size());
        assertEquals(ProblemType.BIG, result.get(0).getFireShotType());
        assertEquals(Direction.BOTTOM, result.get(0).getFireShotDirection());
        assertEquals(ProblemType.SMALL, result.get(1).getFireShotType());
        assertEquals(Direction.RIGHT, result.get(1).getFireShotDirection());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ extract_and_validate_planets ~~~~~~~~~~~~~~~~~~~~~~~~
    @Test
    void extractAndValidatePlanets_shouldThrowForEmptyArray() {
        assertThrows(JSONException.class,
                () -> JsonValidationCardUtils.extractAndValidatePlanets(new JSONArray()),
                "Should throw JSONException for empty array");
    }

    @Test
    void extractAndValidatePlanets_shouldReturnCorrectList() {
        JSONArray planets = new JSONArray()
                .put(new JSONObject()
                        .put("cargos", new JSONArray().put("RED").put("BLUE")))
                .put(new JSONObject()
                        .put("cargos", new JSONArray().put("GREEN")));

        List<Planet> result = assertDoesNotThrow(
                () -> JsonValidationCardUtils.extractAndValidatePlanets(planets),
                "Should not throw for valid input");

        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getCargos().size());
        assertEquals(1, result.get(1).getCargos().size());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ extract_and_validate_war_zone_penalties ~~~~~~~~~~~~~~~~~~~~~~~~
    @Test
    void extractAndValidateWarZonePenalties_shouldHandleNumericPenalty() {
        JSONArray penalties = new JSONArray()
                .put(new JSONObject()
                        .put("target", "LOWEST_CREW")
                        .put("effect", "MOVE_BACK")
                        .put("value", 3));

        List<WarZonePenalty> result = assertDoesNotThrow(
                () -> JsonValidationCardUtils.extractAndValidateWarZonePenalties(penalties),
                "Should not throw for valid numeric penalty");

        assertEquals(1, result.size());
        assertEquals(WarZonePenaltyType.LOWEST_CREW, result.get(0).getWarZonePenaltyType());
        assertEquals(WarZonePenaltyEffect.MOVE_BACK, result.get(0).getWarZonePenaltyEffect());
        assertEquals(3, result.get(0).getValue());
    }

    @Test
    void extractAndValidateWarZonePenalties_shouldHandleFireShotsPenalty() {
        JSONArray penalties = new JSONArray()
                .put(new JSONObject()
                        .put("target", "LOWEST_FIREPOWER")
                        .put("effect", "HIT_BY_PROJECTILES")
                        .put("fire_shots", new JSONArray()
                                .put(new JSONObject().put("type", "BIG").put("direction", "BOTTOM"))));

        List<WarZonePenalty> result = assertDoesNotThrow(
                () -> JsonValidationCardUtils.extractAndValidateWarZonePenalties(penalties),
                "Should not throw for valid fire shots penalty");

        assertEquals(1, result.size());
        assertEquals(WarZonePenaltyType.LOWEST_FIREPOWER, result.get(0).getWarZonePenaltyType());
        assertEquals(WarZonePenaltyEffect.HIT_BY_PROJECTILES, result.get(0).getWarZonePenaltyEffect());
        assertEquals(1, result.get(0).getFireShots().size());
    }

    // Parameterized tests for invalid inputs
    @ParameterizedTest
    @MethodSource("provideInvalidWarZonePenalties")
    void extractAndValidateWarZonePenalties_shouldThrowForInvalidInputs(JSONObject invalidPenalty) {
        JSONArray penalties = new JSONArray().put(invalidPenalty);
        assertThrows(Exception.class,
                () -> JsonValidationCardUtils.extractAndValidateWarZonePenalties(penalties),
                "Should throw for invalid penalty data");
    }

    private static Stream<Arguments> provideInvalidWarZonePenalties() {
        return Stream.of(
                Arguments.of(new JSONObject() // Missing target
                        .put("effect", "MOVE_BACK")
                        .put("value", 3)),
                Arguments.of(new JSONObject() // Invalid target
                        .put("target", "INVALID")
                        .put("effect", "MOVE_BACK")
                        .put("value", 3)),
                Arguments.of(new JSONObject() // Missing effect
                        .put("target", "LOWEST_CREW")
                        .put("value", 3)),
                Arguments.of(new JSONObject() // Missing both value and fire_shots
                        .put("target", "LOWEST_CREW")
                        .put("effect", "MOVE_BACK"))
        );
    }
}