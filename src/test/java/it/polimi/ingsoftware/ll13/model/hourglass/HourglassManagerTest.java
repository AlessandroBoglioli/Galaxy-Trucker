package it.polimi.ingsoftware.ll13.model.hourglass;

import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HourglassManagerTest {

    private HourglassManager hourglassManager;

    @Test
    public void return_hourglassTryLevel(){
        hourglassManager = new HourglassManager(60, GameLevel.TRY_LEVEL);
        assertNotNull(hourglassManager.getHourglass());
    }

    @Test
    public void return_hourglassLevel2(){
        hourglassManager = new HourglassManager(60, GameLevel.TRY_LEVEL);
        assertNotNull(hourglassManager.getHourglass());
    }
}
