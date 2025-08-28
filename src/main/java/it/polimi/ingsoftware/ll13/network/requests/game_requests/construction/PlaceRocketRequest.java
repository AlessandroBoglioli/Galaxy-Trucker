package it.polimi.ingsoftware.ll13.network.requests.game_requests.construction;

import it.polimi.ingsoftware.ll13.model.general_enumerations.RankingPosition;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

public class PlaceRocketRequest extends Request {
    private final RankingPosition position;
    public PlaceRocketRequest(int senderId, RankingPosition startingPosition) {
        super(senderId);
        this.position = startingPosition;
    }

    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            GameController.getInstance().handlePlaceRocketOnMapRequest(this);
        }
    }

    public RankingPosition getPosition() {
        return position;
    }
}
