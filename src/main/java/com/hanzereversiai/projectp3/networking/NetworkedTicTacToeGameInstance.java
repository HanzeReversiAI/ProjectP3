package com.hanzereversiai.projectp3.networking;

import com.thowv.javafxgridgameboard.AbstractTurnEntity;
import com.thowv.javafxgridgameboard.GameBoard;
import com.thowv.javafxgridgameboard.premades.tictactoe.TTToeGameInstance;

/**
 * A Version of the TTToeGameInstance that supports playing against another opponent via the network
 *
 * @author Mike
 */
public class NetworkedTicTacToeGameInstance extends TTToeGameInstance implements NetworkedGameInstance {
    /**
     * Constructor
     * @param gameBoard A GameBoard instance
     * @param entityOne The 1st player
     * @param entityTwo The 2nd Player
     */
    public NetworkedTicTacToeGameInstance(GameBoard gameBoard, AbstractTurnEntity entityOne, AbstractTurnEntity entityTwo) {
        super(gameBoard, entityOne, entityTwo);
        NetworkSingleton.getNetworkInstance().getNetworkHandler().setNetworkedGameInstance(this);
    }

    /**
     * Do a turn on the GameBoard, and send it onwards to the network.
     *
     * @param x X coordinate of the tile on the GameBoard
     * @param y Y coordinate of the tile on the GameBoard
     */
    @Override
    public void doTurn(int x, int y) {
        super.doTurn(x, y);

        int width = getGameBoard().getSize();
        int move = (width * y) + x;
        NetworkSingleton.getNetworkInstance().sendCommand(Command.MOVE, String.valueOf(move));
    }

    /**
     * Do a turn on the GameBoard
     *
     * Because this turn came from the network we don't have to send it back.
     *
     * @param x X coordinate of the tile on the GameBoard
     * @param y Y coordinate of the tile on the GameBoard
     */
    @Override
    public void doTurnFromNetwork(int x, int y) {
        super.doTurn(x, y);
    }

    /**
     * Helper method to implement NetworkedGameInstance's passTurn function
     */
    @Override
    public void passTurn() {
        super.switchTurn();
    }
}
