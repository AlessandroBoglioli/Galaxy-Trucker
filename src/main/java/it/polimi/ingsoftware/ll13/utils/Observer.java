package it.polimi.ingsoftware.ll13.utils;

import it.polimi.ingsoftware.ll13.network.response.Response;

public interface Observer {
    public void update(Response response);
    void updateConditional(Response response, int id);
}
