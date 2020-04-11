package com.hanzereversiai.projectp3.networking;

import com.thowv.javafxgridgameboard.AbstractGameInstance;
import com.thowv.javafxgridgameboard.AbstractTurnEntity;

/**
 * A TurnEntity to be controlled from the Network
 */
public class NetworkTurnEntity extends AbstractTurnEntity {
    /**
     * Default Constructor
     * @param name The name of the player
     */
    public NetworkTurnEntity(String name) {
        super(EntityType.AI, name);
    }

    @Override
    public void takeTurn(AbstractGameInstance abstractGameInstance) { }
}
