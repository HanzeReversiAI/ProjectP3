package com.hanzereversiai.projectp3.ai;

import com.thowv.javafxgridgameboard.GameBoardTile;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiGameInstance;

import java.util.ArrayList;
import java.util.HashMap;

public class TileHitAnalyzer {
    private ReversiGameInstance gameInstance;
    private ArrayList<GameBoardTile> possibleMoves;
    
    public TileHitAnalyzer(ReversiGameInstance gameInstance, ArrayList<GameBoardTile> possibleMoves)
    {
        this.setGameInstance(gameInstance);
        this.setPossibleMoves(possibleMoves);
    }

    public HashMap<GameBoardTile, HashMap<String, Integer>> getTileHitsByDirection()
    {
        HashMap<GameBoardTile, HashMap<String, Integer>> possibleBestMoves = new HashMap<>();

        ArrayList<GameBoardTile> moves = this.getPossibleMoves();

        for (int i=0; i <= moves.size() - 1; i++){

            // Get the current iterated tile
            GameBoardTile availableTile = moves.get(i);

            // Fill in the hits on each position of the current iterated tile
            HashMap<String, Integer> tileHits = new HashMap<>();
            tileHits.put("N", this.getNorthHits(availableTile));
            tileHits.put("NE", this.getNorthEastHits(availableTile));
            tileHits.put("E", this.getEastHits(availableTile));
            tileHits.put("SE", this.getSouthEastHits(availableTile));
            tileHits.put("S", this.getSouthHits(availableTile));
            tileHits.put("SW", this.getSouthWestHits(availableTile));
            tileHits.put("W", this.getWestHits(availableTile));
            tileHits.put("NW", this.getNorthWestHits(availableTile));

            // Add the current tile hits to the hashmap
            possibleBestMoves.put(availableTile, tileHits);
        }

        return  possibleBestMoves;
    }

    private int getNorthHits(GameBoardTile tile)
    {
        int hits = 0;

        for(int y = tile.getYCord() - 1; y >= 0; y--){
            GameBoardTile nextTile = this.getGameInstance().getGameBoard().getTile(tile.getXCord(), y);
            boolean isAiTile = nextTile.getGameBoardTileType().equals(this.getGameInstance().getCurrentTurnEntity().getGameBoardTileType());

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

    private int getNorthEastHits(GameBoardTile tile)
    {
        int hits = 0;

        for(int i = 1; i < this.getGameInstance().getGameBoard().getSize(); i++){
            int xCord = tile.getXCord() + i;
            int yCord = tile.getYCord() - i;

            if(xCord >= this.getGameInstance().getGameBoard().getSize() || yCord < 0){
                break;
            }

            GameBoardTile nextTile = this.getGameInstance().getGameBoard().getTile(xCord, yCord);
            boolean isAiTile = nextTile.getGameBoardTileType().equals(this.getGameInstance().getCurrentTurnEntity().getGameBoardTileType());

            if(!isAiTile){
                hits++;

                // Last tile reached
                if(nextTile.getXCord() == this.getGameInstance().getGameBoard().getSize() - 1 || nextTile.getYCord() == 0){
                    hits = 0;
                }
            }
            else{
                break;
            }
        }

        return hits;
    }

    private int getEastHits(GameBoardTile tile)
    {
        int hits = 0;

        for(int x = tile.getXCord() + 1; x < this.getGameInstance().getGameBoard().getSize(); x++){
            GameBoardTile nextTile = this.getGameInstance().getGameBoard().getTile(x, tile.getYCord());
            boolean isAiTile = nextTile.getGameBoardTileType().equals(this.getGameInstance().getCurrentTurnEntity().getGameBoardTileType());

            if(!isAiTile){
                hits++;

                // Last tile reached
                if(nextTile.getXCord() == this.getGameInstance().getGameBoard().getSize() - 1){
                    hits = 0;
                }
            }
            else{
                break;
            }
        }

        return hits;
    }

    private int getSouthEastHits(GameBoardTile tile)
    {
        int hits = 0;

        for(int i = 1; i < this.getGameInstance().getGameBoard().getSize(); i++){
            int xCord = tile.getXCord() + i;
            int yCord = tile.getYCord() + i;

            if(xCord >= this.getGameInstance().getGameBoard().getSize() || yCord >= this.getGameInstance().getGameBoard().getSize()){
                break;
            }

            GameBoardTile nextTile = this.getGameInstance().getGameBoard().getTile(xCord, yCord);
            boolean isAiTile = nextTile.getGameBoardTileType().equals(this.getGameInstance().getCurrentTurnEntity().getGameBoardTileType());

            if(!isAiTile){
                hits++;

                // Last tile reached
                if(nextTile.getXCord() == this.getGameInstance().getGameBoard().getSize() - 1 || nextTile.getYCord() == this.getGameInstance().getGameBoard().getSize() - 1){
                    hits = 0;
                }
            }
            else{
                break;
            }
        }

        return hits;
    }

    private int getSouthHits(GameBoardTile tile)
    {
        int hits = 0;

        for(int y = tile.getYCord() + 1; y < this.getGameInstance().getGameBoard().getSize(); y++){
            GameBoardTile nextTile = this.getGameInstance().getGameBoard().getTile(tile.getXCord(), y);
            boolean isAiTile = nextTile.getGameBoardTileType().equals(this.getGameInstance().getCurrentTurnEntity().getGameBoardTileType());

            if(!isAiTile){
                hits++;

                // Last tile reached
                if(nextTile.getYCord() == this.getGameInstance().getGameBoard().getSize() - 1){
                    hits = 0;
                }
            }
            else{
                break;
            }
        }

        return hits;
    }

    private int getSouthWestHits(GameBoardTile tile)
    {
        int hits = 0;

        for(int i = 1; i < this.getGameInstance().getGameBoard().getSize(); i++){
            int xCord = tile.getXCord() - i;
            int yCord = tile.getYCord() + i;

            if(xCord < 0 || yCord >= this.getGameInstance().getGameBoard().getSize()){
                break;
            }

            GameBoardTile nextTile = this.getGameInstance().getGameBoard().getTile(xCord, yCord);
            boolean isAiTile = nextTile.getGameBoardTileType().equals(this.getGameInstance().getCurrentTurnEntity().getGameBoardTileType());

            if(!isAiTile){
                hits++;

                // Last tile reached
                if(nextTile.getXCord() == 0 || nextTile.getYCord() == this.getGameInstance().getGameBoard().getSize() - 1){
                    hits = 0;
                }
            }
            else{
                break;
            }
        }

        return hits;
    }

    private int getWestHits(GameBoardTile tile)
    {
        int hits = 0;

        for(int x = tile.getXCord() - 1; x >= 0; x--){
            GameBoardTile nextTile = this.getGameInstance().getGameBoard().getTile(x, tile.getYCord());
            boolean isAiTile = nextTile.getGameBoardTileType().equals(this.getGameInstance().getCurrentTurnEntity().getGameBoardTileType());

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

    private int getNorthWestHits(GameBoardTile tile)
    {
        int hits = 0;

        for(int i = 1; i < this.getGameInstance().getGameBoard().getSize(); i++){
            int xCord = tile.getXCord() - i;
            int yCord = tile.getYCord() - i;

            if(xCord < 0 || yCord < 0){
                break;
            }

            GameBoardTile nextTile = this.getGameInstance().getGameBoard().getTile(xCord, yCord);
            boolean isAiTile = nextTile.getGameBoardTileType().equals(this.getGameInstance().getCurrentTurnEntity().getGameBoardTileType());

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
    
    public ReversiGameInstance getGameInstance() {
        return gameInstance;
    }

    public void setGameInstance(ReversiGameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public ArrayList<GameBoardTile> getPossibleMoves() {
        return possibleMoves;
    }

    public void setPossibleMoves(ArrayList<GameBoardTile> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }
}
