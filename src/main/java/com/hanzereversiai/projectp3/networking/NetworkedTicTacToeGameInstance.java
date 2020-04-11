package com.hanzereversiai.projectp3.networking;

import com.thowv.javafxgridgameboard.AbstractTurnEntity;
import com.thowv.javafxgridgameboard.GameBoard;
import com.thowv.javafxgridgameboard.premades.tictactoe.TTToeGameInstance;

public class NetworkedTicTacToeGameInstance extends TTToeGameInstance implements NetworkedGameInstance {
    public NetworkedTicTacToeGameInstance(GameBoard gameBoard, AbstractTurnEntity entityOne, AbstractTurnEntity entityTwo) {
        super(gameBoard, entityOne, entityTwo);
        NetworkSingleton.getNetworkInstance().getNetworkHandler().setNetworkedGameInstance(this);
    }

    @Override
    public void doTurn(int x, int y) {
        super.doTurn(x, y);

        int width = getGameBoard().getSize();
        int move = (width * y) + x;
        NetworkSingleton.getNetworkInstance().sendCommand(Command.MOVE, String.valueOf(move));
    }

    @Override
    public void doTurnFromNetwork(int x, int y) {
        super.doTurn(x, y);
    }

    @Override
    public void passTurn() {
        super.switchTurn();
    }
}
