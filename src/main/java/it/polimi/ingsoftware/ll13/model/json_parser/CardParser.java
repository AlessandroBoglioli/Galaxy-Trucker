package it.polimi.ingsoftware.ll13.model.json_parser;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.*;
import org.json.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A parser for converting JSON data into Card objects.
 * This class provides functionality to parse a JSON input stream containing card data and convert it into a list of corresponding Card objects using a factory method pattern.
 */
public class CardParser {

    private final Map<String, CardCreator> creators;

    /**
     * Constructs a new CardParser and initializes the card creators.
     * The constructor populates the creators map with all known card types and their corresponding creator implementations.
     */
    public CardParser() {
        this.creators = new HashMap<>();
        initializeCreators();
    }

    private void initializeCreators() {
        creators.put("AbandonedShip", new AbandonedShipCreator());
        creators.put("AbandonedStation", new AbandonedStationCreator());
        creators.put("Epidemic", new EpidemicCreator());
        creators.put("MeteorShower", new MeteorShowerCreator());
        creators.put("OpenSpace", new OpenSpaceCreator());
        creators.put("Pirate", new PirateCreator());
        creators.put("Planets", new PlanetsCreator());
        creators.put("Slavers", new SlaversCreator());
        creators.put("Smugglers", new SmugglersCreator());
        creators.put("StellarSpacedust", new StellarSpaceDustCreator());
        creators.put("WarZone", new WarZoneCreator());
    }

    /**
     * Parses a JSON input stream and converts it into a list of Card objects.
     * The method expects the input stream to contain a JSON object with a "cards" array, where each element represents a card with a type and data.
     *
     * @param inputStream The input stream containing the JSON data to parse
     * @return A list of Card objects created from the JSON data
     * @throws IllegalArgumentException If an unknown card type is encountered in the JSON data
     * @throws JSONException If there is an error parsing the JSON data
     */
    public List<Card> parseCards(InputStream inputStream) throws IllegalArgumentException, JSONException {
        List<Card> cards = new ArrayList<>();

        JSONTokener token = new JSONTokener(inputStream);
        JSONObject root = new JSONObject(token);
        JSONArray cardsArray = root.getJSONArray("cards");

        for (int i = 0; i < cardsArray.length(); i++) {
            JSONObject cardObj = cardsArray.getJSONObject(i);
            String type = cardObj.getString("type");
            JSONObject data = cardObj.getJSONObject("data");

            CardCreator creator = creators.get(type);
            if (creator == null) {
                throw new IllegalArgumentException("Unknown card type: " + type);
            }
            cards.add(creator.createCard(data));
        }

        return cards;
    }
}