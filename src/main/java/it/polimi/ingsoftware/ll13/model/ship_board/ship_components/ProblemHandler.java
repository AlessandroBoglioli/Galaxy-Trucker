package it.polimi.ingsoftware.ll13.model.ship_board.ship_components;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.FireShot;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Meteor;
import it.polimi.ingsoftware.ll13.model.cards.dtos.helper_classes.Coordinates;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.ProblemType;
import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.ship_board.ShipCell;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.ShipMatrixConfig;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.*;

import static it.polimi.ingsoftware.ll13.model.general_enumerations.Direction.BOTTOM;
import static it.polimi.ingsoftware.ll13.model.general_enumerations.Direction.TOP;

/**
 * This class manages some effects related to cards that attack the ship, handles the effects of meteors and fire shots for instance
 */
public class ProblemHandler {
    public ProblemHandler() {
        ;
    }

    // methods for METEORS
    /**
     * Handles a big meteor impact on the ship.
     * @param shipLayout the layout of the ship.
     * @param shipStats the stats of the ship.
     * @param batteryManager the battery manager of the ship.
     * @param cargosManager the cargo manager of the ship.
     * @param meteor The meteor impacting the ship.
     * @param directionNumber the number that represent where the meteor is colliding.
     * @param row row of the eventual double cannon activated.
     * @param col column of the eventual double cannon activated.
     * @return if the ship was correctly defended.
     */
    public boolean handleBigMeteorImpact(ShipLayout shipLayout, ShipStats shipStats, BatteryManager batteryManager, CargosManager cargosManager, Meteor meteor, int directionNumber, int row, int col) {
        if(meteor.getMeteorType() != ProblemType.BIG) return false;

        if (meteor.getMeteorDirection() == TOP || meteor.getMeteorDirection() == BOTTOM) {
            if (directionNumber < 4 || directionNumber > 10) {
                return true;
            }
        } else {
            if (directionNumber < 5 || directionNumber  > 9) {
                return true;
            }
        }

        // Second check if we can defend with cannons
        if (canDefendWithCannon(shipLayout, meteor, directionNumber)) {
            return true;
        }

        // Second check if he can defend with a double cannon
        if (canDefendWithDoubleCannon(shipLayout, batteryManager, meteor, directionNumber, row, col)) {
            return true;
        }

        applyDamage(shipLayout, shipStats, batteryManager, cargosManager, meteor.getMeteorDirection(), directionNumber);
        return true;
    }

    /**
     * Handles a small meteor impact on the ship.
     * @param shipLayout the layout of the ship.
     * @param shipStats the stats of the ship.
     * @param batteryManager the battery manager of the ship.
     * @param cargosManager the cargo manager of the ship.
     * @param meteor The meteor impacting the ship.
     * @param directionNumber the number that represent where the meteor is colliding.
     * @param row row of the eventual double cannon activated.
     * @param col column of the eventual double cannon activated.
     * @return if the ship was correctly defended.
    */
    public boolean handleSmallMeteorImpact(ShipLayout shipLayout, ShipStats shipStats, BatteryManager batteryManager, CargosManager cargosManager, Meteor meteor, int directionNumber, int row, int col) {
        if(meteor.getMeteorType() != ProblemType.SMALL) return false;

        if (meteor.getMeteorDirection() == TOP || meteor.getMeteorDirection() == BOTTOM) {
            if (directionNumber < 4 || directionNumber > 10) {
                return true;
            }
        } else {
            if (directionNumber < 5 || directionNumber  > 9) {
                return true;
            }
        }

        // First check if the side is exposed
        if(!isSideExposed(shipLayout, meteor.getMeteorDirection(), directionNumber)){
            return true;
        }

        // Second check if the is protected and the player wants to use a shield
        if (canDefendWithShield(shipLayout, batteryManager, meteor, row, col)) {
            return true;
        }

        // If we get here, the meteor hits the ship
        applyDamage(shipLayout, shipStats, batteryManager, cargosManager, meteor.getMeteorDirection(), directionNumber);
        return false;
    }

    private boolean canDefendWithCannon(ShipLayout shipLayout, Meteor meteor, int directionNumber) {
        Direction impactDirection = meteor.getMeteorDirection();

        switch(impactDirection) {
            case TOP, BOTTOM: {
                if (impactDirection == TOP) {
                    for(int i = 0; i < ShipMatrixConfig.ROW; i++) {
                        ShipCell currentCell = shipLayout.getMotherBoard()[i][directionNumber - 4];
                        if(currentCell.getTile() instanceof CannonTile && ((CannonTile) currentCell.getTile()).getCannonDirection() == TOP) {
                            return true;
                        }
                    }
                } else {
                    for(int i = (ShipMatrixConfig.ROW - 1); i >= 0; i--) {
                        ShipCell currentCell = shipLayout.getMotherBoard()[i][directionNumber - 4];
                        if(currentCell.getTile() instanceof CannonTile && ((CannonTile) currentCell.getTile()).getCannonDirection() == Direction.BOTTOM) {
                            return true;
                        }
                    }
                }
                break;
            }
            case LEFT, RIGHT: {
                if (impactDirection == Direction.LEFT) {
                    for(int i = 0; i < ShipMatrixConfig.COL; i++) {
                        ShipCell currentCell = shipLayout.getMotherBoard()[directionNumber - 5][i];
                        if(currentCell.getTile() instanceof CannonTile && (Direction)((CannonTile) currentCell.getTile()).getCannonDirection() == Direction.LEFT) {
                            return true;
                        }
                    }
                } else {
                    for(int i = (ShipMatrixConfig.COL-1); i >= 0; i--) {
                        ShipCell currentCell = shipLayout.getMotherBoard()[directionNumber - 5][i];
                        if(currentCell.getTile() instanceof CannonTile && (Direction)((CannonTile) currentCell.getTile()).getCannonDirection() == Direction.RIGHT) {
                            return true;
                        }
                    }
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Impact direction" + impactDirection + " not supported");
            }
        }
        return false;
    }

    private boolean canDefendWithDoubleCannon(ShipLayout shipLayout, BatteryManager batteryManager, Meteor meteor, int directionNumber, int row, int col) {

        // TODO : check if the double cannon is in the same direction number as the meteor

        if(row < 0 || row >= ShipMatrixConfig.ROW) return false;
        if(col < 0 || col >= ShipMatrixConfig.COL) return false;

        ShipCell currentCell = shipLayout.getMotherBoard()[row][col];
        if(!(currentCell.getTile() instanceof DoubleCannonTile)) {
            return false;
        }

        if(batteryManager.getBatteries() < 0) return false;

        Direction impactDirection = meteor.getMeteorDirection();
        switch(impactDirection) {
            case TOP, BOTTOM: {
                if (impactDirection == TOP) {
                    if(((DoubleCannonTile) currentCell.getTile()).getCannonDirection() == TOP && col == directionNumber - 4) {
                        batteryManager.useRandomBattery(shipLayout);
                        return true;
                    }
                } else {
                    if(((DoubleCannonTile) currentCell.getTile()).getCannonDirection() == Direction.BOTTOM && col == directionNumber - 4) {
                        batteryManager.useRandomBattery(shipLayout);
                        return true;
                    }
                }
                break;
            }
            case LEFT, RIGHT: {
                if (impactDirection == Direction.LEFT) {
                    if(((DoubleCannonTile) currentCell.getTile()).getCannonDirection() == Direction.LEFT && row == directionNumber - 5) {
                        batteryManager.useRandomBattery(shipLayout);
                        return true;
                    }
                } else {
                    if(((DoubleCannonTile) currentCell.getTile()).getCannonDirection() == Direction.RIGHT && row == directionNumber - 5) {
                        batteryManager.useRandomBattery(shipLayout);
                        return true;
                    }
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Impact direction" + impactDirection + " not supported");
            }
        }
        return false;
    }

    private boolean canDefendWithShield(ShipLayout shipLayout, BatteryManager batteryManager, Meteor meteor, int row, int col) {
        if (batteryManager.getBatteries() <= 0) {
            return false;
        }

        if(row < 0 || row >= ShipMatrixConfig.ROW) return false;
        if(col < 0 || col >= ShipMatrixConfig.COL) return false;

        if(batteryManager.getBatteries() < 0) return false;

        ShipCell currentCell = shipLayout.getMotherBoard()[row][col];
        if(!(currentCell.getTile() instanceof ShieldTile)) {
            return false;
        }

        Direction impactDirection = meteor.getMeteorDirection();
        if(((ShieldTile) currentCell.getTile()).getDirection1() == impactDirection || ((ShieldTile) currentCell.getTile()).getDirection2() == impactDirection) {
            batteryManager.useRandomBattery(shipLayout);
            return true;
        }
        return false;
    }

    private boolean isShieldCoveringAngles(Tile tile, Direction direction) {
        return (((ShieldTile) tile).getDirection1() == direction || ((ShieldTile) tile).getDirection2() == direction);
    }

    // Helper methods to help the destruction of a tile
    private boolean isSideExposed(ShipLayout shipLayout, Direction direction, int directionNumber) {
        return switch (direction) {
            case TOP -> hasExposedTop(shipLayout, directionNumber);
            case BOTTOM -> hasExposedBottom(shipLayout, directionNumber);
            case LEFT -> hasExposedLeft(shipLayout, directionNumber);
            case RIGHT -> hasExposedRight(shipLayout, directionNumber);
        };
    }

    private boolean hasExposedTop(ShipLayout shipLayout, int directionNumber) {
        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            ShipCell cell = shipLayout.getMotherBoard()[row][directionNumber - 4];
            if (cell != null && cell.isOccupied()) {
                ConnectorType topConnector = cell.getTile().getConnectors()[0].getType();
                return topConnector != ConnectorType.SMOOTH;
            }
        }
        return false;
    }

    private boolean hasExposedBottom(ShipLayout shipLayout, int directionNumber) {
        for (int row = ShipMatrixConfig.ROW-1; row >= 0; row--) {
            ShipCell cell = shipLayout.getMotherBoard()[row][directionNumber - 4];
            if (cell != null && cell.isOccupied()) {
                ConnectorType bottomConnector = cell.getTile().getConnectors()[2].getType();
                return bottomConnector != ConnectorType.SMOOTH;
            }
        }
        return false;
    }

    private boolean hasExposedLeft(ShipLayout shipLayout, int directionNumber) {
        for (int col = 0; col < ShipMatrixConfig.COL; col++) {
            ShipCell cell = shipLayout.getMotherBoard()[directionNumber - 5][col];
            if (cell != null && cell.isOccupied()) {
                ConnectorType leftConnector = cell.getTile().getConnectors()[3].getType();
                return leftConnector != ConnectorType.SMOOTH;
            }
        }
        return false;
    }

    private boolean hasExposedRight(ShipLayout shipLayout, int directionNumber) {
        for (int col = ShipMatrixConfig.COL-1; col >= 0; col--) {
            ShipCell cell = shipLayout.getMotherBoard()[directionNumber - 5][col];
            if (cell != null && cell.isOccupied()) {
                ConnectorType rightConnector = cell.getTile().getConnectors()[1].getType();
                return rightConnector != ConnectorType.SMOOTH;
            }
        }
        return false;
    }

    private void applyDamage(ShipLayout shipLayout, ShipStats shipStats, BatteryManager batteryManager, CargosManager cargosManager, Direction impactDirection, int directionNumber) {
        Coordinates coordinates = null;

        // Determine which cells to damage based on impact direction
        switch (impactDirection) {
            case TOP -> coordinates = getFirstTop(shipLayout, directionNumber);
            case BOTTOM -> coordinates = getFirstBottom(shipLayout, directionNumber);
            case LEFT -> coordinates = getFirstLeft(shipLayout, directionNumber);
            case RIGHT -> coordinates = getFirstRight(shipLayout, directionNumber);
        };

        if(coordinates == null) {
            return;
        }

        handleDamageCause(shipLayout, batteryManager, cargosManager, coordinates);
        shipLayout.eliminateTile(coordinates.getRow(), coordinates.getCol());

        // Recalculate ship stats after damage
        batteryManager.calculateBatteries(shipLayout);
        cargosManager.calculateAllCargos(shipLayout);
        shipStats.calculateAllStats(shipLayout);
    }

    private Coordinates getFirstTop(ShipLayout shipLayout, int directionNumber) {
        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            ShipCell cell = shipLayout.getMotherBoard()[row][directionNumber - 4];
            if (cell != null && cell.isOccupied()) {
                return new Coordinates(row, directionNumber - 4);
            }
        }
        return null;
    }

    private Coordinates getFirstBottom(ShipLayout shipLayout, int directionNumber) {
        for (int row = ShipMatrixConfig.ROW-1; row >= 0; row--) {
            ShipCell cell = shipLayout.getMotherBoard()[row][directionNumber - 4];
            if (cell != null && cell.isOccupied()) {
                return new Coordinates(row, directionNumber - 4);
            }
        }
        return null;
    }

    private Coordinates getFirstLeft(ShipLayout shipLayout, int directionNumber) {
        for (int col = 0; col < ShipMatrixConfig.COL; col++) {
            ShipCell cell = shipLayout.getMotherBoard()[directionNumber - 5][col];
            if (cell != null && cell.isOccupied()) {
                 return new Coordinates(directionNumber - 5, col);
            }
        }
        return null;
    }

    private Coordinates getFirstRight(ShipLayout shipLayout, int directionNumber) {
        for (int col = ShipMatrixConfig.COL-1; col >= 0; col--) {
            ShipCell cell = shipLayout.getMotherBoard()[directionNumber - 5][col];
            if (cell != null && cell.isOccupied()) {
                return new Coordinates(directionNumber - 5, col);
            }
        }
        return null;
    }

    private void handleDamageCause(ShipLayout shipLayout, BatteryManager batteryManager, CargosManager cargosManager, Coordinates coordinates) {
        if(coordinates == null) return;

        // Handling of all the cases
        if(shipLayout.getMotherBoard()[coordinates.getRow()][coordinates.getCol()].getTile() instanceof BatteryStorageTile) {
            batteryManager.removeBatteryTile(coordinates);
        }
        if(shipLayout.getMotherBoard()[coordinates.getRow()][coordinates.getCol()].getTile() instanceof CargoHoldTile) {
            cargosManager.removeCargoHoldTile(coordinates);
        }
    }


    // methods for FIRE SHOTS
    /**
     * The method that handle a big fire shot.
     * @param shipLayout the layout of the ship.
     * @param shipStats the stats of the ship.
     * @param batteryManager the battery manager of the ship.
     * @param cargosManager the cargos manager of the ship.
     * @param fireShot the fire shot that is colliding with the ship.
     * @param directionNumber the number of arriving of the fire shot.
     * @return a boolean that represent if the method was completed correctly.
     */
    public boolean handleBigFireShot(ShipLayout shipLayout, ShipStats shipStats, BatteryManager batteryManager, CargosManager cargosManager, FireShot fireShot, int directionNumber) {
        if ((fireShot.getFireShotDirection() == TOP  || fireShot.getFireShotDirection() == BOTTOM) && (directionNumber < 4 || directionNumber > 10)) {
            return true;
        } else {
            if (directionNumber < 5 || directionNumber > 9) {
                return true;
            }
        }
        applyDamage(shipLayout, shipStats, batteryManager, cargosManager, fireShot.getFireShotDirection(), directionNumber);

        return true;
    }

    /**
     * The method that handle a big fire shot.
     * @param shipLayout the layout of the ship.
     * @param shipStats the stats of the ship.
     * @param batteryManager the battery manager of the ship.
     * @param cargosManager the cargos manager of the ship.
     * @param fireShot the fire shot that is colliding with the ship.
     * @param directionNumber the number of arriving of the fire shot.
     * @return a boolean that represent if the method was completed correctly.
     */
    public boolean handleSmallFireShots(ShipLayout shipLayout, ShipStats shipStats, BatteryManager batteryManager, CargosManager cargosManager, FireShot fireShot, int directionNumber, int row, int col) {
        if ((fireShot.getFireShotDirection() == TOP  || fireShot.getFireShotDirection() == BOTTOM) && (directionNumber < 4 || directionNumber > 10)) {
            return true;
        } else {
            if (directionNumber < 5 || directionNumber > 9) {
                return true;
            }
        }
        if (canDefendWithShield(shipLayout, batteryManager, fireShot, row, col)) {
            return true;
        }

        applyDamage(shipLayout, shipStats, batteryManager, cargosManager, fireShot.getFireShotDirection(), directionNumber);
        return true;
    }

    private boolean canDefendWithShield(ShipLayout shipLayout, BatteryManager batteryManager, FireShot fireShot, int row, int col) {
        if (batteryManager.getBatteries() <= 0) {
            return false;
        }

        if(row < 0 || row >= ShipMatrixConfig.ROW) return false;
        if(col < 0 || col >= ShipMatrixConfig.COL) return false;

        // Check if any shield tile exists and then is powered
        ShipCell currentCell = shipLayout.getMotherBoard()[row][col];
        if(!(currentCell.getTile() instanceof ShieldTile)) {
            return false;
        }
        if(isShieldCoveringAngles(currentCell.getTile(), fireShot.getFireShotDirection())) {
            batteryManager.useRandomBattery(shipLayout);
            return true;
        }
        return false;
    }

    // Methods for the SMUGGLERS
    /**
     * This method handle the smugglers effect.
     * @param shipLayout the layout of the ship.
     * @param batteryManager the battery manager of the ship.
     * @param cargosManager the cargos manager of the ship.
     * @param cargoPenalty the integer number representing the cargo penalties.
     */
    public void handleSmugglers(ShipLayout shipLayout, BatteryManager batteryManager, CargosManager cargosManager,int cargoPenalty) {
        boolean result;
        while (cargoPenalty > 0) {
            // Cargo
            result = cargosManager.removeBestCargo(shipLayout);
            if (result) {
                cargoPenalty--;
            } else {
                // Batteries
                result = batteryManager.useRandomBattery(shipLayout);
                if (result) {
                    cargoPenalty--;
                } else {
                    return;
                }
            }
        }
    }
}
