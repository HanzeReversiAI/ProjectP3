package com.hanzereversiai.projectp3.ai;

import com.thowv.javafxgridgameboard.GameBoardTile;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiGameInstance;

import java.util.HashMap;

public class TilePositionAnalyzer {

    private ReversiGameInstance gameInstance;
    private HashMap<GameBoardTile, HashMap<String, Integer>> availableTiles;

    public TilePositionAnalyzer(ReversiGameInstance gameInstance, HashMap<GameBoardTile, HashMap<String, Integer>> availableTiles)
    {
        this.setGameInstance(gameInstance);
        this.setAvailableTiles(availableTiles);
    }

    public HashMap<GameBoardTile, HashMap<String, Integer>> getAvailableSideTiles()
    {
        HashMap<GameBoardTile, HashMap<String, Integer>> availableSideTiles = new HashMap<>();

        for(HashMap.Entry<GameBoardTile, HashMap<String, Integer>> entry : this.getAvailableTiles().entrySet()){
            GameBoardTile tile = entry.getKey();
            HashMap<String, Integer> directionHits = entry.getValue();

            if((tile.getYCord() == 0 || tile.getYCord() == (this.getGameInstance().getGameBoard().getSize() - 1)) ||
                    (tile.getXCord() == 0 || tile.getXCord() == (this.getGameInstance().getGameBoard().getSize() - 1))){
                availableSideTiles.put(tile, directionHits);
            }
        }

        return availableSideTiles;
    }

    public HashMap<GameBoardTile, HashMap<String, Integer>> getAvailableCornerTiles()
    {
        HashMap<GameBoardTile, HashMap<String, Integer>> availableCornerTiles = new HashMap<>();

        for(HashMap.Entry<GameBoardTile, HashMap<String, Integer>> entry : this.getAvailableTiles().entrySet()){
            GameBoardTile tile = entry.getKey();
            HashMap<String, Integer> directionHits = entry.getValue();

            if((tile.getXCord() == 0 && tile.getYCord() == 0) ||
                (tile.getXCord() == 0 && tile.getYCord() == (this.getGameInstance().getGameBoard().getSize() - 1)) ||
                (tile.getXCord() == (this.getGameInstance().getGameBoard().getSize() - 1) && tile.getYCord() == 0) ||
                (tile.getXCord() == (this.getGameInstance().getGameBoard().getSize() - 1) && tile.getYCord() == (this.getGameInstance().getGameBoard().getSize() - 1))
            ){
                availableCornerTiles.put(tile, directionHits);
            }
        }

        return availableCornerTiles;
    }

    public HashMap<GameBoardTile, HashMap<String, Integer>> getAvailableTiles() {
        return availableTiles;
    }

    public void setAvailableTiles(HashMap<GameBoardTile, HashMap<String, Integer>> availableTiles) {
        this.availableTiles = availableTiles;
    }

    public ReversiGameInstance getGameInstance() {
        return gameInstance;
    }

    public void setGameInstance(ReversiGameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }
}
