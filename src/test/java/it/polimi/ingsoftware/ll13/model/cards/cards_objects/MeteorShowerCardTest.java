package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.ProblemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MeteorShowerCardTest {

    private MeteorShowerCard meteorShowerCard;
    private List<Meteor> meteors;
    private String image;

    @BeforeEach
    void setUp() {
        image = "/cards/GT-cards_1_meteor_shower_1.png";
        meteors = Arrays.asList(
            new Meteor(ProblemType.SMALL, Direction.TOP),
            new Meteor(ProblemType.BIG, Direction.RIGHT)
        );
        meteorShowerCard = new MeteorShowerCard(1, image, meteors);
    }

    @Test
    void testConstructor() {
        assertNotNull(meteorShowerCard);
        assertEquals("Meteor Shower", meteorShowerCard.getName());
        assertEquals(1, meteorShowerCard.getLevel());
        assertEquals("/cards/GT-cards_1_meteor_shower_1.png", meteorShowerCard.getImage());
        assertEquals(meteors, meteorShowerCard.getMeteors());
    }

    @Test
    void testGetLevel() {
        assertEquals(1, meteorShowerCard.getLevel());
    }

    @Test
    void testGetImage() {
        assertEquals(image, meteorShowerCard.getImage());
    }

    @Test
    void testGetMeteors() {
        assertEquals(meteors, meteorShowerCard.getMeteors());
    }

    // TODO: apply effect

}
