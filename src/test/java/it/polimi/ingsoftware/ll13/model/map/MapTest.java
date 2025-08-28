package it.polimi.ingsoftware.ll13.model.map;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.general_enumerations.RankingPosition;
import it.polimi.ingsoftware.ll13.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapTest {

    Player player1;
    Player player2;
    Player player3;
    Player player4;
    Map map;
    MapManager mapManager;

    @BeforeEach
    public void setPlayersTest(){
        player1 = new Player(3,"gasi", PlayerColors.RED);
        player2 = new Player(4,"laura", PlayerColors.BLUE);
        player3 = new Player(5,"aura", PlayerColors.YELLOW);
        player4 = new Player(7,"kfrenezy", PlayerColors.GREEN);

        Player[] players = { player1, player2, player3, player4 };

        for (Player player : players) {
            assertFalse(player.isDubbed());
            assertEquals(0, player.getLaps());
            assertEquals(-1, player.getMapPosition());
        }
    }

    @BeforeEach
    public void setMapTest(){

        mapManager = new MapManager();

        map = mapManager.getMap(false);
        assertEquals(24, map.getDimension());
        for (int i = 0; i < map.getDimension(); i++) {
            assertNull(map.getPlayerAt(i));
        }
        assertEquals(0, map.getStartingPositions().get(0));
        assertEquals(1, map.getStartingPositions().get(1));
        assertEquals(3, map.getStartingPositions().get(2));
        assertEquals(6, map.getStartingPositions().get(3));

        assertNull(mapManager.getMap(true));
    }

    @Test
    public void firstPlayerPlacement(){
        // 10 is not a valid starting position
        assertFalse(map.firstPlayerPlacement(player1, 10));

        // If I place player1 in position 0, I should not be able to put him in position 1
        assertTrue(map.firstPlayerPlacement(player1, 0));
        assertFalse(map.firstPlayerPlacement(player1, 1));
        assertEquals(player1, map.getPlayerAt(0));
        assertNull(map.getPlayerAt(1));

        // Now that player1 was placed in position 0, I will check if the others parameters are correct
        assertEquals(0, player1.getMapPosition());
        assertEquals(RankingPosition.First, player1.getRankingPosition());

        // I should not be able to put another player on an occupied position
        assertFalse(map.firstPlayerPlacement(player2, 0));

        // Now if I place player2 in position 1, the new parameters should be correct and the ranking updated
        assertTrue(map.firstPlayerPlacement(player2, 1));
        assertEquals(player2, map.getPlayerAt(1));
        assertEquals(1, player2.getMapPosition());
        assertEquals(RankingPosition.First, player2.getRankingPosition());
        assertEquals(RankingPosition.Second, player1.getRankingPosition());
    }

    @Test
    public void movePlayersForward(){
        map.firstPlayerPlacement(player1, 0);
        map.firstPlayerPlacement(player2, 1);
        map.firstPlayerPlacement(player3, 3);
        map.firstPlayerPlacement(player4, 6);

        // I move player 1 and player2 by one position
        map.movePlayerForward(player2, 1);
        assertEquals(player2, map.getPlayerAt(2));
        assertEquals(2, player2.getMapPosition());
        map.movePlayerForward(player1, 1);
        assertEquals(player1, map.getPlayerAt(1));
        assertEquals(1, player1.getMapPosition());

        // I make player1 move by one position, it should "jump" player2 and player3
        map.movePlayerForward(player1, 1);
        assertEquals(player1, map.getPlayerAt(4));
        assertEquals(4, player1.getMapPosition());
        assertEquals(RankingPosition.Second, player1.getRankingPosition());
        assertEquals(RankingPosition.Third, player3.getRankingPosition());
        assertEquals(RankingPosition.Fourth, player2.getRankingPosition());
        assertFalse(player2.isDubbed());
        assertFalse(player3.isDubbed());

        // I bring player4 to the last position and then I move it by one position bringing it to the start
        map.movePlayerForward(player4, 17);
        assertEquals(player4, map.getPlayerAt(23));
        map.movePlayerForward(player4, 1);
        assertEquals(player4, map.getPlayerAt(0));
        assertEquals(1, player4.getLaps());

        // I make player4 dub the other players
        map.movePlayerForward(player4, 2);
        assertEquals(player4, map.getPlayerAt(5));
        assertTrue(player1.isDubbed());
        assertTrue(player2.isDubbed());
        assertTrue(player3.isDubbed());
    }

    @Test
    public void movePlayersBackward(){
        map.firstPlayerPlacement(player1, 0);
        map.firstPlayerPlacement(player2, 1);
        map.firstPlayerPlacement(player3, 3);
        map.firstPlayerPlacement(player4, 6);

        map.movePlayerBackward(player3, 1);
        assertEquals(player3, map.getPlayerAt(2));

        map.movePlayerBackward(player3, 1);
        assertEquals(player3, map.getPlayerAt(23));
        assertEquals(23, player3.getMapPosition());
        assertEquals(-1, player3.getLaps());
        assertFalse(player3.isDubbed());
        assertEquals(RankingPosition.Fourth, player3.getRankingPosition());
        assertEquals(RankingPosition.Second, player2.getRankingPosition());
        assertEquals(RankingPosition.Third, player1.getRankingPosition());

        map.movePlayerBackward(player3, 24);
        assertTrue(player3.isDubbed());
    }
    @Test
    public void rankingTest(){
        map.firstPlayerPlacement(player1,0);
        map.firstPlayerPlacement(player2,6);
        map.firstPlayerPlacement(player3,3);
        assertEquals(player1.getRankingPosition(),RankingPosition.Third);
        assertEquals(player2.getRankingPosition(),RankingPosition.First);
        assertEquals(player3.getRankingPosition(),RankingPosition.Second);
    }

}
