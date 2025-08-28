package it.polimi.ingsoftware.ll13.model.player;

import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.general_enumerations.RankingPosition;
import it.polimi.ingsoftware.ll13.model.ship_board.Ship;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;

/**
 * This class represent a very simple example for the player
 *
 */

public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;

    private final int id;
    private final String username;
    private Ship ship;
    private boolean inMatch = false;
    private int points = 0;
    private int credits = 0;
    private RankingPosition rankingPosition;
    private int mapPosition;
    private boolean dubbed = false;
    private int laps = 0;
    private PlayerColors color;
    private boolean validated = false;
    private boolean crewPlaced = false;
    private boolean lost = false;

    // ---> Builder <---

    public Player(int id, String username, PlayerColors color) {
        // Not valid integer for the position on the map;
        this.mapPosition = -1;
        this.id = id;
        this.username = username;
        this.color = color;
    }

    // ---> Getters <---

    public RankingPosition getRankingPosition() {
        return rankingPosition;
    }

    public int getMapPosition() {
        return mapPosition;
    }

    public boolean isDubbed() {
        return dubbed;
    }

    public int getLaps() {
        return laps;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Ship getShip() {
        return ship;
    }

    public int getPoints() {
        return points;
    }

    public int getCredits() {return credits;}

    public boolean isInMatch() {
        return inMatch;
    }

    public PlayerColors getColor() {
        return color;
    }

    public boolean isValidated() {
        return validated;
    }

    public boolean isCrewPlaced() {
        return crewPlaced;
    }

    public boolean hasLost() {
        return lost;
    }


    // ---> Setters <---

    public void setRankingPosition(RankingPosition rankingPosition) {
        this.rankingPosition = rankingPosition;
    }

    public void setMapPosition(int mapPosition) {
        this.mapPosition = mapPosition;
    }

    public void setDubbed() {
        this.dubbed = true;
    }

    public void setLaps(int laps) {
        this.laps = laps;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setCredits(int credits) {this.credits = credits;}

    public void setInMatch(boolean inMatch) {
        this.inMatch = inMatch;
    }

    public void setDubbed(boolean dubbed) {
        this.dubbed = dubbed;
    }

    public void setColor(PlayerColors color) {
        this.color = color;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public void setCrewPlaced(boolean crewPlaced) {
        this.crewPlaced = crewPlaced;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }
    // ---> Methods <---
    /**
     * This method checks if a player dubs another player,
     * this can happen when a player moves forward or backward
     * @param player This parameter represent the dubbed player
     */

    public void checkDubbed(@NotNull Player player) {
        if (this.rankingPosition.ordinal() < player.rankingPosition.ordinal()){
            player.setDubbed();
        }
    }

    /**
     * This method add the points to the current points value.
     *
     * @param credits This is the number of point that needs to be added to the point value.
     */
    public void addCredits(int credits) {
        this.credits += credits;
    }


}