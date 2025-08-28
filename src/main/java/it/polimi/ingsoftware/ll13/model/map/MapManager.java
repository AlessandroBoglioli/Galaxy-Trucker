package it.polimi.ingsoftware.ll13.model.map;

public class MapManager {

    private Map map = null;
    boolean firstFlight;

    public MapManager() { } //Default constructor

    public Map getMap(boolean firstFlight) {
        if (map == null) {
            this.firstFlight = firstFlight;
            map = new Map(firstFlight);
        }
        if (firstFlight != this.firstFlight) {
            return null;
        }
        return map;
    }

}
