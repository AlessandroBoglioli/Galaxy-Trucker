package it.polimi.ingsoftware.ll13.network.requests.game_requests.validation;

import it.polimi.ingsoftware.ll13.model.crew_members.CrewMember;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

public class PlaceCrewRequest extends Request {
    private final int row;
    private final int col;
    private final CrewMember crewMember;

    public PlaceCrewRequest(int senderId, int row, int col, CrewMember crewMember) {
        super(senderId);
        this.row = row;
        this.col = col;
        this.crewMember = crewMember;
    }

    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            GameController.getInstance().handlePlaceCrewRequest(this);
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public CrewMember getCrewMember() {
        return crewMember;
    }
}
