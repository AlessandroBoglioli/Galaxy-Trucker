package it.polimi.ingsoftware.ll13.utils;

import it.polimi.ingsoftware.ll13.network.response.Response;

import java.util.ArrayList;
import java.util.List;
/**
 * this is the concrete observer (as the java manual suggests)
 */
public abstract class Observable {
    private final List<Observer> observers = new ArrayList<>();
    public void addObserver(Observer observer){
        observers.add(observer);
    }
    public void removeObserver(Observer observer){
        observers.remove(observer);
    }
    public void notifyAll(Response response){
        for(Observer observer : observers){
            observer.update(response);
        }
    }
    public void notify(Response response, int id) {
        for(Observer observer : observers){
            observer.updateConditional(response,id);
        }
    }

}
