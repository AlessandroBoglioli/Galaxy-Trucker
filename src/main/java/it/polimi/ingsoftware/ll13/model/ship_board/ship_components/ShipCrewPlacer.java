package it.polimi.ingsoftware.ll13.model.ship_board.ship_components;

import it.polimi.ingsoftware.ll13.model.crew_members.Alien;
import it.polimi.ingsoftware.ll13.model.crew_members.AlienColor;
import it.polimi.ingsoftware.ll13.model.crew_members.CrewMember;
import it.polimi.ingsoftware.ll13.model.crew_members.Human;
import it.polimi.ingsoftware.ll13.model.ship_board.ShipCell;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.ColorMapper;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.ShipMatrixConfig;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.VitalSupportColor;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.CabinTile;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.StartingCabinTile;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.VitalSupportTile;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

/**
 * This class manages logic to add crew to the cabin tiles present on the ship
 */
public class ShipCrewPlacer implements Serializable {
    private int alienCount;

    private int humanCount = 2;
    private int yellowAlienCount;
    private int purpleAlienCount;

    public ShipCrewPlacer() {
        alienCount = 0;
    }

    public int getHumanCount() {
        return humanCount;
    }

    public int getPurpleAlienCount() {
        return purpleAlienCount;
    }

    public int getYellowAlienCount() {
        return yellowAlienCount;
    }

    public boolean placeCrewMembers(ShipLayout shipLayout, int row, int col, CrewMember crewMember){
            if (crewMember instanceof Alien alien) {
                return placeAlien(shipLayout, row, col, alien.getColor());
            } else if (crewMember instanceof Human) {
                return placeHuman(shipLayout, row, col);
            }
            return false;
    }

    private boolean isConnectedToVitalSupport(ShipLayout shipLayout, int row, int col, AlienColor color) {
        VitalSupportColor vitalSupportColor = ColorMapper.fromAlienColor(color);
        ShipCell currentCell = shipLayout.getMotherBoard()[row][col];
        Tile currentTile = currentCell.getTile();
        if (currentTile == null) return false;
        int[] dRows = {-1, 0, 1, 0};
        int[] dCols = {0, 1, 0, -1};
        int[] currentTileConnectorIndices = {0, 1, 2, 3};
        int[] adjacentTileConnectorIndices = {2, 3, 0, 1};
        for (int i = 0; i < 4; i++) {
            int newRow = row + dRows[i];
            int newCol = col + dCols[i];
            if (newRow < 0 || newRow >= ShipMatrixConfig.ROW || newCol < 0 || newCol >= ShipMatrixConfig.COL) {
                continue;
            }
            ShipCell adjacentCell = shipLayout.getMotherBoard()[newRow][newCol];
            if (adjacentCell == null) continue;
            Tile adjacentTile = adjacentCell.getTile();
            if (!(adjacentTile instanceof VitalSupportTile vsTile)) continue;
            ConnectorType currentConnector = currentTile.getConnectors()[currentTileConnectorIndices[i]].getType();
            ConnectorType adjacentConnector = adjacentTile.getConnectors()[adjacentTileConnectorIndices[i]].getType();
            if (currentConnector != ConnectorType.SMOOTH &&
                    adjacentConnector != ConnectorType.SMOOTH &&
                    vsTile.getVitalSupportColor() == vitalSupportColor) {
                return true;
            }
        }
        return false;
    }

    private boolean placeAlien(ShipLayout shipLayout, int row, int col, AlienColor color){
        if(!(shipLayout.getMotherBoard()[row][col].getTile() instanceof CabinTile cabinTile)){
            return false;
        }
        if (!isConnectedToVitalSupport(shipLayout, row, col, color)) {
            return false;
        }
        if(cabinTile.getCrewCount() != 0){
            return false;
        }
        cabinTile.addCrewMember(new Alien(color));
        if(color == AlienColor.PURPLE){
            purpleAlienCount++;
        } else if (color == AlienColor.YELLOW) {
            yellowAlienCount++;

        }
        alienCount++;
        return true;
    }
    private boolean placeHuman(ShipLayout shipLayout, int row, int col){
        if(!(shipLayout.getMotherBoard()[row][col].getTile() instanceof CabinTile cabinTile)){
            return false;
        }
        if(cabinTile.getCrewCount() != 0){
            return false;
        }
        cabinTile.addCrewMember(new Human());
        humanCount = humanCount +2;
        return true;
    }

    public int getAlienCount(){
        return alienCount;
    }

    public boolean isAllCabinPopulated(ShipLayout shipLayout){
        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            for (int col = 0; col < ShipMatrixConfig.COL; col++) {
                ShipCell cell = shipLayout.getMotherBoard()[row][col];
                if (cell != null && cell.getTile() instanceof CabinTile cabin && !(cell.getTile() instanceof StartingCabinTile)) {
                    if (cabin.getCrewCount() == 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public void placeCrewTryLevel(ShipLayout shipLayout){
        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            for (int col = 0; col < ShipMatrixConfig.COL; col++) {
                ShipCell cell = shipLayout.getMotherBoard()[row][col];
                if (cell != null && cell.getTile() instanceof CabinTile cabin) {
                    cabin.addCrewMember(new Human());
                }
            }
        }
    }
}