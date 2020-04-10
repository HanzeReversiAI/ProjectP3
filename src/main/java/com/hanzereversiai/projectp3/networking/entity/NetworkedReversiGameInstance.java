package com.hanzereversiai.projectp3.networking.entity;

import com.hanzereversiai.projectp3.networking.*;
import com.thowv.javafxgridgameboard.AbstractTurnEntity;
import com.thowv.javafxgridgameboard.GameBoard;
import com.thowv.javafxgridgameboard.GameBoardTileType;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiGameInstance;

public class NetworkedReversiGameInstance extends ReversiGameInstance implements InputListener, NetworkedGameInstance {
    public NetworkedReversiGameInstance(GameBoard gameBoard, AbstractTurnEntity entityOne, AbstractTurnEntity entityTwo) {
        super(gameBoard, entityOne, entityTwo);
        DelegateInputListener delegateInputListener = NetworkSingleton.getNetworkInstance().getDelegateInputListener();
        delegateInputListener.SUBSCRIBE_MOVE(this);
        delegateInputListener.SUBSCRIBE_WIN(this::endGameWin);
        delegateInputListener.SUBSCRIBE_LOSS(this::endGameLoss);
        delegateInputListener.SUBSCRIBE_YOURTURN(this::getTurnFromNetwork);
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

    public void getTurnFromNetwork(String input) {
        if (getCurrentTurnEntity() instanceof  NetworkTurnEntity) {
            NetworkTurnEntity networkTurnEntity = (NetworkTurnEntity) getCurrentTurnEntity();
            networkTurnEntity.getTurnFromNetwork(this);
        }
    }

    public void endGameWin(String input) {
        disconnect();
        super.end(getCurrentTurnEntity(), getNotCurrentTurnEntity());
    }

    public void endGameLoss(String input) {
        disconnect();
        super.end(getNotCurrentTurnEntity(), getCurrentTurnEntity());
    }

    private void disconnect() {
        DelegateInputListener delegateInputListener = NetworkSingleton.getNetworkInstance().getDelegateInputListener();
        delegateInputListener.UNSUBSCRIBE_MOVE(this);
        delegateInputListener.UNSUBSCRIBE_WIN(this::endGameWin);
        delegateInputListener.UNSUBSCRIBE_LOSS(this::endGameLoss);
        delegateInputListener.UNSUBSCRIBE_YOURTURN(this::getTurnFromNetwork);
    }

    private AbstractTurnEntity getNotCurrentTurnEntity() {
        AbstractTurnEntity current = getCurrentTurnEntity();
        for (AbstractTurnEntity entity : getTurnEntities()){
            if (!entity.equals(current)) {
                return  entity;
            }
        }
        return null;
    }
}
