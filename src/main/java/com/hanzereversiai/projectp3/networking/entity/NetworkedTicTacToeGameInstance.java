package com.hanzereversiai.projectp3.networking.entity;

import com.hanzereversiai.projectp3.networking.Command;
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
        if (getCurrentTurnEntity() instanceof  NetworkTurnEntity) {
            NetworkTurnEntity networkTurnEntity =(NetworkTurnEntity) getCurrentTurnEntity();
            networkTurnEntity.handleInput(input, this);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void doTurn(int x, int y) {
        super.doTurn(x, y);

        int width = getGameBoard().getSize();
        int move = (width * y) + x;
        NetworkSingleton.getNetworkInstance().sendCommand(Command.MOVE, String.valueOf(move));
    }
}
