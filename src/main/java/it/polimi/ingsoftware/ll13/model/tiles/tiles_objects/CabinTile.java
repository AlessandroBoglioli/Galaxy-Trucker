package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.crew_members.Alien;
import it.polimi.ingsoftware.ll13.model.crew_members.CrewMember;
import it.polimi.ingsoftware.ll13.model.crew_members.Human;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;

/**
 * The class represent the cabin tile that will be inserted in the ship.
 */
public class CabinTile extends Tile {

    private final CrewMember[] crewMembers = new CrewMember[2];

    /**
     * The builder generates the array for representing the connectors of the tile.
     *
     * @param image         The image of the tile.
     * @param topType       Indicates the type of the connector positioned on the top side of the card.
     * @param rightType     Indicates the type of the connector positioned on the right side of the card.
     * @param bottomType    Indicates the type of the connector positioned on the bottom side of the card.
     * @param leftType      Indicates the type of the connector positioned on the left side of the card.
     */
    public CabinTile(String image, ConnectorType topType, ConnectorType rightType, ConnectorType bottomType, ConnectorType leftType) {
        super(image, topType, rightType, bottomType, leftType);
    }

    /**
     * Getter method for the crewMembers array
     * @return CrewMember array
     */
    public CrewMember[] getCrewMembers() {
        return crewMembers;
    }

    /**
     * This method adds a crew member to the cabin, ensuring that:
     * - If the cabin is empty, two humans will be added.
     * - If an alien is provided, only one alien can be added.
     * - The cabin cannot accommodate mixed types (i.e., humans and aliens cannot coexist in the same cabin).
     *
     * @param member The crew member to be added. This can be either a Human or an Alien.
     * @return {@code true} if the crew member was successfully added to the cabin;
     *         {@code false} if the cabin is already full or if the attempt to mix crew types fails.
     */
    public boolean addCrewMember(CrewMember member){
        if (crewMembers[0] == null && crewMembers[1] == null) {
            if (member instanceof Human) {
                crewMembers[0] = member;
                crewMembers[1] = member;
                return true;
            } else if (member instanceof Alien) {
                crewMembers[0] = member;
                crewMembers[1] = null;
                return true;
            }
        }
        return false;
    }

    /**
     * This method retrieves the number of crew members inside a cabin tile
     * @return the number of elements in the crewMembers array
     */
    public int getCrewCount() {
        int count = 0;
        if (crewMembers[0] != null) count++;
        if (crewMembers[1] != null) count++;
        return count;
    }

    /**
     * This method check if there is an alien inside the cabin
     * @return the alien, if present, null otherwise
     */
    public Alien getAlien() {
        if (hasAlien()) {
            return (Alien) crewMembers[0];
        }
        return null;
    }

    /**
     * Checks if in a cabin there is an alien
     * @return true if there's an alien false otherwise
     */
    public boolean hasAlien(){
        return crewMembers[0] instanceof Alien;
    }

    /**
     * This method eliminates the given number of crew members from the Cabin
     * @param count the given number of members to remove
     */
    public void eliminateCrewMembers(int count){
        if(crewMembers == null || crewMembers.length == 0 || count<=0){
            return;
        }
        int removedCount = 0;
        for (int i = 0; i < crewMembers.length && removedCount < count; i++) {
            if (crewMembers[i] != null) {
                crewMembers[i] = null;
                removedCount++;
            }
        }
        if (removedCount == crewMembers.length) {
            crewMembers[0] = null;
            crewMembers[1] = null;
        }
    }

    /**
     * Fill a tile only with astronauts.
     */
    public void fillWithAstronauts() {
        crewMembers[0] = new Human();
        crewMembers[1] = new Human();
    }
}
