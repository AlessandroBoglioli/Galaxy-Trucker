package it.polimi.ingsoftware.ll13.server.Comunication;

import it.polimi.ingsoftware.ll13.model.GameModel;
import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.player.Player;
import it.polimi.ingsoftware.ll13.model.ship_board.Ship;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.MotorTile;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.validation.EliminateTileRequest;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.validation.ValidateShipRequest;
import it.polimi.ingsoftware.ll13.server.controller.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerValidationTest {
    private GameController controller;
    private List<Player> players;

    private void setUp(){
        controller = GameController.getInstance();
        controller.reset();
        players = new ArrayList<>();
        Player player1 = new Player(2,"Manuxo", PlayerColors.GREEN);
        Player player2 = new Player(3,"Il maestro", PlayerColors.YELLOW);
        players.add(player1);
        players.add(player2);

    }
    @Test
    void testValidateShipRequest() throws InterruptedException {
        setUp();
        controller.startMatch(new ArrayList<>(), players, GameLevel.TRY_LEVEL);
        controller.setGameModel(new GameModel(GameLevel.TRY_LEVEL, players));
        controller.getGameModel().setHourglass(new FakeHourglass());
        Thread controllerThread = new Thread(controller);
        controllerThread.start();
        Ship ship1 = players.getFirst().getShip();
        Ship ship2 = players.get(1).getShip();
        Tile tile1 = new Tile("", ConnectorType.SMOOTH,ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL);
        ship1.getShipLayout().addTile(tile1, 2, 4);
        Tile tile2 = new MotorTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL, Direction.TOP);
        ship1.getShipLayout().addTile(tile2,2,5);
        Tile tile3 = new Tile("", ConnectorType.UNIVERSAL,ConnectorType.SINGLE,ConnectorType.SINGLE,ConnectorType.SINGLE);
        ship1.getShipLayout().addTile(tile3, 3,3);
        Tile tile4 = new Tile("", ConnectorType.UNIVERSAL,ConnectorType.DOUBLE,ConnectorType.SMOOTH,ConnectorType.DOUBLE);
        ship2.getShipLayout().addTile(tile4,2,4);
        ValidateShipRequest validateShipRequest1 = new ValidateShipRequest(players.getFirst().getId());
        ValidateShipRequest validateShipRequest2 = new ValidateShipRequest(players.get(1).getId());
        controller.submitRequest(validateShipRequest1);
        controller.submitRequest(validateShipRequest2);
        Thread.sleep(200);
        assertTrue(ship2.getShipLayout().getInvalidCells().isEmpty(), "Player 2 should have invalid cells in his ship");
        assertFalse(ship1.getShipLayout().getInvalidCells().isEmpty());
        Tile tile = ship1.getShipLayout().getInvalidCells().getFirst().getTile();
        assertInstanceOf(MotorTile.class, tile);
        Thread.sleep(20);
        EliminateTileRequest eliminateTileRequest = new EliminateTileRequest(players.getFirst().getId(),7,9);
        controller.submitRequest(eliminateTileRequest);
        Thread.sleep(30);
        controller.submitRequest(new ValidateShipRequest(players.getFirst().getId()));
        Thread.sleep(100);
        assertTrue(ship1.getShipLayout().getInvalidCells().isEmpty());
    }
}
