package it.polimi.ingsoftware.ll13.model.crew_members;

public class Alien extends CrewMember {

    private final AlienColor color;

    /**
     * Constructs an Alien instance with the specified color.
     *
     * @param color the color of the alien, represented as an AlienColor enumeration value
     */
    public Alien(AlienColor color) {
        this.color = color;
    }

    /**
     * Retrieves the color assigned to this alien.
     *
     * @return the color of the alien, which is an instance of the AlienColor enumeration.
     */
    public AlienColor getColor() {
        return color;
    }
}
