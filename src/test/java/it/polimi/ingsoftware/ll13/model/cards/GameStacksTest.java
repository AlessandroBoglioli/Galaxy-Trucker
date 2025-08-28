package it.polimi.ingsoftware.ll13.model.cards;

import it.polimi.ingsoftware.ll13.model.cards.decks.CardStack;
import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONException;
import java.util.List;

class GameStacksTest {

    @Test
    void constructor_shouldCreateValidStacksForTryLevel() throws JSONException {
        GameStacks gameStacks = new GameStacks(GameLevel.TRY_LEVEL);

        assertEquals(GameLevel.TRY_LEVEL, gameStacks.getCurrentLevel());
        assertNotNull(gameStacks.getStacks());
        assertFalse(gameStacks.getStacks().isEmpty());
    }

    @Test
    void constructor_shouldCreateValidStacksForLevel2() throws JSONException {
        GameStacks gameStacks = new GameStacks(GameLevel.TRY_LEVEL);

        assertEquals(GameLevel.TRY_LEVEL, gameStacks.getCurrentLevel());
        assertNotNull(gameStacks.getStacks());
        assertFalse(gameStacks.getStacks().isEmpty());
    }

    @ParameterizedTest
    @EnumSource(GameLevel.class)
    void getCurrentLevel_shouldReturnCorrectLevel(GameLevel level) throws JSONException {
        GameStacks gameStacks = new GameStacks(level);
        assertEquals(level, gameStacks.getCurrentLevel());
    }

    @ParameterizedTest
    @EnumSource(GameLevel.class)
    void getStacks_shouldReturnNonEmptyList(GameLevel level) throws JSONException {
        GameStacks gameStacks = new GameStacks(level);
        List<CardStack> stacks = gameStacks.getStacks();

        assertNotNull(stacks);
        assertFalse(stacks.isEmpty());
    }

    @Test
    void constructor_shouldThrowForUnknownLevel() {
        assertThrows(IllegalArgumentException.class, () -> {
            new GameStacks(null);
        });
    }

    @Test
    void tryLevelStacks_shouldHaveCorrectStructure() throws JSONException {
        GameStacks gameStacks = new GameStacks(GameLevel.TRY_LEVEL);
        List<CardStack> stacks = gameStacks.getStacks();

        // Verify number of stacks
        assertEquals(4, stacks.size());

        // Verify each stack has 2 cards
        for (CardStack stack : stacks) {
            assertEquals(2, stack.getCards().size());
        }
    }

    @Test
    void level2Stacks_shouldHaveCorrectStructure() throws JSONException {
        GameStacks gameStacks = new GameStacks(GameLevel.LEVEL_2);
        List<CardStack> stacks = gameStacks.getStacks();

        // Verify number of stacks
        assertEquals(4, stacks.size());

        // Verify each stack has 3 cards (1x L0/L1 + 2x L2)
        for (CardStack stack : stacks) {
            assertEquals(3, stack.getCards().size());
        }
    }
}