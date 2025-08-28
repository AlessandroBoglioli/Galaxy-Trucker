package it.polimi.ingsoftware.ll13.model.cards.dtos;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a General DTO (Data Transfer Object)
 */
public class GeneralDTO implements Serializable {
    private int playerId;
    private Set<Integer> affectedPlayerIds = new HashSet<>();


    public GeneralDTO() {
        this.playerId = -1;
    }

    public Set<Integer> getAffectedPlayerIds() {
        return affectedPlayerIds;
    }


    public GeneralDTO(int playerId) {
        this.playerId = playerId;
    }

    // --> GETTERS <--
    public int getPlayerId() {
        return playerId;
    }

    // --> SETTERS <--
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
