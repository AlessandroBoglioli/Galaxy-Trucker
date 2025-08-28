package it.polimi.ingsoftware.ll13.network.response.match_responses;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.network.response.Dtos.PlayerDTO;
import it.polimi.ingsoftware.ll13.network.response.Response;

import java.util.List;

public class UpdateMap extends Response {
    private final List<PlayerDTO> positionDTOList;
    private final boolean success;

    public UpdateMap(List<PlayerDTO> positionDTOList, boolean success) {
        this.positionDTOList = positionDTOList;
        this.success = success;
    }

    @Override
    public void execute(Handler handler) {
        handler.handleUpdateMapResponse(this);
    }

    public List<PlayerDTO> getPositionDTOList() {
        return positionDTOList;
    }

    public boolean isSuccess() {
        return success;
    }
}
