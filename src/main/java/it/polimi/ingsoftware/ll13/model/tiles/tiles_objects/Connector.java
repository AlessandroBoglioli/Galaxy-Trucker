package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * This class represent the different types and direction of the connectors for the tiles.
 */
public class Connector implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    private final Direction direction;
    private final ConnectorType type;

    /**
     * This class is the creator of the single connector.
     *
     * @param direction     The direction of the connector.
     * @param type          The type of the connector.
     */
    public Connector(Direction direction,ConnectorType type) {
        this.direction = direction;
        this.type = type;
    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Retrieves the direction of the current connector.
     *
     * @return the direction of the connector.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Retrieves the type of the current connector.
     *
     * @return the type of the connector.
     */
    public ConnectorType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connector that = (Connector) o;
        return direction == that.direction && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(direction, type);
    }

}
