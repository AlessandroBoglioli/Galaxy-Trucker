package it.polimi.ingsoftware.ll13.server.controller;

import it.polimi.ingsoftware.ll13.model.GameModel;
import it.polimi.ingsoftware.ll13.model.GamePhase;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.*;
import it.polimi.ingsoftware.ll13.model.cards.decks.CardStack;
import it.polimi.ingsoftware.ll13.model.cards.dtos.*;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.WarZonePenaltyEffect;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.WarZonePenaltyType;
import it.polimi.ingsoftware.ll13.model.crew_members.Alien;
import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.general_enumerations.RankingPosition;
import it.polimi.ingsoftware.ll13.model.hourglass.HourglassLevel2;
import it.polimi.ingsoftware.ll13.model.player.Player;
import it.polimi.ingsoftware.ll13.model.ship_board.Ship;
import it.polimi.ingsoftware.ll13.model.ship_board.ShipCell;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.network.requests.ViewShipRequest;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.adventure.*;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.construction.*;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.validation.EliminateTileRequest;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.validation.PlaceCrewRequest;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.validation.ValidateShipRequest;
import it.polimi.ingsoftware.ll13.network.response.Dtos.PlayerDTO;
import it.polimi.ingsoftware.ll13.network.response.Response;
import it.polimi.ingsoftware.ll13.network.response.adventure.*;
import it.polimi.ingsoftware.ll13.network.response.match_responses.*;
import it.polimi.ingsoftware.ll13.network.response.match_responses.construction.*;
import it.polimi.ingsoftware.ll13.network.response.match_responses.phase_change.AdventurePhase;
import it.polimi.ingsoftware.ll13.network.response.match_responses.phase_change.ConstructionPhase;
import it.polimi.ingsoftware.ll13.network.response.match_responses.phase_change.CrewPlacePhaseStarted;
import it.polimi.ingsoftware.ll13.network.response.match_responses.phase_change.ValidationPhaseStarted;
import it.polimi.ingsoftware.ll13.network.response.match_responses.validation.CrewStatusResponse;
import it.polimi.ingsoftware.ll13.network.response.match_responses.validation.ValidatedShip;
import it.polimi.ingsoftware.ll13.server.ClientNotifier;
import it.polimi.ingsoftware.ll13.server.Server;
import it.polimi.ingsoftware.ll13.utils.Observable;


import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class GameController extends Observable implements Controller, Runnable {
    private static GameController instance;
    private GameModel gameModel;
    private List<Player> players;
    private boolean isStarted = false;
    private boolean isActive = true;
    private ArrayList<ClientNotifier> clientNotifiers;
    private BlockingQueue<Request> requests;
    private GamePhase gamePhase;
    private List<Player> loggedPlayers = new ArrayList<>();

    private boolean crewPhaseAlreadyStarted = false;
    private boolean adventurePhaseAlreadyStarted = false;

    private int numberOfEffects = 0;
    private int meteorShowerExpectedEffects = -1;
    private double worstFirePower = 0;
    private int worstThrustPower = 0;
    private int worstId = 0;

    public static GameController getInstance(){
        if(instance==null){
            instance=new GameController();
        }
        return instance;
    }

    public BlockingQueue<Request> getRequests(){
        return this.requests;
    }
    public GamePhase getGamePhase(){
        return this.gamePhase;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public int getNumberOfEffects() {
        return numberOfEffects;
    }

    public double getWorstFirePower() {
        return worstFirePower;
    }

    public int getWorstThrustPower() {
        return worstThrustPower;
    }

    public int getWorstId() {
        return worstId;
    }

    public boolean isStarted() {
        return isStarted;
    }

    // --> Setters <--

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void submitRequest(Request request) throws InterruptedException {
        requests.put(request);
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void setNumberOfEffects(int numberOfEffects) {
        this.numberOfEffects = numberOfEffects;
    }

    public void setWorstFirePower(double worstFirePower) {
        this.worstFirePower = worstFirePower;
    }

    public void setWorstThrustPower(int worstThrustPower) {
        this.worstThrustPower = worstThrustPower;
    }

    public void setWorstId(int worstId) {
        this.worstId = worstId;
    }

    public void restoreNumberOfEffects() {
        this.numberOfEffects = 0;
    }

    @Override
    public void run() {
        try{
            while(isActive){
                Request request = requests.take();
                request.execute(this);
            }
        } catch (InterruptedException e) {
            Server.logError("failed handling request");
        }
    }

    public Player getPlayerById(int id){
        for(Player player : players){
            if(player.getId() == id){
                return player;
            }
        }
        return null;
    }

    public void startMatch( ArrayList<ClientNotifier> clientNotifiers, List<Player> loggedPlayers, GameLevel gameLevel){
        this.clientNotifiers = clientNotifiers;
        this.players = loggedPlayers; // used for game messages purposes
        this.loggedPlayers = loggedPlayers; // used for disconnecting purposes
        this.requests = new LinkedBlockingDeque<>();
        this.gameModel = new GameModel(gameLevel, players);
        setStarted(true);
        setupObservers();
        for(Player player : players){
            List<TileCoordinates> initialTiles = getShipTiles(player);
            if(player.getUsername() == players.getFirst().getUsername()){
                notify(new MatchStartedResponse(initialTiles, true, gameLevel),player.getId());
            }else {
                notify(new MatchStartedResponse(initialTiles, false, gameLevel), player.getId());
            }
        }
        Thread gameController = new Thread(this);
        gameController.start();
    }


    //----->StartMatch<-----//
    //when the players start they get their screen setup, this request, the first players (host) view will be different  he will have
    //an option for starting the construction phase

    /**
     * method called by the host, when he sends the start construction phase request, and it basically triggers
     * the hourglass thread to start, and notifies all players of the game state
     */
    public void startConstruction() throws InterruptedException {
        List<CardStack> cardStacks = gameModel.getGameStacks().getStacks();
        notifyAll(new ConstructionPhase(gameModel.getTiles().getSize(),gameModel.getFlippedTileDeck().getTiles(), cardStacks)); //will start timer and show options to draw tiles
        gameModel.startHourglass(() -> {
            if (gameModel.getCurrentLevel() == GameLevel.TRY_LEVEL) {
                gameModel.autoPlaceRemainingRockets();
                List<PlayerDTO> playerPositionDTOS = createPlayerPositionDTOs(gameModel.getPlayers());
                notifyAll(new UpdateMap(playerPositionDTOS,true));
                gameModel.setCurrentPhase(GamePhase.VALIDATION);
                for(Player player : players){
                    Ship ship = player.getShip();
                    ship.getShipLayout().getMotherBoard()[0][5].clearTile();
                    ship.getShipLayout().getMotherBoard()[0][6].clearTile();
                }
                notifyAll(new ValidationPhaseStarted(GamePhase.VALIDATION));
            }
            else if (gameModel.getCurrentLevel() == GameLevel.LEVEL_2 && ((HourglassLevel2)gameModel.getHourglass()).isFlipped()) {
                gameModel.autoPlaceRemainingRockets();
                List<PlayerDTO> playerPositionDTOS = createPlayerPositionDTOs(gameModel.getPlayers());
                notifyAll(new UpdateMap(playerPositionDTOS,true));
                gameModel.setCurrentPhase(GamePhase.VALIDATION);
                for(Player player : players){
                    Ship ship = player.getShip();
                    ship.getShipLayout().getMotherBoard()[0][5].clearTile();
                    ship.getShipLayout().getMotherBoard()[0][6].clearTile();
                }
                notifyAll(new ValidationPhaseStarted(GamePhase.VALIDATION));
            }
        });
    }

    //the player that sends the flip hourglass request cannot modify his ship for the remaining time

    /**
     * method invoked when a player decides to flip the hourglass
     * @param flipHourglassRequest request sent by the player
     */
    public void handleFlipHourglassRequest(FlipHourGlassRequest flipHourglassRequest){
        int id = flipHourglassRequest.getSenderId();
        boolean flipped = gameModel.flipHourglass(id);
        if(flipped){
            gameModel.startHourglass(() -> {
                gameModel.autoPlaceRemainingRockets();

                List<PlayerDTO> playerPositionDTOS = createPlayerPositionDTOs(gameModel.getPlayers());
                notifyAll(new UpdateMap(playerPositionDTOS,true));
                gameModel.setCurrentPhase(GamePhase.VALIDATION);
                notifyAll(new ValidationPhaseStarted(GamePhase.VALIDATION));
                for(Player player : players){
                    Ship ship = player.getShip();
                    ship.getShipLayout().getMotherBoard()[0][5].clearTile();
                    ship.getShipLayout().getMotherBoard()[0][6].clearTile();
                }
            });
            notifyAll(new NotifyHourglass(true)); // Started last round
        } else {
            notify(new NotifyHourglass(false), id);
        }
    }

    /**
     * method invoked when a player wants to rotate a tile
     * @param rotateTileRequest request sent by the client
     */
    public void handleTileRotateRequest(RotateTileRequest rotateTileRequest){
        Tile tile = rotateTileRequest.getTile();
        int times = rotateTileRequest.getRotationTimes();
        Response response= gameModel.rotateTile(tile,times);
        notify(response,rotateTileRequest.getSenderId());
    }
    /**
     * method invoked when a player wants view another players ship
     * @param viewShipRequest request sent by the client
     */
    public void handleViewShipRequest(ViewShipRequest viewShipRequest) {
        PlayerColors color = viewShipRequest.getColor();
        Player requestPlayer = getPlayerById(viewShipRequest.getSenderId());
        Player targetPlayer = null;
        for (Player player : players) {
            if (player.getColor() == color) {
                targetPlayer = player;
                break;
            }
        }
        if (targetPlayer == null) {
            return;
        }
        List<TileCoordinates> updatedTiles = getShipTiles(targetPlayer);
        boolean isMyShip = (requestPlayer.getId() == targetPlayer.getId());
        notify(new ViewAnotherShipResponse(updatedTiles, isMyShip, color), requestPlayer.getId());
    }

    /**
     * method invoked when a player wants to draw a tile from the random tile deck or select one
     * from the flipped one
     * @param drawTileRequest request sent by the client
     */
    public void handleDrawTileRequest(DrawTileRequest drawTileRequest){
        int id = drawTileRequest.getSenderId();
        boolean flippedDeck = drawTileRequest.isDrawFromFlippedDeck();
        int index = drawTileRequest.getFlippedTileIndex();
        DrawTileResponse response = (DrawTileResponse) gameModel.drawTile(id,flippedDeck,index);
        if(response.getDrawnTile() == null){
            notify(response,drawTileRequest.getSenderId());
        }else {
            notify(response,drawTileRequest.getSenderId());
            if(drawTileRequest.isDrawFromFlippedDeck()){
                notifyAll(new UpdatedFlippedDeck(gameModel.getFlippedTileDeck().getTiles()));
                return;
            }
            notifyAll(new UpdatedTileDeck());
        }
    }

    /**
     * method invoked when a player decides to select a tile he placed in the temp zone of his ship
     * @param drawTileTempRequest request sent by the client
     */
    public void handleDrawTileTempRequest(DrawTileTempRequest drawTileTempRequest){
        Player player = getPlayerById(drawTileTempRequest.getSenderId());
        int row = drawTileTempRequest.getRow();
        int col = drawTileTempRequest.getCol();
        DrawFromTempResponse response = (DrawFromTempResponse) gameModel.drawFromTemp(player,row,col);
        if(response.getTile() == null){
            notify(response,drawTileTempRequest.getSenderId());
        }else {
            notify(response,player.getId()); //has a new ship used to update
        }

    }

    /**
     * method invoked when a player decides where to place the tile he drawn
     * @param placeTileRequest request sent by the client
     */
    public void handlePlaceTileRequest(PlaceTileRequest placeTileRequest){
        Player player = getPlayerById(placeTileRequest.getSenderId());
        Tile tile = placeTileRequest.getTile();
        int row = placeTileRequest.getRow();
        int col = placeTileRequest.getCol();
        boolean placed = gameModel.placeTile(player,row,col,tile);
        if(placed){
            List<TileCoordinates> tiles = getShipTiles(player);
            Ship ship = player.getShip();
            player.getShip().getShipStats().calculateAllStats(player.getShip().getShipLayout());
            ship.getBatteryManager().fillBatteryList(ship.getShipLayout());
            ship.getBatteryManager().calculateBatteries(ship.getShipLayout());
            ship.getCargosManager().fillCargoHoldList(ship.getShipLayout());
            notify(new UpdatedShip(tiles, true,ship.getBatteryManager().getBatteries(),ship.getShipStats().getCrewMembers(),ship.getShipLayout().getWasteTiles(),ship.getCargosManager().calculateTotalCargosPoints(),ship.getShipStats().getExposedConnectors(),player.getCredits()), player.getId());
        }else{
            notify(new TilePlacedResponse(tile,false),player.getId());
        }
    }

    /**
     * method invoked when a player decides to discard a tile he drawn
     * @param discardRequest request sent by the client
     */
    public void handleDiscardRequest(DiscardRequest discardRequest){
        Player player = getPlayerById(discardRequest.getSenderId());
        Tile tile = discardRequest.getTile();
        Response response = gameModel.discardTile(player,tile);
        if(response instanceof UpdatedShip){
            notify(response, player.getId()); //updated ship request
        }else{
            notifyAll(response); //updated flipped tile deck
        }
    }

    /**
     * method invoked when a player decides to place his rocket on the map
     * @param placeRocketRequest request sent by the client
     */
    public void handlePlaceRocketOnMapRequest(PlaceRocketRequest placeRocketRequest){
        Player player = getPlayerById(placeRocketRequest.getSenderId());
        RankingPosition rankingPosition = placeRocketRequest.getPosition();
        boolean placed = gameModel.placeRocketInMap(player,rankingPosition);
        if(placed){
            List<PlayerDTO> playerPositionDTOS = createPlayerPositionDTOs(gameModel.getPlayers());
            notifyAll(new UpdateMap(playerPositionDTOS,true));
        }else{
            List<PlayerDTO> playerPositionDTOS = createPlayerPositionDTOs(gameModel.getPlayers());
            notify(new UpdateMap(playerPositionDTOS,false),player.getId()); //retry
        }
    }

    //----->Validation<-----//

    /**
     * method invoked when a player asks his ship to be validated
     * @param validateShipRequest request sent by the client
     */
    public void handleValidateShipRequest(ValidateShipRequest validateShipRequest){
        Player player = getPlayerById(validateShipRequest.getSenderId());
        boolean isValid = gameModel.validateShip(player);
        if(isValid){
            notify(new ValidatedShip(null,true),player.getId());
            checkIfAllValidated();
        }else{
            notify(new ValidatedShip(player.getShip().getShipLayout().getInvalidCells(),false),player.getId());
        }
    }

    /**
     * method invoked when a player chooses to eliminate a tile
     * @param eliminateTileRequest request sent by the client
     */
    public void handleEliminateTileRequest(EliminateTileRequest eliminateTileRequest){
        Player player = getPlayerById(eliminateTileRequest.getSenderId());
        int row = eliminateTileRequest.getRow();
        int col = eliminateTileRequest.getCol();
        gameModel.eliminateTile(player,row,col);
        List<TileCoordinates> remaining = new ArrayList<>();
        remaining = getShipTiles(player);
        Ship ship = player.getShip();
        player.getShip().getShipStats().calculateAllStats(player.getShip().getShipLayout());
        ship.getBatteryManager().fillBatteryList(ship.getShipLayout());
        ship.getBatteryManager().calculateBatteries(ship.getShipLayout());
        ship.getCargosManager().fillCargoHoldList(ship.getShipLayout());
        notify(new UpdatedShip(remaining, true,ship.getBatteryManager().getBatteries(),ship.getShipStats().getCrewMembers(),ship.getShipLayout().getWasteTiles(),ship.getCargosManager().calculateTotalCargosPoints(),ship.getShipStats().getExposedConnectors(),player.getCredits()), player.getId());
    }
    private synchronized void checkIfAllValidated(){
        if(crewPhaseAlreadyStarted){
            return;
        }
        if(adventurePhaseAlreadyStarted){
            return;
        }
        boolean ready = true;
        for(Player player : players){
            if (!player.isValidated()) {
                ready = false;
                break;
            }
        }
        if(ready){
            if(gameModel.getCurrentLevel() == GameLevel.TRY_LEVEL){
                gameModel.fillTryLevelShip();
                gameModel.setCurrentPhase(GamePhase.FLIGHT);
                calculateAllShipStats();
                for(Player player : players){
                    Ship ship = player.getShip();
                    player.getShip().getShipStats().calculateAllStats(player.getShip().getShipLayout());
                    ship.getBatteryManager().calculateBatteries(ship.getShipLayout());
                    if(player.getRankingPosition() == RankingPosition.First){
                        notify(new AdventurePhase(true,ship.getBatteryManager().getBatteries(),ship.getShipStats().getCrewMembers(),ship.getShipLayout().getWasteTiles(),ship.getCargosManager().calculateTotalCargosPoints(),ship.getShipStats().getExposedConnectors(),player.getCredits()), player.getId());
                    }else {
                        notify(new AdventurePhase(false,ship.getBatteryManager().getBatteries(),ship.getShipStats().getCrewMembers(),ship.getShipLayout().getWasteTiles(),ship.getCargosManager().calculateTotalCargosPoints(),ship.getShipStats().getExposedConnectors(),player.getCredits()), player.getId());
                    }
                }
            }else{
                crewPhaseAlreadyStarted = true;
                for(Player player : players){
                    notify(new CrewPlacePhaseStarted(gameModel.getAllCabinTileCoordinates(player)), player.getId());
                    if(player.getShip().getShipCrewPlacer().isAllCabinPopulated(player.getShip().getShipLayout())){
                        player.setCrewPlaced(true);
                    }

                }
                checkIfAllPlacedCrew();
            }
        }
    }

    /**
     * method to calculate all ship stats, very useful to update ship status before every UpdateShip response is sent to the clients
     */
    private void calculateAllShipStats(){
        for(Player player : players){
            player.getShip().getShipStats().calculateAllStats(player.getShip().getShipLayout());
        }
    }

    public void handlePlaceCrewRequest(PlaceCrewRequest placeCrewRequest){
        if(placeCrewRequest.getCrewMember() instanceof Alien){
        }
        Player player = getPlayerById(placeCrewRequest.getSenderId());
        boolean placed = gameModel.placeCrewMembers(player,placeCrewRequest.getRow(),placeCrewRequest.getCol(),placeCrewRequest.getCrewMember());
        if(placed){
            notify(new CrewStatusResponse(true, player.getShip().getShipCrewPlacer().getHumanCount(),player.getShip().getShipCrewPlacer().getPurpleAlienCount(),player.getShip().getShipCrewPlacer().getYellowAlienCount(),gameModel.getAllCabinTileCoordinates(player)), player.getId());
            checkIfAllPlacedCrew();
            return;
        }
        notify(new CrewStatusResponse(false,-1,-1,-1,gameModel.getAllCabinTileCoordinates(player)), player.getId());
    }

    private void checkIfAllPlacedCrew(){
        if (adventurePhaseAlreadyStarted) {
            return;
        }
        boolean ready = true;
        for(Player player : players){
            if (!player.isCrewPlaced()) {
                ready = false;
                break;
            }
        }
        if(ready){
            adventurePhaseAlreadyStarted = true;
            gameModel.setCurrentPhase(GamePhase.FLIGHT);
            for(Player player : players){
                Ship ship = player.getShip();
                player.getShip().getShipStats().calculateAllStats(player.getShip().getShipLayout());
                ship.getBatteryManager().calculateBatteries(ship.getShipLayout());
                if(player.getRankingPosition() == RankingPosition.First){
                    notify(new AdventurePhase(true,ship.getBatteryManager().getBatteries(),ship.getShipStats().getCrewMembers(),ship.getShipLayout().getWasteTiles(),ship.getCargosManager().calculateTotalCargosPoints(),ship.getShipStats().getExposedConnectors(),player.getCredits()), player.getId());
                }else {
                    notify(new AdventurePhase(false,ship.getBatteryManager().getBatteries(),ship.getShipStats().getCrewMembers(),ship.getShipLayout().getWasteTiles(),ship.getCargosManager().calculateTotalCargosPoints(),ship.getShipStats().getExposedConnectors(),player.getCredits()), player.getId());
                }
            }
        }
    }


    // --> Cards <--
    // Obtaining of the cards from the gameModel, elaboration of them and sending to the client

    /**
     * Handles the request from a player to draw a card during the game.
     * The method retrieves a card from the game model according to the player's request.
     * If no card is available, it notifies all players with an end-game response.
     * Otherwise, it sends the drawn card as a response to the requesting player.
     *
     * @param drawCardRequest The request sent by the player to draw a card. It contains the ID of the player who initiated the request.
     */
    public void handleDrawCardRequest(DrawCardRequest drawCardRequest) {
        Card card = getGameModel().handleDrawCardDrawing(drawCardRequest.getSenderId());

        if(card == null) {
            notifyAll(new EndGameResponse(getLeaderboardNames(), getLeaderboardPoints()));
            return;
        }

        sendResponse(drawCardRequest, card);
    }

    private void sendResponse(DrawCardRequest drawCardRequest, Card card) {
        if(card instanceof AbandonedShipCard) {
            notify(new DrawAbandonedShipResponse(card), drawCardRequest.getSenderId());
            return;
        }
        if(card instanceof AbandonedStationCard) {
            notify(new DrawAbandonedStationResponse(card), drawCardRequest.getSenderId());
            return;
        }
        if(card instanceof EpidemicCard) {
            notifyAll(new DrawEpidemicResponse(card));
            return;
        }
        if(card instanceof MeteorShowerCard) {
            meteorShowerExpectedEffects = (int) players.stream()
                    .filter(p -> !p.hasLost())
                    .count();
            notifyAll(new DrawMeteorShowerResponse(card));
        }
        if(card instanceof OpenSpaceCard) {
            notify(new DrawOpenSpaceResponse(card), drawCardRequest.getSenderId());
        }
        if(card instanceof PirateCard) {
            notify(new DrawPirateResponse(card), drawCardRequest.getSenderId());
        }
        if(card instanceof PlanetsCard) {
            notify(new DrawPlanetsResponse(card), drawCardRequest.getSenderId());
        }
        if(card instanceof SlaversCard) {
            notify(new DrawSlaversResponse(card), drawCardRequest.getSenderId());
        }
        if(card instanceof SmugglersCard) {
            notify(new DrawSmugglersResponse(card), drawCardRequest.getSenderId());
        }
        if(card instanceof StellarSpaceDustCard) {
            int lastPlayerId = getGameModel().getLastPlayerId();
            notify(new DrawStellarSpaceDustResponse(card), lastPlayerId);
        }
        if(card instanceof WarZoneCard) {
            prepareForWarZone(card);
        }
    }

    private void prepareForWarZone(Card card) {
        switch(((WarZoneCard) card).getPenalties().get(((WarZoneCard) card).getWarZoneDTO().getPenaltyNumber()).getWarZonePenaltyType()) {
            case WarZonePenaltyType.LOWEST_CREW: {
                int playerId = getGameModel().getPlayerIdWithLowestCrew();
                ((WarZoneCard) card).getWarZoneDTO().setPlayerId(playerId);
                notify(new DrawWarZoneResponse(card), playerId);
                break;
            }
            case WarZonePenaltyType.LOWEST_FIREPOWER: {
                notifyAll(new CalculateFirePowerResponse(card));
                break;
            }
            case WarZonePenaltyType.LOWEST_THRUST: {
                notifyAll(new CalculateThrustPowerResponse(card));
                break;
            }
        }
    }
    // -
    // Handling of the card effect from the controller side
    // -



    /**
     * Handles the application of the "Abandoned Ship" effect during the game.
     * This method processes the effect by updating the game state, managing player statuses,
     * and sending appropriate notifications to the involved players.
     * The method ensures that:
     * - The game state and map are updated for all players.
     * - Individual players' statuses are checked, including removing those who have lost the game.
     * - The flow of the "Abandoned Ship" effect is passed to the next eligible player, or ends the effect when all conditions are met.
     * - Notifications are sent to players regarding ongoing state changes and the conclusion of the effect.
     *
     * @param request The request containing the "Abandoned Ship" card and associated data needed to process the effect.
     */
    public void handleAbandonedShipEffect(ApplyAbandonedShipEffectRequest request) {
        Card card = request.getCard();
        Player currentPlayer = getPlayerById(request.getSenderId());
        card.getDTO().getAffectedPlayerIds().add(currentPlayer.getId());
        if (!(card.getDTO() instanceof AbandonedShipDTO dto)) {
            Server.logError("handleAbandonedShipEffect: Tipo di DTO inatteso per la carta: " + card.getName());
            return;
        }

        dto.setPlayerId(currentPlayer.getId());
        boolean result = getGameModel().handleAbandonedShipEffect(card);
        notifyAll(new UpdateMap(createPlayerPositionDTOs(getGameModel().getPlayers()), true));
        updateShip(currentPlayer);
        Player nextPlayer = getNextPlayer(currentPlayer.getId(), card.getDTO().getAffectedPlayerIds());
        boolean eliminated = updatePlayerStateAndCheckElimination(currentPlayer);
        if (!result || eliminated) {
            if (nextPlayer != null) {
                dto.setPlayerId(nextPlayer.getId());
                getGameModel().updateAbandonedShipDTO(card);
                notify(new ApplyAbandonedShipOngoingResponse(card), nextPlayer.getId());

            } else {
                notifyFinishedCardEffectToActivePlayers();
            }
            if (eliminated) {
                notify(new OutOfGameResponse(calculateOutOfGamePoints(currentPlayer.getId())), currentPlayer.getId());
            }
            return;
        }
        notifyFinishedCardEffectToActivePlayers();
    }
    /**
     * Handles the application of the "Abandoned Station" effect during the game.
     * This method processes the effect by updating the game state, managing player statuses,
     * and sending appropriate notifications to the involved players. The method ensures that:
     * - The game state and map are updated for all players.
     * - Individual players' statuses are checked, including removing those who have lost the game.
     * - The flow of the "Abandoned Station" effect is passed to the next eligible player, or ends the effect when all conditions are met.
     * - Notifications are sent to players regarding ongoing state changes and the conclusion of the effect.
     *
     * @param request The request containing the "Abandoned Station" card and associated data needed to process the effect.
     */
    public void handleAbandonedStationEffect(ApplyAbandonedStationEffectRequest request) {
        Card card = request.getCard();
        Player currentPlayer = getPlayerById(request.getSenderId());
        card.getDTO().getAffectedPlayerIds().add(currentPlayer.getId());
        if (!(card.getDTO() instanceof AbandonedStationDTO dto)) {
            return;
        }
        dto.setPlayerId(currentPlayer.getId());
        boolean result = getGameModel().handleAbandonedStationEffect(card);
        notifyAll(new UpdateMap(createPlayerPositionDTOs(getGameModel().getPlayers()), true));
        updateShip(currentPlayer);
        Player nextPlayer = getNextPlayer(currentPlayer.getId(), card.getDTO().getAffectedPlayerIds());
        boolean eliminated = updatePlayerStateAndCheckElimination(currentPlayer);
        if (!result || eliminated) {
            if (nextPlayer != null) {
                dto.setPlayerId(nextPlayer.getId());
                getGameModel().updateAbandonedStationDTO(card);
                notify(new ApplyAbandonedStationOngoingResponse(card), nextPlayer.getId());

            } else {
                notifyFinishedCardEffectToActivePlayers();
            }
            if (eliminated) {
                notify(new OutOfGameResponse(), currentPlayer.getId());
            }
            return;
        }
        notifyFinishedCardEffectToActivePlayers();
    }
    /**
     * Handles the effects of an epidemic card in the game. This method processes the epidemic
     * card's effects, updates the game state, synchronizes players' statuses, and notifies
     * relevant parties when certain conditions are met (e.g., when a player is out of the game
     * or when all effects are processed).
     *
     * @param request an object containing the necessary inputs to apply the epidemic effect.
     *                This includes the epidemic card and its associated player information.
     */
    public void handleEpidemicEffect(ApplyEpidemicEffectRequest request) {
        Card card = request.getCard();
        boolean result = getGameModel().handleEpidemicEffect(card);

        synchronized (this) {
            setNumberOfEffects(getNumberOfEffects() + 1);
        }
        Player player = getPlayerById(request.getSenderId());
        card.getDTO().getAffectedPlayerIds().add(player.getId());
        Ship ship = player.getShip();
        updateShip(player);
        List<TileCoordinates> tiles = getShipTiles(player);
        notify(new UpdatedShip(tiles, true,ship.getBatteryManager().getBatteries(),ship.getShipStats().getCrewMembers(),ship.getShipLayout().getWasteTiles(),ship.getCargosManager().calculateTotalCargosPoints(),ship.getShipStats().getExposedConnectors(),player.getCredits()), player.getId());

        if(getGameModel().getPlayerById(card.getDTO().getPlayerId()).hasLost()){
            getGameModel().removePlayer(getGameModel().getPlayerById(card.getDTO().getPlayerId()));

            notify(new OutOfGameResponse(), card.getDTO().getPlayerId());
            notifyAll(new UpdateMap(createPlayerPositionDTOs(getGameModel().getPlayers()), true));
        }

        if(!result) {
            return;
        }

        // Notify the first player that the effect is finished and he needs to draw
        if(getNumberOfEffects() == (getGameModel().countPlayersInGame())){
            notifyFirstPlayer();
            restoreNumberOfEffects();
        }
    }

    /**
     * Handles the meteor shower effect in the game. This method applies the meteor shower effect to the
     * specified card, updates the game state accordingly, and sends relevant notifications. It also
     * ensures proper synchronization for managing the number of effects and determines if any player
     * has lost due to the effect.
     *
     * @param request the request object containing the details of the card to which the meteor shower
     *                effect is to be applied, including the card's player and effect details
     */
    public void handleMeteorShowerEffect(ApplyMeteorShowerEffectRequest request) {
        Card card = request.getCard();
        Player affectedPlayer = getPlayerById(request.getSenderId());
        if (!(card.getDTO() instanceof MeteorShowerDTO dto)) {
            return;
        }
        dto.setPlayerId(affectedPlayer.getId());

        boolean effectCompleted = getGameModel().handleMeteorShowerEffect(card);
        updateShip(affectedPlayer);
        boolean eliminated = updatePlayerStateAndCheckElimination(affectedPlayer);
        int currentProcessed;
        synchronized (this) {
            setNumberOfEffects(getNumberOfEffects() + 1);
            currentProcessed = getNumberOfEffects();
        }
        List<Player> activePlayers = players.stream()
                .filter(p -> !p.hasLost())
                .toList();
        int expectedEffects = activePlayers.size();
        if (currentProcessed < expectedEffects) {
            return;
        }
        restoreNumberOfEffects();
        if (!effectCompleted) {
            getGameModel().updateMeteorShowerDTO(card);
            for (Player player : activePlayers) {
                notify(new ApplyMeteorShowerOngoingResponse(card), player.getId());
            }
            return;
        }
        meteorShowerExpectedEffects = -1;
        notifyFinishedCardEffectToActivePlayers();
    }


    /**
     * Handles the "Open Space" effect for the given request. This involves updating the game state, notifying players
     * about the changes, and managing scenarios such as player elimination and determining the next turn.
     *
     * @param request The request containing necessary information to apply the "Open Space" effect, including the card
     *                and its associated data.
     */
    public void handleOpenSpaceEffect(ApplyOpenSpaceRequest request) {
        Card card = request.getCard();
        Player currentPlayer = getPlayerById(request.getSenderId());
        card.getDTO().getAffectedPlayerIds().add(currentPlayer.getId());
        getGameModel().handleOpenSpaceEffect(card);
        notifyAll(new UpdateMap(createPlayerPositionDTOs(getGameModel().getPlayers()), true));
        updateShip(currentPlayer);
        Player nextPlayer = getNextPlayer(currentPlayer.getId(), card.getDTO().getAffectedPlayerIds());
        boolean eliminated = updatePlayerStateAndCheckElimination(currentPlayer);


        if (eliminated || nextPlayer != null) {
            if (nextPlayer != null) {
                card.getDTO().setPlayerId(nextPlayer.getId());
                getGameModel().updateOpenSpaceDTO(card);
                notify(new ApplyOpenSpaceOngoingResponse(card), nextPlayer.getId());
            } else {
                card.getDTO().setPlayerId(-1);
                notifyFinishedCardEffectToActivePlayers();
            }
            if (eliminated) {
                notify(new OutOfGameResponse(), currentPlayer.getId());
            }
            return;
        }
        notifyFinishedCardEffectToActivePlayers();
    }


    /**
     * Handles the application of the "Pirate" card effect during the game.
     * This method processes the card effect by updating the game state, handling player-specific statuses
     * such as loss conditions, and managing the required gameplay notifications to the involved players.
     * The method ensures that:
     * - The game state and map updates occur for all players.
     * - Individual player statuses are updated, including removal of players who have lost the game.
     * - The flow of the pirate effect is passed to the next eligible player, or ends the effect when conditions are met.
     * - Notifications are sent to players regarding ongoing state changes and the conclusion of the pirate effect.
     *
     * @param request The request containing the "Pirate" card to be played and related data.
     */
    public void handlePirateEffect(ApplyPirateEffectRequest request) {
        Card card = request.getCard();
        Player affectedPlayer = getPlayerById(request.getSenderId());
        card.getDTO().getAffectedPlayerIds().add(affectedPlayer.getId());
        if (!(card.getDTO() instanceof PiratesDTO piratesDTO)) {
            return;
        }
        piratesDTO.setPlayerId(affectedPlayer.getId());

        boolean result = getGameModel().handlePirateEffect(card);

        notifyAll(new UpdateMap(createPlayerPositionDTOs(getGameModel().getPlayers()), true));
        updateShip(affectedPlayer);
        boolean eliminated = affectedPlayer.hasLost();
        Player nextPlayer = getNextPlayer(affectedPlayer.getId(), card.getDTO().getAffectedPlayerIds());
        if (eliminated) {
            updatePlayerStateAndCheckElimination(affectedPlayer);
        }
        PirateCard pirateCard = (PirateCard) card;
        int currentShot = piratesDTO.getFireShotNumber();
        int totalShots = pirateCard.getFireShots().size();
        if (!result) {
            if (currentShot >= totalShots - 1) {
                if (nextPlayer != null) {
                    piratesDTO.setPlayerId(nextPlayer.getId());
                    getGameModel().updatePirateCardDTO_nextPlayer(card);
                    notify(new DrawPirateResponse(card), nextPlayer.getId());
                } else {
                    notifyFinishedCardEffectToActivePlayers();
                }
            } else {
                if (eliminated) {
                    if (nextPlayer != null) {
                        piratesDTO.setPlayerId(nextPlayer.getId());
                        getGameModel().updatePirateCardDTO_nextPlayer(card);
                        notify(new DrawPirateResponse(card), nextPlayer.getId());
                    } else {
                        notifyFinishedCardEffectToActivePlayers();
                    }
                    notify(new OutOfGameResponse(), affectedPlayer.getId());
                } else {
                    getGameModel().updatePirateCardDTO_samePlayer(card);
                    notify(new ApplyPirateOngoingResponse(card), piratesDTO.getPlayerId());
                }
            }
            return;
        }
        notifyFinishedCardEffectToActivePlayers();
    }
    /**
     * Handles the processing of the "Planets" card effect during the game.
     * It applies the card effect, updates the game state for all players,
     * notifies players and observers about
     * */
    public void handlePlanetsEffect(ApplyPlanetsEffectRequest request) {
        Card card = request.getCard();
        Player currentPlayer = getPlayerById(request.getSenderId());
        card.getDTO().getAffectedPlayerIds().add(currentPlayer.getId());
        if (!(card.getDTO() instanceof PlanetsDTO dto)) {
            return;
        }
        dto.setPlayerId(currentPlayer.getId());
        getGameModel().handlePlanetsEffect(card);

        notifyAll(new UpdateMap(createPlayerPositionDTOs(getGameModel().getPlayers()), true));
        updateShip(currentPlayer);
        Player nextPlayer = getNextPlayer(currentPlayer.getId(), card.getDTO().getAffectedPlayerIds());
        boolean eliminated = currentPlayer.hasLost();
        if (eliminated) {
            updatePlayerStateAndCheckElimination(currentPlayer);
        }
        if (nextPlayer != null) {
            dto.setPlayerId(nextPlayer.getId());
            getGameModel().updatePlanetsDTO(card);
            notify(new ApplyPlanetsOngoingResponse(card), nextPlayer.getId());
            return;
        }
        notifyFinishedCardEffectToActivePlayers();
    }

    /**
     * Handles the processing of the "Smugglers" card effect during the game.
     * It performs the card effect, updates the game state for all players,
     * manages player elimination if necessary, and notifies the relevant players
     * and observers about changes and actions.
     *
     * @param request the request containing the card to be played and relevant data
     */
    public void handleSmugglersEffect(ApplySmugglersEffectRequest request) {
        Card card = request.getCard();
        Player currentPlayer = getPlayerById(request.getSenderId());
        card.getDTO().getAffectedPlayerIds().add(currentPlayer.getId());
        boolean result = getGameModel().handleSmugglersEffect(card);
        notifyAll(new UpdateMap(createPlayerPositionDTOs(getGameModel().getPlayers()), true));
        updateShip(currentPlayer);
        Player nextPlayer = getNextPlayer(currentPlayer.getId(), card.getDTO().getAffectedPlayerIds());
        boolean eliminated = updatePlayerStateAndCheckElimination(currentPlayer);

        if (!result || eliminated) {
            if (nextPlayer != null) {
                ((SmugglersCard) card).getSmugglersDTO().setPlayerId(nextPlayer.getId());
                getGameModel().updateSmugglersDTO(card);
                notify(new ApplySmugglersOngoingResponse(card), nextPlayer.getId());
            } else {
                notifyFinishedCardEffectToActivePlayers();
            }
            return;
        }
        notifyFinishedCardEffectToActivePlayers();
    }
    /**
     * Handles the processing of the "Slavers" card effect during the game.
     * It executes the card effect, updates the game state for all players,
     * manages player elimination if applicable, and notifies the relevant players
     * and observers about the required game actions and updates.
     *
     * @param request the request containing the card to be played and associated data
     */

    public void handleSlaversEffect(ApplySlaversEffectRequest request) {
        Card card = request.getCard();
        Player currentPlayer = getPlayerById(request.getSenderId());
        card.getDTO().getAffectedPlayerIds().add(currentPlayer.getId());
        boolean result = getGameModel().handleSlaversEffect(card);
        notifyAll(new UpdateMap(createPlayerPositionDTOs(getGameModel().getPlayers()), true));
        updateShip(currentPlayer);
        Player nextPlayer = getNextPlayer(currentPlayer.getId(), card.getDTO().getAffectedPlayerIds());
        boolean eliminated = updatePlayerStateAndCheckElimination(currentPlayer);

        if (!result || eliminated) {
            if (nextPlayer != null) {
                ((SlaversCard) card).getSlaversDTO().setPlayerId(nextPlayer.getId());
                getGameModel().updateSlaversDTO(card);
                notify(new ApplySlaversOngoingResponse(card), nextPlayer.getId());
            } else {
                notifyFinishedCardEffectToActivePlayers();
            }
            return;
        }

        notifyFinishedCardEffectToActivePlayers();
    }

    /**
     * Handles the effects of stellar space dust in the game. This method processes actions related
     * to the application of the stellar space dust effect on a particular card and updates the game
     * state accordingly. It also handles notifications to players about the state of the game.
     *
     * @param request the request containing the card and associated details required to apply
     *                the stellar space dust effect
     */
    public void handleStellarSpaceDustEffect(ApplyStellarSpaceDustEffectRequest request) {
        Card card = request.getCard();
        Player currentPlayer = getPlayerById(card.getDTO().getPlayerId());
        card.getDTO().getAffectedPlayerIds().add(currentPlayer.getId());
        boolean result = getGameModel().handleStellarSpaceDustEffect(card);
        notifyAll(new UpdateMap(createPlayerPositionDTOs(getGameModel().getPlayers()), true));
        updateShip(currentPlayer);


        boolean eliminated = updatePlayerStateAndCheckElimination(currentPlayer);

        Player previousPlayer = getPreviousPlayer(currentPlayer.getId(), card.getDTO().getAffectedPlayerIds());

        System.out.println(previousPlayer);

        if (!result || eliminated) {
            if (previousPlayer != null) {
                card.getDTO().setPlayerId(previousPlayer.getId());
                getGameModel().updateStellarSpaceDustDTO(card);
                notify(new ApplyStellarSpaceDustOngoingResponse(card), previousPlayer.getId());
            } else {
                notifyFinishedCardEffectToActivePlayers();
            }
            return;
        }
        notifyFinishedCardEffectToActivePlayers();
    }


    /**
     * Handles the calculation of the lowest fire power among players in a game round.
     * This method updates internal state variables to determine the player with the lowest
     * fire power and sends a notification when all players in the game round have been processed.
     *
     * @param request the request object containing the card and its associated data
     *                required to calculate fire power and update game state
     */
    public void handleCalculationOfLowestFirePower(SendFirePowerRequest request) {
        Card card = request.getCard();
        double firePower = getGameModel().calculateFirePower(card);

        synchronized (this) {
            setNumberOfEffects(getNumberOfEffects() + 1);
            if (getWorstFirePower() == 0 && getWorstId() == 0) {
                // It's the first time the worst firePower is updated
                setWorstFirePower(firePower);
                setWorstId(card.getDTO().getPlayerId());
            } else {
                // It's not the first time worst firePower is updated
                if (getWorstFirePower() > firePower) {
                    setWorstFirePower(firePower);
                    setWorstId(card.getDTO().getPlayerId());
                }
            }
        }

        if(getNumberOfEffects() == (getGameModel().countPlayersInGame())){
            card.getDTO().setPlayerId(getWorstId());
            notify(new ApplyWarZoneEffectResponseOngoing(card), getWorstId());

            setNumberOfEffects(0);
            setWorstFirePower(0);
            setWorstId(0);
        }
    }

    /**
     * Handles the calculation of the lowest thrust power among the players and updates the game
     * state based on the results. This method determines the player with the lowest thrust power
     * and applies game logic accordingly.
     *
     * @param request an instance of SendThrustPowerRequest containing the card information
     *                and the request data needed to calculate thrust power.
     */
    public void handleCalculationOfLowestThrustPower(SendThrustPowerRequest request) {
        Card card = request.getCard();
        int thrustPower = getGameModel().calculateThrustPower(card);

        synchronized (this) {
            setNumberOfEffects(getNumberOfEffects() + 1);
            if (getWorstThrustPower() == 0 && getWorstId() == 0) {
                // It's the first time the worst firePower is updated
                setWorstThrustPower(thrustPower);
                setWorstId(card.getDTO().getPlayerId());
            } else {
                // It's not the first time worst firePower is updated
                if (getWorstThrustPower() > thrustPower) {
                    setWorstThrustPower(thrustPower);
                    setWorstId(card.getDTO().getPlayerId());
                }
            }
        }

        if(getNumberOfEffects() == (getGameModel().countPlayersInGame())){
            card.getDTO().setPlayerId(getWorstId());
            notify(new ApplyWarZoneEffectResponseOngoing(card), getWorstId());

            setNumberOfEffects(0);
            setWorstFirePower(0);
            setWorstId(0);
        }
    }

    /**
     * Processes the effects of a war zone card and updates the game state accordingly.
     *
     * @param request the request object containing details of the war zone effect to be applied.
     *                It includes the card to be processed and its associated data.
     */
    public void handleWarZoneEffect(ApplyWarZoneEffectRequest request) {
        Card card = request.getCard();
        boolean result = getGameModel().handleWarZoneEffect(card);

        // Update map for all players and ship for the player
        notifyAll(new UpdateMap(createPlayerPositionDTOs(getGameModel().getPlayers()), true));
        Player player = getPlayerById(request.getSenderId());
        Ship ship = player.getShip();
        updateShip(player);
        List<TileCoordinates> tiles = getShipTiles(player);
        notify(new UpdatedShip(tiles, true,ship.getBatteryManager().getBatteries(),ship.getShipStats().getCrewMembers(),ship.getShipLayout().getWasteTiles(),ship.getCargosManager().calculateTotalCargosPoints(),ship.getShipStats().getExposedConnectors(),player.getCredits()), player.getId());

        // Check if player has lost
        if(getGameModel().getPlayerById(card.getDTO().getPlayerId()).hasLost()){
            getGameModel().removePlayer(getGameModel().getPlayerById(card.getDTO().getPlayerId()));
            notify(new OutOfGameResponse(), card.getDTO().getPlayerId());
            notifyAll(new UpdateMap(createPlayerPositionDTOs(getGameModel().getPlayers()), true));
        }

        if(!result) {
            // Update DTO that needs to be passed to the next player
            getGameModel().updateWarZoneDTO(card);

            if(((WarZoneCard)card).getPenalties().get(((WarZoneCard)card).getWarZoneDTO().getPenaltyNumber()).getWarZonePenaltyEffect() == WarZonePenaltyEffect.HIT_BY_PROJECTILES && ((WarZoneCard)card).getWarZoneDTO().getFireShotNumber() != 0) {
                if(!getGameModel().getPlayerById(card.getDTO().getPlayerId()).hasLost()){
                    notify(new ApplyWarZoneProjectileOngoingResponse(card), card.getDTO().getPlayerId());
                    return;
                }
            }

            prepareForWarZone(card);
            return;
        }

        // Notify the first player that the effect is finished and he needs to draw
        notifyFirstPlayer();
    }

    private void notifyFirstPlayer() {
        // This is the part where the card is finished.
        int firstPlayer = getGameModel().getFirstPlayerId();
        for (Player player : players) {
            if (player.getId() == firstPlayer) {
                notify(new FinishedCardEffectResponse(true),player.getId());
            }
            else {
                notify(new FinishedCardEffectResponse(false),player.getId());
            }
        }
    }
    // ---------------
    private void setupObservers(){
        for(ClientNotifier clientNotifier : clientNotifiers){
            addObserver(clientNotifier);
        }
    }
    private static List<PlayerDTO> createPlayerPositionDTOs(List<Player> players) {
        List<PlayerDTO> dtoList = new ArrayList<>();
        for (Player player : players) {
            dtoList.add(new PlayerDTO(
                    player.getId(),
                    player.getUsername(),
                    player.getColor(),
                    player.getMapPosition()
            ));
        }

        return dtoList;
    }
    private List<TileCoordinates> getShipTiles(Player player){
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
        return tiles;

    }
    private void updateShip(Player player) {
        Ship ship = player.getShip();
        List<TileCoordinates> tiles = getShipTiles(player);
        ship.getShipStats().calculateAllStats(ship.getShipLayout());
        ship.getBatteryManager().fillBatteryList(ship.getShipLayout());
        ship.getBatteryManager().calculateBatteries(ship.getShipLayout());
        ship.getCargosManager().fillCargoHoldList(ship.getShipLayout());
        notify(new UpdatedShip(tiles, true,
                ship.getBatteryManager().getBatteries(),
                ship.getShipStats().getCrewMembers(),
                ship.getShipLayout().getWasteTiles(),
                ship.getCargosManager().calculateTotalCargosPoints(),
                ship.getShipStats().getExposedConnectors(),
                player.getCredits()), player.getId());
    }
    private Player getNextPlayer(int currentPlayerId, Set<Integer> alreadyAffected) {
        int currentRank = getGameModel().translateRankingPosition(currentPlayerId);
        return players.stream()
                .filter(p -> !p.hasLost())
                .filter(p -> !alreadyAffected.contains(p.getId()))
                .filter(p -> getGameModel().translateRankingPosition(p.getId()) > currentRank)
                .min(Comparator.comparingInt(p -> getGameModel().translateRankingPosition(p.getId())))
                .orElse(null);
    }
    private Player getPreviousPlayer(int currentPlayerId, Set<Integer> alreadyAffected) {
        List<Player> orderedPlayers = players.stream()
                .filter(p -> !p.hasLost())
                .sorted(Comparator.comparingInt(p -> getGameModel().translateRankingPosition(p.getId())))
                .toList();

        int currentIndex = -1;
        for (int i = 0; i < orderedPlayers.size(); i++) {
            if (orderedPlayers.get(i).getId() == currentPlayerId) {
                currentIndex = i;
                break;
            }
        }
        for (int i = currentIndex - 1; i >= 0; i--) {
            Player p = orderedPlayers.get(i);
            if (!alreadyAffected.contains(p.getId())) {
                return p;
            }
        }

        for (int i = orderedPlayers.size() - 1; i > currentIndex; i--) {
            Player p = orderedPlayers.get(i);
            if (!alreadyAffected.contains(p.getId())) {
                return p;
            }
        }
        return null;
    }



    private boolean updatePlayerStateAndCheckElimination(Player player) {
        if (!player.hasLost()) {
            return false;
        }
        getGameModel().removePlayer(player);
        notifyAll(new UpdateMap(createPlayerPositionDTOs(getGameModel().getPlayers()), true));
        notify(new OutOfGameResponse(calculateOutOfGamePoints(player.getId())), player.getId());
        this.players.removeIf(p -> p.getId() == player.getId());
        ClientNotifier toRemove = null;
        for (ClientNotifier notifier : this.clientNotifiers) {
            if (notifier.getId() == player.getId()) {
                toRemove = notifier;
                break;
            }
        }
        if (toRemove != null) {
            removeObserver(toRemove);
        }

        return true;
    }
    private void notifyFinishedCardEffectToActivePlayers() {
        int firstPlayer = getGameModel().getFirstPlayerId();
        List<Player> activePlayers = getGameModel().getPlayers().stream()
                .filter(p -> !p.hasLost())
                .toList();

        for (Player player : activePlayers) {
            notify(new FinishedCardEffectResponse(player.getId() == firstPlayer), player.getId());
        }
    }

    /**
     * method used to disconnect client for various reasons, and if only one players is left in the server, game ends
     * otherwise it sends an update game state to all players still in game
     * @param playerId of the player to disconnect
     */
    public synchronized void disconnectClient(int playerId) {
        Player player = getPlayerById(playerId);
        if (player == null) return;
        loggedPlayers.remove(player);
        ClientNotifier toRemove = null;
        for (ClientNotifier notifier : this.clientNotifiers) {
            if (notifier.getId() == player.getId()) {
                toRemove = notifier;
                break;
            }
        }
        if (toRemove != null) {
            removeObserver(toRemove);
        }
        if (loggedPlayers.size() == 1) {
            // notifyAll(new EndGameResponse(getLeaderboardNames(), getLeaderboardPoints()));
        } else {
            players.remove(player);
            notifyAll(new UpdateMap(createPlayerPositionDTOs(getGameModel().getPlayers()), true));
        }
    }

    /**
     * test purpose method, used only for resetting the controller state in the test classes
     */
    public void reset() {
        this.gameModel = null;
        this.players = new ArrayList<>();
        this.isStarted = false;
        this.isActive = true;
        this.clientNotifiers = new ArrayList<>();
        this.requests = new LinkedBlockingDeque<>();
        this.gamePhase = null;
        this.crewPhaseAlreadyStarted = false;
    }

    private float calculateOutOfGamePoints(int playerId){
        float points = 0;
        points += (float) (0.5 * gameModel.getPlayerById(playerId).getShip().getCargosManager().calculateTotalCargosPoints());
        points -= gameModel.getPlayerById(playerId).getShip().getShipLayout().getWasteTiles();
        return points;
    }

    private List<String> getLeaderboardNames() {
        List<String> leaderboardNames = new ArrayList<>(Arrays.asList("", "", "", ""));
        for (Player player : loggedPlayers) {
            switch (player.getRankingPosition()) {
                case First -> leaderboardNames.set(0, player.getUsername());
                case Second -> leaderboardNames.set(1, player.getUsername());
                case Third -> leaderboardNames.set(2, player.getUsername());
                case Fourth -> leaderboardNames.set(3, player.getUsername());
            }
        }
        return leaderboardNames;
    }

    

    private Float calculateEndOfGamePoints(int playerId){
        float points = 0;
        points -= gameModel.getPlayerById(playerId).getShip().getShipLayout().getWasteTiles();
        points += gameModel.getPlayerById(playerId).getShip().getCargosManager().calculateTotalCargosPoints();
        switch(gameModel.getPlayerById(playerId).getRankingPosition()) {
            case First: {
                points += gameModel.getCurrentLevel() == GameLevel.TRY_LEVEL ? 4 : 8;
                break;
            }
            case Second: {
                points += gameModel.getCurrentLevel() == GameLevel.TRY_LEVEL ? 3 : 6;
                break;
            }
            case Third: {
                points += gameModel.getCurrentLevel() == GameLevel.TRY_LEVEL ? 2 : 4;
                break;
            }
            case Fourth: {
                points += gameModel.getCurrentLevel() == GameLevel.TRY_LEVEL ? 1 : 2;
                break;
            }
        }
        for (Player player : getGameModel().getPlayers()) {
            if (getGameModel().getPlayerById(player.getId()).getShip().getShipStats().getExposedConnectors() > player.getShip().getShipStats().getExposedConnectors()) {
                return points;
            }
        }
        points += gameModel.getCurrentLevel() == GameLevel.TRY_LEVEL ? 2 : 4;
        return points;
    }

    private List<Float> getLeaderboardPoints (){
        List<Float> leaderboardPoints = new ArrayList<>(Arrays.asList(0.0F, 0.0F, 0.0F, 0.0F));
        for (Player player : getGameModel().getPlayers()) {
            switch(player.getRankingPosition()) {
                case First: {
                    leaderboardPoints.add(0, calculateEndOfGamePoints(player.getId()));
                    break;
                }
                case Second: {
                    leaderboardPoints.add(1, calculateEndOfGamePoints(player.getId()));
                    break;
                }
                case Third: {
                    leaderboardPoints.add(2, calculateEndOfGamePoints(player.getId()));
                    break;
                }
                case Fourth: {
                    leaderboardPoints.add(3, calculateEndOfGamePoints(player.getId()));
                    break;
                }
            }
        }
        return leaderboardPoints;
    }

}