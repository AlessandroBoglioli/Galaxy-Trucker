package it.polimi.ingsoftware.ll13.model.ship_board.ship_creator;

import it.polimi.ingsoftware.ll13.model.ship_board.Ship;
import it.polimi.ingsoftware.ll13.model.ship_board.ship_levels.LevelTwoShip;
import it.polimi.ingsoftware.ll13.model.ship_board.ship_levels.TryLevelShip;
import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;

public class ShipCreator {
    public Ship createShipForLevel(PlayerColors color, GameLevel level){
        return switch (level) {
            case TRY_LEVEL -> new TryLevelShip(color);
            case LEVEL_2 -> new LevelTwoShip(color);
            default -> throw new IllegalArgumentException("Unknown game level: " + level);
        };
    }
}

