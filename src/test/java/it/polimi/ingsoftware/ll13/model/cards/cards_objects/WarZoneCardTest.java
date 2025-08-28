package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.ProblemType;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.WarZonePenaltyEffect;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.WarZonePenaltyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WarZoneCardTest {

    private WarZoneCard warZoneCard;
    private List<WarZonePenalty> penalties;
    private String image;

    @BeforeEach
    void setUp() {
        image = "/cards/GT-cards_1_war_zone_1.png";
        penalties = Arrays.asList(
                new WarZonePenalty(WarZonePenaltyType.LOWEST_CREW, WarZonePenaltyEffect.MOVE_BACK, 2),
                new WarZonePenalty(WarZonePenaltyType.LOWEST_FIREPOWER, WarZonePenaltyEffect.HIT_BY_PROJECTILES, Arrays.asList(
                    new FireShot(ProblemType.SMALL, Direction.TOP),
                    new FireShot(ProblemType.BIG, Direction.RIGHT)
                ))
        );
        warZoneCard = new WarZoneCard(1, image, penalties);
    }

    @Test
    void testConstructor() {
        assertNotNull(warZoneCard);
        assertEquals("War zone", warZoneCard.getName());
        assertEquals(1, warZoneCard.getLevel());
        assertEquals(image, warZoneCard.getImage());
        assertEquals(penalties, warZoneCard.getPenalties());
    }

    @Test
    void testGetLevel() {
        assertEquals(1, warZoneCard.getLevel());
    }

    @Test
    void testGetImage() {
        assertEquals(image, warZoneCard.getImage());
    }

    @Test
    void testGetPenalties() {
        assertEquals(penalties, warZoneCard.getPenalties());
    }

    // TODO: apply effect

}