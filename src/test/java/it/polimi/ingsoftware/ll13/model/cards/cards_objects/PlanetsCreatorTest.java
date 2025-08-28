package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlanetsCreatorTest {

    private final PlanetsCreator creator = new PlanetsCreator();
    private final String image = "/cards/GT-cards_1_planets_1.png";

    // Helper method to create valid test data
    private JSONObject createValidPlanetsData(int level, String image, JSONArray planets, int reduction) {
        return new JSONObject()
                .put("level", level)
                .put("image", image)
                .put("planets", planets)
                .put("flight_days_reduction", reduction);
    }

    @Test
    void createCard_shouldCreateValidPlanetsCard() throws Exception {
        JSONArray planets = new JSONArray()
                .put(new JSONObject().put("cargos", new JSONArray().put("RED").put("BLUE")))
                .put(new JSONObject().put("cargos", new JSONArray().put("GREEN")));

        JSONObject validData = createValidPlanetsData(1, image, planets, 2);

        Card card = creator.createCard(validData);

        assertInstanceOf(PlanetsCard.class, card);
        PlanetsCard planetsCard = (PlanetsCard) card;

        assertEquals(1, planetsCard.getLevel());
        assertEquals(image, planetsCard.getImage());
        assertEquals(2, planetsCard.getFlightDaysReduction());

        List<Planet> planetList = planetsCard.getPlanets();
        assertEquals(2, planetList.size());
        assertEquals(2, planetList.get(0).getCargos().size());
        assertEquals(1, planetList.get(1).getCargos().size());
        assertEquals(CargoColor.RED, planetList.get(0).getCargos().get(0));
        assertEquals(CargoColor.BLUE, planetList.get(0).getCargos().get(1));
        assertEquals(CargoColor.GREEN, planetList.get(1).getCargos().get(0));
    }

    @Test
    void createCard_shouldThrowForNullData() {
        assertThrows(IllegalArgumentException.class,
                () -> creator.createCard(null),
                "Should throw IllegalArgumentException for null data");
    }

    @Test
    void createCard_shouldThrowForMissingLevelField() {
        JSONArray planets = new JSONArray()
                .put(new JSONObject().put("cargos", new JSONArray().put("YELLOW")));
        JSONObject data = new JSONObject()
                .put("planets", planets)
                .put("flight_days_reduction", 1);
        assertThrows(JSONException.class,
                () -> creator.createCard(data),
                "Should throw JSONException for missing planets field");
    }

    @Test
    void createCard_shouldThrowForMissingPlanetsField() {
        JSONObject data = new JSONObject()
                .put("level", 1)
                .put("flight_days_reduction", 1);
        assertThrows(JSONException.class,
                () -> creator.createCard(data),
                "Should throw JSONException for missing planets field");
    }

    @Test
    void createCard_shouldThrowForMissingFlightDaysField() {
        JSONArray planets = new JSONArray()
                .put(new JSONObject().put("cargos", new JSONArray().put("YELLOW")));
        JSONObject data = new JSONObject()
                .put("level", 1)
                .put("planets", planets);

        assertThrows(JSONException.class,
                () -> creator.createCard(data),
                "Should throw JSONException for missing flight_days_reduction field");
    }

    @Test
    void createCard_shouldThrowForEmptyPlanetsArray() {
        JSONObject data = createValidPlanetsData(1, image, new JSONArray(), 1);
        assertThrows(JSONException.class,
                () -> creator.createCard(data),
                "Should throw JSONException for empty planets array");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -100})
    void createCard_shouldThrowForInvalidLevels(int level) {
        JSONArray planets = new JSONArray()
                .put(new JSONObject().put("cargos", new JSONArray().put("RED")));
        JSONObject data = createValidPlanetsData(level, image, planets, 1);

        assertThrows(IllegalArgumentException.class,
                () -> creator.createCard(data),
                "Should throw IllegalArgumentException for negative flight days reduction");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -100})
    void createCard_shouldThrowForInvalidFlightDaysReduction(int invalidReduction) {
        JSONArray planets = new JSONArray()
                .put(new JSONObject().put("cargos", new JSONArray().put("RED")));
        JSONObject data = createValidPlanetsData(1, image, planets, invalidReduction);

        assertThrows(IllegalArgumentException.class,
                () -> creator.createCard(data),
                "Should throw IllegalArgumentException for negative flight days reduction");
    }

    @Test
    void createCard_shouldHandleMultiplePlanets() throws Exception {
        JSONArray planets = new JSONArray()
                .put(new JSONObject().put("cargos", new JSONArray().put("RED")))
                .put(new JSONObject().put("cargos", new JSONArray().put("YELLOW").put("GREEN")))
                .put(new JSONObject().put("cargos", new JSONArray().put("BLUE").put("BLUE").put("RED")));

        JSONObject data = createValidPlanetsData(1, image, planets, 3);

        Card card = creator.createCard(data);

        PlanetsCard planetsCard = (PlanetsCard) card;
        assertEquals(3, planetsCard.getPlanets().size());
        assertEquals(1, planetsCard.getPlanets().get(0).getCargos().size());
        assertEquals(2, planetsCard.getPlanets().get(1).getCargos().size());
        assertEquals(3, planetsCard.getPlanets().get(2).getCargos().size());
    }

    @Test
    void createCard_shouldThrowForEmptyCargos() {
        JSONArray planets = new JSONArray()
                .put(new JSONObject().put("cargos", new JSONArray()));

        JSONObject data = createValidPlanetsData(1, image, planets, 1);

        assertThrows(JSONException.class,
                () -> creator.createCard(data),
                "Should throw for empty cargos array");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 10, 100})
    void createCard_shouldAcceptValidLevels(int level) throws Exception {
        JSONArray planets = new JSONArray()
                .put(new JSONObject().put("cargos", new JSONArray().put("GREEN")));

        JSONObject data = createValidPlanetsData(level, image, planets, 2);

        assertDoesNotThrow(() -> {
            Card card = creator.createCard(data);
            PlanetsCard planetsCard = (PlanetsCard) card;
            assertEquals(level, planetsCard.getLevel());
        }, "Should accept valid flight days reduction values");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 10, 100})
    void createCard_shouldAcceptValidFlightDaysReduction(int reduction) throws Exception {
        JSONArray planets = new JSONArray()
                .put(new JSONObject().put("cargos", new JSONArray().put("GREEN")));

        JSONObject data = createValidPlanetsData(1, image, planets, reduction);

        assertDoesNotThrow(() -> {
            Card card = creator.createCard(data);
            PlanetsCard planetsCard = (PlanetsCard) card;
            assertEquals(reduction, planetsCard.getFlightDaysReduction());
        }, "Should accept valid flight days reduction values");
    }

    @Test
    void createCard_shouldPreservePlanetOrder() throws Exception {
        JSONArray planets = new JSONArray()
                .put(new JSONObject().put("cargos", new JSONArray().put("RED")))
                .put(new JSONObject().put("cargos", new JSONArray().put("YELLOW")))
                .put(new JSONObject().put("cargos", new JSONArray().put("GREEN")));

        JSONObject data = createValidPlanetsData(1, image, planets, 2);

        Card card = creator.createCard(data);

        PlanetsCard planetsCard = (PlanetsCard) card;
        List<Planet> planetList = planetsCard.getPlanets();
        assertEquals(CargoColor.RED, planetList.get(0).getCargos().getFirst());
        assertEquals(CargoColor.YELLOW, planetList.get(1).getCargos().getFirst());
        assertEquals(CargoColor.GREEN, planetList.get(2).getCargos().getFirst());
    }
}