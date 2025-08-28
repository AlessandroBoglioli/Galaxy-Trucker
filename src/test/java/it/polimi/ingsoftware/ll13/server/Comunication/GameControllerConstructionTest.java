package it.polimi.ingsoftware.ll13.server.Comunication;

import it.polimi.ingsoftware.ll13.model.GameModel;
import it.polimi.ingsoftware.ll13.model.cards.decks.CardStack;
import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.general_enumerations.RankingPosition;
import it.polimi.ingsoftware.ll13.model.player.Player;
import it.polimi.ingsoftware.ll13.model.ship_board.ShipCell;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.construction.*;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.validation.ValidateShipRequest;
import it.polimi.ingsoftware.ll13.network.response.match_responses.construction.ViewStackResponse;
import it.polimi.ingsoftware.ll13.network.response.match_responses.phase_change.CrewPlacePhaseStarted;
import it.polimi.ingsoftware.ll13.server.controller.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerConstructionTest {
    private GameController controller;
    private List<Player> players;

    private void setUp(){
        controller = GameController.getInstance();
        controller.reset();
        players = new ArrayList<>();
        Player player = new Player(0, "Kfrenezy", PlayerColors.RED);
        Player player2 = new Player(1, "Zanone", PlayerColors.BLUE);
        players.add(player);
        players.add(player2);
    }


    @Test
     void testPlaceTileRequest() throws InterruptedException{
        setUp();
        controller.startMatch(new ArrayList<>(), players, GameLevel.TRY_LEVEL);
        controller.setGameModel(new GameModel(GameLevel.TRY_LEVEL,players));
        Thread controllerThread = new Thread(controller);
        controllerThread.start();
        for(Player player : players) {
            List<TileCoordinates> initialTiles = new ArrayList<>();
            ShipCell[][] board = player.getShip().getShipLayout().getMotherBoard();
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[r].length; c++) {
                    Tile t = board[r][c].getTile();
                    if (t != null) {
                        initialTiles.add(new TileCoordinates(r, c, t));
                    }
                }
            }
        }
        Tile dummyTile = new Tile("", ConnectorType.SINGLE,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE,ConnectorType.SINGLE);
        PlaceTileRequest placeTileRequest = new PlaceTileRequest(players.getFirst().getId(), 6,7,dummyTile);
        PlaceTileRequest placeTileRequest1 = new PlaceTileRequest(players.get(1).getId(),8,6,dummyTile);
        controller.submitRequest(placeTileRequest);
        controller.submitRequest(placeTileRequest1);
        Thread.sleep(100);
        Player player = players.getFirst();
        Player player1 = players.get(1);
        Tile placedTile = player.getShip().getShipLayout().getMotherBoard()[1][3].getTile();
        Tile placedTile1 = player1.getShip().getShipLayout().getMotherBoard()[3][2].getTile();
        assertNotNull(placedTile);
        assertEquals(dummyTile, placedTile);
        assertNull(placedTile1);
        controllerThread.interrupt();
    }
    @Test
    void testRotateTileRequest() throws InterruptedException{
        setUp();
        controller.startMatch(new ArrayList<>(), players, GameLevel.TRY_LEVEL);
        Thread controllerThread = new Thread(controller);
        controllerThread.start();
        Tile dummyTile = new Tile("", ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL,ConnectorType.SMOOTH);
        ConnectorType topBefore = dummyTile.getTopConnector().getType();
        ConnectorType rightBefore = dummyTile.getRightConnector().getType();
        ConnectorType bottomBefore = dummyTile.getBottomConnector().getType();
        ConnectorType leftBefore = dummyTile.getLeftConnector().getType();
        RotateTileRequest rotateTileRequest = new RotateTileRequest(players.getFirst().getId(), dummyTile, 3);
        controller.submitRequest(rotateTileRequest);
        Thread.sleep(100);
        assertEquals(topBefore, dummyTile.getLeftConnector().getType());
        assertEquals(rightBefore, dummyTile.getTopConnector().getType());
        assertEquals(bottomBefore, dummyTile.getRightConnector().getType());
        assertEquals(leftBefore, dummyTile.getBottomConnector().getType());
        controllerThread.interrupt();
    }
    @Test
    void testDrawFromTempZoneRequest() throws InterruptedException {
        setUp();
        controller.startMatch(new ArrayList<>(), players, GameLevel.TRY_LEVEL);
        controller.getGameModel().setHourglass(new FakeHourglass());
        Thread controllerThread = new Thread(controller);
        controllerThread.start();
        Player player = players.getFirst();
        Tile dummyTile = new Tile("", ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL,ConnectorType.SMOOTH);
        PlaceTileRequest placeTileRequest = new PlaceTileRequest(player.getId(),5,9,dummyTile);
        PlaceTileRequest placeTileRequest1 = new PlaceTileRequest(players.get(1).getId(),7,6,dummyTile);
        controller.submitRequest(placeTileRequest);
        controller.submitRequest(placeTileRequest1);
        Player player1 = players.get(1);
        Thread.sleep(200);
        assertNotNull(player.getShip().getShipLayout().getMotherBoard()[0][5].getTile());
        assertNotNull(player1.getShip().getShipLayout().getMotherBoard()[2][2].getTile());
        assertEquals(dummyTile, player.getShip().getShipLayout().getMotherBoard()[0][5].getTile());
        assertEquals(dummyTile, player1.getShip().getShipLayout().getMotherBoard()[2][2].getTile());
        Thread.sleep(100);
        Tile afterDrawTile = player.getShip().getShipLayout().getMotherBoard()[0][4].getTile();
        assertNull(afterDrawTile);
        controllerThread.interrupt();
    }
    @Test
    void testDrawTileRequest() throws InterruptedException {
        setUp();
        controller.startMatch(new ArrayList<>(), players, GameLevel.TRY_LEVEL);
        controller.getGameModel().setHourglass(new FakeHourglass());
        Thread controllerThread = new Thread(controller);
        controllerThread.start();
        int initialDeckSize = controller.getGameModel().getTiles().getSize();
        DrawTileRequest drawTileRequest = new DrawTileRequest(players.getFirst().getId(), false, -1);
        controller.submitRequest(drawTileRequest);
        Thread.sleep(100);
        int afterDrawSize = controller.getGameModel().getTiles().getSize();
        assertEquals(initialDeckSize-1, afterDrawSize );
        controllerThread.interrupt();
    }
    @Test
    void testDrawTileRaceCondition() throws InterruptedException {
        setUp();
        controller.startMatch(new ArrayList<>(), players, GameLevel.TRY_LEVEL);
        controller.setGameModel(new GameModel(GameLevel.TRY_LEVEL,players));
        controller.getGameModel().setHourglass(new FakeHourglass());
        Thread controllerThread = new Thread(controller);
        controllerThread.start();
        Player player = players.getFirst();
        Player anotherPlayer = players.get(1);
        Tile discardedTile = new Tile("", ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.SMOOTH);
        controller.getGameModel().getFlippedTileDeck().addTile(discardedTile);
        CountDownLatch latch = new CountDownLatch(1);
        Thread t1 = new Thread(() -> {
            try {
                latch.await(); // wait for the start
                controller.submitRequest(new DrawTileRequest(player.getId(), true, 0)); // index 0
            } catch (Exception ignored) {}
        });
        Thread t2 = new Thread(() -> {
            try {
                latch.await();
                controller.submitRequest(new DrawTileRequest(anotherPlayer.getId(), true, 0)); // index 0
            } catch (Exception ignored) {}
        });

        t1.start();
        t2.start();
        latch.countDown();
        t1.join();
        t2.join();
        Thread.sleep(300);
        int flippedDeckSize = controller.getGameModel().getFlippedTileDeck().getSize();
        assertEquals(0, flippedDeckSize);
        controllerThread.interrupt();
    }

    @Test
    void testPlaceRocketRequest() throws InterruptedException {
        setUp();
        controller.startMatch(new ArrayList<>(), players, GameLevel.TRY_LEVEL);
        controller.setGameModel(new GameModel(GameLevel.TRY_LEVEL,players));
        controller.getGameModel().setHourglass(new FakeHourglass());
        Thread controllerThread = new Thread(controller);
        controllerThread.start();
        Player player = players.getFirst();
        Player player1 = players.get(1);
        List<Integer> startingPositions = controller.getGameModel().getGameMap().getStartingPositions();
        RankingPosition positionForPlayer = RankingPosition.First;
        PlaceRocketRequest placeRocketRequest = new PlaceRocketRequest(player.getId(), positionForPlayer);
        controller.submitRequest(placeRocketRequest);
        Thread.sleep(100);
        assertNotEquals(-1, player.getMapPosition());
        assertEquals(4, player.getMapPosition());
        assertEquals(RankingPosition.First, player.getRankingPosition());
        assertTrue(startingPositions.contains(player.getMapPosition()));
        controllerThread.interrupt();
    }
    @Test
    void stressTestHandlePlaceRocketOnMapRequest() throws InterruptedException {
        setUp();
        controller.startMatch(new ArrayList<>(), players, GameLevel.TRY_LEVEL);
        controller.setGameModel(new GameModel(GameLevel.TRY_LEVEL,players));
        controller.getGameModel().setHourglass(new FakeHourglass());
        Thread controllerThread = new Thread(controller);
        controllerThread.start();
        Player player2 = new Player(2, "Il RE", PlayerColors.BLUE);
        Player player3 = new Player(3, "Manuxo", PlayerColors.GREEN);
        players.add(player2);
        players.add(player3);
        List<Integer> startingPositions = controller.getGameModel().getGameMap().getStartingPositions();
        int contestedPosition = startingPositions.get(3); //giving one position to all of them

        CountDownLatch latch = new CountDownLatch(1);
        List<Thread> threads = new ArrayList<>();
        for (Player player : players) {
            Thread t = new Thread(() -> {
                try {
                    latch.await(); //synchronize before start
                    controller.submitRequest(new PlaceRocketRequest(player.getId(), RankingPosition.First));
                } catch (InterruptedException ignored) {}
            });
            threads.add(t);
        }

        // start all thread together
        threads.forEach(Thread::start);
        latch.countDown();

        for (Thread t : threads) {
            t.join();
        }
        Thread.sleep(300);
        long placedCount = players.stream()
                .filter(p -> p.getMapPosition() == contestedPosition)
                .count();

        assertEquals(1, placedCount, "Only one player should be placed on the contested position");

        for (Player player : players) {
            if (player.getMapPosition() == contestedPosition) {
            } else {
                assertNotEquals(contestedPosition, player.getMapPosition(), "Other players must not be on the contested position");
            }
        }
        players.subList(2, players.size()).clear();
        controllerThread.interrupt();
    }

    @Test
    void testAutoPlaceRockets() throws InterruptedException {
        setUp();
        Player player2 = new Player(2, "Il RE", PlayerColors.BLUE);
        Player player3 = new Player(3, "Manuxo", PlayerColors.GREEN);
        Player player = players.getFirst();
        Player player1 = players.get(1);
        List<Player> players1 = new ArrayList<>();
        players1.add(player1);
        players1.add(player2);
        players1.add(player3);
        players1.add(player);

        controller.startMatch(new ArrayList<>(), players1, GameLevel.TRY_LEVEL);
        controller.setGameModel(new GameModel(GameLevel.TRY_LEVEL,players1));
        controller.getGameModel().setHourglass(new FakeHourglass());
        Thread controllerThread = new Thread(controller);
        controllerThread.start();
        controller.submitRequest(new StartConstruction(player.getId()));
        controller.submitRequest(new PlaceRocketRequest(player.getId(),RankingPosition.First));
        Thread.sleep(1500);
        Set<Integer> seenPositions = new HashSet<>();
        List<Integer> validStart = controller.getGameModel().getGameMap().getStartingPositions();
        for (Player p : players1) {
            int pos = p.getMapPosition();
            assertNotEquals(-1, pos, "Player " + p.getUsername() + " should have a position assigned");
            assertTrue(validStart.contains(pos), "Position must be from starting positions");
            assertTrue(seenPositions.add(pos), "Position " + pos + " already used by another player");
        }
        controllerThread.interrupt();
    }


}
