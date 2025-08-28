package it.polimi.ingsoftware.ll13.server.Comunication;

import it.polimi.ingsoftware.ll13.network.response.Response;
import it.polimi.ingsoftware.ll13.utils.Observer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MockObserver implements Observer {
    private final List<Response> receivedResponses = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void update(Response response) {
        receivedResponses.add(response);
    }

    @Override
    public void updateConditional(Response response, int id) {
        receivedResponses.add(response);
    }

    public List<Response> getReceivedResponses() {
        return receivedResponses;
    }

    public int countResponseOfType(@NotNull Class<? extends Response> c) {
        return (int) receivedResponses.stream().filter(c::isInstance).count();
    }


}
