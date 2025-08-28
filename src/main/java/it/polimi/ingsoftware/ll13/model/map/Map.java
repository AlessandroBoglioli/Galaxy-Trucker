package it.polimi.ingsoftware.ll13.model.map;
import it.polimi.ingsoftware.ll13.model.general_enumerations.RankingPosition;
import it.polimi.ingsoftware.ll13.model.player.Player;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class represent the map where the players move during the game, it is represented with an array
 * so it's easier keeping track of the positions and with the dimension and startingPositions we can differentiate
 * between the first flight mode and the regular one.
 */

public class Map {

    private final int dimension;
    private static Player[] map = null;
    private final ArrayList<Integer> startingPositions;
    private final Lock placementLock = new ReentrantLock();

    // ---> Builder <---

    /**
     * This builder sets every attribute of the map:
     *  - dimension
     *  - map array
     *  - starting positions
     * depending on the boolean firstFlight.
     * @param firstFlight indicates if we are doing a first flight or the regular mode.
     */

    protected Map(boolean firstFlight) {
        this.startingPositions = new ArrayList<Integer>();
        startingPositions.add(0);
        startingPositions.add(1);
        if (firstFlight) {
            this.dimension = 18;
            startingPositions.add(2);
            startingPositions.add(4);
        } else {
            this.dimension = 24;
            startingPositions.add(3);
            startingPositions.add(6);
        }
        map = new Player[this.dimension];
    }

    // ---> Getters <---
    public int getDimension() {
        return dimension;
    }

    public ArrayList<Integer> getStartingPositions() {
        return startingPositions;
    }

    protected Player getPlayerAt(int position) {
        return this.map[position];
    }

    // ---> Setters <---

    private void setPlayerOnMap(Player player, int position) {
        map[position] = player;
    }

    // ---> Methods <---

    /**
     * This method checks if a position on the map is already occupied by a player
     * @param position This parameter indicates the position on the map that we want to check
     * @return Returns true if the position is occupied, false otherwise.
     */

    protected boolean isOccupied(Integer position) {
        return this.map[position] != null;
    }

    /**
     * This method is a check if a position where I want to put my player the first time
     * is valid as a starting position.
     * This happens checking the startingPosition arraylist.
     * @param position This parameter indicates the position on the map that we want to check.
     * @return Returns  true if the position is valid, false otherwise
     */

    private boolean isStartingPosition(Integer position) {
        return this.startingPositions.contains(position);
    }

    /**
     * This method place for the first time a player on the board,
     * If a player is already placed on the board it can't call this method.
     * @param player This parameter represent witch player I want to place on the map
     * @param position This parameter represent in witch position I want to place the player
     * @return Returns true if the placement of that player is valid
     */

    public boolean firstPlayerPlacement(Player player, Integer position) {
        placementLock.lock();
        try{
            if (player.getMapPosition() != -1){
                return false;
            }
            if (isOccupied(position)) {
                return false;
            }
            if (!isStartingPosition(position)) {
                return false;
            }

            setPlayerOnMap(player, position);
        player.setMapPosition(position);
        checkRankings();
        return true;
        }finally {
            placementLock.unlock();
        }
    }

    /**
     * This method represent the movement of a player on the map,
     * For every movement the ranking position gets updated and if a player gets dubbed it will be eliminated
     * @param player This parameter represent witch player I want to move
     * @param positions This parameter represent by how many positions i want to move the player
     */

    public void movePlayerForward(Player player, Integer positions) {
        // At the start I "lift" the player from the map, so I don't have to consider his presence on the map
        map[player.getMapPosition()] = null;
        int nextPlayerPosition;
        for (int i = 0; i < positions; i++) {
            nextPlayerPosition  = (player.getMapPosition() + 1);
            if (nextPlayerPosition >= dimension) {
                nextPlayerPosition = nextPlayerPosition % dimension;
                player.setLaps(player.getLaps() + 1);
            }
            if (!isOccupied(nextPlayerPosition)) {
                player.setMapPosition(nextPlayerPosition);
            } else {
                while (isOccupied(nextPlayerPosition)) {
                    player.checkDubbed(map[nextPlayerPosition]);
                    nextPlayerPosition = (nextPlayerPosition + 1);
                    if (nextPlayerPosition >= dimension) {
                        nextPlayerPosition = nextPlayerPosition % dimension;
                        player.setLaps(player.getLaps() + 1);
                    }

                }
                player.setMapPosition(nextPlayerPosition);
            }
        }
        // Now that I know his next position I can place it on the map
        map[player.getMapPosition()] = player;
        checkRankings();
    }

    /**
     * This method represent the movement of a player on the map,
     * For every movement the ranking position gets updated and if a player gets dubbed it will be eliminated
     * (int this case is the moving player to get dubbed)
     * @param player This parameter represent witch player I want to move
     * @param positions This parameter represent by how many positions i want to move the player
     */

    public void movePlayerBackward(Player player, Integer positions) {
        // At the start I "lift" the player from the map, so I don't have to consider his presence on the map
        map[player.getMapPosition()] = null;
        int nextPlayerPosition;
        for (int i = 0; i < positions; i++) {
            nextPlayerPosition = (player.getMapPosition() - 1);
            if (nextPlayerPosition < 0) {
                nextPlayerPosition += dimension;
                player.setLaps(player.getLaps() - 1);
            }
            if (!isOccupied(nextPlayerPosition)) {
                player.setMapPosition(nextPlayerPosition);
            } else {
                while (isOccupied(nextPlayerPosition)) {
                    map[nextPlayerPosition].checkDubbed(player);
                    nextPlayerPosition--;
                    if (nextPlayerPosition < 0) {
                        nextPlayerPosition += dimension;
                        player.setLaps(player.getLaps() - 1);
                    }
                }
                player.setMapPosition(nextPlayerPosition);
            }
        }
        // Now that I know his next position I can place it on the map
        map[player.getMapPosition()] = player;
        checkRankings();
    }
    /**
     * Removes a player from the map when they lose the game.
     * Sets the player's lost flag to true, clears their map position, and updates the rankings.
     * @param player The player to be removed from the map.
     */
    public void removePlayer(Player player){
        placementLock.lock();
        try{
            int pos = player.getMapPosition();
            if (pos >= 0 && pos < dimension && map[pos] == player) {
                map[pos] = null;
            }
            player.setLost(true);
            player.setMapPosition(-1);
            checkRankings();
        }finally {
            placementLock.unlock();
        }
    }

    /**
     * This method updates the ranking of the players on the board.
     */

    private void checkRankings() {
        for (Player player : map) {
            if (player == null) continue;
            int position = 0;
            for (Player player2 : map) {
                if (player2 != null && player2 != player) {
                    if (player2.getLaps() > player.getLaps()) {
                        position++;
                    } else if (player2.getLaps() == player.getLaps() && player2.getMapPosition() > player.getMapPosition()) {
                        position++;
                    }
                }
            }
            switch (position) {
                case 0: player.setRankingPosition(RankingPosition.First); break;
                case 1: player.setRankingPosition(RankingPosition.Second); break;
                case 2: player.setRankingPosition(RankingPosition.Third); break;
                case 3: player.setRankingPosition(RankingPosition.Fourth); break;
            }
        }
    }



}