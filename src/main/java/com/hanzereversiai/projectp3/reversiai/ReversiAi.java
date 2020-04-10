package com.hanzereversiai.projectp3.reversiai;

import com.thowv.javafxgridgameboard.AbstractGameInstance;
import com.thowv.javafxgridgameboard.GameBoardTile;
import com.thowv.javafxgridgameboard.GameBoardTileType;
import com.thowv.javafxgridgameboard.premades.AbstractTurnEntityRandomAI;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiAlgorithms;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiGameInstance;

import java.util.ArrayList;
import java.util.HashMap;

public class ReversiAi extends AbstractTurnEntityRandomAI {


    @Override
    public void takeTurn(AbstractGameInstance gameInstance) {
        takeTurn((ReversiGameInstance)gameInstance);
    }

    private void takeTurn(ReversiGameInstance gameInstance) {
        ArrayList<GameBoardTile> possibleGameBoardTiles = ReversiAlgorithms.determineTilePossibilities(
                gameInstance.getGameBoard(), getGameBoardTileType());

        if (possibleGameBoardTiles.size() != 0) {
            gameInstance.getGameBoard().setTileTypes(possibleGameBoardTiles,
                    GameBoardTileType.VISIBLE);

            GameBoardTile bestMove = this.getBestMove(possibleGameBoardTiles, gameInstance);

            gameInstance.doTurn(bestMove.getXCord(), bestMove.getYCord());
        }
        else{
            gameInstance.passTurn();
            System.out.println("Passed turn");
        }
    }

    public GameBoardTile getBestMove(ArrayList<GameBoardTile> availableTilesMoves, ReversiGameInstance gameInstance)
    {
        // Creating a tile hit analyzer
        TileHitAnalyzer hitAnalyzer = new TileHitAnalyzer(gameInstance, availableTilesMoves);
        // Adding given hits that a tile can make.
        HashMap<GameBoardTile, HashMap<String, Integer>> availableTilesHits = hitAnalyzer.getTileHitsByDirection();
        // Creating a position analyzer class
        TilePositionAnalyzer positionAnalyzer = new TilePositionAnalyzer(gameInstance, availableTilesHits);

        // Get from our possible moves (with directional hits) the side and the corner tiles.
        // If we have a corner tile the possibility to win increases.
        HashMap<GameBoardTile, HashMap<String, Integer>> availableSideTiles   = positionAnalyzer.getAvailableSideTiles();
        // Side tile give a higher chance to win (but corner tiles are the best)
        HashMap<GameBoardTile, HashMap<String, Integer>> availableCornerTiles = positionAnalyzer.getAvailableCornerTiles();


        // Prioritize corner tiles --> strategy to win, they can't take it back
        if(availableCornerTiles.size() > 0){
            return this.getBestMoveByHits(availableCornerTiles);
        }

        // After corner tiles the side tiles have a priority (1 flank less)
        availableSideTiles = this.removeTilesThatBenefitOpponent(availableSideTiles, gameInstance);
        if(availableSideTiles.size() > 0){
            return this.getBestMoveByHits(availableSideTiles);
        }

        // After side tiles, only mid-board tiles are left
        HashMap<GameBoardTile, HashMap<String, Integer>> filteredAvailableTilesHits =
                this.removeTilesThatBenefitOpponent(availableTilesHits, gameInstance);
        if(filteredAvailableTilesHits.size() > 0){
            return this.getBestMoveByHits(filteredAvailableTilesHits);
        }

        // Forced to play a move that might benefit opponent
        return this.getBestMoveByHits(availableTilesHits);
    }

    private HashMap<GameBoardTile, HashMap<String, Integer>> removeTilesThatBenefitOpponent(HashMap<GameBoardTile, HashMap<String, Integer>> inputTiles, ReversiGameInstance gameInstance){
        HashMap<GameBoardTile, HashMap<String, Integer>> filteredTiles = new HashMap<>();

        for(HashMap.Entry<GameBoardTile, HashMap<String, Integer>> entry : inputTiles.entrySet()){
            GameBoardTile tile = entry.getKey();
            HashMap<String, Integer> directionHits = entry.getValue();

            // If move will enable opponent to get side tiles
            if(!(
                    tile.getXCord() == 1 ||
                    tile.getXCord() == gameInstance.getGameBoard().getSize() - 2 ||
                    tile.getYCord() == 1||
                    tile.getYCord() == gameInstance.getGameBoard().getSize() - 2
            )){
                filteredTiles.put(tile, directionHits);
            }
        }

        return filteredTiles;
    }

    private GameBoardTile getBestMoveByHits(HashMap<GameBoardTile, HashMap<String, Integer>> tiles)
    {
        GameBoardTile bestMove = null;
        int highestYet = 0;

        for(HashMap.Entry<GameBoardTile, HashMap<String, Integer>> entry : tiles.entrySet()) {
            GameBoardTile tile = entry.getKey();
            HashMap<String, Integer> directionHits = entry.getValue();

            if(this.getDirectionHitsSum(directionHits) > highestYet){
                bestMove = tile;
            }
        }

        return bestMove;
    }

    private int getDirectionHitsSum(HashMap<String, Integer> directionHits)
    {
        int directionHitsSum = 0;

        for (HashMap.Entry<String, Integer> direction : directionHits.entrySet()) {
            int hits = direction.getValue();

            directionHitsSum += hits;
        }

        return directionHitsSum;
    }
}
