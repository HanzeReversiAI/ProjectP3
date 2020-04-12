package com.hanzereversiai.projectp3.ai;

import com.thowv.javafxgridgameboard.*;
import com.thowv.javafxgridgameboard.premades.AbstractTurnEntityRandomAI;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiAlgorithms;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiGameInstance;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ReversiTurnEntityAdvancedAI extends AbstractTurnEntityRandomAI {

    private int[][] tileWeights;

    public ReversiTurnEntityAdvancedAI(String name) {
        super(name);

        this.initiateWeights();
    }

    @Override
    public void takeTurn(AbstractGameInstance gameInstance) {
        takeTurn((ReversiGameInstance)gameInstance);
    }

    private void takeTurn(ReversiGameInstance gameInstance) {
        ArrayList<GameBoardTile> possibleGameBoardTiles = ReversiAlgorithms.determineTilePossibilities(
                gameInstance.getGameBoard(), getGameBoardTileType());
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

    private void doBestMove(ReversiGameInstance gameInstance, ArrayList<GameBoardTile> possibleMoves)
    {
        GameBoardTile bestMove = this.findBestMove(gameInstance, possibleMoves);

//        gameInstance.doTurn(bestMove.getXCord(), bestMove.getYCord());
    }

    private GameBoardTile findBestMove(ReversiGameInstance gameInstance, ArrayList<GameBoardTile> possibleMoves)
    {
        GameBoardTileType currentTileType = gameInstance.getCurrentTurnEntity().getGameBoardTileType();
        int bestScoreYet = -9999;
        GameBoardTile bestTileYet = possibleMoves.get(0);

//        HashMap<GameBoardTile, Integer> moveScores = new HashMap<>();

        for(GameBoardTile possibleMove : possibleMoves){
            GameBoard copiedGameBoard = this.copyGameBoard(gameInstance.getGameBoard());
            System.out.println(copiedGameBoard);
            System.out.println("current amount for " + currentTileType.toString() + ": " +copiedGameBoard.countTilesByType(currentTileType));
            System.out.println("current amount for " + AlgorithmHelper.flipTileType(currentTileType).toString() + ": " +copiedGameBoard.countTilesByType(AlgorithmHelper.flipTileType(currentTileType)));
            System.out.println(gameInstance.getGameBoard());
            System.out.println("current amount for " + currentTileType.toString() + ": " +gameInstance.getGameBoard().countTilesByType(currentTileType));
            System.out.println("current amount for " + AlgorithmHelper.flipTileType(currentTileType).toString() + ": " +gameInstance.getGameBoard().countTilesByType(AlgorithmHelper.flipTileType(currentTileType)));

            copiedGameBoard.setTileType(possibleMove.getXCord(), possibleMove.getYCord(), currentTileType);

            System.out.println(copiedGameBoard);
            System.out.println("after move amount for " + currentTileType.toString() + ": " +copiedGameBoard.countTilesByType(currentTileType));
            System.out.println("after move amount for " + AlgorithmHelper.flipTileType(currentTileType).toString() + ": " +copiedGameBoard.countTilesByType(AlgorithmHelper.flipTileType(currentTileType)));
            System.out.println(gameInstance.getGameBoard());
            System.out.println("after move amount for " + currentTileType.toString() + ": " +gameInstance.getGameBoard().countTilesByType(currentTileType));
            System.out.println("after move amount for " + AlgorithmHelper.flipTileType(currentTileType).toString() + ": " +gameInstance.getGameBoard().countTilesByType(AlgorithmHelper.flipTileType(currentTileType)));

//            int moveScore = getScoreAfterMove(copiedGameBoard, possibleMove, currentTileType);
//            moveScore += getNextMoveScore(copiedGameBoard, 5, 0, false, AlgorithmHelper.flipTileType(currentTileType));
//            copiedGameBoard.setTileType(possibleMove.getXCord(), possibleMove.getYCord(), GameBoardTileType.HIDDEN);

//            if(moveScore > bestScoreYet){
//                bestTileYet = possibleMove;
//                bestScoreYet = moveScore;
//            }
        }

        // TODO: fix threading issues, board seems to be shared among threads
//        ExecutorService executorService = Executors.newCachedThreadPool();
//
//        for(GameBoardTile possibleMove : possibleMoves){
//            executorService.execute(new Runnable(){
//                @Override
//                public void run() {
//                    GameBoard copiedGameBoard = copyGameBoard(gameInstance.getGameBoard());
//                    int moveScore = getScoreAfterMove(copiedGameBoard, possibleMove, currentTileType);
//                    moveScore += getNextMoveScore(copiedGameBoard, 15, 0, false, AlgorithmHelper.flipTileType(currentTileType));
//                    gameInstance.getGameBoard().setTileType(possibleMove.getXCord(), possibleMove.getYCord(), GameBoardTileType.HIDDEN);
//
//                    moveScores.put(possibleMove, moveScore);
//                }
//            });
//        }
//
//        executorService.shutdown();
//
//        try {
//            if(!executorService.awaitTermination(8, TimeUnit.SECONDS)){
//                executorService.shutdownNow();
//            }
//        } catch (InterruptedException e){
//            e.printStackTrace();
//            executorService.shutdownNow();
//        }
//
//        for(HashMap.Entry<GameBoardTile, Integer> entry : moveScores.entrySet()){
//            GameBoardTile move = entry.getKey();
//            Integer moveScore = entry.getValue();
//
//            if(moveScore > bestScoreYet){
//                bestTileYet = move;
//                bestScoreYet = moveScore;
//            }
//        }

        return bestTileYet;
    }

    private int getNextMoveScore(GameBoard gameBoard, int nOfMovesAhead, int nOfMovesProcessed, boolean aiTurn, GameBoardTileType currentTileType){
        ArrayList<GameBoardTile> possibleMoves = ReversiAlgorithms.determineTilePossibilities(gameBoard, currentTileType);

        // If final move
        if(gameBoard.countTilesByType(GameBoardTileType.HIDDEN) <= 1){
            int currentPlayerTiles = gameBoard.countTilesByType(currentTileType);
            int opponentTiles = gameBoard.countTilesByType(AlgorithmHelper.flipTileType(currentTileType));

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
            int moveScore = this.getScoreAfterMove(gameBoard, possibleMove, currentTileType);
            int nextMoveScore = this.getNextMoveScore(gameBoard, nOfMovesAhead, ++nOfMovesProcessed, !aiTurn, AlgorithmHelper.flipTileType(currentTileType));
            gameBoard.setTileType(possibleMove.getXCord(), possibleMove.getYCord(), GameBoardTileType.HIDDEN);

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

    private int getScoreAfterMove(GameBoard gameBoard, GameBoardTile move, GameBoardTileType currentTileType)
    {
        int scoreOldBoard = this.evaluateBoard(gameBoard, currentTileType);
        gameBoard.setTileType(move.getXCord(), move.getYCord(), currentTileType);
        // TODO: also flip tiles after move
//        ReversiAlgorithms.flipTilesFromOrigin(gameBoard, currentTileType, move.getXCord(), move.getYCord());
        int scoreNewBoard = this.evaluateBoard(gameBoard, currentTileType);

        return scoreNewBoard - scoreOldBoard;
    }

    private int evaluateBoard(GameBoard gameBoard, GameBoardTileType currentTileType)
    {
        ArrayList<GameBoardTile> tilesCurrentTurnEntity = gameBoard.getTilesByType(currentTileType);
        ArrayList<GameBoardTile> tilesOpponentTurnEntity = gameBoard.getTilesByType(AlgorithmHelper.flipTileType(currentTileType));
        int scoreCurrentMinusOpponent = 0;

        for(GameBoardTile tile : tilesCurrentTurnEntity){
            scoreCurrentMinusOpponent += this.tileWeights[tile.getYCord()][tile.getXCord()];
        }

        for(GameBoardTile tile : tilesOpponentTurnEntity){
            scoreCurrentMinusOpponent -= this.tileWeights[tile.getYCord()][tile.getXCord()];
        }

        return scoreCurrentMinusOpponent;
    }

    private void initiateWeights()
    {
        // TODO: experiment with dynamic weights, I.E.: change [1, 1] from -24 to 99 when [0, 0] is already filled
        // TODO: at the endgame, the weights wont properly represent the real worth of the tile, causing the AI to make bad choices
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

    private GameBoard copyGameBoard(GameBoard gameBoard)
    {
        int size = gameBoard.getSize();
        GameBoard newGameBoard = new GameBoard(size);
        GameBoardTile[][] tiles = new GameBoardTile[size][size];

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                tiles[i][j] = gameBoard.getTile(i, j);
            }
        }

        newGameBoard.getGameBoardBehavior().setGameBoardTiles(tiles);

        return newGameBoard;
    }
}
