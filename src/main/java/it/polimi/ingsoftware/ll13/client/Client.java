package it.polimi.ingsoftware.ll13.client;

import it.polimi.ingsoftware.ll13.client.controller.ClientController;
import it.polimi.ingsoftware.ll13.client.exceptions.ExceptionHandler;
import it.polimi.ingsoftware.ll13.client.exceptions.SocketConnectionError;
import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.network.connection.ClientHearthBeat;
/**
 * This class represents the main client entity in a client-server application.
 * It is implemented as a singleton and is responsible for managing client-specific state
 * This class implements {@code Runnable} and its {@code run()} method initiates the connection
 * to the server, starts the response handler thread, sets up the heartbeat mechanism, and manages
 * the client lifecycle within a match.
 */
public class Client implements Runnable{
    private static Client instance;
    private String username;
    private PlayerColors color;
    private GameLevel gameLevel;
    private boolean readyToValidate;
    private boolean validated;
    private boolean inFlight;
    private ClientController controller;
    private boolean isActive = true;
    private boolean inMatch;
    private boolean logged;
    private ExceptionHandler exceptionHandler;

    public static Client getInstance(){
        if(instance == null){
            instance = new Client();
        }
        return instance;
    }

    //----->GETTERS<-----//

    public String getUsername() {
        return username;
    }
    /**
     * Returns whether the client is currently active.
     * @return true if active, otherwise false
     */
    public boolean isActive() {
        return isActive;
    }

    public boolean isLogged() {
        return logged;
    }

    public boolean isInMatch() {
        return inMatch;
    }

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public PlayerColors getColor() {
        return color;
    }

    public GameLevel getGameLevel() {
        return gameLevel;
    }

    public boolean isReadyToValidate(){
        return readyToValidate;
    }

    public boolean isValidated(){
        return validated;
    }

    public boolean isInFlight(){
        return inFlight;
    }
    //-----> SETTERS<-----//

    public void setUsername(String username) {
        this.username = username;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setInMatch(boolean inMatch) {
        this.inMatch = inMatch;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public void setController(ClientController controller) {
        this.controller = controller;
    }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void setColor(PlayerColors color) {
        this.color = color;
    }

    public void setGameLevel(GameLevel gameLevel) {
        this.gameLevel = gameLevel;
    }

    public void setReadyToValidate(boolean readyToValidate){
        this.readyToValidate = readyToValidate;
    }

    public void setValidated(boolean validated){
        this.validated = validated;
    }

    public void setInFlight(boolean inFlight) {
        this.inFlight = inFlight;
    }

    /**
     * Starts the client logic
     */
    @Override
    public void run() {
        // Instantiated both view and view controller
        controller.setClient(this);

        try {
            controller.connectToServer();
        }catch (SocketConnectionError e){
            exceptionHandler.handle(e);
            return;
        }
        controller.setUp();
        controller.setId();
        Thread responseReader = controller.responseReaderThread();
        responseReader.start();
        startHeartBeat();
        controller.join();
        controller.waitingRoom();
        controller.waitForHostOk();
        controller.buildingShip();
        controller.validationPhase();
        controller.setEquipe();
        controller.adventureShip();
    }

    /**
     * Initializes the heartbeat mechanism by associating the controller with the
     * {@code ClientHearthBeat} class and starting the heartbeat scheduler.
     */
    private void startHeartBeat(){
        ClientHearthBeat.setController(controller);
        ClientHearthBeat.heartBeat();
    }

}

