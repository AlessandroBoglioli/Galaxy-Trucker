package it.polimi.ingsoftware.ll13.server.controller;

import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.player.Player;
import it.polimi.ingsoftware.ll13.network.requests.menu_requests.NewMatchRequest;
import it.polimi.ingsoftware.ll13.network.response.menu_response.*;
import it.polimi.ingsoftware.ll13.server.ClientHandler;
import it.polimi.ingsoftware.ll13.server.ClientNotifier;
import it.polimi.ingsoftware.ll13.server.Server;
import it.polimi.ingsoftware.ll13.network.connection.ServerHearthBeat;
import it.polimi.ingsoftware.ll13.network.requests.menu_requests.LoginRequest;
import it.polimi.ingsoftware.ll13.network.requests.PingRequest;
import it.polimi.ingsoftware.ll13.network.requests.PongRequest;
import it.polimi.ingsoftware.ll13.network.response.PongResponse;
import it.polimi.ingsoftware.ll13.network.response.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The MenuController class handles the preparation phase of the game. It is responsible for logging in players,
 * setting the expected number of players for the game, and managing the lobby before the match starts.
 * It also handles communication between the server and clients regarding the match start and player status.
 * This class is a singleton and should only be instantiated once in the lifecycle of the game.
 * It also manages the Ping/Pong requests to keep the connection alive or safely disconnect players
 */
public class MenuController implements Controller{
    private static MenuController instance;
    private static ArrayList<Player> loggedPlayers;
    private static  ArrayList<ClientNotifier> clientNotifiers;
    private static ArrayList<ClientHandler> clientHandlers;
    private static ArrayList<ServerHearthBeat> serverHearthBeats;
    private int expectedPlayers;
    private boolean matchStarted;
    private GameLevel gameLevel;

    protected MenuController(){
        loggedPlayers = new ArrayList<>();
        clientNotifiers = new ArrayList<>();
        clientHandlers = new ArrayList<>();
        this.matchStarted=false;
        serverHearthBeats = new ArrayList<>();
    }


    /**
     * @return the singleton instance of the MenuController.
     */
    public static MenuController getInstance(){
        if(instance==null){
            instance = new MenuController();
        }
        return instance;
    }
    /**
     * Adds a heartbeat handler to the list of heartbeat handlers.
     * This keeps track of the heartbeats for all active clients.
     * @param serverHearthBeat the heartbeat handler to add.
     */
    public synchronized void addHeartBeat(ServerHearthBeat serverHearthBeat){
        serverHearthBeats.add(serverHearthBeat);
    }
    /**
     * Removes a heartbeat handler from the list and shuts it down.
     * @param serverHearthBeat the heartbeat handler to remove.
     */
    public static synchronized void removeHearthBeat(ServerHearthBeat serverHearthBeat){
        serverHearthBeat.shutdown();
        serverHearthBeats.remove(serverHearthBeat);
    }
    //RESPONSE HELPERS
    /**
     * Adds a {@link ClientNotifier} to the list of client notifiers.
     * @param clientNotifier the {@link ClientNotifier} to be added to the list
     */
    public synchronized void addClientNotifier(ClientNotifier clientNotifier){
        clientNotifiers.add(clientNotifier);
    }

    /**
     * Removes a {@link ClientNotifier} from the list of client notifiers.
     * @param clientNotifier the {@link ClientNotifier} to be removed from the list
     */
    public static synchronized void removeClientNotifier(ClientNotifier clientNotifier){
        clientNotifiers.remove(clientNotifier);
    }
    /**
     * Adds a {@link ClientHandler} to the list of client notifiers.
     * @param clientHandler the {@link ClientHandler} to be added to the list
     */
    public synchronized void addClientHandler(ClientHandler clientHandler){
        clientHandlers.add(clientHandler);
    }
    /**
     * Removes a {@link ClientHandler} from the list of client notifiers.
     * @param clientHandler the {@link ClientHandler} to be removed from the list
     */
    public static synchronized void removeClientHandler(ClientHandler clientHandler){
        clientHandlers.remove(clientHandler);
    }

    /**
     *
     * @param id of the player to which the response is being sent to
     * @return the player's Notifier
     */
    private static synchronized ClientNotifier getClientNotifier(int id){
        return clientNotifiers.stream().filter(clientNotifier -> clientNotifier.getId() == id).findFirst().orElse(null);
    }
    /**
     * @param id of the player to which the response is being sent to
     * @return the player's handler
     */
    private static synchronized ClientHandler getClientHandler(int id){
        return clientHandlers.stream().filter(clientHandler -> clientHandler.getId() == id).findFirst().orElse(null);
    }
    /**
     * @param id the unique identifier of the player (client).
     * @return the ServerHearthBeat instance associated with the player, or null if not found.
     */
    private static ServerHearthBeat getServerHearthBeat(int id) {
        return serverHearthBeats.stream().filter(heartBeatHandler -> heartBeatHandler.getId() == id).findFirst().orElse(null);
    }

    /**
     *
     * @param id of the player which the server is looking for
     * @param response to send to the player
     */
    public static synchronized void send(int id, Response response){
        getClientNotifier(id).send(response);
    }
    /**
     *
     * @param id of the player
     * @return the Player having the searched id
     */
    private static synchronized Player getPlayer(int id){
        return loggedPlayers.stream().filter(player -> player.getId() == id).findFirst().orElse(null);
    }
    //-------------
    /**
     * Validates if the username is already taken by another player.
     * @param username the username to validate
     * @return true if the username is available (not taken), false otherwise
     */
    public synchronized boolean validateUsername(@NotNull String username){
        return loggedPlayers.stream().map(Player::getUsername).noneMatch(username::equalsIgnoreCase);
    }

    /**
     * Starts the match once all the expected players have joined.
     * Updates the controller for each client handler to the GameController and informs the clients that the match has started.
     */
    private synchronized void startMatch(){
        matchStarted=true;
        //call a method that sets the controller field of every clientHandler in the List to MatchController
        Server.logInfo("All players have joined, Starting Match...");
        for(Player player : loggedPlayers){
            player.setInMatch(true);
        }
        clientHandlers.forEach(clientHandler -> clientHandler.setController(GameController.getInstance()));
        GameController.getInstance().startMatch(clientNotifiers,loggedPlayers, gameLevel);
    }

    public synchronized void newMatchRequestHandler(@NotNull NewMatchRequest newMatchRequest){
        if(!canCreateNewMatch()){
            send(newMatchRequest.getSenderId(), new JoinedMatchResponse(false,-1, true));
            return;
        }
        this.expectedPlayers = newMatchRequest.getNumberOfPlayers();
        this.gameLevel = newMatchRequest.getGameLevel();
        Player host = new Player(newMatchRequest.getSenderId(), newMatchRequest.getUsername(), PlayerColors.RED);
        loggedPlayers.add(host);
        send(newMatchRequest.getSenderId(), new JoinedMatchResponse(true, 1, false)); // only him is in the lobby rn

    }

    /**
     * Handles the login request of a player connecting to the server.
     * If the match has already started, it sends a response indicating that the match has begun.
     * If the username is taken, it sends a response indicating that the username is not available.
     * If the player is the first to join, it prompts them to set the expected number of players.
     * @param loginRequest the login request sent by the player.
     */
    public synchronized void loginRequestHandler(@NotNull LoginRequest loginRequest){
        Server.logInfo("Received Login Request");
        int id = loginRequest.getSenderId();
        String username = loginRequest.getUsername();
        if(matchStarted){
            send(id, new JoinedMatchResponse(false,-1, true));
            return;
        }
        if(loggedPlayers.isEmpty()){
            send(id, new JoinedMatchResponse(false,0, false));
        }
        if(!validateUsername(username)){
            send(id, new UsernameTakenResponse());
            return;
        }
        PlayerColors color = assignColor(loggedPlayers.size());
        loggedPlayers.add(new Player(
                id,
                loginRequest.getUsername(),
                color
        ));
        send(id, new JoinedMatchResponse(true, loggedPlayers.size(), false));
        if(loggedPlayers.size() == expectedPlayers){
            startMatch();
        }


    }
    /**
     * Handles the PongRequest sent by the client in response to a PingRequest.
     * Resets the heartbeat counter for the client when the PongResponse is received.
     * @param pongRequest the PongRequest sent by the client.
     */
    public synchronized void pongRequestHandler(PongRequest pongRequest){
        getServerHearthBeat(pongRequest.getSenderId()).resetCounter();
    }
    /**
     * Handles the PingRequest sent by the client to check the server's responsiveness.
     * The server responds with a PongResponse to confirm the connection.
     * @param pingRequest the PingRequest sent by the client.
     */
    public synchronized void pingRequestHandler(PingRequest pingRequest){
        send(pingRequest.getSenderId(),new PongResponse());

    }

    /**
     * method used to disconnect a player
     * @param playerId the client id to disconnect
     */
    public static synchronized void disconnectClient(int playerId) {
        Player player = getPlayer(playerId);
        if (player != null && loggedPlayers.contains(player)) {
            if (player.isInMatch()) {
                GameController.getInstance().disconnectClient(playerId);
                return;
            }
        }
        ServerHearthBeat heartBeat = getServerHearthBeat(playerId);
        if (heartBeat != null) {
            removeHearthBeat(heartBeat);
        }
        ClientNotifier clientNotifier = getClientNotifier(playerId);
        if(clientNotifier != null){
            removeClientNotifier(clientNotifier);
        }
        ClientHandler clientHandler = getClientHandler(playerId);
        if (clientHandler != null) {
            try {
                clientHandler.getClientSocket().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            removeClientHandler(clientHandler);
        }

        Server.logInfo(">> [" + playerId + "] client disconnected");
    }

    private synchronized boolean canCreateNewMatch(){
        return !matchStarted && loggedPlayers.isEmpty();
    }

    private PlayerColors assignColor(int playerIndex) {
        return switch (playerIndex) {
            case 0 -> PlayerColors.RED;
            case 1 -> PlayerColors.BLUE;
            case 2 -> PlayerColors.YELLOW;
            case 3 -> PlayerColors.GREEN;
            default -> throw new IllegalStateException();
        };
    }
}

