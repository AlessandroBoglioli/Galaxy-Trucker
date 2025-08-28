package it.polimi.ingsoftware.ll13.server.Comunication;

import it.polimi.ingsoftware.ll13.model.GameModel;
import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.general_enumerations.RankingPosition;
import it.polimi.ingsoftware.ll13.model.player.Player;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.construction.DiscardRequest;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.construction.DrawTileRequest;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.construction.PlaceRocketRequest;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.validation.ValidateShipRequest;
import it.polimi.ingsoftware.ll13.server.controller.GameController;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ComunicationStressTest {
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
    private @NotNull Request generateRandomRequest(Player player) {
        setUp();
        int choice = ThreadLocalRandom.current().nextInt(0, 4); // 0-4

        return switch (choice) {
            case 0 -> new DrawTileRequest(player.getId(), false, -1); // draw from deck
            case 1 -> {
                Tile fakeTile = new Tile("", ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.SMOOTH);
                yield new DiscardRequest(player.getId(), fakeTile); // try discarding (might fail if not drawn)
            }
            case 2 -> new ValidateShipRequest(player.getId()); // try to validate the ship
            case 3 -> {
                RankingPosition randomRanking = RankingPosition.values()[ThreadLocalRandom.current().nextInt(RankingPosition.values().length)];
                yield new PlaceRocketRequest(player.getId(), randomRanking); // try to place rocket
            }
            default -> throw new IllegalStateException("Unexpected random choice: " + choice);
        };
    }
}
