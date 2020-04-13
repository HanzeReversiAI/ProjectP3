package com.hanzereversiai.projectp3.ai;

import com.thowv.javafxgridgameboard.*;
import com.thowv.javafxgridgameboard.premades.AbstractTurnEntityRandomAI;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiAlgorithms;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiGameInstance;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.ArrayList;

public class ReversiTurnEntityAdvancedAI extends AbstractTurnEntityRandomAI {


    private int[][] tileWeights;
    private int nOfMovesAhead;

    /**
     * Initiates ReversiTurnEntityAdvancedAI with the default nOfMovesAhead.
     *
     * @param name name of the player
     */
    public ReversiTurnEntityAdvancedAI(String name)
    {
        // 5 moves ahead seems to be ideal (good combination of calculating far ahead but remaining realistic)
        this(name, 5);
    }

    /**
     * Initiates ReversiTurnEntityAdvancedAI with the given nOfMovesAhead.
     *
     * @param name name of the player
     * @param nOfMovesAhead number of moves to calculate
     */
    public ReversiTurnEntityAdvancedAI(String name, int nOfMovesAhead)
    {
        super(name);

        this.nOfMovesAhead = nOfMovesAhead;
        this.initiateWeights();
    }

    /**
     * Calls takeTurn with the appropriate GameInstance class.
     *
     * @param gameInstance the active game instance to calculate from
     */
    @Override
    public void takeTurn(AbstractGameInstance gameInstance)
    {
        takeTurn((ReversiGameInstance)gameInstance);
    }

    /**
     * Handles the AI turn.
     *
     * @param gameInstance the active game instance to calculate from
     */
    private void takeTurn(ReversiGameInstance gameInstance)
    {
        ArrayList<GameBoardTile> possibleGameBoardTiles = ReversiAlgorithms.determineTilePossibilities(
                gameInstance.getGameBoard().getAllTiles(), getGameBoardTileType());
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));

        if (possibleGameBoardTiles.size() != 0) {
            gameInstance.getGameBoard().setTileTypes(possibleGameBoardTiles,
                    GameBoardTileType.VISIBLE);

            pauseTransition.setOnFinished(e -> this.doBestMove(gameInstance, possibleGameBoardTiles));
        }
        else{
            pauseTransition.setOnFinished(e -> gameInstance.passTurn());
            System.out.println("Passed turn");
        }

        pauseTransition.play();
    }

    /**
     * Performs the best possible move.
     *
     * @param gameInstance the active game instance to calculate from
     * @param possibleMoves all currently possible moves
     */
    private void doBestMove(ReversiGameInstance gameInstance, ArrayList<GameBoardTile> possibleMoves)
    {
        GameBoardTile bestMove = this.findBestMove(gameInstance, possibleMoves);

        gameInstance.doTurn(bestMove.getXCord(), bestMove.getYCord());
    }

    /**
     * Finds the best possible move.
     *
     * @param gameInstance the active game instance to calculate from
     * @param possibleMoves all currently possible moves
     * @return best possible move
     */
    private GameBoardTile findBestMove(ReversiGameInstance gameInstance, ArrayList<GameBoardTile> possibleMoves)
    {
        GameBoardTileType currentTileType = gameInstance.getCurrentTurnEntity().getGameBoardTileType();
        int bestScoreYet = -9999;
        GameBoardTile bestTileYet = possibleMoves.get(0);

        for(GameBoardTile possibleMove : possibleMoves){
            GameBoardTile[][] gameBoardTiles = gameInstance.getGameBoard().getAllTiles();

            GameBoardTileType[][] resetArray = this.getResetArray(gameBoardTiles);

            int moveScore = getScoreAfterMove(gameBoardTiles, possibleMove, currentTileType);
            moveScore += getNextMoveScore(gameBoardTiles, this.nOfMovesAhead, 0, false, AlgorithmHelper.flipTileType(currentTileType));
            this.resetBoardTo(gameBoardTiles, resetArray);

            if(moveScore > bestScoreYet){
                bestTileYet = possibleMove;
                bestScoreYet = moveScore;
            }
        }

        return bestTileYet;
    }

    /**
     * Recursively calculates the obtained score after performing each possible move.
     *
     * @param gameBoardTiles 2d array of current board contents
     * @param nOfMovesAhead number of moves to calculate
     * @param nOfMovesProcessed number of moves already processed
     * @param aiTurn if it's the AI's or not
     * @param currentTileType tile type of the current player
     * @return best possible score of out of all possible moves
     */
    private int getNextMoveScore(GameBoardTile[][] gameBoardTiles, int nOfMovesAhead, int nOfMovesProcessed, boolean aiTurn, GameBoardTileType currentTileType){
        ArrayList<GameBoardTile> possibleMoves = ReversiAlgorithms.determineTilePossibilities(gameBoardTiles, currentTileType);

        // If final move
        if(this.isFinalMove(gameBoardTiles)){
            int currentPlayerTiles = this.getNOfTilesByType(gameBoardTiles, currentTileType);
            int opponentTiles = this.getNOfTilesByType(gameBoardTiles, AlgorithmHelper.flipTileType(currentTileType));

            if(currentPlayerTiles > opponentTiles){
                // Current player wins
                return 1000;
            } else if(currentPlayerTiles == opponentTiles){
                // Draw
                return 500;
            } else {
                // Current player lost
                return -1000;
            }
        }

        if(nOfMovesProcessed > nOfMovesAhead || possibleMoves.size() <= 0){
            return 0;
        }

        int bestScoreYet;
        if(aiTurn){
            bestScoreYet = -9999;
        } else {
            bestScoreYet =  9999;
        }

        for(GameBoardTile possibleMove : possibleMoves){
            GameBoardTileType[][] resetArray = this.getResetArray(gameBoardTiles);

            int moveScore = this.getScoreAfterMove(gameBoardTiles, possibleMove, currentTileType);
            int nextMoveScore = this.getNextMoveScore(gameBoardTiles, nOfMovesAhead, ++nOfMovesProcessed, !aiTurn, AlgorithmHelper.flipTileType(currentTileType));
            this.resetBoardTo(gameBoardTiles, resetArray);

            if(!aiTurn){
                moveScore *= -1;
            } else {
                nextMoveScore *= -1;
            }

            moveScore += nextMoveScore;

            if((aiTurn && moveScore > bestScoreYet) || (!aiTurn && moveScore < bestScoreYet)){
                bestScoreYet = moveScore;
            }
        }

        return bestScoreYet;
    }

    /**
     * Gets the score after the specified move.
     *
     * @param gameBoardTiles 2d array of current board contents
     * @param move move to perform
     * @param currentTileType tile type of the current player
     * @return score after move.
     */
    private int getScoreAfterMove(GameBoardTile[][] gameBoardTiles, GameBoardTile move, GameBoardTileType currentTileType)
    {
        int scoreOldBoard = this.evaluateBoard(gameBoardTiles, currentTileType);
        gameBoardTiles[move.getXCord()][move.getYCord()].setGameBoardTileType(currentTileType);
        ReversiAlgorithms.flipTilesFromOrigin(gameBoardTiles, move.getXCord(), move.getYCord());
        int scoreNewBoard = this.evaluateBoard(gameBoardTiles, currentTileType);

        return scoreNewBoard - scoreOldBoard;
    }

    /**
     * Calculates the net score of the current player compared to the opponent.
     *
     * @param gameBoardTiles 2d array of current board contents
     * @param currentTileType tile type of the current player
     * @return net score of current player compared to opponent
     */
    private int evaluateBoard(GameBoardTile[][] gameBoardTiles, GameBoardTileType currentTileType)
    {
        int scoreCurrentMinusOpponent = 0;

        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                if(gameBoardTiles[x][y].getGameBoardTileType() == currentTileType){
                    scoreCurrentMinusOpponent += this.tileWeights[x][y];
                } else if(gameBoardTiles[x][y].getGameBoardTileType() == AlgorithmHelper.flipTileType(currentTileType)){
                    scoreCurrentMinusOpponent -= this.tileWeights[x][y];
                }
            }
        }

        return scoreCurrentMinusOpponent;
    }

    /**
     * Initiates the weight of each tile.
     */
    private void initiateWeights()
    {
        int[][] weights = {
            {99,  -8,  8,  6,  6,  8,  -8, 99},
            {-8, -24, -4, -3, -3, -4, -24, -8},
            { 8,  -4,  7,  4,  4,  7,  -4,  8},
            { 6,  -3,  4,  0,  0,  0,  -3,  6},
            { 6,  -3,  4,  0,  0,  0,  -3,  6},
            { 8,  -4,  7,  4,  4,  7,  -4,  8},
            {-8, -24, -4, -3, -3, -4, -24, -8},
            {99,  -8,  8,  6,  6,  8,  -8, 99}
        };

        this.tileWeights = weights;
    }

    /**
     * Gets a 2d array of the current board.
     *
     * @param gameBoardTiles 2d array of the current board contents
     * @return 2d array of the current board contents as a new object
     */
    private GameBoardTileType[][] getResetArray(GameBoardTile[][] gameBoardTiles)
    {
        GameBoardTileType[][] resetArray = new GameBoardTileType[8][8];

        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){

                // Manually setting them will ensure it is not the same object. Using the code in the next line will cause errors.
                // resetArray[x][y] = gameBoardTiles[x][y].getGameBoardTileType()

                if(gameBoardTiles[x][y].getGameBoardTileType() == GameBoardTileType.HIDDEN){
                    resetArray[x][y] = GameBoardTileType.HIDDEN;
                } else if(gameBoardTiles[x][y].getGameBoardTileType() == GameBoardTileType.VISIBLE){
                    resetArray[x][y] = GameBoardTileType.VISIBLE;
                } else if(gameBoardTiles[x][y].getGameBoardTileType() == GameBoardTileType.INTERACTABLE){
                    resetArray[x][y] = GameBoardTileType.INTERACTABLE;
                } else if(gameBoardTiles[x][y].getGameBoardTileType() == GameBoardTileType.PLAYER_1){
                    resetArray[x][y] = GameBoardTileType.PLAYER_1;
                } else if(gameBoardTiles[x][y].getGameBoardTileType() == GameBoardTileType.PLAYER_2){
                    resetArray[x][y] = GameBoardTileType.PLAYER_2;
                }
            }
        }

        return resetArray;
    }

    /**
     * Resets the board to the given 2d array.
     *
     * @param gameBoardTiles 2d array of the board
     * @param resetArray 2d array to reset the board to
     */
    private void resetBoardTo(GameBoardTile[][] gameBoardTiles, GameBoardTileType[][] resetArray)
    {
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                gameBoardTiles[x][y].setGameBoardTileType(resetArray[x][y]);
            }
        }
    }

    /**
     * Checks whether the next move is the final move.
     *
     * @param gameBoardTiles 2d array of the current board contents
     * @return true if final move, false if not
     */
    private boolean isFinalMove(GameBoardTile[][] gameBoardTiles)
    {
        int nOfPlayerOccupiedTiles = this.getNOfTilesByType(gameBoardTiles, GameBoardTileType.PLAYER_1);
        nOfPlayerOccupiedTiles += this.getNOfTilesByType(gameBoardTiles, GameBoardTileType.PLAYER_2);

        return nOfPlayerOccupiedTiles <= 1;
    }

    /**
     * Gets the number of tiles of specified type.
     *
     * @param gameBoardTiles 2d array of current board contents
     * @param gameBoardTileType type to find
     * @return number of tiles of specified type
     */
    private int getNOfTilesByType(GameBoardTile[][] gameBoardTiles, GameBoardTileType gameBoardTileType)
    {
        int n = 0;

        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                if(gameBoardTiles[x][y].getGameBoardTileType() == gameBoardTileType){
                    n++;
                }
            }
        }

        return n;
    }
}
