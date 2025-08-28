package it.polimi.ingsoftware.ll13.model.map;

import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.general_enumerations.RankingPosition;
import it.polimi.ingsoftware.ll13.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RemovingTest {

    private Map map;
    private Player p1, p2, p3;


    private void setUp() {
        map = new Map(false); // regular mode
        p1 = new Player(0,"Kfrenezy", PlayerColors.RED);
        p2 = new Player(1, "AoZA", PlayerColors.BLUE);
        p3 = new Player(2, "IlMaestro", PlayerColors.GREEN);

        ArrayList<Integer> starts = map.getStartingPositions();

        assertTrue(map.firstPlayerPlacement(p1, starts.get(0)));
        assertTrue(map.firstPlayerPlacement(p2, starts.get(1)));
        assertTrue(map.firstPlayerPlacement(p3, starts.get(2)));
        map.movePlayerForward(p3, 3);
        map.movePlayerForward(p2, 2);
        map.movePlayerForward(p1, 1);
    }

    @Test
    public void testRemovePlayerAndUpdateRankings() {
        setUp();
        assertEquals(RankingPosition.Third, p1.getRankingPosition());
        assertEquals(RankingPosition.Second, p2.getRankingPosition());
        assertEquals(RankingPosition.First, p3.getRankingPosition());
        map.removePlayer(p2);
        assertTrue(p2.hasLost());
        assertEquals(-1, p2.getMapPosition());
        for (int pos : map.getStartingPositions()) {
            if (map.getPlayerAt(pos) == null) {
                continue;
            }
            assertNotEquals(p2, map.getPlayerAt(pos));
        }
        assertEquals(RankingPosition.Second, p1.getRankingPosition());
        assertEquals(RankingPosition.First, p3.getRankingPosition());
    }
}

