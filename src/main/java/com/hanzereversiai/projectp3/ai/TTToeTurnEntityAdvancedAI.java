package com.hanzereversiai.projectp3.ai;

import com.thowv.javafxgridgameboard.*;
import com.thowv.javafxgridgameboard.premades.tictactoe.TTToeAlgorithms;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class TTToeTurnEntityAdvancedAI extends AbstractTurnEntity {
    public TTToeTurnEntityAdvancedAI(String name) {
        super(EntityType.AI, name);
    }

    @Override
    public void takeTurn(AbstractGameInstance gameInstance) {
        GameBoardTile best = findBestMove(gameInstance);

        // Prevent the AI from going too fast, instead, visualize the moves
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(1000));
        pauseTransition.setOnFinished((e) -> gameInstance.doTurn(best.getXCord(),best.getYCord()));
        pauseTransition.play();
    }

    // This function returns true if there are moves remaining on the board. It returns false if there are no moves left to play.
    static Boolean hasMovesLeft(AbstractGameInstance gameInstance) {
        return gameInstance.getGameBoard().getTilesByType(GameBoardTileType.HIDDEN).size() > 0;
    }

    // This is an evaluation function
    static int evaluate(GameBoard gameBoard, GameBoardTileType gameBoardTileType) {
        if (TTToeAlgorithms.checkThreeInRow(gameBoard.getAllTiles()) == gameBoardTileType)
            return +10;
        else if (TTToeAlgorithms.checkThreeInRow(gameBoard.getAllTiles()) == AlgorithmHelper.flipTileType(gameBoardTileType))
            return -10;
        else
            return 0;
    }

    //minMax func
    static int minMax(AbstractGameInstance gameInstance, int depth, Boolean isMax) {
        int score = evaluate(gameInstance.getGameBoard(), gameInstance.getCurrentTurnEntity().getGameBoardTileType());

        //predict max wins
        if (score == 10)
            return score;
        //predict min wins
        if (score == -10)
            return score;
        // predicts no winner = a tie
        if (!hasMovesLeft(gameInstance))
            return 0;

        // If this maximizer's move
        if (isMax) {
            int best = -1000;

            // Traverse all cells
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    // Check if cell is empty
                    if (gameInstance.getGameBoard().getTile(i,j).getGameBoardTileType() != GameBoardTileType.PLAYER_1 && gameInstance.getGameBoard().getTile(i,j).getGameBoardTileType() != GameBoardTileType.PLAYER_2){
                        // Make the move
                        gameInstance.getGameBoard().setTileType(i,j,gameInstance.getCurrentTurnEntity().getGameBoardTileType());
                        // Call minmax recursively and choose the maximum value
                        best = Math.max(best, minMax(gameInstance,depth + 1, false));
                        // Undo the move
                        gameInstance.getGameBoard().setTileType(i,j,GameBoardTileType.HIDDEN);
                    }
                }
            }

            return best;
        }

        // If this minimizer's move
        else {
            int best = 1000;
            // Traverse all cells
            for (int i = 0; i < 3; i++){
                for (int j = 0; j < 3; j++){
                    // Check if cell is empty
                    if (gameInstance.getGameBoard().getTile(i,j).getGameBoardTileType() != GameBoardTileType.PLAYER_1 && gameInstance.getGameBoard().getTile(i,j).getGameBoardTileType() != GameBoardTileType.PLAYER_2){

                        // Make the move
                        gameInstance.getGameBoard().setTileType(i,j,AlgorithmHelper.flipTileType(gameInstance.getCurrentTurnEntity().getGameBoardTileType()));

                        // Call minmax recursively and choose the minimum value
                        best = Math.min(best, minMax(gameInstance, depth + 1, true));

                        // Undo the move
                        gameInstance.getGameBoard().setTileType(i,j,GameBoardTileType.HIDDEN);                    }
                }
            }

            return best;
        }
    }

    // This will return the best possible move for the AI
    static GameBoardTile findBestMove(AbstractGameInstance gameInstance) {
        int bestVal = -1000;
        int bestMoveX = -1;
        int bestMoveY = -1;

        // Traverse all cells, evaluate minmax function for all empty cells. And return the cell with optimal value.
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                // Check if cell is empty
                if (gameInstance.getGameBoard().getTile(i,j).getGameBoardTileType() != GameBoardTileType.PLAYER_1 && gameInstance.getGameBoard().getTile(i,j).getGameBoardTileType() != GameBoardTileType.PLAYER_2){

                    // Make the move
                    gameInstance.getGameBoard().setTileType(i,j,gameInstance.getCurrentTurnEntity().getGameBoardTileType());

                    // compute evaluation function for this move.
                    int moveVal = minMax(gameInstance, 0, false);

                    // Undo the move
                    gameInstance.getGameBoard().setTileType(i,j,GameBoardTileType.HIDDEN);

                    // If the value of the current move is more than the best value, then update best
                    if (moveVal > bestVal) {
                        bestMoveX = i;
                        bestMoveY = j;
                        bestVal = moveVal;
                    }
                }
            }
        }

        return (gameInstance.getGameBoard().getTile(bestMoveX,bestMoveY));
    }
}

