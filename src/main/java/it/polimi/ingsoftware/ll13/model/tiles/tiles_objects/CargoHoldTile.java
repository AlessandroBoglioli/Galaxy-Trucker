package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.CompartmentType;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;

import java.util.ArrayList;
import java.util.List;

/**
 * The class represent the cargo holds that will be inserted in the ship
 */
public class CargoHoldTile extends Tile {

    private final CompartmentType compartmentType;
    private final int compartmentCapacity;
    private List<CargoColor> cargos;

    /**
     * The builder generates the array for representing the connectors of the tile and sets the maximum capacity of the cargo hold.
     *
     * @param image                 The image of the tile.
     * @param topType               Indicates the type of the connector positioned on the top side of the card.
     * @param rightType             Indicates the type of the connector positioned on the right side of the card.
     * @param bottomType            Indicates the type of the connector positioned on the bottom side of the card.
     * @param leftType              Indicates the type of the connector positioned on the left side of the card.
     * @param compartmentCapacity          Indicates the number of cargo holders.
     */
    public CargoHoldTile(String image, ConnectorType topType, ConnectorType rightType, ConnectorType bottomType, ConnectorType leftType, CompartmentType compartmentType, int compartmentCapacity) {
        super(image, topType, rightType, bottomType, leftType);

        if (compartmentType == null) {
            throw new IllegalArgumentException("compartmentType cannot be null");
        }
        if (compartmentCapacity <= 0) {
            throw new IllegalArgumentException("compartmentCapacity must be a positive integer");
        }

        this.compartmentType = compartmentType;
        this.compartmentCapacity = compartmentCapacity;
        cargos = new ArrayList<CargoColor>();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Retrieves the compartment type.
     *
     * @return the compartment type.
     */
    public CompartmentType getCompartmentType() {
        return compartmentType;
    }

    /**
     * Retrieves the compartment capacity.
     *
     * @return the compartment capacity.
     */
    public int getCompartmentCapacity() {
        return compartmentCapacity;
    }

    /**
     * Retrieves cargos contained into the cargo holder.
     *
     * @return the cargos contained into the cargo holder.
     */
    public List<CargoColor> getCargos() {
        return cargos;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ EXTRA FUNCTIONS ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Add a cargo into the cargo holder if there's space.
     */
    public void addCargo(CargoColor color) {
        if(cargos.size() < compartmentCapacity) {
            cargos.add(color);
        }
    }

    public void removeCargo(CargoColor color) {
        cargos.remove(color);
    }
}
