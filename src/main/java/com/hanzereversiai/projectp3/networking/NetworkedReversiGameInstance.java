package com.hanzereversiai.projectp3.networking;

import com.thowv.javafxgridgameboard.AbstractTurnEntity;
import com.thowv.javafxgridgameboard.GameBoard;
import com.thowv.javafxgridgameboard.GameBoardTileType;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiGameInstance;

/**
 * A Version of the ReversiGameInstance that supports playing against another opponent via the network
 */
public class NetworkedReversiGameInstance extends ReversiGameInstance implements NetworkedGameInstance {
    /**
     * Constructor
     * @param gameBoard A GameBoard instance
     * @param entityOne The 1st player
     * @param entityTwo The 2nd Player
     */
    public NetworkedReversiGameInstance(GameBoard gameBoard, AbstractTurnEntity entityOne, AbstractTurnEntity entityTwo) {
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
    public void doTurnFromNetwork(int x, int y) {
        super.doTurn(x, y);
    }

    /**
     * Start the game with the GameBoard reversed from the local version.
     *
     * (To make it the same as the server used to play on the network)
     */
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
}
