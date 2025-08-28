package it.polimi.ingsoftware.ll13.model.json_parser;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.*;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CardParserTest {

    private final CardParser cardParser = new CardParser();

//    @Test
//    void parseCards_shouldParseAllCardsFromLevel2File() throws Exception {
//        // Found online that meaven add automatically /src/main/resources/... to the path file
//        InputStream is = getClass().getResourceAsStream("/json/cards_level2.json");
//        assertNotNull(is, "Test file not found");
//
//        List<Card> cards = cardParser.parseCards(is);
//
//        assertEquals(20, cards.size(), "Should parse all 20 cards from the file");
//
//        // Verify card types distribution
//        long abandonedShipCount = cards.stream().filter(c -> c instanceof AbandonedShipCard).count();
//        long abandonedStationCount = cards.stream().filter(c -> c instanceof AbandonedStationCard).count();
//        long epidemicCount = cards.stream().filter(c -> c instanceof EpidemicCard).count();
//        long meteorShowerCount = cards.stream().filter(c -> c instanceof MeteorShowerCard).count();
//        long openSpaceCount = cards.stream().filter(c -> c instanceof OpenSpaceCard).count();
//        long pirateCount = cards.stream().filter(c -> c instanceof PirateCard).count();
//        long planetsCount = cards.stream().filter(c -> c instanceof PlanetsCard).count();
//        long slaversCount = cards.stream().filter(c -> c instanceof SlaversCard).count();
//        long smugglersCount = cards.stream().filter(c -> c instanceof SmugglersCard).count();
//        long stellarSpacedustCount = cards.stream().filter(c -> c instanceof StellarSpaceDustCard).count();
//        long warZoneCount = cards.stream().filter(c -> c instanceof WarZoneCard).count();
//
//        assertEquals(2, abandonedShipCount);
//        assertEquals(2, abandonedStationCount);
//        assertEquals(1, epidemicCount);
//        assertEquals(3, meteorShowerCount);
//        assertEquals(3, openSpaceCount);
//        assertEquals(1, pirateCount);
//        assertEquals(4, planetsCount);
//        assertEquals(1, slaversCount);
//        assertEquals(1, smugglersCount);
//        assertEquals(1, stellarSpacedustCount);
//        assertEquals(1, warZoneCount);
//    }

    @Test
    void parseCards_shouldCreateCorrectAbandonedShipCards() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/json/cards_level2.json");

        List<Card> cards = cardParser.parseCards(inputStream);
        List<AbandonedShipCard> shipCards = cards.stream()
                .filter(c -> c instanceof AbandonedShipCard)
                .map(c -> (AbandonedShipCard) c)
                .toList();

        assertEquals(2, shipCards.size());

        // First AbandonedShip card
        assertEquals(4, shipCards.get(0).getCrewSacrifice());
        assertEquals(6, shipCards.get(0).getCreditsReward());
        assertEquals(1, shipCards.get(0).getFlightDaysReduction());

        // Second AbandonedShip card
        assertEquals(5, shipCards.get(1).getCrewSacrifice());
        assertEquals(8, shipCards.get(1).getCreditsReward());
        assertEquals(2, shipCards.get(1).getFlightDaysReduction());
    }

    @Test
    void parseCards_shouldCreateCorrectPlanetsCards() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/json/cards_level2.json");

        List<Card> cards = cardParser.parseCards(inputStream);
        List<PlanetsCard> planetsCards = cards.stream()
                .filter(c -> c instanceof PlanetsCard)
                .map(c -> (PlanetsCard) c)
                .toList();

        assertEquals(4, planetsCards.size());

        // Verify first Planets card
        assertEquals(2, planetsCards.get(0).getFlightDaysReduction());
        assertEquals(4, planetsCards.get(0).getPlanets().size());
        assertEquals(2, planetsCards.get(0).getPlanets().get(0).getCargos().size()); // RED, YELLOW
        assertEquals(3, planetsCards.get(0).getPlanets().get(1).getCargos().size()); // YELLOW, GREEN, BLUE
        assertEquals(2, planetsCards.get(0).getPlanets().get(2).getCargos().size()); // GREEN, GREEN
        assertEquals(1, planetsCards.get(0).getPlanets().get(3).getCargos().size()); // YELLOW
    }

//    @Test
//    void parseCards_shouldCreateCorrectWarZoneCard() throws Exception {
//        InputStream inputStream = getClass().getResourceAsStream("/json/cards_level2.json");
//
//        List<Card> cards = cardParser.parseCards(inputStream);
//        WarZoneCard warZoneCard = (WarZoneCard) cards.stream()
//                .filter(c -> c instanceof WarZoneCard)
//                .findFirst()
//                .orElseThrow();
//
//        assertEquals(3, warZoneCard.getPenalties().size());
//
//        // First penalty (MOVE_BACK)
//        assertEquals("LOWEST_FIREPOWER", warZoneCard.getPenalties().get(0).getWarZonePenaltyType().name());
//        assertEquals("MOVE_BACK", warZoneCard.getPenalties().get(0).getWarZonePenaltyEffect().name());
//        assertEquals(4, warZoneCard.getPenalties().get(0).getValue());
//
//        // Third penalty (HIT_BY_PROJECTILES)
//        assertEquals("LOWEST_CREW", warZoneCard.getPenalties().get(2).getWarZonePenaltyType().name());
//        assertEquals("HIT_BY_PROJECTILES", warZoneCard.getPenalties().get(2).getWarZonePenaltyEffect().name());
//        assertEquals(4, warZoneCard.getPenalties().get(2).getFireShots().size());
//    }

    @Test
    void parseCards_shouldHandleEmptyDataForSimpleCards() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/json/cards_level2.json");

        List<Card> cards = cardParser.parseCards(inputStream);
        long epidemicCount = cards.stream()
                .filter(c -> c instanceof EpidemicCard)
                .count();
        long openSpaceCount = cards.stream()
                .filter(c -> c instanceof OpenSpaceCard)
                .count();
        long stellarSpacedustCount = cards.stream()
                .filter(c -> c instanceof StellarSpaceDustCard)
                .count();

        assertEquals(1, epidemicCount);
        assertEquals(3, openSpaceCount);
        assertEquals(1, stellarSpacedustCount);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidJsonFiles")
    void parseCards_shouldThrowForInvalidJsonFiles(String resourcePath, Class<? extends Exception> expectedException) {
        InputStream inputStream = getClass().getResourceAsStream(resourcePath);

        assertThrows(expectedException, () -> cardParser.parseCards(inputStream));
    }

    private static Stream<Arguments> provideInvalidJsonFiles() {
        return Stream.of(
                Arguments.of("/json/invalid_missing_type.json", JSONException.class),
                Arguments.of("/json/invalid_missing_data.json", JSONException.class),
                Arguments.of("/json/invalid_unknown_type.json", IllegalArgumentException.class),
                Arguments.of("/json/invalid_syntax.json", JSONException.class)
        );
    }
}