package it.polimi.ingsoftware.ll13;

import it.polimi.ingsoftware.ll13.server.Server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerLauncher {
    /**
     *
     * @param args args[0] will be the port on which the server will listen
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }
        int port = Integer.parseInt(args[0]);
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Server server = new Server(serverSocket, port);
            new Thread(server).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
