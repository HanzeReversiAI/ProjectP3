package com.hanzereversiai.projectp3.ai;

import com.thowv.javafxgridgameboard.*;
import com.thowv.javafxgridgameboard.premades.AbstractTurnEntityRandomAI;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiAlgorithms;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiGameInstance;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;

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

    private void doBestMove(ReversiGameInstance gameInstance, ArrayList<GameBoardTile> possibleMoves)
    {
        GameBoardTile bestMove = this.findBestMove(gameInstance, possibleMoves);

        gameInstance.doTurn(bestMove.getXCord(), bestMove.getYCord());
    }

    private GameBoardTile findBestMove(ReversiGameInstance gameInstance, ArrayList<GameBoardTile> possibleMoves)
    {
        GameBoardTileType currentTileType = gameInstance.getCurrentTurnEntity().getGameBoardTileType();
        int bestScoreYet = -9999;
        GameBoardTile bestTileYet = possibleMoves.get(0);

//        HashMap<GameBoardTile, Integer> moveScores = new HashMap<>();

        for(GameBoardTile possibleMove : possibleMoves){
            GameBoardTile[][] gameBoardTiles = gameInstance.getGameBoard().getAllTiles();

            GameBoardTileType[][] resetArray = this.getResetArray(gameBoardTiles);

            int moveScore = getScoreAfterMove(gameBoardTiles, possibleMove, currentTileType);
            moveScore += getNextMoveScore(gameBoardTiles, 5, 0, false, AlgorithmHelper.flipTileType(currentTileType));
            this.resetBoardTo(gameBoardTiles, resetArray);

            if(moveScore > bestScoreYet){
                bestTileYet = possibleMove;
                bestScoreYet = moveScore;
            }
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

    private int getScoreAfterMove(GameBoardTile[][] gameBoardTiles, GameBoardTile move, GameBoardTileType currentTileType)
    {
        int scoreOldBoard = this.evaluateBoard(gameBoardTiles, currentTileType);
        gameBoardTiles[move.getXCord()][move.getYCord()].setGameBoardTileType(currentTileType);
        ReversiAlgorithms.flipTilesFromOrigin(gameBoardTiles, move.getXCord(), move.getYCord());
        int scoreNewBoard = this.evaluateBoard(gameBoardTiles, currentTileType);

        return scoreNewBoard - scoreOldBoard;
    }

    private int evaluateBoard(GameBoardTile[][] gameBoardTiles, GameBoardTileType currentTileType)
    {
        int scoreCurrentMinusOpponent = 0;

        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                if(gameBoardTiles[x][y].getGameBoardTileType() == currentTileType){
                    scoreCurrentMinusOpponent += this.tileWeights[x][y];
                } else {
                    scoreCurrentMinusOpponent -= this.tileWeights[x][y];
                }
            }
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

    private void resetBoardTo(GameBoardTile[][] gameBoardTiles, GameBoardTileType[][] resetArray)
    {
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                gameBoardTiles[x][y].setGameBoardTileType(resetArray[x][y]);
            }
        }
    }

    private boolean isFinalMove(GameBoardTile[][] gameBoardTiles)
    {
        int nOfPlayerOccupiedTiles = this.getNOfTilesByType(gameBoardTiles, GameBoardTileType.PLAYER_1);
        nOfPlayerOccupiedTiles += this.getNOfTilesByType(gameBoardTiles, GameBoardTileType.PLAYER_2);

        return nOfPlayerOccupiedTiles <= 1;
    }

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
