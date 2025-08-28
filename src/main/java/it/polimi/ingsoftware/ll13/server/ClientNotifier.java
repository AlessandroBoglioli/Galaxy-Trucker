package it.polimi.ingsoftware.ll13.server;

import it.polimi.ingsoftware.ll13.server.controller.MenuController;
import it.polimi.ingsoftware.ll13.network.response.Response;
import it.polimi.ingsoftware.ll13.utils.Observer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientNotifier implements Observer {
    private ObjectOutputStream outputStream;
    private final Socket clientSocket;
    private int id;
    public ClientNotifier(@NotNull Socket socket){
        this.clientSocket=socket;
        this.id =socket.hashCode();
    }
    public void OutputStream(){
        try {
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        }catch(IOException e){
            MenuController.getInstance();
            MenuController.disconnectClient(id);
        }
    }

    /**
     *
     * @param response sent to the client
     */
    @Override
    public void update(Response response) {
       send(response);
    }

    /**
     *
     * @param response sent to the client
     * @param id client identifier based on his socket hashcode
     */
    @Override
    public void updateConditional(Response response, int id) {
        if (this.id == id){
            send(response);
        }

    }


    public synchronized void send(Response response){
        try{
            outputStream.reset();
            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException e) {
            MenuController.getInstance();
            MenuController.disconnectClient(id);
        }
    }
    public int getId() {
        return id;
    }

}
