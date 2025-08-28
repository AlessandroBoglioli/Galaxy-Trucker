package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.cards.enumerations.WarZonePenaltyType;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.WarZonePenaltyEffect;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a penalty applied in a war zone.
 * This class encapsulates the target, effect, penalty value or directions of shots.
 * It provides constructors for both numeric and non-numeric penalties, including those with directional effects.
 */
public class WarZonePenalty implements Serializable {

    private final WarZonePenaltyType warZonePenaltyType;
    private final WarZonePenaltyEffect warZonePenaltyEffect;
    private final int value;
    private final List<FireShot> fireShots;

    /**
     * Constructs a new WarZonePenalty with the specified target, effect, and numeric value.
     *
     * @param warZonePenaltyType The target of the penalty.
     * @param warZonePenaltyEffect The effect of the penalty.
     * @param value  The numeric value of the penalty.
     */
    public WarZonePenalty(WarZonePenaltyType warZonePenaltyType, WarZonePenaltyEffect warZonePenaltyEffect, int value) {
        this.warZonePenaltyType = warZonePenaltyType;
        this.warZonePenaltyEffect = warZonePenaltyEffect;
        this.value = value;
        this.fireShots = null;
    }

    /**
     * Constructs a new WarZonePenalty with the specified target, effect, and directions (non-numeric).
     *
     * @param warZonePenaltyType     The target of the penalty.
     * @param warZonePenaltyEffect     The effect of the penalty.
     * @param fireShots  The list of fire shots associated with the penalty.
     */
    public WarZonePenalty(WarZonePenaltyType warZonePenaltyType, WarZonePenaltyEffect warZonePenaltyEffect, List<FireShot> fireShots) {
        this.warZonePenaltyType = warZonePenaltyType;
        this.warZonePenaltyEffect = warZonePenaltyEffect;
        this.value = 0;
        this.fireShots = fireShots;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Retrieves the target of the penalty.
     *
     * @return The target of the penalty.
     */
    public WarZonePenaltyType getWarZonePenaltyType() {
        return warZonePenaltyType;
    }

    /**
     * Retrieves the effect of the penalty.
     *
     * @return The effect of the penalty.
     */
    public WarZonePenaltyEffect getWarZonePenaltyEffect() {
        return warZonePenaltyEffect;
    }

    /**
     * Retrieves the value of the penalty.
     *
     * @return The value of the penalty.
     */
    public int getValue() {
        return value;
    }

    /**
     * Retrieves the list of directions of fire shots.
     *
     * @return The directions of fire shots.
     */
    public List<FireShot> getFireShots() {
        return fireShots;
    }
}