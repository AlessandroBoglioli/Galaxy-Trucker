package it.polimi.ingsoftware.ll13.model;

import it.polimi.ingsoftware.ll13.model.cards.GameStacks;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.*;
import it.polimi.ingsoftware.ll13.model.cards.dtos.PiratesDTO;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.WarZonePenaltyEffect;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.WarZonePenaltyType;
import it.polimi.ingsoftware.ll13.model.crew_members.Alien;
import it.polimi.ingsoftware.ll13.model.crew_members.CrewMember;
import it.polimi.ingsoftware.ll13.model.ship_board.Ship;
import it.polimi.ingsoftware.ll13.model.ship_board.ShipCell;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.ShipMatrixConfig;
import it.polimi.ingsoftware.ll13.model.ship_board.ship_components.ShipCrewPlacer;
import it.polimi.ingsoftware.ll13.model.ship_board.exceptions.InvalidCellException;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import it.polimi.ingsoftware.ll13.model.ship_board.ship_components.ShipLayout;
import it.polimi.ingsoftware.ll13.model.ship_board.ship_creator.ShipCreator;
import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;
import it.polimi.ingsoftware.ll13.model.general_enumerations.RankingPosition;
import it.polimi.ingsoftware.ll13.model.hourglass.Hourglass;
import it.polimi.ingsoftware.ll13.model.hourglass.HourglassLevel2;
import it.polimi.ingsoftware.ll13.model.hourglass.HourglassManager;
import it.polimi.ingsoftware.ll13.model.map.Map;
import it.polimi.ingsoftware.ll13.model.map.MapManager;
import it.polimi.ingsoftware.ll13.model.player.Player;
import it.polimi.ingsoftware.ll13.model.tiles.decks.FlippedTileDeck;
import it.polimi.ingsoftware.ll13.model.tiles.decks.TileDeck;
import it.polimi.ingsoftware.ll13.model.tiles.decks.TileDecksManager;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.CabinTile;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.StartingCabinTile;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.network.response.Response;
import it.polimi.ingsoftware.ll13.network.response.match_responses.*;
import it.polimi.ingsoftware.ll13.network.response.match_responses.construction.*;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class GameModel {
    private final List<Player> players;
    private GamePhase currentPhase;
    private final GameLevel currentLevel;
    private final Map gameMap;
    private boolean matchFinished = false;
    private final GameStacks gameStacks;
    private final TileDeck tiles;
    private final FlippedTileDeck flippedTileDeck;
    private final List<TileCoordinates> tempZoneTiles;
    private Hourglass hourglass;
    private int flipperId = -1;

    private final int ADJUSTED_ROW_OFFSET = 5;
    private final int ADJUSTED_COL_OFFSET = 4;


    //---->CONSTRUCTOR<----//

    public GameModel(GameLevel gameLevel,List<Player> players) {
        this.currentLevel = gameLevel;
        this.players = players;
        gameMap = new MapManager().getMap(gameLevel == GameLevel.TRY_LEVEL);
        this.gameStacks = new GameStacks(gameLevel);
        initializePlayers();
        this.tiles = new TileDecksManager().getTileDeck();
        this.flippedTileDeck = new TileDecksManager().getFlippedTileDeck();
        this.tempZoneTiles = new ArrayList<>();
        this.hourglass = new HourglassManager(60, gameLevel).getHourglass();
        this.currentPhase = GamePhase.SETUP;
    }

    //---->GETTERS<-----//

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isMatchFinished() {
        return matchFinished;
    }

    public GameLevel getCurrentLevel() {
        return currentLevel;
    }

    public GameStacks getGameStacks() {
        return gameStacks;
    }

    public Map getGameMap() {
        return gameMap;
    }

    public TileDeck getTiles() {
        return tiles;
    }

    public GamePhase getCurrentPhase(){
        return currentPhase;
    }

    public FlippedTileDeck getFlippedTileDeck() {
        return flippedTileDeck;
    }

    public Hourglass getHourglass(){
        return hourglass;
    }

    public List<TileCoordinates> getTempZoneTiles() {
        return tempZoneTiles;
    }

    public synchronized void setCurrentPhase(GamePhase phase){
        this.currentPhase = phase;
    }

    //Test purpose only//
    public void setHourglass(Hourglass hourglass){
        this.hourglass = hourglass;
    }
    //----->SETUP<-----//



    /**
     * initialization of the players game state
     */
    private void initializePlayers(){
        for(Player player : players){
            player.setShip(new ShipCreator().createShipForLevel(player.getColor(),currentLevel));
        }
    }



    public void setMatchFinished(boolean matchFinished) {
        this.matchFinished = matchFinished;
    }

    //----->CONSTRUCTION<-----//

    /**
     * method to rotate a tile after players sends a RotateTileRequest message
     * @param tile tile to rotate
     * @param times how many times to perform rotation
     * @return a status (RotatedTileResponse) response
     */
    public Response rotateTile(@NotNull Tile tile, int times){
        tile.rotateMultiple(times);
        return new RotatedTileResponse(tile,true);
    }

    /**
     * method to draw a tile (handling DrawTileRequest) either from the {@link TileDeck} or from the {@link FlippedTileDeck}
     * @param id player that sent the request
     * @param drawFromFlippedDeck boolean to identify if player asked to choose a tile from the flipped deck
     * @param flippedTileIndex if players chose to draw from the flipped deck, the wanted tiles index
     * @return a status (DrawTileResponse) response
     */
    public Response drawTile(int id, boolean drawFromFlippedDeck, int flippedTileIndex){
        if(id == flipperId){
            return new DrawTileResponse(null,false);
        }
        tempZoneTiles.clear();
        Tile drawnTile = null;
        if(drawFromFlippedDeck){
            if(flippedTileDeck.getSize() == 0){
                return new DrawTileResponse(null,false);
            }
            try{
                synchronized (flippedTileDeck){
                drawnTile = flippedTileDeck.getTilePosition(flippedTileIndex);
                }
            }catch(IndexOutOfBoundsException e){
                return new DrawTileResponse(null,false);
            }
        }else {
            synchronized (tiles){
            if(tiles.getSize() > 0){
                drawnTile = tiles.drawTile();
            }
            }
        }
        if(drawnTile != null){
            return new DrawTileResponse(drawnTile,true);
        }else {
            return new DrawTileResponse(null,false);
        }
    }


    /**
     * method to handle a DrawTileFromTempRequest, sent when a player wants to take the tile on their temp zone
     * @param player player that sent the request
     * @param row row of the selected cell
     * @param col column of the selected cell
     * @return a status (DrawFromTempResponse) response
     */
    public Response drawFromTemp(@NotNull Player player, int row, int col){
        if(player.getId() == flipperId){
            return new DrawFromTempResponse(null,new ArrayList<>(), false);
        }
        int adjustedRow = row-ADJUSTED_ROW_OFFSET;
        int adjustedCol = col-ADJUSTED_COL_OFFSET;
        ShipCell tempZoneCell = player.getShip().getShipLayout().getMotherBoard()[adjustedRow][adjustedCol];
        if(tempZoneCell != null && tempZoneCell.isOccupied()){
            Tile drawnTile = tempZoneCell.getTile();
            tempZoneCell.setTile(null);
            TileCoordinates tileCoordinates = new TileCoordinates(adjustedRow, adjustedCol, drawnTile);
            tempZoneTiles.add(tileCoordinates);
            List<TileCoordinates> tiles = new ArrayList<>();
            ShipCell[][] board = player.getShip().getShipLayout().getMotherBoard();
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[r].length; c++) {
                    Tile t = board[r][c].getTile();
                    if (t != null) {
                        tiles.add(new TileCoordinates(r, c, t));
                    }
                }
            }
            return new DrawFromTempResponse(drawnTile,tiles,true);
        }
        return new DrawFromTempResponse(null, new ArrayList<>(), false);

    }

    /**
     * method to handle a PlaceTileRequest, to place a tile on the ship
     * @param player player that sent the request
     * @param row row of the selected cell
     * @param col row of the selected cell
     * @param tile tile to place
     * @return true if placed, false otherwise
     */
    public boolean placeTile(@NotNull Player player, int row, int col, Tile tile){
        int adjustedRow = row-ADJUSTED_ROW_OFFSET;
        int adjustedCol = col-ADJUSTED_COL_OFFSET;
        return player.getShip().getShipLayout().addTile(tile,adjustedRow,adjustedCol);
    }
    public Response discardTile(@NotNull Player player, @NotNull Tile tile){
        if(!tempZoneTiles.isEmpty()){
            TileCoordinates tempZoneTile = tempZoneTiles.getFirst();//only one of them in there max
            player.getShip().getShipLayout().addTile(tile,tempZoneTile.getRow(),tempZoneTile.getCol());
            tempZoneTiles.removeFirst();
            List<TileCoordinates> remaining = new ArrayList<>();
            ShipCell[][] board = player.getShip().getShipLayout().getMotherBoard();
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[r].length; c++) {
                    Tile t = board[r][c].getTile();
                    if (t != null) {
                        remaining.add(new TileCoordinates(r, c, t));
                    }
                }
            }
            Ship ship = player.getShip();
            player.getShip().getShipStats().calculateAllStats(player.getShip().getShipLayout());
            ship.getBatteryManager().fillBatteryList(ship.getShipLayout());
            ship.getBatteryManager().calculateBatteries(ship.getShipLayout());
            ship.getCargosManager().fillCargoHoldList(ship.getShipLayout());
            return new UpdatedShip(remaining, true, ship.getBatteryManager().getBatteries(),ship.getShipStats().getCrewMembers(),ship.getShipLayout().getWasteTiles(),ship.getCargosManager().calculateTotalCargosPoints(),ship.getShipStats().getExposedConnectors(),player.getCredits());
        }else{
            synchronized (flippedTileDeck){flippedTileDeck.addTile(tile);
            }
            return new UpdatedFlippedDeck(flippedTileDeck.getTiles());
        }
    }

    /**
     * method to handle a PlaceRocketRequest, sent by client when he finishes his ship assembly
     * @param player that sent the request
     * @param rankingPosition position he want to be placed in
     * @return true if placed, false otherwise
     */
    public boolean placeRocketInMap(@NotNull Player player, RankingPosition rankingPosition){
        int position = getPositionForRanking(rankingPosition);
        return gameMap.firstPlayerPlacement(player,position);
    }
    private int getPositionForRanking(@NotNull RankingPosition rankingPosition){
        // First player gets the last position
        return switch (rankingPosition) {
            case First -> gameMap.getStartingPositions().get(3);
            case Second -> gameMap.getStartingPositions().get(2);
            case Third -> gameMap.getStartingPositions().get(1);
            case Fourth -> gameMap.getStartingPositions().get(0);
        };
    }

    /**
     * this method checks if every players has placed its rocket after his ship is ready, and if did not
     * automatically gives him a position after time is over, randomly
     */
    public synchronized void autoPlaceRemainingRockets(){
        List<Integer> availablePositions = new ArrayList<>(gameMap.getStartingPositions());
        for(Player player : players){
            if (player.getMapPosition() != -1) {
                availablePositions.remove(Integer.valueOf(player.getMapPosition()));
            }
        }
        Collections.shuffle(availablePositions);
        for (Player player : players) {
            if (player.getMapPosition() == -1) {
                int randomPosition = availablePositions.removeFirst();
                gameMap.firstPlayerPlacement(player, randomPosition);
            }
        }

    }

    /**
     * method called when construction phase starts, or when hourglass gets flipped (in a level2 game), and it starts the hourglass thread
     * @param onHourglassFinished callback thread to execute once hourglass finishes
     */
    public void startHourglass(Runnable onHourglassFinished){
        hourglass.startTime(onHourglassFinished);
    }

    /**
     * method to handle an hourglass flip request, and it starts the hourglass thread, if can
     * @param id player that flipped the hourglass
     * @return true if flipped, false otherwise
     */
    public boolean flipHourglass(int id){
        if(currentLevel == GameLevel.TRY_LEVEL){
            return false;
        }
        else{
            boolean flipped = ((HourglassLevel2) hourglass).flip(); //if false means someone already did it
            if(flipped){
                this.flipperId = id;
            }
            return flipped;
        }
    }

    //----->VALIDATION<-----//
    public boolean validateShip(@NotNull Player player)  {
        boolean isValid;
        Ship ship = player.getShip();
        try {
            isValid = ship.getShipLayout().isValidShip();
        } catch (InvalidCellException e) {
            throw new RuntimeException(e);
        }
        if(isValid){
            player.setValidated(true);
        }
        return isValid;
    }
    public void eliminateTile(@NotNull Player player, int row, int col){
        int adjustedRow = row - ADJUSTED_ROW_OFFSET;
        int adjustedCol = col - ADJUSTED_COL_OFFSET;
        Ship ship = player.getShip();
        ship.getShipLayout().eliminateTile(adjustedRow,adjustedCol);
        ShipCell[][] layout = ship.getShipLayout().getMotherBoard();
        for (int row1 = 0; row1 < ShipMatrixConfig.ROW; row1++) {
            for (int col1 = 0; col1 < ShipMatrixConfig.COL; col1++) {
                ShipCell cell = layout[row1][col1];
                if (cell != null && cell.getTile() != null) {
                }
            }
        }

    }
    //----->PLACE CREW MEMBERS<-----//
    public synchronized boolean placeCrewMembers(@NotNull Player player, int row, int col, CrewMember crewMember){
        int adjustedRow = row - ADJUSTED_ROW_OFFSET;
        int adjustedCol = col - ADJUSTED_COL_OFFSET;
        Ship ship = player.getShip();
        ShipCrewPlacer crewPlacer= ship.getShipCrewPlacer();
        if(crewPlacer.getAlienCount() == 2 && crewMember instanceof Alien){
            return false;
        }
        boolean placed = crewPlacer.placeCrewMembers(ship.getShipLayout(), adjustedRow, adjustedCol, crewMember);
        if (placed && crewPlacer.isAllCabinPopulated(ship.getShipLayout())) {
            player.setCrewPlaced(true);
        }
        ship.getShipStats().calculateCrewMembers(ship.getShipLayout());
        return placed;
    }
    public ArrayList<TileCoordinates> getAllCabinTileCoordinates(Player player) {
        ArrayList<TileCoordinates> result = new ArrayList<>();
        ShipLayout layout = player.getShip().getShipLayout();
        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            for (int col = 0; col < ShipMatrixConfig.COL; col++) {
                ShipCell cell = layout.getMotherBoard()[row][col];
                if (cell != null && cell.getTile() instanceof CabinTile && !(cell.getTile() instanceof StartingCabinTile)) {
                    CabinTile cabinTile = (CabinTile) cell.getTile();
                    if(cabinTile.getCrewCount() == 0){
                        result.add(new TileCoordinates(row,col,cabinTile));
                    }
                }
            }
        }
        return result;
    }

    public int countPlayersInGame() {
        int count = 0;
        for (Player player : getPlayers()) {
            if(!player.hasLost()) {
                count++;
            }
        }
        return count;
    }

    public int getLastPlayerId() {
        List<Player> inGamePlayers = new ArrayList<Player>();
        for (Player player : getPlayers()) {
            if(!player.hasLost()) {
                inGamePlayers.add(player);
            }
        }

        int lastPlayerId = inGamePlayers.getFirst().getId();
        for(Player player : inGamePlayers){
            if(translateRankingPosition(lastPlayerId) < translateRankingPosition(player.getId())) {
                lastPlayerId = player.getId();
            }
        }

        return lastPlayerId;
    }

    public int getPlayerIdWithLowestCrew() {
        List<Player> inGamePlayers = new ArrayList<Player>();
        for (Player player : getPlayers()) {
            if(!player.hasLost()) {
                inGamePlayers.add(player);
            }
        }

        int playerIdWithLowestCrew = inGamePlayers.getFirst().getId();
        for(Player player : inGamePlayers){
            if(player.getShip().getShipStats().getCrewMembers() < getPlayerById(playerIdWithLowestCrew).getShip().getShipStats().getCrewMembers()) {
                playerIdWithLowestCrew = player.getId();
            }
        }
        return playerIdWithLowestCrew;
    }

    //----->CARD PHASE<-----//
    // ----------------------------
    // Handling of the card drawing
    // ----------------------------
    /**
     * This method pick a card from the stack and return the card correctly formatted.
     * @param playerId the id of the player.
     * @return the card that needs to be sent to the controller.
     */
    public Card handleDrawCardDrawing(int playerId) {
        // Check if the game is ended
        if(getGameStacks().getStacks().isEmpty() || getGameStacks().getStacks().get(0).getCards().isEmpty()) {
            // End game
            return null;
        }

        // Drawing of the card and handling of the stack
        Card card = getGameStacks().getStacks().getFirst().getCards().getFirst();
        initializeCardDTOValues(card, playerId);

        getGameStacks().getStacks().getFirst().getCards().removeFirst();
        if(getGameStacks().getStacks().get(0).getCards().isEmpty()){
            getGameStacks().getStacks().remove(0);
        }

        // Send to controller that will send the request
        return card;
    }

    private void initializeCardDTOValues(Card card, int playerId) {
        if(card instanceof AbandonedShipCard) {
            // Filling of the AbandonedShipDTO
            ((AbandonedShipCard)card).getAbandonedShipDTO().setPlayerId(playerId);
            return;
        }
        if(card instanceof AbandonedStationCard) {
            ((AbandonedStationCard)card).getAbandonedStationDTO().setPlayerId(playerId);
            return;
        }
        if(card instanceof MeteorShowerCard) {
            ((MeteorShowerCard)card).getMeteorShowerDTO().setPlayerId(playerId);

            if(((MeteorShowerCard)card).getMeteorShowerDTO().getMeteorNumber() == -1)
                ((MeteorShowerCard)card).getMeteorShowerDTO().setMeteorNumber(0);

            // Creation of the random direction of the meteor
            Random random = new Random();
            ((MeteorShowerCard)card).getMeteorShowerDTO().setMeteorDirectionNumber(random.nextInt(11) + 2);
            return;
        }
        if(card instanceof OpenSpaceCard) {
            ((OpenSpaceCard)card).getOpenSpaceDTO().setPlayerId(playerId);
            return;
        }
        if(card instanceof PirateCard) {
            ((PirateCard)card).getPiratesDTO().setPlayerId(playerId);

            if(((PirateCard)card).getPiratesDTO().getFireShotNumber() == -1)
                ((PirateCard)card).getPiratesDTO().setFireShotNumber(0);

            // Creation of the random direction of the fireShot
            Random random = new Random();
            ((PirateCard)card).getPiratesDTO().setFireShotDirectionNumber(random.nextInt(11) + 2);
            return;
        }
        if(card instanceof PlanetsCard) {
            ((PlanetsCard)card).getPlanetsDTO().setPlayerId(playerId);
            return;
        }
        if(card instanceof SlaversCard) {
            ((SlaversCard)card).getSlaversDTO().setPlayerId(playerId);
            return;
        }
        if(card instanceof SmugglersCard) {
            ((SmugglersCard)card).getSmugglersDTO().setPlayerId(playerId);
            return;
        }
        if(card instanceof WarZoneCard) {
            if(((WarZoneCard)card).getWarZoneDTO().getPenaltyNumber() == -1){
                ((WarZoneCard)card).getWarZoneDTO().setPenaltyNumber(0);
            }

            Random random = new Random();
            int randomNumber = random.nextInt(11) + 2;
            ((WarZoneCard)card).getWarZoneDTO().setFireShotDirectionNumber(randomNumber);
        }
    }

    private int getIdLowestCrew(Card card) {
        int idWithLowestCrew = getPlayers().getFirst().getId();
        int lowestCrew = getPlayers().getFirst().getShip().getShipStats().getCrewMembers();
        for(Player player : getPlayers()) {
            if(player.getShip().getShipStats().getCrewMembers() < lowestCrew) {
                idWithLowestCrew = player.getId();
            }
        }
        return idWithLowestCrew;
    }

    // ----------------------------
    // Handling of the card effects
    // ----------------------------
    /**
     * Handles the effect of an abandoned ship card on the game model.
     * This method applies the effect of the card on the current game state and determines
     * if specific game-ending conditions are met for the involved player.
     *
     * @param card the card representing the abandoned ship effect to be applied.
     * @return true if the card's effect is successfully applied, false otherwise.
     */
    public boolean handleAbandonedShipEffect(Card card) {
        boolean result = card.applyEffect(this);

        // Controls for the end of the game for the current player
        setLostIfDubbed(card);
        setLostIfNoCrew(card);

        return result;
    }

    /**
     * Handles the effect of an abandoned station card on the game model.
     * This method applies the effect of the card on the current game state and checks
     * for specific game-ending conditions for the involved player.
     *
     * @param card the card representing the abandoned station effect to be applied.
     * @return true if the card's effect is successfully applied, false otherwise.
     */
    public boolean handleAbandonedStationEffect(Card card) {
        boolean result = card.applyEffect(this);

        // Controls for the end of the game for the current player
        setLostIfDubbed(card);
        setLostIfNoCrew(card);

        return result;
    }

    /**
     * Handles the effect of an epidemic card on the game model.
     * This method applies the effect of the epidemic card on the current game state,
     * potentially triggering specific game-ending conditions for the current player.
     *
     * @param card the card representing the epidemic effect to be applied
     * @return true if the card's effect is successfully applied, false otherwise
     */
    public boolean handleEpidemicEffect(Card card) {
        boolean result = card.applyEffect(this);

        // Controls for the end of the game for the current player
        setLostIfNoCrew(card);

        return result;
    }

    /**
     * Handles the effect of a meteor shower card on the game model.
     * This method applies the meteor shower card's effect to the current game state
     * and checks for game-ending conditions related to the involved player, such as
     * the absence of crew or critical components like the central cabin.
     *
     * @param card the meteor shower card that triggers the effect to be applied
     * @return true if the card's effect is successfully applied, false otherwise
     */
    public boolean handleMeteorShowerEffect(Card card) {
        boolean result = card.applyEffect(this);

        // Controls for the end of the game for the current player
        setLostIfNoCrew(card);
        setLostIfNoCentralCabin(card);

        return result;
    }

    /**
     * Handles the effect of an open space card on the game model.
     * This method applies the effect of the card to the current game state
     * and evaluates the outcome for the player. If the effect indicates failure,
     * it marks the player as having lost.
     *
     * @param card the card representing the open space effect to be applied
     */
    public void handleOpenSpaceEffect(Card card) {
        boolean result = card.applyEffect(this);

        // Controls for the end of the game for the current player
        if(!result) {
            setLost(card);
        }
    }

    /**
     * Handles the pirate effect for a given card by applying its effect and updating the player's game state.
     *
     * @param card the card whose effect is to be applied
     * @return true if the card's effect was successfully applied, false otherwise
     */
    public boolean handlePirateEffect(Card card) {
        boolean result = card.applyEffect(this);

        // Controls for the end of the game for the current player
        setLostIfDubbed(card);
        setLostIfNoCrew(card);
        setLostIfNoCentralCabin(card);

        return result;
    }

    /**
     * Processes the effect of a given card on the current state of a planetary system.
     *
     * @param card the card whose effect is to be applied
     * @return true if the card's effect was successfully applied, false otherwise
     */
    public boolean handlePlanetsEffect(Card card) {
        boolean result = card.applyEffect(this);

        // Controls for the end of the game for the current player
        setLostIfDubbed(card);

        return result;
    }

    /**
     * Handles the effects of the Slaver's card on the current player by applying the card's effect and
     * controlling game state conditions such as crew depletion or game loss conditions.
     *
     * @param card the card whose effect is to be applied to the current player
     * @return true if the effect was successfully applied, false otherwise
     */
    public boolean handleSlaversEffect(Card card) {

        boolean result = card.applyEffect(this);
        // Controls for the end of the game for the current player
        setLostIfNoCrew(card);
        setLostIfDubbed(card);

        return result;
    }

    /**
     * Handles the effect of a smuggler card and applies game logic based on its result.
     *
     * @param card the card whose effect is to be handled and applied to the current player
     * @return true if the effect was successfully applied, false otherwise
     */
    public boolean handleSmugglersEffect(Card card) {
        boolean result = card.applyEffect(this);

        // Controls for the end of the game for the current player
        setLostIfDubbed(card);

        return result;
    }

    /**
     * Handles the effect of stellar space dust on the given card and updates the game state accordingly.
     *
     * @param card the card on which the stellar space dust effect is to*/
    public boolean handleStellarSpaceDustEffect(Card card) {
        boolean result = card.applyEffect(this);

        // Controls for the end of the game for the current player
        setLostIfDubbed(card);

        return result;
    }

    /**
     * Handles the effects of a specific war zone card on the current game state,
     * applying the card's effect and checking various game end conditions.
     *
     * @param card the card whose effect is to be applied
     * @return true if the card effect was successfully applied, false otherwise
     */
    public boolean handleWarZoneEffect(Card card) {
        boolean result = card.applyEffect(this);

        // Controls for the end of the game for the current player
        setLostIfDubbed(card);
        setLostIfNoCentralCabin(card);
        setLostIfNoCrew(card);

        return result;
    }

    private void setLostIfDubbed(Card card) {
        if(getPlayerById(card.getDTO().getPlayerId()) == null){
            return;
        }
        if(getPlayerById(card.getDTO().getPlayerId()).isDubbed()) {
            getPlayerById(card.getDTO().getPlayerId()).setLost(true);
        }
    }

    private void setLostIfNoCrew(Card card) {
        if(getPlayerById(card.getDTO().getPlayerId()) == null){
            return;
        }
        if(getPlayerById(card.getDTO().getPlayerId()).getShip().getShipStats().getCrewMembers() <= 0) {
            getPlayerById(card.getDTO().getPlayerId()).setLost(true);
        }
    }

    private void setLostIfNoCentralCabin(Card card) {
        Tile centralTile = getPlayerById(card.getDTO().getPlayerId())
                .getShip().getShipLayout().getMotherBoard()[ShipMatrixConfig.CENTRAL_ROW][ShipMatrixConfig.CENTRAL_COL]
                .getTile();

        if (!(centralTile instanceof StartingCabinTile)) {
            getPlayerById(card.getDTO().getPlayerId()).setLost(true);
        }

    }

    private void setLost(Card card) {
        getPlayerById(card.getDTO().getPlayerId()).setLost(true);
    }

    /**
     * Updates the AbandonedShipDTO by modifying its choice property to false
     * and sets the next player's ID based on the given card.
     *
     * @param card The card object which is expected to be an instance of
     *             AbandonedShipCard containing the AbandonedShipDTO to be*/
    public void updateAbandonedShipDTO(Card card) {
        setNextPlayerId(card);
        ((AbandonedShipCard)card).getAbandonedShipDTO().setChoice(false);
    }

    /**
     * Updates the AbandonedStationDTO associated with the provided card.
     * Sets the choice of the AbandonedStationDTO to false and updates the next player ID.
     *
     * @param card the card containing the AbandonedStationDTO to be updated
     */
    public void updateAbandonedStationDTO(Card card) {
        setNextPlayerId(card);
        ((AbandonedStationCard)card).getAbandonedStationDTO().setChoice(false);
    }

    /**
     * Updates the meteor shower data transfer object (DTO) associated with the specified card.
     * Increments the meteor number, sets a random direction number,
     * and updates the coordinates to default values.
     *
     * @param card the card that contains the meteor shower DTO to be updated.
     *             Must be of type MeteorShowerCard.
     */
    public void updateMeteorShowerDTO(Card card) {
        ((MeteorShowerCard)card).getMeteorShowerDTO().setMeteorNumber(((MeteorShowerCard)card).getMeteorShowerDTO().getMeteorNumber() + 1);

        Random random = new Random();
        int randomNumber = random.nextInt(11) + 2;
        ((MeteorShowerCard)card).getMeteorShowerDTO().setMeteorDirectionNumber(randomNumber);

        ((MeteorShowerCard)card).getMeteorShowerDTO().setCoordinates(-1, -1);
    }

    /**
     * Updates the OpenSpaceDTO associated with the given card.
     * This involves setting the next player ID and resetting the tiles with batteries.
     *
     * @param card the card containing the OpenSpaceDTO to be updated. Must be an instance of OpenSpaceCard.
     */
    public void updateOpenSpaceDTO(Card card) {
        setNextPlayerId(card);
        ((OpenSpaceCard)card).getOpenSpaceDTO().emptyUsingTilesWithBattery();
    }

    /**
     * Updates the PirateCardDTO for the next player by resetting and assigning necessary values.
     *
     * @param card The card object, which must be an instance of PirateCard, to update its related PirateCardDTO
     *             properties for the next player's turn.
     */
    public void updatePirateCardDTO_nextPlayer(Card card) {
        setNextPlayerId(card);
        ((PirateCard)card).getPiratesDTO().setFireShotNumber(-1);
        ((PirateCard)card).getPiratesDTO().emptyUsingDoubleCannons();
        ((PirateCard)card).getPiratesDTO().setUsingShield(-2,-2);

        Random random = new Random();
        int randomNumber = random.nextInt(11) + 2;
        ((PirateCard)card).getPiratesDTO().setFireShotDirectionNumber(randomNumber);
    }

    /**
     * Updates the PirateCardDTO properties for the same player. This method modifies
     * the fire shot number, generates a new random fire shot direction number, and
     * sets the using shield properties to default or specific values for the pirate card DTO.
     *
     * @param card the card object, which must be an instance of PirateCard,
     * where its PirateCardDTO properties will be updated
     */
    public void updatePirateCardDTO_samePlayer(Card card) {
        PiratesDTO dto = ((PirateCard)card).getPiratesDTO();

        if (dto.getFireShotNumber() >= -1) {
            dto.setFireShotNumber(dto.getFireShotNumber() + 1);
        }

        dto.setFireShotDirectionNumber(new Random().nextInt(11) + 2);
        dto.setUsingShield(-2, -2);
    }


    /**
     * Updates the PlanetsDTO associated with the given card.
     *
     * @param card The card containing the PlanetsDTO that needs to be updated. Must be an instance of PlanetsCard.
     */
    public void updatePlanetsDTO(Card card) {
        setNextPlayerId(card);
        ((PlanetsCard)card).getPlanetsDTO().setChoice(0);
    }

    /**
     * Updates the SlaversDTO by setting the next player ID and clearing tiles with battery usage.
     *
     * @param card the card containing the SlaversDTO and*/
    public void updateSlaversDTO(Card card) {
        setNextPlayerId(card);
        ((SlaversCard)card).getSlaversDTO().emptyUsingTilesWithBattery();
    }

    /**
     * Updates the smugglers' DTO associated with the given card.
     * This method modifies the state of the DTO by emptying its tiles with battery
     * after setting the next player ID.
     *
     * @param card the card object associated with the current operation,
     *             expected to be an instance of SmugglersCard
     */
    public void updateSmugglersDTO(Card card) {
        setNextPlayerId(card);
        ((SmugglersCard)card).getSmugglersDTO().emptyUsingTilesWithBattery();
    }

    /**
     * Updates the StellarSpaceDust Data Transfer Object (DTO) based on the given card.
     *
     * @param card the card object used to update the StellarSpaceDust DTO.
     */
    public void updateStellarSpaceDustDTO(Card card) {
        setPreviousPlayerId(card);
    }

    /**
     * Updates the WarZoneDTO of a given WarZoneCard. This method adjusts the penalty
     * number, clears the list of selected tiles, and sets a random fire shot direction number
     * if certain conditions are met.
     *
     * @param card the card object to be updated. It must be an instance of WarZoneCard.
     */
    public void updateWarZoneDTO(Card card) {
        if((((WarZoneCard)card).getPenalties().get(((WarZoneCard)card).getWarZoneDTO().getPenaltyNumber()).getWarZonePenaltyEffect() == WarZonePenaltyEffect.HIT_BY_PROJECTILES && ((WarZoneCard)card).getWarZoneDTO().getFireShotNumber() != (((WarZoneCard)card).getPenalties().get(((WarZoneCard)card).getWarZoneDTO().getPenaltyNumber()).getFireShots().size() - 1))) {
            ((WarZoneCard)card).getWarZoneDTO().setFireShotNumber(((WarZoneCard)card).getWarZoneDTO().getFireShotNumber() + 1);
            ((WarZoneCard)card).getWarZoneDTO().emptyList(); // Remove all tiles selected

            Random random = new Random();
            int randomNumber = random.nextInt(11) + 2;
            ((WarZoneCard)card).getWarZoneDTO().setFireShotDirectionNumber(randomNumber);
            return;
        }

        ((WarZoneCard)card).getWarZoneDTO().setPenaltyNumber(((WarZoneCard)card).getWarZoneDTO().getPenaltyNumber() + 1);

        ((WarZoneCard)card).getWarZoneDTO().emptyList(); // Remove all tiles selected
    }

    private void setNextPlayerId(Card card) {
        int actualPlayerId = card.getDTO().getPlayerId();
        int nextRankingPosition = getNextRankingPosition(actualPlayerId);
        card.getDTO().setPlayerId(nextRankingPosition);
    }

    private void setPreviousPlayerId(Card card) {
        int actualPlayerId = card.getDTO().getPlayerId();
        int previousRankingPosition = getPreviousRankingPosition(actualPlayerId);
        card.getDTO().setPlayerId(previousRankingPosition);
    }
    public int getNextRankingPosition(int currentPlayerId) {
        List<Player> activePlayers = getPlayers().stream()
                .filter(p -> !p.hasLost())
                .sorted(Comparator.comparing(Player::getRankingPosition))
                .toList();

        for (int i = 0; i < activePlayers.size(); i++) {
            if (activePlayers.get(i).getId() == currentPlayerId && i + 1 < activePlayers.size()) {
                return activePlayers.get(i + 1).getId();
            }
        }
        return -1;
    }


    private int getPreviousRankingPosition(int actualPlayerId) {
        List<Player> activePlayers = getPlayers().stream()
                .filter(p -> !p.hasLost())
                .sorted(Comparator.comparing(Player::getRankingPosition))
                .toList();

        for (int i = 0; i < activePlayers.size(); i++) {
            if (activePlayers.get(i).getId() == actualPlayerId && i - 1 >= 0) {
                return activePlayers.get(i - 1).getId();
            }
        }
        return -1;
    }

    /**
     * Translates the ranking position of a player to a zero-based index.
     *
     * @param playerId the unique identifier of the player whose ranking position is to be translated
     * @return the zero-based index corresponding to the player's ranking position, or -1 if the position is not recognized
     */
    public int translateRankingPosition(int playerId) {
        List<Player> activePlayers = getPlayers().stream()
                .filter(p -> !p.hasLost())
                .sorted(Comparator.comparing(Player::getRankingPosition))
                .toList();

        for (int i = 0; i < activePlayers.size(); i++) {
            if (activePlayers.get(i).getId() == playerId) {
                return i;
            }
        }

        return -1;
    }


    /**
     * Retrieves the ID of the first player based on their ranking position.
     *
     * @return the ID of the player with the first ranking position, or -1 if no such player exists.
     */
    public int getFirstPlayerId() {
        for(Player player : getPlayers()) {
            if(player.getRankingPosition() == RankingPosition.First) {
                return player.getId();
            }
        }
        return -1;
    }

    /**
     *
     */
    public double calculateFirePower(Card card) {
        return getPlayerById(card.getDTO().getPlayerId()).getShip().getShipStats().calculateTotalFirePower(
                getPlayerById(card.getDTO().getPlayerId()).getShip().getShipLayout(),
                getPlayerById(card.getDTO().getPlayerId()).getShip().getShipCrewPlacer(),
                getPlayerById(card.getDTO().getPlayerId()).getShip().getBatteryManager(),
                ((WarZoneCard)card).getWarZoneDTO().getUsingTilesWithBattery()
        );
    }

    /**
     * Calculates the total thrust power for the ship associated with the given card.
     *
     * @param card the card containing the necessary data to calculate thrust power,
     *             including the player's ship details and the war zone information
     * @return the total thrust power for the player's ship based on its current layout,
     *         battery manager, and the tiles being used with the battery
     */
    public int calculateThrustPower(Card card) {
        return getPlayerById(card.getDTO().getPlayerId()).getShip().getShipStats().calculateTotalThrustPower(
                getPlayerById(card.getDTO().getPlayerId()).getShip().getShipLayout(),
                getPlayerById(card.getDTO().getPlayerId()).getShip().getShipCrewPlacer(),
                getPlayerById(card.getDTO().getPlayerId()).getShip().getBatteryManager(),
                ((WarZoneCard)card).getWarZoneDTO().getUsingTilesWithBattery()
        );
    }

    //-----------//
    public Player getPlayerById(int id){
        for(Player player : players){
            if(player.getId() == id){
                return player;
            }
        }
        return null;
    }
    public void fillTryLevelShip(){
        for(Player player1 : players){
            Ship ship = player1.getShip();
            ship.getShipCrewPlacer().placeCrewTryLevel(ship.getShipLayout());
        }
    }

    public synchronized void removePlayer(Player player){
        gameMap.removePlayer(player);
        player.setLost(true);
    }
}
