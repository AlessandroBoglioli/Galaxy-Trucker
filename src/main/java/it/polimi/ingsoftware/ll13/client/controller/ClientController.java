package it.polimi.ingsoftware.ll13.client.controller;

import it.polimi.ingsoftware.ll13.client.Client;
import it.polimi.ingsoftware.ll13.client.exceptions.ExceptionHandler;
import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.network.response.Response;
import it.polimi.ingsoftware.ll13.network.response.SenderIdResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public abstract class ClientController {
    protected Socket socket;
    protected Client client;
    protected int id;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    protected Thread responseReader;
    private Handler responseHandler;
    private ExceptionHandler exceptionHandler;
    private boolean isHost = false;


    //----->GETTERS<-----//
    public Socket getSocket() {
        return socket;
    }
    public Client getClient() {
        return client;
    }
    public int getId() {
        return id;
    }
    public ObjectOutputStream getOut() {
        return out;
    }
    public ObjectInputStream getIn() {
        return in;
    }

    public Handler getResponseHandler() {
        return responseHandler;
    }
    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public boolean isHost() {
        return isHost;
    }
    //----->SETTERS<-----//

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }
    public void setIn(ObjectInputStream in) {
        this.in = in;
    }
     public void setResponseHandler(Handler handler){
        this.responseHandler = handler;
     }
     public void setExceptionHandler(ExceptionHandler handler){
        this.exceptionHandler = handler;
     }

    public void setBeingHost() {
        isHost = true;
    }

    //----->SETUP COMMUNICATION<-----//
    public void setUp(){
        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
        } catch (IOException e) {
            close();
            exceptionHandler.handle(e);
        }
        try{
            in = new ObjectInputStream(socket.getInputStream());
        }catch (IOException e){
            close();
            exceptionHandler.handle(e);
        }
    }

    public Thread responseReaderThread() {
        this.responseReader = new Thread(() -> {
            try {
                while (client.isActive()) {
                    Response response = (Response) in.readObject();
                    response.execute(responseHandler);
                }
            }  catch (SocketException | ClassNotFoundException e) {
                if(client.isActive()){
                    exceptionHandler.handle(e);
                    client.setActive(false);
                    close();
                }
            } catch (IOException e) {
                if(client.isActive()){
                    exceptionHandler.handle(e);
                    client.setActive(false);
                    close();
                }
            }
        });
        return this.responseReader;
    }
    public synchronized void send(Request request){
        try{
            out.reset();
            out.writeObject(request);
            out.flush();
        } catch (IOException e) {
            close();
            exceptionHandler.handle(e);
        }
    }

    public void setId(){
        try{
            SenderIdResponse senderIdResponse = (SenderIdResponse) in.readObject();
            id = senderIdResponse.getSenderId();
        }catch (IOException | ClassNotFoundException e){
            close();
            exceptionHandler.handle(e);
        }
    }


    //----->ABSTRACT METHODS<-----//
    public abstract void close();
    public abstract void connectToServer();
    public abstract void join();
    public abstract void waitingRoom();
    public abstract void waitForHostOk();
    public abstract void buildingShip();
    public abstract void validationPhase();
    public abstract void setEquipe();
    public abstract void adventureShip();

}

