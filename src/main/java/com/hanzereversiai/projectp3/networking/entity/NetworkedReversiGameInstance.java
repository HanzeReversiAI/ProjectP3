package com.hanzereversiai.projectp3.networking.entity;

import com.hanzereversiai.projectp3.networking.Command;
import com.hanzereversiai.projectp3.networking.InputListener;
import com.hanzereversiai.projectp3.networking.NetworkSingleton;
import com.thowv.javafxgridgameboard.AbstractTurnEntity;
import com.thowv.javafxgridgameboard.GameBoard;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiGameInstance;

public class NetworkedReversiGameInstance extends ReversiGameInstance implements InputListener {
    public NetworkedReversiGameInstance(GameBoard gameBoard, AbstractTurnEntity entityOne, AbstractTurnEntity entityTwo) {
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

    @Override
    public void doTurn(int x, int y) {
        super.doTurn(x, y);

        int width = getGameBoard().getSize();
        int move = (width * y) + x;
        NetworkSingleton.getNetworkInstance().SendCommand(Command.MOVE, String.valueOf(move));
    }
}
