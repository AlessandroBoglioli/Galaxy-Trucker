package it.polimi.ingsoftware.ll13.model.hourglass;
import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;

/**
 * This manager is used to implement the singleton design pattern and a pseudo farm
 */

public class HourglassManager {

    private static Hourglass hourglass;

    /**
     * The builder of this class creates the hourglass that we want
     * @param maxTime indicates the timer value
     * @param gameLevel indicates the type of hourglass that we will use
     */

    public HourglassManager(int maxTime, GameLevel gameLevel) {
        if (hourglass == null) {
            if (gameLevel == GameLevel.TRY_LEVEL) hourglass = new Hourglass(maxTime);
            else hourglass = new HourglassLevel2(maxTime);
        }
    }

    public Hourglass getHourglass() {
        return hourglass;
    }
}
