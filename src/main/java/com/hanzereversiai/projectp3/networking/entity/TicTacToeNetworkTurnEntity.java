package com.hanzereversiai.projectp3.networking.entity;

import com.hanzereversiai.projectp3.networking.Network;
import com.thowv.javafxgridgameboard.AbstractGameInstance;
import com.thowv.javafxgridgameboard.GameBoardTile;

public class TicTacToeNetworkTurnEntity extends AbstractNetworkTurnEntity {

    public TicTacToeNetworkTurnEntity(Network network) {
        super(network);
    }

    @Override
    public void takeTurn(AbstractGameInstance abstractGameInstance) {
        GameBoardTile gameBoardTile = getGameTile(abstractGameInstance);
        abstractGameInstance.doTurn(gameBoardTile.getXCord(), gameBoardTile.getYCord());
    }
}
