package com.hanzereversiai.projectp3.networking.entity;

import com.hanzereversiai.projectp3.networking.InputListener;
import com.hanzereversiai.projectp3.networking.NetworkSingleton;
import com.thowv.javafxgridgameboard.AbstractTurnEntity;
import com.thowv.javafxgridgameboard.GameBoard;
import com.thowv.javafxgridgameboard.premades.tictactoe.TTToeGameInstance;

public class NetworkedTicTacToeGameInstance extends TTToeGameInstance implements InputListener {
    public NetworkedTicTacToeGameInstance(GameBoard gameBoard, AbstractTurnEntity entityOne, AbstractTurnEntity entityTwo) {
        super(gameBoard, entityOne, entityTwo);
        NetworkSingleton.getNetworkInstance().getDelegateInputListener().SUBSCRIBE_MOVE(this);
    }

    @Override
    public void handleInput(String input) {
        if (getEntityOne() instanceof NetworkTurnEntity) {
           NetworkTurnEntity networkTurnEntity =(NetworkTurnEntity) getEntityOne();
           networkTurnEntity.handleInput(input);
           networkTurnEntity.takeTurn(this);
        }
        else if (getEntityTwo() instanceof NetworkTurnEntity) {
            NetworkTurnEntity networkTurnEntity =(NetworkTurnEntity) getEntityOne();
            networkTurnEntity.handleInput(input);
            networkTurnEntity.takeTurn(this);
        }
    }
}
