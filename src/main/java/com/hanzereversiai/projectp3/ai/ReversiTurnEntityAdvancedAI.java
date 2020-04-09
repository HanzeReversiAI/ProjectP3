package com.hanzereversiai.projectp3.ai;

import com.thowv.javafxgridgameboard.AbstractGameInstance;
import com.thowv.javafxgridgameboard.GameBoardTile;
import com.thowv.javafxgridgameboard.GameBoardTileType;
import com.thowv.javafxgridgameboard.premades.AbstractTurnEntityRandomAI;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiAlgorithms;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiGameInstance;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;

public class ReversiTurnEntityAdvancedAI extends AbstractTurnEntityRandomAI {
    public ReversiTurnEntityAdvancedAI(String name) {
        super(name);
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

            GameBoardTile bestMove = this.getBestMove(possibleGameBoardTiles, gameInstance);

            pauseTransition.setOnFinished(e -> gameInstance.doTurn(bestMove.getXCord(), bestMove.getYCord()));
        }
        else{
            pauseTransition.setOnFinished(e -> gameInstance.passTurn());
            System.out.println("Passed turn");
        }

        pauseTransition.play();
    }

    public GameBoardTile getBestMove(ArrayList<GameBoardTile> availableTilesMoves, ReversiGameInstance gameInstance)
    {
        HashMap<GameBoardTile, HashMap<String, Integer>> availableTiles = this.getTileDirectionsHits(availableTilesMoves, gameInstance);
        HashMap<GameBoardTile, HashMap<String, Integer>> availableSideTiles = this.getAvailableSideTiles(gameInstance, availableTiles);
        HashMap<GameBoardTile, HashMap<String, Integer>> availableCornerTiles = this.getAvailableCornerTiles(gameInstance, availableTiles);

        HashMap<GameBoardTile, HashMap<String, Integer>> bestTiles = new HashMap<>();
        if(availableCornerTiles.size() > 0){
            bestTiles = availableCornerTiles;
        } else if(availableSideTiles.size() > 0){
            bestTiles = availableSideTiles;
        } else if(availableTiles.size() > 0) {
            bestTiles = availableTiles;
        } else {
            bestTiles = availableTiles;
        }

        return this.getBestMoveByHits(bestTiles);
    }

    private HashMap<GameBoardTile, HashMap<String, Integer>> getTileDirectionsHits(ArrayList<GameBoardTile> availableTilesMoves, ReversiGameInstance gameInstance)
    {
        HashMap<GameBoardTile, HashMap<String, Integer>> possibleBestMoves = new HashMap<>();

        for (int i=0; i <= availableTilesMoves.size() - 1; i++){

            // Get the current iterated tile
            GameBoardTile availableTile = availableTilesMoves.get(i);

            // Fill in the hits on each position of the current iterated tile
            HashMap<String, Integer> tileHits = new HashMap<>();
            tileHits.put("N", this.getNorthHits(availableTile, gameInstance));
            tileHits.put("NE", this.getNorthEastHits(availableTile, gameInstance));
            tileHits.put("E", this.getEastHits(availableTile, gameInstance));
            tileHits.put("SE", this.getSouthEastHits(availableTile, gameInstance));
            tileHits.put("S", this.getSouthHits(availableTile, gameInstance));
            tileHits.put("SW", this.getSouthWestHits(availableTile, gameInstance));
            tileHits.put("W", this.getWestHits(availableTile, gameInstance));
            tileHits.put("NW", this.getNorthWestHits(availableTile, gameInstance));

            // Add the current tile hits to the hashmap
            possibleBestMoves.put(availableTile, tileHits);
        }

        return  possibleBestMoves;
    }

    private HashMap<GameBoardTile, HashMap<String, Integer>> getAvailableSideTiles(ReversiGameInstance gameInstance, HashMap<GameBoardTile, HashMap<String, Integer>> allTiles)
    {
        HashMap<GameBoardTile, HashMap<String, Integer>> availableSideTiles = new HashMap<>();

        for(HashMap.Entry<GameBoardTile, HashMap<String, Integer>> entry : allTiles.entrySet()){
            GameBoardTile tile = entry.getKey();
            HashMap<String, Integer> directionHits = entry.getValue();

            if((tile.getYCord() == 0 || tile.getYCord() == (gameInstance.getGameBoard().getSize() - 1)) ||
                    (tile.getXCord() == 0 || tile.getXCord() == (gameInstance.getGameBoard().getSize() - 1))){
                availableSideTiles.put(tile, directionHits);
            }
        }

        return availableSideTiles;
    }

    private HashMap<GameBoardTile, HashMap<String, Integer>> getAvailableCornerTiles(ReversiGameInstance gameInstance, HashMap<GameBoardTile, HashMap<String, Integer>> allTiles)
    {
        HashMap<GameBoardTile, HashMap<String, Integer>> availableCornerTiles = new HashMap<>();

        for(HashMap.Entry<GameBoardTile, HashMap<String, Integer>> entry : allTiles.entrySet()){
            GameBoardTile tile = entry.getKey();
            HashMap<String, Integer> directionHits = entry.getValue();

            if(
                    (tile.getXCord() == 0 && tile.getYCord() == 0) ||
                            (tile.getXCord() == 0 && tile.getYCord() == (gameInstance.getGameBoard().getSize() - 1)) ||
                            (tile.getXCord() == (gameInstance.getGameBoard().getSize() - 1) && tile.getYCord() == 0) ||
                            (tile.getXCord() == (gameInstance.getGameBoard().getSize() - 1) && tile.getYCord() == (gameInstance.getGameBoard().getSize() - 1))
            ){
                availableCornerTiles.put(tile, directionHits);
            }
        }

        return availableCornerTiles;
    }

    private int getNorthHits(GameBoardTile tile, ReversiGameInstance gameInstance)
    {
        int hits = 0;

        for(int y = tile.getYCord() - 1; y >= 0; y--){
            GameBoardTile nextTile = gameInstance.getGameBoard().getTile(tile.getXCord(), y);
            boolean isAiTile = nextTile.getGameBoardTileType().equals(gameInstance.getCurrentTurnEntity().getGameBoardTileType());

            if(!isAiTile){
                hits++;

                // Last tile reached
                if(nextTile.getYCord() == 0){
                    hits = 0;
                }
            }
            else{
                break;
            }
        }

        return hits;
    }

    private int getNorthEastHits(GameBoardTile tile, ReversiGameInstance gameInstance)
    {
        int hits = 0;

        for(int i = 1; i < gameInstance.getGameBoard().getSize(); i++){
            int xCord = tile.getXCord() + i;
            int yCord = tile.getYCord() - i;

            if(xCord >= gameInstance.getGameBoard().getSize() || yCord < 0){
                break;
            }

            GameBoardTile nextTile = gameInstance.getGameBoard().getTile(xCord, yCord);
            boolean isAiTile = nextTile.getGameBoardTileType().equals(gameInstance.getCurrentTurnEntity().getGameBoardTileType());

            if(!isAiTile){
                hits++;

                // Last tile reached
                if(nextTile.getXCord() == gameInstance.getGameBoard().getSize() - 1 || nextTile.getYCord() == 0){
                    hits = 0;
                }
            }
            else{
                break;
            }
        }

        return hits;
    }

    private int getEastHits(GameBoardTile tile, ReversiGameInstance gameInstance)
    {
        int hits = 0;

        for(int x = tile.getXCord() + 1; x < gameInstance.getGameBoard().getSize(); x++){
            GameBoardTile nextTile = gameInstance.getGameBoard().getTile(x, tile.getYCord());
            boolean isAiTile = nextTile.getGameBoardTileType().equals(gameInstance.getCurrentTurnEntity().getGameBoardTileType());

            if(!isAiTile){
                hits++;

                // Last tile reached
                if(nextTile.getXCord() == gameInstance.getGameBoard().getSize() - 1){
                    hits = 0;
                }
            }
            else{
                break;
            }
        }

        return hits;
    }

    private int getSouthEastHits(GameBoardTile tile, ReversiGameInstance gameInstance)
    {
        int hits = 0;

        for(int i = 1; i < gameInstance.getGameBoard().getSize(); i++){
            int xCord = tile.getXCord() + i;
            int yCord = tile.getYCord() + i;

            if(xCord >= gameInstance.getGameBoard().getSize() || yCord >= gameInstance.getGameBoard().getSize()){
                break;
            }

            GameBoardTile nextTile = gameInstance.getGameBoard().getTile(xCord, yCord);
            boolean isAiTile = nextTile.getGameBoardTileType().equals(gameInstance.getCurrentTurnEntity().getGameBoardTileType());

            if(!isAiTile){
                hits++;

                // Last tile reached
                if(nextTile.getXCord() == gameInstance.getGameBoard().getSize() - 1 || nextTile.getYCord() == gameInstance.getGameBoard().getSize() - 1){
                    hits = 0;
                }
            }
            else{
                break;
            }
        }

        return hits;
    }

    private int getSouthHits(GameBoardTile tile, ReversiGameInstance gameInstance)
    {
        int hits = 0;

        for(int y = tile.getYCord() + 1; y < gameInstance.getGameBoard().getSize(); y++){
            GameBoardTile nextTile = gameInstance.getGameBoard().getTile(tile.getXCord(), y);
            boolean isAiTile = nextTile.getGameBoardTileType().equals(gameInstance.getCurrentTurnEntity().getGameBoardTileType());

            if(!isAiTile){
                hits++;

                // Last tile reached
                if(nextTile.getYCord() == gameInstance.getGameBoard().getSize() - 1){
                    hits = 0;
                }
            }
            else{
                break;
            }
        }

        return hits;
    }

    private int getSouthWestHits(GameBoardTile tile, ReversiGameInstance gameInstance)
    {
        int hits = 0;

        for(int i = 1; i < gameInstance.getGameBoard().getSize(); i++){
            int xCord = tile.getXCord() - i;
            int yCord = tile.getYCord() + i;

            if(xCord < 0 || yCord >= gameInstance.getGameBoard().getSize()){
                break;
            }

            GameBoardTile nextTile = gameInstance.getGameBoard().getTile(xCord, yCord);
            boolean isAiTile = nextTile.getGameBoardTileType().equals(gameInstance.getCurrentTurnEntity().getGameBoardTileType());

            if(!isAiTile){
                hits++;

                // Last tile reached
                if(nextTile.getXCord() == 0 || nextTile.getYCord() == gameInstance.getGameBoard().getSize() - 1){
                    hits = 0;
                }
            }
            else{
                break;
            }
        }

        return hits;
    }

    private int getWestHits(GameBoardTile tile, ReversiGameInstance gameInstance)
    {
        int hits = 0;

        for(int x = tile.getXCord() - 1; x >= 0; x--){
            GameBoardTile nextTile = gameInstance.getGameBoard().getTile(x, tile.getYCord());
            boolean isAiTile = nextTile.getGameBoardTileType().equals(gameInstance.getCurrentTurnEntity().getGameBoardTileType());

            if(!isAiTile){
                hits++;

                // Last tile reached
                if(nextTile.getXCord() == 0){
                    hits = 0;
                }
            }
            else{
                break;
            }
        }

        return hits;
    }

    private int getNorthWestHits(GameBoardTile tile, ReversiGameInstance gameInstance)
    {
        int hits = 0;

        for(int i = 1; i < gameInstance.getGameBoard().getSize(); i++){
            int xCord = tile.getXCord() - i;
            int yCord = tile.getYCord() - i;

            if(xCord < 0 || yCord < 0){
                break;
            }

            GameBoardTile nextTile = gameInstance.getGameBoard().getTile(xCord, yCord);
            boolean isAiTile = nextTile.getGameBoardTileType().equals(gameInstance.getCurrentTurnEntity().getGameBoardTileType());

            if(!isAiTile){
                hits++;

                // Last tile reached
                if(nextTile.getXCord() == 0 || nextTile.getYCord() == 0){
                    hits = 0;
                }
            }
            else{
                break;
            }
        }

        return hits;
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
