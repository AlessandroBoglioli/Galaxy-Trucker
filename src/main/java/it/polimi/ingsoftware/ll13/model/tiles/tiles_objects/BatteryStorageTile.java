package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;

/**
 * This type of tile will represent the battery storages
 */
public class BatteryStorageTile extends Tile {

    private int batteryNumber;
    private final int batteryCapacity;

    /**
     * The builder generates the array for representing the connectors of the tile and sets the maximum capacity of the storage
     *
     * @param image             The image of the tile.
     * @param topType           Indicates the type of the connector positioned on the top side of the card
     * @param rightType         Indicates the type of the connector positioned on the right side of the card
     * @param bottomType        Indicates the type of the connector positioned on the bottom side of the card
     * @param leftType          Indicates the type of the connector positioned on the left side of the card
     * @param batteryCapacity   Indicates the maximum number of batteries the storage can hold
     */
    public BatteryStorageTile(String image, ConnectorType topType, ConnectorType rightType, ConnectorType bottomType, ConnectorType leftType, int batteryCapacity) throws IllegalArgumentException {
        super(image, topType, rightType, bottomType, leftType);

        if (batteryCapacity < 0) {
            throw new IllegalArgumentException("Battery capacity cannot be negative.");
        }

        this.batteryCapacity = batteryCapacity;
        this.batteryNumber = batteryCapacity;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Retrieves the number of battery number inside of the battery storage.
     *
     * @return number of battery inside battery storage.
     */
    public int getBatteryNumber() {
        return batteryNumber;
    }

    /**
     * Retrieves the number of battery capacity inside of the battery storage.
     *
     * @return number of battery capacity of the battery storage.
     */
    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Sets the number of batteries available in the storage
     */
    public void setBatteryNumber(int batteryNumber) {
        this.batteryNumber = batteryNumber;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Extra methods ~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Use a battery of the tile.
     */
    public void useBattery() {
        this.batteryNumber--;
    }

    /**
     * Use a number of battery from the tile.
     * @param batteryNumber number of batteries that needs to be used.
     * @return the number that was actually removed.
     */
    public int useBatteryNumber(int batteryNumber) {
        while (this.batteryNumber > 0) {
            this.batteryNumber--;
            batteryNumber--;
        }
        return batteryNumber;
    }
}
