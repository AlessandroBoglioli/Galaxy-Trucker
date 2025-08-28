package it.polimi.ingsoftware.ll13.network.connection;
import it.polimi.ingsoftware.ll13.server.Server;
import it.polimi.ingsoftware.ll13.server.controller.MenuController;
import it.polimi.ingsoftware.ll13.network.response.PingResponse;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/**
 * This class manages the heartbeat process on the server side for a client.
 * It periodically sends Ping responses to the client and expects Pong responses back.
 * If the server does not receive a Pong response after several attempts, the client is considered disconnected.
 * The heartbeat mechanism helps maintain the health of the connection by verifying that the client is still active.
 * It also resets the heartbeat counter when the client responds, preventing disconnection if the client is still connected.
 */
public class ServerHearthBeat {
    private int pingCounter;
    private final int id;
    private ScheduledExecutorService executorService;
    private static final int MAX_MISSED_PINGS =10;

    public ServerHearthBeat(int id) {
        this.id = id;
        this.pingCounter = 0;
    }
    /**
     * Increments the heartbeat counter, called every time the server sends a ping.
     * This counter tracks the number of missed ping responses.
     */
    public synchronized void incrementCounter(){
        this.pingCounter++ ;
    }
    /**
     * Resets the heartbeat counter when the server receives a Pong response from the client.
     */
    public synchronized void resetCounter(){
        this.pingCounter = 0;
    }
    /**
     * Starts the heartbeat process by scheduling periodic ping responses to the client.
     * Pings are sent every second, and if no Pong response is received after several attempts, the client is disconnected.
     */
    public void hearthBeat(){
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            MenuController.send(id, new PingResponse());  // Send Ping to client
            incrementCounter();  // Increment the counter after sending Ping
            // Check if the client hasn't responded for 10 consecutive Pings
            if (pingCounter > MAX_MISSED_PINGS) {
                MenuController.disconnectClient(id);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
    /**
     * Returns the unique identifier (hashcode) for the client.
     *
     * @return the unique identifier of the client
     */
    public int getId() {
        return id;
    }

    /**
     * Stops the heartbeat process by shutting down the scheduled executor.
     * This method is called when the client is disconnected or the game ends.
     */
    public synchronized void shutdown(){
        executorService.shutdown();
    }
}
