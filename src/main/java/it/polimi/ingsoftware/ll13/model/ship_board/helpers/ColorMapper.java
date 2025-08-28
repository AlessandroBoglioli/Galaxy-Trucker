package it.polimi.ingsoftware.ll13.model.ship_board.helpers;

import it.polimi.ingsoftware.ll13.model.crew_members.AlienColor;

import it.polimi.ingsoftware.ll13.model.tiles.enumerations.VitalSupportColor;

/**
 * class used to map an alien color to a vital support to a cabin tile, used to determine if a specific type of alien can populate a certain type
 * of cabin
 */
public class ColorMapper {
    public static VitalSupportColor fromAlienColor(AlienColor color) {
        return switch (color) {
            case YELLOW -> VitalSupportColor.YELLOW;
            case PURPLE-> VitalSupportColor.PURPLE;
        };
    }
}
