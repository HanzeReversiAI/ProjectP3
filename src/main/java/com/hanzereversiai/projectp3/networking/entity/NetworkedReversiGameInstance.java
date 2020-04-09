package com.hanzereversiai.projectp3.networking.entity;

import com.hanzereversiai.projectp3.networking.Command;
import com.hanzereversiai.projectp3.networking.InputListener;
import com.hanzereversiai.projectp3.networking.NetworkSingleton;
import com.hanzereversiai.projectp3.networking.NetworkedGameInstance;
import com.thowv.javafxgridgameboard.AbstractTurnEntity;
import com.thowv.javafxgridgameboard.GameBoard;
import com.thowv.javafxgridgameboard.GameBoardTileType;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiGameInstance;

public class NetworkedReversiGameInstance extends ReversiGameInstance implements InputListener, NetworkedGameInstance {
    public NetworkedReversiGameInstance(GameBoard gameBoard, AbstractTurnEntity entityOne, AbstractTurnEntity entityTwo) {
        super(gameBoard, entityOne, entityTwo);
        NetworkSingleton.getNetworkInstance().getDelegateInputListener().SUBSCRIBE_MOVE(this);
        NetworkSingleton.getNetworkInstance().getDelegateInputListener().SUBSCRIBE_WIN(this::endGameWin);
        NetworkSingleton.getNetworkInstance().getDelegateInputListener().SUBSCRIBE_LOSS(this::endGameLoss);
    }

    @Override
    public void handleInput(String input) {
        if (getCurrentTurnEntity() instanceof  NetworkTurnEntity) {
           NetworkTurnEntity networkTurnEntity = (NetworkTurnEntity) getCurrentTurnEntity();
           networkTurnEntity.handleInput(input, this);
        }
    }

    @Override
    public void doTurn(int x, int y) {
        doTurnFromNetwork(x, y);

        int width = getGameBoard().getSize();
        int move = (width * y) + x;
        NetworkSingleton.getNetworkInstance().sendCommand(Command.MOVE, String.valueOf(move));
    }

    public void doTurnFromNetwork(int x, int y) {
        super.doTurn(x, y);
    }

    @Override
    public void startGame() {
        int halfSize = (int)Math.floor((double)super.getGameBoard().getSize() / 2);

        // Set the starting tiles
        super.getGameBoard().setTileType(halfSize - 1, halfSize - 1, GameBoardTileType.PLAYER_2);
        super.getGameBoard().setTileType(halfSize, halfSize - 1, GameBoardTileType.PLAYER_1);
        super.getGameBoard().setTileType(halfSize, halfSize, GameBoardTileType.PLAYER_2);
        super.getGameBoard().setTileType(halfSize - 1, halfSize, GameBoardTileType.PLAYER_1);

        super.startGame(this);
    }

    public void endGameWin(String input) {
        super.end(getCurrentTurnEntity(), getCurrentTurnEntity());
    }

    public void endGameLoss(String input) {
        super.end(getCurrentTurnEntity(), getCurrentTurnEntity());
    }
}
