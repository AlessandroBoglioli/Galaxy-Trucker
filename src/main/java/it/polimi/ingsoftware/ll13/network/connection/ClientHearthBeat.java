package it.polimi.ingsoftware.ll13.network.connection;

import it.polimi.ingsoftware.ll13.client.controller.ClientController;
import it.polimi.ingsoftware.ll13.client.exceptions.ServerCrashError;
import it.polimi.ingsoftware.ll13.network.requests.PingRequest;

import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This Class is responsible for managing the heartbeat mechanism
 * between the client and server. It periodically sends {@code PingRequest} messages to
 * verify the server is still responsive. If the server fails to respond after a threshold
 * number of attempts, it assumes a server crash and handles it accordingly.
 * This class is implemented as a singleton and uses a scheduled executor to run periodic tasks.
 */
public class ClientHearthBeat {
    private static ClientHearthBeat instance;
    private static ClientController controller;
    private static int pingSentCounter = 0;
    private static ScheduledExecutorService executorService;

    public static ClientHearthBeat getInstance(){
        if(instance == null){
            instance = new ClientHearthBeat();
        }
        return instance;
    }
    //----->GETTERS<-----//
    public static ClientController getController() {
        return controller;
    }

    public static int getPingSentCounter() {
        return pingSentCounter;
    }

    public static ScheduledExecutorService getExecutorService() {
        return executorService;
    }
    //----->SETTERS<-----//
    public static void setInstance(ClientHearthBeat instance) {
        ClientHearthBeat.instance = instance;
    }

    public static void setController(ClientController controller) {
        ClientHearthBeat.controller = controller;
    }

    public static void setPingSentCounter(int pingSentCounter) {
        ClientHearthBeat.pingSentCounter = pingSentCounter;
    }

    public static void setExecutorService(ScheduledExecutorService executorService) {
        ClientHearthBeat.executorService = executorService;
    }

    //----->HEARTBEAT<-----//

    /**
     * Starts the heartbeat mechanism. A ping is sent every second.
     * If more than 10 pings are sent without reset, a {@code ServerCrashError} is thrown,
     * and the client connection is closed.
     */
    public static void heartBeat(){
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            if(controller.getClient().isActive()) {
                try {
                    controller.send(new PingRequest(controller.getId()));
                    incrementCounter();
                    if (pingSentCounter > 10) {
                        throw new ServerCrashError();
                    }
                } catch (ServerCrashError e) {
                    if (controller.getClient().isActive()) {
                        controller.getExceptionHandler().handle(e);
                        controller.close();
                        controller.getClient().setActive(false);
                    }
                }
            }
        }, 0, 1,  TimeUnit.SECONDS);
    }

    /**
     * Increments the ping sent counter in a thread-safe manner.
     */
    public static synchronized void incrementCounter(){
        pingSentCounter++;
    }
    /**
     * Resets the ping sent counter to zero in a thread-safe manner.
     */
    public static synchronized void reset(){
        pingSentCounter = 0;
    }
    /**
     * Shuts down the heartbeat executor service.
     */
    public static void shutDown(){
        executorService.shutdown();
    }
}
