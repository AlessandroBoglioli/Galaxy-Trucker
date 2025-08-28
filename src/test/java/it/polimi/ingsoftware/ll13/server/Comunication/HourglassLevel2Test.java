package it.polimi.ingsoftware.ll13.server.Comunication;

import it.polimi.ingsoftware.ll13.model.hourglass.HourglassLevel2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HourglassLevel2Test {
    @Test
    public void testFlipOnlyOnce() {
        HourglassLevel2 hourglass = new HourglassLevel2(2);
        assertFalse(hourglass.isFlipped());
        assertEquals(2000, hourglass.getRemainingTime());
        boolean firstFlip = hourglass.flip();
        assertFalse(firstFlip);
        assertFalse(hourglass.isFlipped());
        assertEquals(2000, hourglass.getRemainingTime());
    }
    @Test
    void testFlipOnlyAfterFinish() throws InterruptedException {
        HourglassLevel2 hourglass = new HourglassLevel2(1);
        boolean started = hourglass.startTime(() -> {
        });

        assertTrue(started, "Hourglass should start successfully");
        boolean flippedEarly = hourglass.flip();
        assertFalse(flippedEarly, "Hourglass should not be flippable before finishing");
        Thread.sleep(1200);
        boolean flippedAfterFinish = hourglass.flip();
        assertTrue(flippedAfterFinish, "Hourglass should be flippable after finishing");
        boolean secondFlip = hourglass.flip();
        assertFalse(secondFlip, "Hourglass should only flip once");
    }

}
