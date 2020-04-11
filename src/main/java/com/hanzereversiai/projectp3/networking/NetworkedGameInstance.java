package com.hanzereversiai.projectp3.networking;

import com.thowv.javafxgridgameboard.AbstractTurnEntity;
import com.thowv.javafxgridgameboard.GameBoard;

/**
 * Interface used to support doing moves from the Network and to expose game logic functions to the network.
 */
public interface NetworkedGameInstance {
    void doTurnFromNetwork(int x, int y);
    GameBoard getGameBoard();
    AbstractTurnEntity getCurrentTurnEntity();
    AbstractTurnEntity[] getTurnEntities();
    void passTurn();
    void end(AbstractTurnEntity winningTurnEntity, AbstractTurnEntity losingTurnEntity);
    void end(AbstractTurnEntity[] tieTurnEntities);
}
