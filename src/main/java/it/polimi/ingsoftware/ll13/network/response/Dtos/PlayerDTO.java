package it.polimi.ingsoftware.ll13.network.response.Dtos;

import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;

import java.io.Serializable;

public class PlayerDTO implements Serializable {
    private final int id;
    private final String username;
    private final PlayerColors color;
    private final int position;

    public PlayerDTO(int id, String username, PlayerColors color, int position) {
        this.id = id;
        this.username = username;
        this.color = color;
        this.position = position;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public PlayerColors getColor() { return color; }
    public int getPosition() { return position; }
}
