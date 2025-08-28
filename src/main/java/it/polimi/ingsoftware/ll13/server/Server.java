package it.polimi.ingsoftware.ll13.server;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * This class is the Main server for the Galaxy Trucker game
 */
public class Server implements Runnable{
    private final ServerSocket serverSocket;
    private final int port; //the port will be given as an command line input in the ServerLauncher main class
    private final  ExecutorService threadPool;
    private boolean running=true;

    public Server(ServerSocket serverSocket, int port) {
        this.serverSocket=serverSocket;
        this.port=port;
        this.threadPool= Executors.newCachedThreadPool();
    }


    @Override
    public void run() {
        while(running){
            try{
                Socket clientSocket=serverSocket.accept();
                logInfo("New client Connected to the server, with adress: "+clientSocket.getInetAddress());
                threadPool.submit(new ClientHandler(clientSocket));
            }catch(Exception e){
                this.running=false;//it stops the running of this server TODO: implementing an ERROR banner on screen after setup
                logError("UNEXPECTED ERROR HAS OCCURRED, SERVER IS OFFLINE");
            }
        }

    }

    public int getPort() {
        return port;
    }
    public static void logInfo(String message){
        System.out.println("[INFO]: "+message);
    }
    public static void logError(String message){
        System.out.println("[ERROR]: "+message);
    }
}

