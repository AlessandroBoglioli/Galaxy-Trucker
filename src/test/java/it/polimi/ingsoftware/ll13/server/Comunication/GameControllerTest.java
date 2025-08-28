package it.polimi.ingsoftware.ll13.server.Comunication;

import it.polimi.ingsoftware.ll13.model.GameModel;
import it.polimi.ingsoftware.ll13.model.GamePhase;
import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.player.Player;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.construction.DrawTileRequest;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.construction.FlipHourGlassRequest;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.construction.StartConstruction;
import it.polimi.ingsoftware.ll13.server.controller.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameControllerTest {
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
    void testTryLevelCommunication() throws InterruptedException {
        setUp();
        controller.startMatch(new ArrayList<>(), players, GameLevel.TRY_LEVEL);
        controller.setGameModel(new GameModel(GameLevel.TRY_LEVEL,players));
        controller.getGameModel().setHourglass(new FakeHourglass());
        Thread controllerThread = new Thread(controller);
        controllerThread.start();
        StartConstruction startConstruction = new StartConstruction(players.getFirst().getId());
        controller.submitRequest(startConstruction);
        assertEquals(GamePhase.SETUP, controller.getGameModel().getCurrentPhase());
        Thread.sleep(1500);
        assertEquals(GamePhase.VALIDATION, controller.getGameModel().getCurrentPhase());
        controllerThread.interrupt();

    }
    @Test
    void testLevel2Communication() throws InterruptedException {
        setUp();
        controller.startMatch(new ArrayList<>(), players, GameLevel.LEVEL_2);
        controller.setGameModel(new GameModel(GameLevel.LEVEL_2,players));
        controller.getGameModel().setHourglass(new FakeHourglassLevel2());
        Thread controllerThread = new Thread(controller);
        controllerThread.start();

        StartConstruction startConstruction = new StartConstruction(players.getFirst().getId());
        controller.submitRequest(startConstruction);

        Thread.sleep(1500);// wait a bit for the first (fake) hourglass
        assertEquals(GamePhase.SETUP, controller.getGameModel().getCurrentPhase());

        FlipHourGlassRequest flipRequest = new FlipHourGlassRequest(players.get(1).getId());
        controller.submitRequest(flipRequest);

        Thread.sleep(1500); // wait a bit again for the second (fake) hourglass

        assertEquals(GamePhase.VALIDATION, controller.getGameModel().getCurrentPhase());
        controllerThread.interrupt();
    }
}
