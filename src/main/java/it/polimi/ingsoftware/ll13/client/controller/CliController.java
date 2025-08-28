package it.polimi.ingsoftware.ll13.client.controller;

import it.polimi.ingsoftware.ll13.client.utils.printers.specific_printers.*;
import it.polimi.ingsoftware.ll13.client.exceptions.SocketConnectionError;
import it.polimi.ingsoftware.ll13.client.view.cli.CliView;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.*;
import it.polimi.ingsoftware.ll13.model.cards.decks.CardStack;
import it.polimi.ingsoftware.ll13.model.cards.dtos.*;
import it.polimi.ingsoftware.ll13.model.cards.dtos.helper_classes.Coordinates;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.WarZonePenaltyEffect;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.WarZonePenaltyType;
import it.polimi.ingsoftware.ll13.model.crew_members.Alien;
import it.polimi.ingsoftware.ll13.model.crew_members.AlienColor;
import it.polimi.ingsoftware.ll13.model.crew_members.CrewMember;
import it.polimi.ingsoftware.ll13.model.crew_members.Human;
import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;
import it.polimi.ingsoftware.ll13.model.general_enumerations.RankingPosition;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.network.connection.ClientHearthBeat;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.adventure.*;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.construction.*;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.validation.EliminateTileRequest;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.validation.PlaceCrewRequest;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.validation.ValidateShipRequest;
import it.polimi.ingsoftware.ll13.network.requests.menu_requests.LoginRequest;
import it.polimi.ingsoftware.ll13.network.requests.menu_requests.NewMatchRequest;
import it.polimi.ingsoftware.ll13.network.response.match_responses.phase_change.CrewPlacePhaseStarted;
import it.polimi.ingsoftware.ll13.utils.input.InputChecker;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CliController extends ClientController{
    private static CliController instance;
    private static final Object lock = new Object();
    private final Scanner in = new Scanner(System.in);
    private GamePhase gamePhase;
    private LoginResult result;
    private BuildingPhase buildingPhase;
    private ValidationPhase validationPhase;
    private SetUpPhase setUpPhase;
    private AdventurePhase adventurePhase;

    public enum GamePhase {
        CONNECT,
        LOGIN,
        WAITING_FOR_PLAYERS,
        BUILDING,
        VALIDATING,
        SETTING_EQUIPE,
        ADVENTURE
    }

    public enum LoginResult{
        NONE,
        SUCCESS,
        USERNAME_TAKEN,
        NO_MATCH,
        MATCH_STARTED
    }

    public enum BuildingPhase {
        WAITING_FOR_HOST,
        CHOOSING,
        DISCARDED_TILE,
        DRAWING_TILE,
        DRAWING_TILE_ERROR,
        ROTATING_TILE,
        PLACED_TILE_CORRECTLY,
        PLACED_TILE_WRONGLY,
        SHOWING_DREW_CARDS,
        PLACING_ROCKET,
        FLIPPED_HOURGLASS,
        WAITING_FINISH,
        OUT_OF_GAME,
        FINISHED_BUILDING
    }

    public enum ValidationPhase {
        VALIDATING,
        VALIDATING_ERROR,
        VALIDATED
    }

    public enum SetUpPhase {
        WAITING_START,
        SETTING_UP,
        SETTING_UP_ERROR,
        PLACED_CREW_MEMBERS,
        SET_UP_FINISHED
    }

    public enum AdventurePhase {
        ON_GOING,
        DREW_CARD,
        METEOR_SHOWER_ONGOING,
        METEOR_SHOWER_FINISHED,
        PIRATES_ONGOING,
        PIRATES_FINISHED,
        START_WAR_ZONE,
        WAR_ZONE_ONGOING,
        WAR_ZONE_LOWEST_FIRE_POWER,
        WAR_ZONE_LOWEST_THRUST_POWER,
        WAR_ZONE_FIRE_SHOTS_FINISHED,
        WAR_ZONE_FINISHED,
        FINISHED_CARD_EFFECT,
        FINISHED, OUT_OF_GAME,
    }

    private String input = "";
    private Tile currentTile;
    private List<Tile> flippedTiles;
    private List<TileCoordinates> coordinatesOfTheShip;
    private List<CardStack> cardStacks;
    private final Tile[] savedTiles = new Tile[2];
    private int drawnFromSavedIndex = -1; // -1 means not from saved, 0 or 1 means from savedTiles[0] or savedTiles[1]

    private List<TileCoordinates> invalidCoordinates = new ArrayList<>();

    // Adventure phase
    private boolean isFirst = false;
    private int batteryCount = 0;
    private int crewCount = 0;
    private int wasteTilesCount = 0;
    private int cargoCount = 0;
    private int exposedConnectorsCount = 0;
    private int creditCount = 0;

    private Card currentCard;

    // End game
    private int score;
    private List<String> playersPositions;
    private List<Float> playersPoints;

    // --> Getters <--
    public static CliController getInstance(){
        if(instance == null){
            instance = new CliController();
        }
        return instance;
    }

    public CliView getCliView() {
        return CliView.getInstance();
    }

    public LoginResult getResult() {
        return result;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public BuildingPhase getBuildingPhase() {
        return buildingPhase;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public List<Tile> getFlippedTiles() {
        return flippedTiles;
    }

    public List<CardStack> getCardStacks() {
        return cardStacks;
    }

    public List<TileCoordinates> getCoordinatesOfTheShip() {
        return coordinatesOfTheShip;
    }

    public ValidationPhase getValidationPhase() {
        return validationPhase;
    }

    public List<TileCoordinates> getInvalidCoordinates() {
        return invalidCoordinates;
    }

    public SetUpPhase getSetUpPhase() {
        return setUpPhase;
    }

    public AdventurePhase getAdventurePhase() {
        return adventurePhase;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public int getBatteryCount() {
        return batteryCount;
    }

    public int getCrewCount() {
        return crewCount;
    }

    public int getWasteTilesCount() {
        return wasteTilesCount;
    }

    public int getCargoCount() {
        return cargoCount;
    }

    public int getExposedConnectorsCount() {
        return exposedConnectorsCount;
    }

    public int getCreditCount() {
        return creditCount;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public int getScore() {
        return score;
    }

    public List<String> getPlayersPositions() {
        return playersPositions;
    }

    public List<Float> getPlayersPoints() {
        return playersPoints;
    }

    // --> Setters <--
    public void setResult(LoginResult result) {
        this.result = result;
    }

    public void setBuildingPhase(BuildingPhase buildingPhase) {
        this.buildingPhase = buildingPhase;
    }

    public void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
    }

    public void setFlippedTiles(List<Tile> flippedTiles) {
        this.flippedTiles = flippedTiles;
    }

    public void setCardStacks(List<CardStack> cardStacks) {
        this.cardStacks = cardStacks;
    }

    public void setCoordinatesOfTheShip(List<TileCoordinates> coordinatesOfTheShip) {
        this.coordinatesOfTheShip = coordinatesOfTheShip;
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    public void setValidationPhase(ValidationPhase validationPhase) {
        this.validationPhase = validationPhase;
    }

    public void setInvalidCoordinates(List<TileCoordinates> invalidCoordinates) {
        this.invalidCoordinates = invalidCoordinates;
    }

    public void setSetUpPhase(SetUpPhase setUpPhase) {
        this.setUpPhase = setUpPhase;
    }

    public void setAdventurePhase(AdventurePhase adventurePhase) {
        this.adventurePhase = adventurePhase;
    }

    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public void setBatteryCount(int batteryCount) {
        this.batteryCount = batteryCount;
    }

    public void setCrewCount(int crewCount) {
        this.crewCount = crewCount;
    }

    public void setWasteTilesCount(int wasteTilesCount) {
        this.wasteTilesCount = wasteTilesCount;
    }

    public void setCargoCount(int cargoCount) {
        this.cargoCount = cargoCount;
    }

    public void setExposedConnectorsCount(int exposedConnectorsCount) {
        this.exposedConnectorsCount = exposedConnectorsCount;
    }

    public void setCreditCount(int creditCount) {
        this.creditCount = creditCount;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPlayersPositions(List<String> playersPositions) {
        this.playersPositions = playersPositions;
    }

    public void setPlayersPoints(List<Float> playersPoints) {
        this.playersPoints = playersPoints;
    }

    // --> Other methods <--
    public Object getLock(){
        return lock;
    }

    private CrewPlacePhaseStarted pendingCrewPlacePhaseStarted = null;
    private boolean hasPendingCrewPhaseBeenProcessed = false;

    public synchronized void storePendingCrewPlacePhaseStarted(CrewPlacePhaseStarted data) {
        if (this.gamePhase == GamePhase.SETTING_EQUIPE && this.setUpPhase == SetUpPhase.WAITING_START) {
            // System.out.println("[DEBUG] CliController: storePendingCrewPlacePhaseStarted - Controller già in attesa, processo immediatamente.");
            processCrewPlacePhaseStartedMessage(data);
        } else if (this.gamePhase != GamePhase.SETTING_EQUIPE) {
            this.pendingCrewPlacePhaseStarted = data;
            this.hasPendingCrewPhaseBeenProcessed = false;
            // System.out.println("[DEBUG] CliController: storePendingCrewPlacePhaseStarted - Messaggio memorizzato come pendente.");
        } else {
            // System.out.println("[DEBUG] CliController: storePendingCrewPlacePhaseStarted - GamePhase è SETTING_EQUIPE ma SetUpPhase non è WAITING_START, o messaggio già processato. Current SetUpPhase: " + this.setUpPhase);
        }
    }

    private synchronized void processCrewPlacePhaseStartedMessage(CrewPlacePhaseStarted dataToProcess) {
        if (dataToProcess == null) return;

        // System.out.println("[DEBUG] CliController: Processing CrewPlacePhaseStarted message. Cabine vuote: " + dataToProcess.getCabinTiles().isEmpty());
        if (!dataToProcess.getCabinTiles().isEmpty()) {
            setSetUpPhase(SetUpPhase.SETTING_UP);
        } else {
            setSetUpPhase(SetUpPhase.SET_UP_FINISHED);
        }
        this.hasPendingCrewPhaseBeenProcessed = true;

        synchronized (getLock()) {
            getLock().notifyAll();
        }
    }



    @Override
    public void close() {
        synchronized (lock){
            if(!socket.isClosed()){
                try{
                    if(!responseReader.isInterrupted()){
                        responseReader.interrupt();
                    }
                    ClientHearthBeat.shutDown();
                    socket.close();
                    lock.notifyAll();
                }catch (IOException e){
                    client.setActive(false);
                }
            }
        }

    }

    @Override
    public void connectToServer() throws SocketConnectionError {
        Socket clientSocket;
        String input;
        String[] inputs;

        setGamePhase(GamePhase.CONNECT);

        getCliView().setCliInterface(new CLIConnectionPrinter());
        getCliView().displayPage(null);

        do {
            input = in.nextLine().trim();

            // Input is empty -> auto-assignment
            if (input.isEmpty()) {
                input = "127.0.0.1:1234";
            }
            inputs = input.split(":");

            // Check for the errors
            if (inputs.length != 2 || (!InputChecker.isValidIP(inputs[0]) && !InputChecker.isValidPort(inputs[1]))) {
                getCliView().updatePageState(new CLIConnectionPrinter.InvalidInput());
                getCliView().displayPage(new String[]{input});
            } else if (!InputChecker.isValidIP(inputs[0])) {
                getCliView().updatePageState(new CLIConnectionPrinter.InvalidIP());
                getCliView().displayPage(new String[]{input});
            } else if (!InputChecker.isValidPort(inputs[1])) {
                getCliView().updatePageState(new CLIConnectionPrinter.InvalidPort());
                getCliView().displayPage(new String[]{input});
            }
        } while(inputs.length != 2 || !InputChecker.isValidIP(inputs[0]) || !InputChecker.isValidPort(inputs[1]));

        try {
            clientSocket = new Socket(inputs[0], Integer.parseInt(inputs[1]));
        } catch (Exception e) {
            throw new SocketConnectionError();
        }
        setSocket(clientSocket);
    }

    @Override
    public void join() {
        String input;
        String username;
        int numberOfPlayers = 0;
        String levelCaptured = "";
        String temp = "";
        GameLevel levelType = null;
        boolean redo;

        setGamePhase(GamePhase.LOGIN);

        getCliView().setCliInterface(new CLIJoinPrinter());
        getCliView().displayPage(null);

        // Selection of the type of role -> lobby creator or lobby joiner
        do {
            redo = false;

            input = in.nextLine().trim();
            if(InputChecker.isStringValid(new String[]{"start", "join"}, input)){
                getCliView().updatePageState(new CLIJoinPrinter.InsertUsername());
                getCliView().displayPage(new String[]{input});

                if(input.equalsIgnoreCase("start")){
                    // Username insert
                    username = in.nextLine().trim();

                    // Number of player insert
                    getCliView().updatePageState(new CLIJoinPrinter.InsertPlayers());
                    getCliView().displayPage(new String[]{username});

                    do{
                        try {
                            temp = in.nextLine().trim();
                            numberOfPlayers = Integer.parseInt(temp);
                        } catch (NumberFormatException e) {
                            getCliView().updatePageState(new CLIJoinPrinter.InvalidInputCausePlayers());
                            getCliView().displayPage(new String[]{temp});
                            continue;
                        }

                        if(numberOfPlayers < 2 || numberOfPlayers > 4) {
                            getCliView().updatePageState(new CLIJoinPrinter.InvalidInputCausePlayers());
                            getCliView().displayPage(new String[]{numberOfPlayers + ""});
                        }
                    } while(numberOfPlayers < 2 || numberOfPlayers > 4);

                    // Game mode selection
                    getCliView().updatePageState(new CLIJoinPrinter.InsertLevelType());
                    getCliView().displayPage(new String[]{numberOfPlayers + ""});

                    do {
                        levelCaptured = in.nextLine().trim().toLowerCase();
                        switch (levelCaptured) {
                            case "try" -> levelType = GameLevel.TRY_LEVEL;
                            case "level2" -> levelType = GameLevel.LEVEL_2;
                            default -> {
                                getCliView().updatePageState(new CLIJoinPrinter.InvalidInputCauseLevel());
                                getCliView().displayPage(new String[]{levelCaptured});
                            }
                        }
                    }while(levelType == null);

                    getClient().setGameLevel(levelType);

                    // Creation of the game
                    synchronized (getLock()) {
                        setResult(LoginResult.NONE);
                        send(new NewMatchRequest(id, numberOfPlayers, levelType, username));

                        while (getResult() == LoginResult.NONE) {
                            try {
                                getLock().wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }

                        if (getResult() == LoginResult.SUCCESS) {
                            client.setUsername(username);
                            break;
                        } else {
                            getCliView().updatePageState(new CLIJoinPrinter.InvalidInputCauseLobbyAlreadyCreated());
                            getCliView().displayPage(new String[]{levelCaptured});
                            redo = true;
                        }
                    }

                } else {
                    // Username insert
                    username = in.nextLine().trim();

                    if (client.isActive()) {
                        synchronized (getLock()) {
                            setResult(LoginResult.NONE);
                            send(new LoginRequest(id, username));

                            while (getResult() == LoginResult.NONE) {
                                try {
                                    getLock().wait();
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }

                            switch (getResult()) {
                                case SUCCESS -> {
                                    client.setUsername(username);
                                }
                                case LoginResult.USERNAME_TAKEN -> {
                                    // Username insert
                                    getCliView().updatePageState(new CLIJoinPrinter.UsernameAlreadyTaken());
                                    getCliView().displayPage(new String[]{username});
                                    redo = true;
                                }
                                case NO_MATCH -> {
                                    getCliView().updatePageState(new CLIJoinPrinter.MatchNotExisting());
                                    getCliView().displayPage(new String[]{username});
                                    redo = true;
                                }
                            }
                        }
                    }
                }
            } else {
                getCliView().updatePageState(new CLIJoinPrinter.InvalidInputCauseHosting());
                getCliView().displayPage(new String[]{input});
            }
        }while(!InputChecker.isStringValid(new String[]{"start", "join"}, input) || redo);
    }

    @Override
    public void waitingRoom() {
        getCliView().setCliInterface(new CLIWaitingPrinter());
        getCliView().displayPage(new String[]{});

        setGamePhase(GamePhase.WAITING_FOR_PLAYERS);

        synchronized (getLock()) {
            try {
                getLock().wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void waitForHostOk() {
        setBuildingPhase(BuildingPhase.WAITING_FOR_HOST);

        // The host chooses when to start the game
        if(isHost()) {
            getCliView().updatePageState(new CLIGamePrinter.StartGameAdmin());
            getCliView().displayPage(new Object[]{});

            while(true) {
                input = in.nextLine().trim();
                if(checkIfStartGameIsValid(input)) {
                    send(new StartConstruction(id));
                    break;
                }
                getCliView().updatePageState(new CLIGamePrinter.InvalidGeneralInputState());
                getCliView().displayPage(new Object[]{input});
            }
        } else {
            synchronized (getLock()) {
                while(getBuildingPhase() != BuildingPhase.CHOOSING) {
                    try {
                        getLock().wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }
    }

    private boolean checkIfStartGameIsValid(String input) {
        return input.equalsIgnoreCase("start");
    }

    @Override
    public void buildingShip() {
        int row = 0;
        int col = 0;
        flippedTiles = new ArrayList<>();
        coordinatesOfTheShip = new ArrayList<TileCoordinates>();

        setGamePhase(GamePhase.BUILDING);

        getCliView().updatePageState(new CLIGamePrinter.GameStarted());
        getCliView().displayPage(new Object[]{input});

        do {
            setBuildingPhase(BuildingPhase.CHOOSING);

            input = in.nextLine().trim();

            // Check if the game is finished
            if(getBuildingPhase() == BuildingPhase.FINISHED_BUILDING) {
                break;
            }

            if(input.equalsIgnoreCase("draw")) {
                // Check and return currentTile to saved if it came from there
                if (currentTile != null && drawnFromSavedIndex != -1) {
                    // Put the current tile back into its original saved slot
                    send(new PlaceTileRequest(id, 5, (drawnFromSavedIndex == 0 ? 9 : 10), currentTile));
                    synchronized (getLock()) {
                        while (getBuildingPhase() != BuildingPhase.PLACED_TILE_CORRECTLY && getBuildingPhase() != BuildingPhase.PLACED_TILE_WRONGLY && getBuildingPhase() != BuildingPhase.FINISHED_BUILDING) {
                            try {
                                getLock().wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                    }
                    savedTiles[drawnFromSavedIndex] = currentTile;

                    // Clear the current tile and reset the flag
                    currentTile = null;
                    drawnFromSavedIndex = -1;

                } else if (currentTile != null) {
                    send(new DiscardRequest(id, currentTile));
                    synchronized (getLock()) {
                        while (getBuildingPhase() != BuildingPhase.DISCARDED_TILE && getBuildingPhase() != BuildingPhase.FINISHED_BUILDING) {
                            try {
                                getLock().wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                    }
                }

                // Check if the game is finished. If so, exit the loop
                if (getBuildingPhase() == BuildingPhase.FINISHED_BUILDING) {
                    break;
                }

                setBuildingPhase(BuildingPhase.CHOOSING);
                send(new DrawTileRequest(id, false, -1));
                synchronized (getLock()) {
                    while (getBuildingPhase() != BuildingPhase.DRAWING_TILE && getBuildingPhase() != BuildingPhase.FINISHED_BUILDING) {
                        try {
                            getLock().wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }

                if(getBuildingPhase() == BuildingPhase.FINISHED_BUILDING) {
                    break;
                }
                getCliView().updatePageState(new CLIGamePrinter.DrawnActionState());
                getCliView().displayPage(new Object[]{input, currentTile});

            } else if (input.equalsIgnoreCase("save")) {
                // If the current tile was already saved, there's no need to re-save it
                if(currentTile != null && drawnFromSavedIndex != -1) {
                    // Case save card was already saved
                    send(new DiscardRequest(id, currentTile));
                    synchronized (getLock()) {
                        while (getBuildingPhase() != BuildingPhase.PLACED_TILE_CORRECTLY && getBuildingPhase() != BuildingPhase.FINISHED_BUILDING) {
                            try {
                                getLock().wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                    }

                    currentTile = null;
                    drawnFromSavedIndex = -1;

                    getCliView().updatePageState(new CLIGamePrinter.AlreadySavedTileState());
                    getCliView().displayPage(new Object[]{input});
                } else if (currentTile != null) {
                    // If the current tile was not previously saved...
                    if (savedTiles[0] == null) {
                        // In case the first slot of saved tiles is empty
                        send(new PlaceTileRequest(id, 5, 9, currentTile));
                        synchronized (getLock()) {
                            while (getBuildingPhase() != BuildingPhase.PLACED_TILE_CORRECTLY && getBuildingPhase() != BuildingPhase.PLACED_TILE_WRONGLY && getBuildingPhase() != BuildingPhase.FINISHED_BUILDING) {
                                try {
                                    getLock().wait();
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }
                        }

                        if (getBuildingPhase() == BuildingPhase.FINISHED_BUILDING) {
                            break;
                        }

                        if (getBuildingPhase() == BuildingPhase.PLACED_TILE_CORRECTLY) {
                            savedTiles[0] = currentTile;
                            currentTile = null;
                            drawnFromSavedIndex = -1;

                            getCliView().updatePageState(new CLIGamePrinter.SaveTileState());
                            getCliView().displayPage(new Object[]{input});

                        } else {
                            getCliView().updatePageState(new CLIGamePrinter.SaveTileErrorState());
                            getCliView().displayPage(new Object[]{input});

                        }

                    } else if (savedTiles[1] == null) {
                        // The case where the second saved tile slot is empty
                        send(new PlaceTileRequest(id, 5, 10, currentTile));
                        synchronized (getLock()) {
                            while (getBuildingPhase() != BuildingPhase.PLACED_TILE_CORRECTLY && getBuildingPhase() != BuildingPhase.PLACED_TILE_WRONGLY && getBuildingPhase() != BuildingPhase.FINISHED_BUILDING) {
                                try {
                                    getLock().wait();
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }
                        }

                        if (getBuildingPhase() == BuildingPhase.FINISHED_BUILDING) {
                            break;
                        }

                        if (getBuildingPhase() == BuildingPhase.PLACED_TILE_CORRECTLY) {
                            savedTiles[1] = currentTile;
                            currentTile = null;
                            drawnFromSavedIndex = -1;

                            getCliView().updatePageState(new CLIGamePrinter.SaveTileState());
                            getCliView().displayPage(new Object[]{input});
                        } else {
                            getCliView().updatePageState(new CLIGamePrinter.SaveTileErrorState());
                            getCliView().displayPage(new Object[]{input});
                        }

                    } else {
                        getCliView().updatePageState(new CLIGamePrinter.SaveTileErrorState());
                        getCliView().displayPage(new Object[]{input});
                        drawnFromSavedIndex = -1;
                    }
                } else {
                    getCliView().updatePageState(new CLIGamePrinter.NoTileInDrawingStageState());
                    getCliView().displayPage(new Object[]{input});
                }

            } else if (isDrawCommandWithNumber(input)) {
                int number = Integer.parseInt(input.split(":")[1]);
                int savedIndex = number - 1; // Convert 1/2 to 0/1 index

                if (savedIndex >= 0 && savedIndex < 2) {
                    if (savedTiles[savedIndex] != null) {
                        if(currentTile != null && drawnFromSavedIndex == -1) { // current tile was not previously saved in the saved tiles
                            send(new DiscardRequest(id, currentTile));
                            synchronized (getLock()) {
                                while (getBuildingPhase() != BuildingPhase.DISCARDED_TILE && getBuildingPhase() != BuildingPhase.FINISHED_BUILDING) {
                                    try {
                                        getLock().wait();
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                        break;
                                    }
                                }
                            }
                            // Clear the current tile and reset the flag
                            drawnFromSavedIndex = -1;
                            currentTile = null;
                        } else {
                            if (currentTile != null) {
                                // current tile was previously saved in the saved tiles
                                send(new DiscardRequest(id, currentTile));
                                synchronized (getLock()) {
                                    while (getBuildingPhase() != BuildingPhase.PLACED_TILE_CORRECTLY && getBuildingPhase() != BuildingPhase.FINISHED_BUILDING) {
                                        try {
                                            getLock().wait();
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            break;
                                        }
                                    }
                                }
                                savedTiles[drawnFromSavedIndex] = currentTile;

                                // Clear the current tile and reset the flag
                                drawnFromSavedIndex = -1;
                                currentTile = null;
                            }
                        }

                        setBuildingPhase(BuildingPhase.CHOOSING);

                        if (currentTile == null) {
                            drawnFromSavedIndex = savedIndex;
                            send(new DrawTileTempRequest(id, (savedIndex == 0 ? 5 : 5), (savedIndex == 0 ? 9 : 10)));
                            synchronized (getLock()) {
                                while (getBuildingPhase() != BuildingPhase.DRAWING_TILE && getBuildingPhase() != BuildingPhase.FINISHED_BUILDING) {
                                    try {
                                        getLock().wait();
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                        break;
                                    }
                                }
                            }

                            if (getBuildingPhase() == BuildingPhase.FINISHED_BUILDING) {
                                break;
                            }

                            if(getBuildingPhase() == BuildingPhase.DRAWING_TILE) {
                                getCliView().updatePageState(new CLIGamePrinter.DrawFromSavedTilesState());
                                getCliView().displayPage(new Object[]{input, currentTile});
                            } else {
                                System.out.println("Big error. Why you here?");
                                drawnFromSavedIndex = -1;
                            }
                        } else {
                            System.out.println("Big Big error!");
                        }

                    } else {
                        // Saved tile slot is empty
                        getCliView().updatePageState(new CLIGamePrinter.DrawFromSavedTilesErrorState());
                        getCliView().displayPage(new Object[]{input});
                    }
                } else {
                    // Invalid number for drawing from saved tiles
                    getCliView().updatePageState(new CLIGamePrinter.DrawFromSavedTilesErrorState());
                    getCliView().displayPage(new Object[]{input});
                }

            } else if (isRotateCommandWithNumber(input)) {
                if(getCurrentTile() != null) {
                    int rotations = Integer.parseInt(input.split(":")[1]);

                    if(rotations < 0 || rotations > 3) {
                        getCliView().updatePageState(new CLIGamePrinter.InvalidRotationState());
                        getCliView().displayPage(new Object[]{input});
                    } else {
                        send(new RotateTileRequest(id, getCurrentTile(), Integer.parseInt(input.split(":")[1])));
                        synchronized (getLock()) {
                            while (getBuildingPhase() != BuildingPhase.ROTATING_TILE && getBuildingPhase() != BuildingPhase.FINISHED_BUILDING) {
                                try {
                                    getLock().wait();
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }
                        }

                        // Check if the game is finished. If so, exit the loop
                        if(getBuildingPhase() == BuildingPhase.FINISHED_BUILDING) {
                            break;
                        }

                        getCliView().updatePageState(new CLIGamePrinter.DrawnActionState());
                        getCliView().displayPage(new Object[]{input, getCurrentTile()});
                    }
                } else {
                    getCliView().updatePageState(new CLIGamePrinter.NoTileInDrawingStageState());
                    getCliView().displayPage(new Object[]{input});
                }

            } else if (input.equalsIgnoreCase("place")) {
                if(currentTile != null) {
                    getCliView().updatePageState(new CLIGamePrinter.PlacingTileState());
                    getCliView().displayPage(new Object[]{input, currentTile});
                    do {
                        input = in.nextLine().trim();

                        if(!isTilePlacementCorrect(input)) {
                            getCliView().updatePageState(new CLIGamePrinter.InvalidFormatState());
                            getCliView().displayPage(new Object[]{input});
                        } else {
                            row = Integer.parseInt(input.split(":")[0]);
                            col = Integer.parseInt(input.split(":")[1]);
                            if(row < 4 || row > 10 || col < 5 || col > 9) {
                                getCliView().updatePageState(new CLIGamePrinter.InvalidCoordinatesState());
                                getCliView().displayPage(new Object[]{input});
                            }
                        }
                    } while(!isTilePlacementCorrect(input)); // Loop until format is correct
                    // Add another check here if coordinates were invalid

                    send(new PlaceTileRequest(id, row, col, currentTile));
                    synchronized (getLock()) {
                        while (getBuildingPhase() != BuildingPhase.PLACED_TILE_CORRECTLY && getBuildingPhase() != BuildingPhase.PLACED_TILE_WRONGLY && getBuildingPhase() != BuildingPhase.FINISHED_BUILDING) {
                            try {
                                getLock().wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                    }

                    // Check if the game is finished. If so, exit the loop
                    if(getBuildingPhase() == BuildingPhase.FINISHED_BUILDING) {
                        break;
                    }

                    if(getBuildingPhase() == BuildingPhase.PLACED_TILE_CORRECTLY) {
                        // If the tile placed came from a saved slot, clear that slot
                        if (drawnFromSavedIndex != -1) {
                            savedTiles[drawnFromSavedIndex] = null;
                        }

                        getCliView().updatePageState(new CLIGamePrinter.PlacedTileState());
                        getCliView().displayPage(new Object[]{input});
                        currentTile = null; // Clear the current tile after placement
                        drawnFromSavedIndex = -1; // Reset the flag
                    } else {
                        // If placement failed, the tile remains as currentTile
                        getCliView().updatePageState(new CLIGamePrinter.PlacedTileErrorState());
                        getCliView().displayPage(new Object[]{input});
                        // drawnFromSavedIndex remains the same, tile is still "drawn from saved"
                    }
                } else {
                    getCliView().updatePageState(new CLIGamePrinter.NoTileInDrawingStageState());
                    getCliView().displayPage(new Object[]{input});
                }

            } else if (input.equalsIgnoreCase("show")) {
                if (currentTile != null && drawnFromSavedIndex != -1) {
                    // Put the current tile back into its original saved slot
                    send(new DiscardRequest(id, currentTile));
                    synchronized (getLock()) {
                        while (getBuildingPhase() != BuildingPhase.PLACED_TILE_CORRECTLY && getBuildingPhase() != BuildingPhase.FINISHED_BUILDING) {
                            try {
                                getLock().wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                    }
                    savedTiles[drawnFromSavedIndex] = currentTile;

                    // Clear the current tile and reset the flag
                    drawnFromSavedIndex = -1;
                    currentTile = null;

                    setBuildingPhase(BuildingPhase.CHOOSING);
                }

                if(!flippedTiles.isEmpty()) {
                    getCliView().updatePageState(new CLIGamePrinter.ShowDrewTilesState());
                    getCliView().displayPage(new Object[]{input, flippedTiles});

                    setBuildingPhase(BuildingPhase.SHOWING_DREW_CARDS);

                    do {
                        input = in.nextLine().trim();

                        if(!isDiscoveredTileSelectionCorrect(input)) {
                            getCliView().updatePageState(new CLIGamePrinter.InvalidChooseDrewTilesState());
                            getCliView().displayPage(new Object[]{input, flippedTiles});
                        } else {
                            if(currentTile != null) {
                                // Discard actual tile (if it wasn't from saved)
                                send(new DiscardRequest(id, currentTile));
                                synchronized (getLock()) {
                                    while (getBuildingPhase() != BuildingPhase.DISCARDED_TILE && getBuildingPhase() != BuildingPhase.FINISHED_BUILDING) {
                                        try {
                                            getLock().wait();
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        if(getBuildingPhase() == BuildingPhase.FINISHED_BUILDING) {
                            break;
                        }

                    } while(!isDiscoveredTileSelectionCorrect(input));

                    // Now, draw the selected flipped tile (this will become the new currentTile)
                    send(new DrawTileRequest(id, true, Integer.parseInt(input)));
                    synchronized (getLock()) {
                        while (getBuildingPhase() != BuildingPhase.DRAWING_TILE && getBuildingPhase() != BuildingPhase.FINISHED_BUILDING) {
                            try {
                                getLock().wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                    }

                    if(getBuildingPhase() == BuildingPhase.FINISHED_BUILDING) {
                        break;
                    }

                    getCliView().updatePageState(new CLIGamePrinter.DrawnActionState());
                    getCliView().displayPage(new Object[]{input, currentTile});

                } else {
                    getCliView().updatePageState(new CLIGamePrinter.EmptyFlippedTilesState());
                    getCliView().displayPage(new Object[]{input});
                }

            } else if (isShowCommandWithNumber(input)) {
                if(getClient().getGameLevel() == GameLevel.LEVEL_2) {
                    int number = Integer.parseInt(input.split(":")[1]);

                    if (number < 0 || number >= getCardStacks().size()) {
                        getCliView().updatePageState(new CLIGamePrinter.InvalidCardStackState());
                        getCliView().displayPage(new Object[]{input});
                    } else {
                        getCliView().updatePageState(new CLIGamePrinter.ShowCardStackState());
                        getCliView().displayPage(new Object[]{input, getCardStacks().get(number)});
                    }

                } else {
                    getCliView().updatePageState(new CLIGamePrinter.NoShowingCardInTryLevelState());
                    getCliView().displayPage(new Object[]{input});
                }
            } else if (input.equalsIgnoreCase("showship")){
                if (this.coordinatesOfTheShip == null) {
                    this.coordinatesOfTheShip = new ArrayList<>(); // Defensive initialization
                }

                if (this.coordinatesOfTheShip.isEmpty()) {
                    getCliView().updatePageState(new CLIGamePrinter.EmptyShipState());
                    getCliView().displayPage(new Object[]{input});
                } else {
                    getCliView().updatePageState(new CLIGamePrinter.ShowShipState());
                    getCliView().displayPage(new Object[]{input, coordinatesOfTheShip});
                }

            } else if (input.equalsIgnoreCase("finish")) {
                setBuildingPhase(BuildingPhase.PLACING_ROCKET);

                // 1. Get rocket placement coordinates from the player
                int rocketPosition;
                boolean validRocketInput = false;
                RankingPosition startingPosition = null;

                getCliView().updatePageState(new CLIGamePrinter.ChooseStartingPositionState());
                getCliView().displayPage(new Object[]{input});

                while (!validRocketInput) {
                    input = in.nextLine().trim();
                    try {
                        rocketPosition = Integer.parseInt(input);
                        if(rocketPosition >= 1 && rocketPosition <= 4) {
                            validRocketInput = true;
                            startingPosition = RankingPosition.values()[Integer.parseInt(input) - 1];
                        } else {
                            getCliView().updatePageState(new CLIGamePrinter.InvalidStartingPositionState());
                            getCliView().displayPage(new Object[]{input});
                        }
                    } catch (NumberFormatException e) {
                        getCliView().updatePageState(new CLIGamePrinter.InvalidStartingPositionState());
                        getCliView().displayPage(new Object[]{input});
                    }
                }

                getCliView().updatePageState(new CLIGamePrinter.WaitingForHourglassToFinishState());
                getCliView().displayPage(new Object[]{input});

                if(getClient().getGameLevel() == GameLevel.LEVEL_2) {
                    while(getBuildingPhase() != BuildingPhase.FLIPPED_HOURGLASS && getBuildingPhase() != BuildingPhase.WAITING_FINISH && getBuildingPhase() != BuildingPhase.FINISHED_BUILDING) {
                        send(new FlipHourGlassRequest(id));
                        synchronized (getLock()) {
                            try {
                                getLock().wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                    }
                }

                while(getBuildingPhase() != BuildingPhase.WAITING_FINISH && getBuildingPhase() != BuildingPhase.FINISHED_BUILDING) {
                    send(new PlaceRocketRequest(id, startingPosition));
                    synchronized (getLock()) {
                        try {
                            getLock().wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }

                if(getBuildingPhase() == BuildingPhase.FINISHED_BUILDING) {
                    break;
                }

                // Waiting for finish
                getCliView().updatePageState(new CLIGamePrinter.WaitingForHourglassToFinishState());
                getCliView().displayPage(new Object[]{input});
                synchronized (getLock()) {
                    while(getBuildingPhase() != BuildingPhase.FINISHED_BUILDING) {
                        try {
                            getLock().wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }
            } else {
                getCliView().updatePageState(new CLIGamePrinter.InvalidGeneralInputState());
                getCliView().displayPage(new Object[]{input});

            }
        } while(getBuildingPhase() != BuildingPhase.FINISHED_BUILDING);

        getCliView().updatePageState(new CLIGamePrinter.FinishTurnState());
        getCliView().displayPage(new Object[]{input});

    }

    private boolean isShowCommandWithNumber(String input) {
        return input.matches("show:\\d+"); // Matches "show:" followed by one or more digits
    }

    private boolean isRotateCommandWithNumber(String input) {
        return input.matches("rotate:\\d+");
    }

    private boolean isTilePlacementCorrect(String input) {
        return input.matches("\\d+:\\d+");
    }

    private boolean isDiscoveredTileSelectionCorrect(String input) {
        return input.matches("\\d+") && Integer.parseInt(input) >= 0 && Integer.parseInt(input) < flippedTiles.size();
    }

    private boolean isStartingPositionCorrect(String input) {
        return input.matches("\\d+") && Integer.parseInt(input) >= 1 && Integer.parseInt(input) < 5;
    }

    private boolean isDrawCommandWithNumber(String input) {
        return input.matches("draw:\\d+");
    }

    @Override
    public void validationPhase() {
        int row = 0;
        int col = 0;

        setGamePhase(GamePhase.VALIDATING);

        getCliView().setCliInterface(new CLIValidationPrinter());
        getCliView().displayPage(new Object[]{getClient().getColor()});

        do {
            setValidationPhase(ValidationPhase.VALIDATING);

            input = in.nextLine().trim();

            if(input.equalsIgnoreCase("validate")) {
                synchronized (getLock()) {
                    send(new ValidateShipRequest(id));
                    while(getValidationPhase() != ValidationPhase.VALIDATING_ERROR && getValidationPhase() != ValidationPhase.VALIDATED) {
                        try {
                            getLock().wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }

                if(getValidationPhase() == ValidationPhase.VALIDATING_ERROR) {
                    // Eliminate tiles screen -> display all invalid tiles
                    getCliView().updatePageState(new CLIValidationPrinter.NeedToDeleteTilesState());
                    getCliView().displayPage(new Object[]{input, coordinatesOfTheShip});


                    do {
                        input = in.nextLine().trim();
                        if(controlIfCoordinates(input)) {
                            row = Integer.parseInt(input.split(":")[0]);
                            col = Integer.parseInt(input.split(":")[1]);
                        } else {
                            // Error in coordinates
                            getCliView().updatePageState(new CLIValidationPrinter.NeedToDeleteTilesErrorState());
                            getCliView().displayPage(new Object[]{input, coordinatesOfTheShip});
                        }
                    } while(!controlIfCoordinates(input));

                    synchronized (getLock()) {
                        send(new EliminateTileRequest(id, row, col));
                        while(getValidationPhase() != ValidationPhase.VALIDATING_ERROR) {
                            try {
                                getLock().wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                    }
                    getCliView().updatePageState(new CLIValidationPrinter.CorrectlyDeletedTile());
                    getCliView().displayPage(new Object[]{input, coordinatesOfTheShip});
                }

            } else {
                getCliView().updatePageState(new CLIValidationPrinter.ValidateCommandErrorState());
                getCliView().displayPage(new Object[]{input, coordinatesOfTheShip});
            }
        } while(getValidationPhase() != ValidationPhase.VALIDATED);
    }

    private boolean controlIfCoordinates(String input) {
        return input.matches("\\d+:\\d+");
    }

    @Override
    public void setEquipe() {
        int row = 0;
        int col = 0;

        setGamePhase(GamePhase.SETTING_EQUIPE);
        setSetUpPhase(SetUpPhase.WAITING_START);

        getCliView().setCliInterface(new CLISetEquipePrinter());
        getCliView().displayPage(new Object[]{getClient().getColor(), getCoordinatesOfTheShip()});

        if(getClient().getGameLevel() == GameLevel.LEVEL_2) {
            // If level 2 -> set-up equipe
            synchronized (getLock()) {
                while (getSetUpPhase() == SetUpPhase.WAITING_START) {
                    try {
                        getLock().wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            getCliView().updatePageState(new CLISetEquipePrinter.AskEquipeLocationState());
            getCliView().displayPage(new Object[]{input});
        } else {
            // If try level -> set-up automatic
            setSetUpPhase(SetUpPhase.SET_UP_FINISHED);
        }


        while(getSetUpPhase() != SetUpPhase.SET_UP_FINISHED) {
            input = in.nextLine().trim();

            if(areCabinCoordinatesRight(input)) {
                row = Integer.parseInt(input.split(":")[0]);
                col = Integer.parseInt(input.split(":")[1]);

                getCliView().updatePageState(new CLISetEquipePrinter.AskEquipeTypeState());
                getCliView().displayPage(new Object[]{input});

                do {
                    input = in.nextLine().trim();

                    if(!isCrewTypeExisting(input)) {
                        getCliView().updatePageState(new CLISetEquipePrinter.AskEquipeTypeErrorState());
                        getCliView().displayPage(new Object[]{input});
                    } else {
                        send(new PlaceCrewRequest(id, row, col, translateCrewMember(input)));
                        synchronized (getLock()) {
                            while(getSetUpPhase() != SetUpPhase.PLACED_CREW_MEMBERS &&
                                    getSetUpPhase() != SetUpPhase.SETTING_UP_ERROR &&
                                    getSetUpPhase() != SetUpPhase.SET_UP_FINISHED) {
                                try {
                                    getLock().wait();
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }
                        }
                    }

                    if(getSetUpPhase() == SetUpPhase.SETTING_UP_ERROR) {
                        getCliView().updatePageState(new CLISetEquipePrinter.EquipeWronglySetState());
                        getCliView().displayPage(new Object[]{input, getCoordinatesOfTheShip()});
                    } else if(getSetUpPhase() == SetUpPhase.PLACED_CREW_MEMBERS) {
                        getCliView().updatePageState(new CLISetEquipePrinter.EquipeSuccessfullySetState());
                        getCliView().displayPage(new Object[]{input, getCoordinatesOfTheShip()});
                    } else if(getSetUpPhase() == SetUpPhase.SET_UP_FINISHED) {
                        ;
                    } else {
                        System.out.println("Big big error");
                    }

                } while (!isCrewTypeExisting(input) &&
                        getSetUpPhase() != SetUpPhase.PLACED_CREW_MEMBERS &&
                        getSetUpPhase() != SetUpPhase.SETTING_UP_ERROR &&
                        getSetUpPhase() != SetUpPhase.SET_UP_FINISHED);

            } else {
                getCliView().updatePageState(new CLISetEquipePrinter.AskEquipeLocationErrorState());
                getCliView().displayPage(new Object[]{input});
            }
        }

        getCliView().updatePageState(new CLISetEquipePrinter.EquipeWaitingFinishState());
        getCliView().displayPage(new Object[]{input, getCoordinatesOfTheShip()});
        while(getGamePhase() != GamePhase.ADVENTURE) {
            synchronized (getLock()) {
                try {
                    getLock().wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }

            }
        }
    }

    private boolean areCabinCoordinatesRight(String input) {
        return input.matches("\\d+:\\d+");
    }

    private boolean isCrewTypeExisting(String input) {
        return input.matches("crew") || input.matches("alienp") || input.matches("alieny");
    }

    private CrewMember translateCrewMember(String input) {
        switch (input) {
            case "crew": {
                return new Human();
            }
            case "alienp": {
                return new Alien(AlienColor.PURPLE);
            }
            default: {
                return new Alien(AlienColor.YELLOW);
            }
        }
    }

    @Override
    public void adventureShip() {
        setGamePhase(GamePhase.ADVENTURE);

        getCliView().setCliInterface(new CLIAdventurePrinter());
        getCliView().displayPage(new Object[]{getClient().getColor()});

        setAdventurePhase(AdventurePhase.ON_GOING);

        do {
            if(isFirst()) {
                getCliView().updatePageState(new CLIAdventurePrinter.DrawCardState());
                getCliView().displayPage(new Object[]{input});
                do {
                    input = in.nextLine().trim();

                    if(!input.equalsIgnoreCase("draw")) {
                        getCliView().updatePageState(new CLIAdventurePrinter.DrawCardErrorState());
                        getCliView().displayPage(new Object[]{input});
                    }
                } while(!input.equalsIgnoreCase("draw"));
                send(new DrawCardRequest(id));
            }

            // Wait for the card drew. Here, the current card will be changed and then we can operate on it
            while(getAdventurePhase() != AdventurePhase.DREW_CARD && getAdventurePhase() != AdventurePhase.FINISHED) {
                getCliView().updatePageState(new CLIAdventurePrinter.WaitingOtherPlayersState());
                getCliView().displayPage(new Object[]{input});
                synchronized (getLock()) {
                    try {
                        getLock().wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }

            if(getAdventurePhase() == AdventurePhase.FINISHED) {
                getCliView().updatePageState(new CLIAdventurePrinter.FinishedGame());
                getCliView().displayPage(new Object[]{getPlayersPositions(), getPlayersPoints()});
                break;
            }

            switch (getCurrentCard()) {
                case AbandonedShipCard abandonedShipCard -> {
                    // Abandoned ship card
                    getCliView().updatePageState(new CLIAdventurePrinter.DrawnAbandonedShipState());
                    getCliView().displayPage(new Object[]{input, getCurrentCard()});

                    do {
                        input = in.nextLine().trim();

                        if(!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no")) {
                            getCliView().updatePageState(new CLIAdventurePrinter.DrawnAbandonedShipErrorState());
                            getCliView().displayPage(new Object[]{input, getCurrentCard()});
                        }
                    } while(!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no"));

                    if (input.equalsIgnoreCase("yes")) {
                        ((AbandonedShipDTO)getCurrentCard().getDTO()).setChoice(true);
                    } else {
                        ((AbandonedShipDTO)getCurrentCard().getDTO()).setChoice(false);
                    }

                    ((AbandonedShipDTO)getCurrentCard().getDTO()).setPlayerId(id);
                    send(new ApplyAbandonedShipEffectRequest(id, getCurrentCard()));
                }
                case AbandonedStationCard abandonedStationCard -> {
                    // Abandoned station card
                    getCliView().updatePageState(new CLIAdventurePrinter.DrawnAbandonedStationState());
                    getCliView().displayPage(new Object[]{input, getCurrentCard()});

                    do {
                        input = in.nextLine().trim();

                        if(!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no")) {
                            getCliView().updatePageState(new CLIAdventurePrinter.DrawnAbandonedStationErrorState());
                            getCliView().displayPage(new Object[]{input, getCurrentCard()});
                        }
                    } while(!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no"));

                    if (input.equalsIgnoreCase("yes")) {
                        ((AbandonedStationDTO)getCurrentCard().getDTO()).setChoice(true);
                    } else {
                        ((AbandonedStationDTO)getCurrentCard().getDTO()).setChoice(false);
                    }

                    ((AbandonedStationDTO)getCurrentCard().getDTO()).setPlayerId(id);
                    send(new ApplyAbandonedStationEffectRequest(id, getCurrentCard()));
                }
                case EpidemicCard epidemicCard -> {
                    // Epidemic card
                    getCliView().updatePageState(new CLIAdventurePrinter.DrawnEpidemicCardState());
                    getCliView().displayPage(new Object[]{input, getCurrentCard()});

                    do {
                        input = in.nextLine().trim();

                        if(!input.isEmpty()) {
                            getCliView().updatePageState(new CLIAdventurePrinter.DrawnEpidemicErrorState());
                            getCliView().displayPage(new Object[]{input, getCurrentCard()});
                        }
                    } while(!input.isEmpty());

                    ((EpidemicDTO)getCurrentCard().getDTO()).setPlayerId(id);
                    send(new ApplyEpidemicEffectRequest(id, getCurrentCard()));
                }
                case MeteorShowerCard meteorShowerCard -> {
                    // Meteor shower card
                    setAdventurePhase(AdventurePhase.METEOR_SHOWER_ONGOING);

                    while(getAdventurePhase() != AdventurePhase.FINISHED_CARD_EFFECT) {
                        getCliView().updatePageState(new CLIAdventurePrinter.DrawnMeteorShowerCardState());
                        getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});

                        do {
                            input = in.nextLine().trim();

                            if(!isMeteorShowerInputCorrect(input)) {
                                getCliView().updatePageState(new CLIAdventurePrinter.DrawnMeteorShowerErrorState());
                                getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                            }
                        } while (!isMeteorShowerInputCorrect(input));

                        if(!input.isEmpty()) {
                            String[] parts = input.split(":");
                            if (parts.length == 2) {
                                try {
                                    int row = Integer.parseInt(parts[0]);
                                    int col = Integer.parseInt(parts[1]);
                                    ((MeteorShowerDTO)getCurrentCard().getDTO()).setCoordinates(row, col);
                                } catch (NumberFormatException e) {
                                    System.err.println("Error in the parsing: " + input + " - " + e.getMessage());
                                }
                            } else {
                                System.err.println("Segment error: " + input);
                            }
                        } else {
                            ((MeteorShowerDTO)getCurrentCard().getDTO()).setCoordinates(-1, -1);
                        }

                        getCliView().updatePageState(new CLIAdventurePrinter.DrawnMeteorShowerWaitState());
                        getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                        send(new ApplyMeteorShowerEffectRequest(id, getCurrentCard()));
                        synchronized (getLock()) {
                            try {
                                getLock().wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }

                        if(getAdventurePhase() == AdventurePhase.OUT_OF_GAME) {
                            break;
                        }
                    }
                }
                case OpenSpaceCard openSpaceCard -> {
                    // Open space card
                    getCliView().updatePageState(new CLIAdventurePrinter.DrawnOpenSpaceCardState());
                    getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});


                    List<Coordinates> coordinatesList = new ArrayList<>();
                    String[] coordinateSegments = input.split("-");
                    boolean error;

                    do {
                        error = false;
                        input = in.nextLine().trim();

                        if(!input.isEmpty()) {
                            for (String segment : coordinateSegments) {
                                String[] parts = segment.split(":");
                                if (parts.length == 2) {
                                    try {
                                        int row = Integer.parseInt(parts[0]);
                                        int col = Integer.parseInt(parts[1]);
                                        coordinatesList.add(new Coordinates(row, col));
                                    } catch (NumberFormatException e) {
                                        getCliView().updatePageState(new CLIAdventurePrinter.DrawnOpenSpaceErrorState());
                                        getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                                        error = true;
                                        break;
                                    }
                                } else {
                                    getCliView().updatePageState(new CLIAdventurePrinter.DrawnOpenSpaceErrorState());
                                    getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                                    error = true;
                                    break;
                                }
                            }
                        }
                    } while(error);

                    ((OpenSpaceDTO)getCurrentCard().getDTO()).setPlayerId(id);
                    ((OpenSpaceDTO)getCurrentCard().getDTO()).setUsingTilesWithBattery(coordinatesList);

                    send(new ApplyOpenSpaceRequest(id, getCurrentCard()));
                }
                case PirateCard pirateCard -> {
                    // Pirate card
                    getCliView().updatePageState(new CLIAdventurePrinter.DrawnPirateCardState());
                    getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});

                    do {
                        input = in.nextLine().trim();

                        if(!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no")) {
                            getCliView().updatePageState(new CLIAdventurePrinter.DrawnPirateErrorState());
                            getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                        }
                    } while(!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no"));


                    if(input.equalsIgnoreCase("yes")) {
                        // Calculate fire power
                        boolean error;
                        List<Coordinates> coordinatesList = new ArrayList<>();
                        String[] coordinateSegments;

                        do {
                            error = false;
                            getCliView().updatePageState(new CLIAdventurePrinter.CalculateFirePowerState());
                            getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                            input = in.nextLine().trim();

                            if(!input.isEmpty()) {
                                coordinateSegments = input.split("-");
                                for (String segment : coordinateSegments) {
                                    String[] parts = segment.split(":");
                                    if (parts.length == 2) {
                                        try {
                                            int row = Integer.parseInt(parts[0]);
                                            int col = Integer.parseInt(parts[1]);
                                            coordinatesList.add(new Coordinates(row, col));
                                        } catch (NumberFormatException e) {
                                            getCliView().updatePageState(new CLIAdventurePrinter.CalculateFirePowerErrorState());
                                            getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                                            error = true;
                                            break;
                                        }
                                    } else {
                                        getCliView().updatePageState(new CLIAdventurePrinter.CalculateFirePowerErrorState());
                                        getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                                        error = true;
                                        break;
                                    }
                                }
                            }
                        } while(error);
                        ((PiratesDTO)getCurrentCard().getDTO()).setChoice(true);
                        ((PiratesDTO)getCurrentCard().getDTO()).setUsingDoubleCannons(coordinatesList);

                        send(new ApplyPirateEffectRequest(id, getCurrentCard()));
                        synchronized (getLock()) {
                            try {
                                getLock().wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                    } else {
                        ((PiratesDTO)getCurrentCard().getDTO()).setChoice(false);
                        send(new ApplyPirateEffectRequest(id, getCurrentCard()));
                        synchronized (getLock()) {
                            try {
                                getLock().wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                    }

                    if(getAdventurePhase() == AdventurePhase.PIRATES_ONGOING) {
                        while(getAdventurePhase() != AdventurePhase.PIRATES_FINISHED && getAdventurePhase() != AdventurePhase.FINISHED_CARD_EFFECT && getAdventurePhase() != AdventurePhase.OUT_OF_GAME) {
                            getCliView().updatePageState(new CLIAdventurePrinter.PirateFireShotState());
                            getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                            input = in.nextLine().trim();

                            if(!isFireShotInputCorrect(input)) {
                                getCliView().updatePageState(new CLIAdventurePrinter.PirateFireShotErrorState());
                                getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                            } else {
                                if(!input.isEmpty()) {
                                    String[] parts = input.split(":");
                                    if (parts.length == 2) {
                                        try {
                                            int row = Integer.parseInt(parts[0]);
                                            int col = Integer.parseInt(parts[1]);
                                            ((PiratesDTO)getCurrentCard().getDTO()).setUsingShield(row, col);
                                        } catch (NumberFormatException e) {
                                            System.err.println("Error in the parsing: " + input + " - " + e.getMessage());
                                        }
                                    } else {
                                        System.err.println("Segment error: " + input);
                                    }
                                } else {
                                    ((PiratesDTO)getCurrentCard().getDTO()).setUsingShield(-1, -1);
                                }

                                send(new ApplyPirateEffectRequest(id, getCurrentCard()));
                                synchronized (getLock()) {
                                    try {
                                        getLock().wait();
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                case PlanetsCard planetsCard -> {
                    // Planets card
                    getCliView().updatePageState(new CLIAdventurePrinter.DrawnPlanetsCardState());
                    getCliView().displayPage(new Object[]{input, getCurrentCard()});

                    do {
                        input = in.nextLine().trim();

                        if(!input.matches("\\d+")) {
                            getCliView().updatePageState(new CLIAdventurePrinter.DrawnPlanetsErrorState());
                            getCliView().displayPage(new Object[]{input, getCurrentCard()});
                        }
                    } while (!input.matches("\\d+"));

                    int choice = Integer.parseInt(input);
                    ((PlanetsDTO)getCurrentCard().getDTO()).setChoice(choice);

                    ((PlanetsDTO)getCurrentCard().getDTO()).setPlayerId(id);
                    send(new ApplyPlanetsEffectRequest(id, getCurrentCard()));

                }
                case SlaversCard slaversCard -> {
                    // Slavers card
                    getCliView().updatePageState(new CLIAdventurePrinter.DrawnSlaversCardState());
                    getCliView().displayPage(new Object[]{input, getCurrentCard()});

                    do {
                        input = in.nextLine().trim();

                        if(!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no")) {
                            getCliView().updatePageState(new CLIAdventurePrinter.DrawnSlaversErrorState());
                            getCliView().displayPage(new Object[]{input});
                        }
                    } while(!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no"));

                    if (input.equalsIgnoreCase("yes")) {
                        ((SlaversDTO)getCurrentCard().getDTO()).setChoice(true);
                    } else {
                        ((SlaversDTO)getCurrentCard().getDTO()).setChoice(false);
                    }
                    ((SlaversDTO)getCurrentCard().getDTO()).setPlayerId(id);
                    send(new ApplySlaversEffectRequest(id, getCurrentCard()));
                }
                case SmugglersCard smugglersCard -> {
                    // Smugglers card
                    getCliView().updatePageState(new CLIAdventurePrinter.DrawnSmugglersCardState());
                    getCliView().displayPage(new Object[]{input, getCurrentCard()});

                    do {
                        input = in.nextLine().trim();

                        if(!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no")) {
                            getCliView().updatePageState(new CLIAdventurePrinter.DrawnSmugglersErrorState());
                            getCliView().displayPage(new Object[]{input, getCurrentCard()});
                        }
                    } while(!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no"));

                    if (input.equalsIgnoreCase("yes")) {
                        ((SmugglersDTO)getCurrentCard().getDTO()).setChoice(true);
                    } else {
                        ((SmugglersDTO)getCurrentCard().getDTO()).setChoice(false);
                    }
                    ((SmugglersDTO)getCurrentCard().getDTO()).setPlayerId(id);
                    send(new ApplySmugglersEffectRequest(id, getCurrentCard()));
                }
                case StellarSpaceDustCard stellarSpaceDustCard -> {
                    // Stellar space dust card
                    getCliView().updatePageState(new CLIAdventurePrinter.DrawnStellarSpaceDustCardState());
                    getCliView().displayPage(new Object[]{input, getCurrentCard()});

                    do {
                        input = in.nextLine().trim();

                        if(!input.isEmpty()) {
                            getCliView().updatePageState(new CLIAdventurePrinter.DrawnStellarSpaceDustErrorState());
                            getCliView().displayPage(new Object[]{input, getCurrentCard()});
                        }
                    } while(!input.isEmpty());

                    getCurrentCard().getDTO().setPlayerId(id);
                    send(new ApplyStellarSpaceDustEffectRequest(id, getCurrentCard()));
                }
                case WarZoneCard warZoneCard -> {
                    // War zone card
                    List<Coordinates> coordinatesList = new ArrayList<>();
                    setAdventurePhase(AdventurePhase.START_WAR_ZONE);

                    getCliView().updatePageState(new CLIAdventurePrinter.DrawnWarZoneCardState());
                    getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});

                    do {
                        input = in.nextLine().trim();
                        if(!input.isEmpty()) {
                            getCliView().updatePageState(new CLIAdventurePrinter.DrawnWarZoneErrorState());
                            getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                        }
                    } while (!input.isEmpty());

                    switch(((WarZoneCard)getCurrentCard()).getPenalties().get(((WarZoneDTO)getCurrentCard().getDTO()).getPenaltyNumber()).getWarZonePenaltyType()) {
                        case WarZonePenaltyType.LOWEST_CREW -> {
                            // Print the fact that the crew is being eliminated
                            getCliView().updatePageState(new CLIAdventurePrinter.WarZoneLowestCrewState());
                            getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});

                            if(((WarZoneCard)getCurrentCard()).getPenalties().get(((WarZoneDTO)getCurrentCard().getDTO()).getPenaltyNumber()).getWarZonePenaltyEffect() != WarZonePenaltyEffect.HIT_BY_PROJECTILES) {
                                // Select the shield
                                while(getAdventurePhase() != AdventurePhase.WAR_ZONE_FIRE_SHOTS_FINISHED && getAdventurePhase() != AdventurePhase.FINISHED_CARD_EFFECT && getAdventurePhase() != AdventurePhase.OUT_OF_GAME) {
                                    getCliView().updatePageState(new CLIAdventurePrinter.WarZoneSelectShieldState());
                                    getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                                    input = in.nextLine().trim();

                                    if(!isFireShotInputCorrect(input)) {
                                        getCliView().updatePageState(new CLIAdventurePrinter.WarZoneSelectShieldErrorState());
                                        getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                                    } else {
                                        if(!input.isEmpty()) {
                                            String[] parts = input.split(":");
                                            if (parts.length == 2) {
                                                try {
                                                    int row = Integer.parseInt(parts[0]);
                                                    int col = Integer.parseInt(parts[1]);
                                                    ((WarZoneDTO)getCurrentCard().getDTO()).emptyList();
                                                    ((WarZoneDTO)getCurrentCard().getDTO()).addCoordinates(new Coordinates(row, col));
                                                } catch (NumberFormatException e) {
                                                    System.err.println("Error in the parsing: " + input + " - " + e.getMessage());
                                                }
                                            } else {
                                                System.err.println("Segment error: " + input);
                                            }
                                        } else {
                                            ((WarZoneDTO)getCurrentCard().getDTO()).emptyList();
                                            ((WarZoneDTO)getCurrentCard().getDTO()).addCoordinates(new Coordinates(-1, -1));
                                        }

                                        send(new ApplyWarZoneEffectRequest(id, getCurrentCard()));
                                        synchronized (getLock()) {
                                            try {
                                                getLock().wait();
                                            } catch (InterruptedException e) {
                                                Thread.currentThread().interrupt();
                                                break;
                                            }
                                        }
                                    }
                                }
                            } else {
                                do {
                                    input = in.nextLine().trim();
                                    if(!input.isEmpty()) {
                                        getCliView().updatePageState(new CLIAdventurePrinter.WarZoneLowestCrewErrorState());
                                        getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                                    }
                                } while (!input.isEmpty());
                                send(new ApplyWarZoneEffectRequest(id, getCurrentCard()));
                            }
                        }
                        case WarZonePenaltyType.LOWEST_FIREPOWER -> {
                            getCliView().updatePageState(new CLIAdventurePrinter.WarZoneSelectFirePowerState());
                            getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                            do {
                                input = in.nextLine().trim();

                                if(!isFireShotsInputCorrect(input)) {
                                    getCliView().updatePageState(new CLIAdventurePrinter.WarZoneSelectFirePowerState());
                                    getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                                } else {
                                    String[] coordinateSegments = input.split("-");
                                    if(!input.isEmpty()) {
                                        for (String segment : coordinateSegments) {
                                            String[] parts = segment.split(":");
                                            if (parts.length == 2) {
                                                try {
                                                    int row = Integer.parseInt(parts[0]);
                                                    int col = Integer.parseInt(parts[1]);
                                                    coordinatesList.add(new Coordinates(row, col));
                                                } catch (NumberFormatException e) {
                                                    System.out.println("Big error");
                                                    break;
                                                }
                                            } else {
                                                System.out.println("Big error");
                                                break;
                                            }
                                        }
                                        ((WarZoneCard) getCurrentCard()).getWarZoneDTO().setUsingTilesWithBattery(coordinatesList);
                                    } else {
                                        ((WarZoneCard) getCurrentCard()).getWarZoneDTO().emptyList();
                                        ((WarZoneCard) getCurrentCard()).getWarZoneDTO().addCoordinates(new Coordinates(-1, -1));
                                    }
                                }
                            } while(!isFireShotsInputCorrect(input));

                            getCliView().updatePageState(new CLIAdventurePrinter.WarZoneWaitingOtherPlayersState());
                            getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                            send(new ApplyWarZoneEffectRequest(id, getCurrentCard()));
                            synchronized (getLock()) {
                                try {
                                    getLock().wait();
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                            }

                            if(getAdventurePhase() == AdventurePhase.WAR_ZONE_LOWEST_FIRE_POWER) {
                                if(((WarZoneCard)getCurrentCard()).getPenalties().get(((WarZoneDTO)getCurrentCard().getDTO()).getPenaltyNumber()).getWarZonePenaltyEffect() != WarZonePenaltyEffect.HIT_BY_PROJECTILES) {
                                    while(getAdventurePhase() != AdventurePhase.WAR_ZONE_FIRE_SHOTS_FINISHED && getAdventurePhase() != AdventurePhase.FINISHED_CARD_EFFECT && getAdventurePhase() != AdventurePhase.OUT_OF_GAME) {
                                        // Select the shield
                                        ((WarZoneDTO)getCurrentCard().getDTO()).emptyList();

                                        getCliView().updatePageState(new CLIAdventurePrinter.WarZoneSelectShieldState());
                                        getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                                        input = in.nextLine().trim();

                                        if(!isFireShotInputCorrect(input)) {
                                            getCliView().updatePageState(new CLIAdventurePrinter.WarZoneSelectShieldErrorState());
                                            getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                                        } else {
                                            if(!input.isEmpty()) {
                                                String[] parts = input.split(":");
                                                if (parts.length == 2) {
                                                    try {
                                                        int row = Integer.parseInt(parts[0]);
                                                        int col = Integer.parseInt(parts[1]);
                                                        coordinatesList.add(new Coordinates(row, col));
                                                    } catch (NumberFormatException e) {
                                                        System.err.println("Error in the parsing: " + input + " - " + e.getMessage());
                                                    }
                                                } else {
                                                    System.err.println("Segment error: " + input);
                                                }
                                                ((WarZoneCard) getCurrentCard()).getWarZoneDTO().setUsingTilesWithBattery(coordinatesList);
                                            } else {
                                                ((WarZoneDTO)getCurrentCard().getDTO()).emptyList();
                                                ((WarZoneDTO)getCurrentCard().getDTO()).addCoordinates(new Coordinates(-1, -1));
                                            }

                                            send(new ApplyWarZoneEffectRequest(id, getCurrentCard()));
                                            synchronized (getLock()) {
                                                try {
                                                    getLock().wait();
                                                } catch (InterruptedException e) {
                                                    Thread.currentThread().interrupt();
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    // Just press enter because you gotta perish
                                    do {
                                        input = in.nextLine().trim();
                                        if(!input.isEmpty()) {
                                            getCliView().updatePageState(new CLIAdventurePrinter.WarZoneLowestCrewErrorState());
                                            getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                                        }
                                    } while (!input.isEmpty());
                                    send(new ApplyWarZoneEffectRequest(id, getCurrentCard()));
                                }
                            }

                        }
                        case WarZonePenaltyType.LOWEST_THRUST -> {
                            getCliView().updatePageState(new CLIAdventurePrinter.WarZoneSelectThrustPowerState());
                            getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                            do {
                                ((WarZoneDTO)getCurrentCard().getDTO()).emptyList();
                                input = in.nextLine().trim();

                                if(!isFireShotsInputCorrect(input)) {
                                    getCliView().updatePageState(new CLIAdventurePrinter.WarZoneSelectThrustPowerErrorState());
                                    getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                                } else {
                                    String[] coordinateSegments = input.split("-");
                                    if(!input.isEmpty()) {
                                        for (String segment : coordinateSegments) {
                                            String[] parts = segment.split(":");
                                            if (parts.length == 2) {
                                                try {
                                                    int row = Integer.parseInt(parts[0]);
                                                    int col = Integer.parseInt(parts[1]);
                                                    coordinatesList.add(new Coordinates(row, col));
                                                } catch (NumberFormatException e) {
                                                    System.out.println("Big error");
                                                    break;
                                                }
                                            } else {
                                                System.out.println("Big error");
                                                break;
                                            }
                                        }
                                        ((WarZoneCard) getCurrentCard()).getWarZoneDTO().setUsingTilesWithBattery(coordinatesList);
                                    } else {
                                        ((WarZoneDTO)getCurrentCard().getDTO()).emptyList();
                                        ((WarZoneDTO)getCurrentCard().getDTO()).addCoordinates(new Coordinates(-1, -1));
                                    }
                                }
                            } while(!isFireShotsInputCorrect(input));

                            getCliView().updatePageState(new CLIAdventurePrinter.WarZoneWaitingOtherPlayersState());
                            getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                            send(new ApplyWarZoneEffectRequest(id, getCurrentCard()));
                            synchronized (getLock()) {
                                try {
                                    getLock().wait();
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                            }

                            if(getAdventurePhase() == AdventurePhase.WAR_ZONE_LOWEST_THRUST_POWER) {
                                if(((WarZoneCard)getCurrentCard()).getPenalties().get(((WarZoneDTO)getCurrentCard().getDTO()).getPenaltyNumber()).getWarZonePenaltyEffect() != WarZonePenaltyEffect.HIT_BY_PROJECTILES) {
                                    while(getAdventurePhase() != AdventurePhase.WAR_ZONE_FIRE_SHOTS_FINISHED && getAdventurePhase() != AdventurePhase.FINISHED_CARD_EFFECT && getAdventurePhase() != AdventurePhase.OUT_OF_GAME) {
                                        // Select the shield
                                        ((WarZoneDTO)getCurrentCard().getDTO()).emptyList();

                                        getCliView().updatePageState(new CLIAdventurePrinter.WarZoneSelectShieldState());
                                        getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                                        input = in.nextLine().trim();

                                        if(!isFireShotInputCorrect(input)) {
                                            getCliView().updatePageState(new CLIAdventurePrinter.WarZoneSelectShieldErrorState());
                                            getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                                        } else {
                                            if(!input.isEmpty()) {
                                                String[] parts = input.split(":");
                                                if (parts.length == 2) {
                                                    try {
                                                        int row = Integer.parseInt(parts[0]);
                                                        int col = Integer.parseInt(parts[1]);
                                                        coordinatesList.add(new Coordinates(row, col));
                                                    } catch (NumberFormatException e) {
                                                        System.err.println("Error in the parsing: " + input + " - " + e.getMessage());
                                                    }
                                                } else {
                                                    System.err.println("Segment error: " + input);
                                                }
                                                ((WarZoneCard) getCurrentCard()).getWarZoneDTO().setUsingTilesWithBattery(coordinatesList);
                                            } else {
                                                ((WarZoneDTO)getCurrentCard().getDTO()).emptyList();
                                                ((WarZoneDTO)getCurrentCard().getDTO()).addCoordinates(new Coordinates(-1, -1));
                                            }

                                            send(new ApplyWarZoneEffectRequest(id, getCurrentCard()));
                                            synchronized (getLock()) {
                                                try {
                                                    getLock().wait();
                                                } catch (InterruptedException e) {
                                                    Thread.currentThread().interrupt();
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    // Just press enter because you gotta perish
                                    do {
                                        input = in.nextLine().trim();
                                        if(!input.isEmpty()) {
                                            getCliView().updatePageState(new CLIAdventurePrinter.WarZoneLowestCrewErrorState());
                                            getCliView().displayPage(new Object[]{input, getCurrentCard(), getCoordinatesOfTheShip()});
                                        }
                                    } while (!input.isEmpty());
                                    send(new ApplyWarZoneEffectRequest(id, getCurrentCard()));
                                }
                            }
                        }
                    }
                }
                case null, default -> System.out.println("Big big error");
            }

            while(getAdventurePhase() != AdventurePhase.FINISHED_CARD_EFFECT && getAdventurePhase() != AdventurePhase.METEOR_SHOWER_FINISHED && getAdventurePhase() != AdventurePhase.OUT_OF_GAME) {
                synchronized (getLock()) {
                    try {
                        getLock().wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }

        } while (getAdventurePhase() != AdventurePhase.FINISHED && getAdventurePhase() != AdventurePhase.OUT_OF_GAME);

        if(getAdventurePhase() == AdventurePhase.OUT_OF_GAME) {
            if(getScore() > 0) {
                getCliView().updatePageState(new CLIAdventurePrinter.FinishedGameWonState());
                getCliView().displayPage(new Object[]{getScore()});
            } else {
                getCliView().updatePageState(new CLIAdventurePrinter.FinishedGameLostState());
                getCliView().displayPage(new Object[]{getScore()});
            }
        } else {
            getCliView().updatePageState(new CLIAdventurePrinter.FinishedGame());
            getCliView().displayPage(new Object[]{getPlayersPositions(), getPlayersPoints()});
        }
    }

    private boolean isMeteorShowerInputCorrect(String input) {
        if (input == null) {
            return false;
        }

        if(input.isEmpty()) {
            return true;
        }

        String pattern = "^\\d+:\\d+";
        return input.matches(pattern);
    }

    private boolean isFireShotInputCorrect(String input) {
        if (input == null) {
            return false;
        }

        if(input.isEmpty()) {
            return true;
        }

        String pattern = "^\\d+:\\d+";
        return input.matches(pattern);
    }

    private boolean isFireShotsInputCorrect(String input) {
        if (input == null) {
            return false;
        }

        if (input.isEmpty()) {
            return true;
        }

        String pattern = "^\\d+:\\d+(-\\d+:\\d+)*$";
        return input.matches(pattern);
    }
}