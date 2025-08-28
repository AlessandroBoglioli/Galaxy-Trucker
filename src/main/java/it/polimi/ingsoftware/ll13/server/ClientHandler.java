package it.polimi.ingsoftware.ll13.server;

import it.polimi.ingsoftware.ll13.network.requests.PingRequest;
import it.polimi.ingsoftware.ll13.network.requests.PongRequest;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;
import it.polimi.ingsoftware.ll13.server.controller.MenuController;
import it.polimi.ingsoftware.ll13.network.connection.ServerHearthBeat;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.network.response.SenderIdResponse;
import it.polimi.ingsoftware.ll13.network.response.Response;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * This class represents the way in which the server communicates with a single client.
 * It manages reading requests, sending responses, and handling the heartbeat mechanism.
 */
public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private ObjectInputStream inputStream;
    private final ClientNotifier clientNotifier;
    private  Controller controller;
    private final int id;
    private boolean running=true;

    public int getId(){
        return this.id;
    }
    public ClientHandler(@NotNull Socket socket){
        this.clientSocket=socket;
        this.id =socket.hashCode();
        this.controller=MenuController.getInstance(); //initialize in the prep phase
        this.clientNotifier=new ClientNotifier(clientSocket);
        if(controller instanceof MenuController){
            MenuController menuController = MenuController.getInstance();
            menuController.addClientNotifier(clientNotifier);
            menuController.addClientHandler(this);
        }

    }
    /**
     * Allows updating the active controller (e.g., from LobbyController to MatchController).
     *
     * @param newController the new active controller.
     */
    public void setController(Controller newController){
       this.controller=newController;
    }
    public Controller getController() {
        return controller;
    }

    @Override
    public void run(){
      clientNotifier.OutputStream();
      try{
          inputStream=new ObjectInputStream(clientSocket.getInputStream());
      } catch (IOException e) {
          throw new RuntimeException(e);
      }

      sendResponse(new SenderIdResponse(clientSocket.hashCode()));// sending immediately an Identification to the new user
      hearthBeatSetup();
      Object obj;
      while(running){
          try{
              obj=inputStream.readObject();
              if(obj instanceof Request request){
                  if(request instanceof PingRequest || request instanceof PongRequest){
                      request.execute(MenuController.getInstance());
                  }
                  if(controller instanceof MenuController){
                      request.execute(MenuController.getInstance());
                  }else{
                      GameController.getInstance().submitRequest(request);
                  }
              }else{
                  throw new IllegalArgumentException("Invalid object sent");
              }
          }catch (IOException | ClassNotFoundException e) {
              running=false;
              cleanUp();
          } catch (InterruptedException e) {
              running = false; // when i put into a blocking queue it needs to catch this exception
              cleanUp();
          }
      }
    }

    /**
     * Sends a response to the client.
     *
     * @param response the response to send.
     */
    public void sendResponse(Response response) {
        clientNotifier.send(response);
    }

    /**
     * Sets up the heartbeat mechanism by initializing and starting the heartbeat handler.
     */
    private void hearthBeatSetup(){
        ServerHearthBeat hearthBeat = new ServerHearthBeat(clientSocket.hashCode());
        hearthBeat.hearthBeat();
        MenuController.getInstance().addHeartBeat(hearthBeat);
    }

    /**
     * Closes the input stream and socket,ensuring proper resource cleanup.
     */
    private void cleanUp(){
        try{
            if(inputStream!=null){
                inputStream.close();
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally {
            MenuController.disconnectClient(id);
        }
    }
    /**
     * @return Client socket that supports its connection
     */
    public Socket getClientSocket() {
        return clientSocket;
    }

}
