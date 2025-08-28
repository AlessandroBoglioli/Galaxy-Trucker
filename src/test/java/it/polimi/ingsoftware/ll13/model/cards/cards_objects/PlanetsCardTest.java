package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PlanetsCardTest {

    private PlanetsCard planetsCard;
    private List<Planet> planets;
    private String image;

    @BeforeEach
    void setUp() {
        image = "/cards/GT-cards_1_planets_1.png";
        planets = Arrays.asList(
            new Planet(Arrays.asList(CargoColor.RED, CargoColor.BLUE)),
            new Planet(List.of(CargoColor.GREEN))
        );
        planetsCard = new PlanetsCard(1, image, planets, 2);
    }

    @Test
    void testConstructor() {
        assertNotNull(planetsCard);
        assertEquals("Planets", planetsCard.getName());
        assertEquals(1, planetsCard.getLevel());
        assertEquals(image, planetsCard.getImage());
        assertEquals(planets, planetsCard.getPlanets());
        assertEquals(2, planetsCard.getFlightDaysReduction());
    }

    @Test
    void testGetLevel() {
        assertEquals(1, planetsCard.getLevel());
    }

    @Test
    void testGetImage() {
        assertEquals(image, planetsCard.getImage());
    }

    @Test
    void testGetPlanets() {
        assertEquals(planets, planetsCard.getPlanets());
    }

    @Test
    void testGetFlightDaysReduction() {
        assertEquals(2, planetsCard.getFlightDaysReduction());
    }

    // TODO: apply effect

}