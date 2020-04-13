package com.hanzereversiai.projectp3;

import com.hanzereversiai.projectp3.ai.ReversiTurnEntityAdvancedAI;
import com.hanzereversiai.projectp3.ai.TTToeTurnEntityAdvancedAI;
import com.hanzereversiai.projectp3.networking.NetworkTurnEntity;
import com.hanzereversiai.projectp3.networking.NetworkedReversiGameInstance;
import com.hanzereversiai.projectp3.networking.NetworkedTicTacToeGameInstance;
import com.hanzereversiai.projectp3.ui.BoardGameOption;
import com.thowv.javafxgridgameboard.AbstractGameInstance;
import com.thowv.javafxgridgameboard.AbstractTurnEntity;
import com.thowv.javafxgridgameboard.GameBoard;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiGameInstance;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiTurnEntityPlayer;
import com.thowv.javafxgridgameboard.premades.tictactoe.TTToeGameInstance;
import com.thowv.javafxgridgameboard.premades.tictactoe.TTToeTurnEntityPlayer;

public class GameFactory {
    public static AbstractGameInstance buildGameInstance(BoardGameOption boardGameOption, String playerOneOption, String playerTwoOption, int aiDepthAmount) {
        return buildGameInstance(boardGameOption, new String[] { playerOneOption, playerTwoOption }, aiDepthAmount);
    }
    public static AbstractGameInstance buildNetworkedGameInstance(BoardGameOption boardGameOption, String playerOneOption, String playerTwoOption, int aiDepthAmount) {
        return buildNetworkedGameInstance(boardGameOption, new String[] { playerOneOption, playerTwoOption }, aiDepthAmount);
    }

    private static AbstractGameInstance buildGameInstance(BoardGameOption boardGameOption, String[] playerOptions, int aiDepthAmount) {
        GameBoard gameBoard;
        AbstractTurnEntity[] turnEntities = new AbstractTurnEntity[2];
        AbstractGameInstance gameInstance = null;

        if (boardGameOption.getShortName().equals("reversi")) {
            gameBoard = new GameBoard(8);

            for (int i = 0; i < playerOptions.length; i++) {
                if (playerOptions[i].equals("Player"))
                    turnEntities[i] = new ReversiTurnEntityPlayer("Player " + (i + 1));
                else if(playerOptions[i].equals("AI"))
                    turnEntities[i] = new ReversiTurnEntityAdvancedAI("AI " + (i + 1), aiDepthAmount);
            }

            gameInstance = new ReversiGameInstance(gameBoard, turnEntities[0], turnEntities[1]);
        }
        else if (boardGameOption.getShortName().equals("tttoe")) {
            gameBoard = new GameBoard(3);

            for (int i = 0; i < playerOptions.length; i++) {
                if (playerOptions[i].equals("Player"))
                    turnEntities[i] = new TTToeTurnEntityPlayer("Player " + (i + 1));
                else if(playerOptions[i].equals("AI"))
                    turnEntities[i] = new TTToeTurnEntityAdvancedAI("AI  " + (i + 1));
            }

            gameInstance = new TTToeGameInstance(gameBoard, turnEntities[0], turnEntities[1]);
        }

        return gameInstance;
    }

    private static AbstractGameInstance buildNetworkedGameInstance(BoardGameOption boardGameOption, String[] playerOptions, int aiDepthAmount) {
        GameBoard gameBoard;
        AbstractTurnEntity[] turnEntities = new AbstractTurnEntity[2];
        AbstractGameInstance gameInstance = null;

        if (boardGameOption.getShortName().equals("reversi")) {
            gameBoard = new GameBoard(8);

            for (int i = 0; i < playerOptions.length; i++) {
                if (playerOptions[i].equals("Player"))
                    turnEntities[i] = new ReversiTurnEntityAdvancedAI("You", aiDepthAmount);
                else if(playerOptions[i].contains("Network")) {
                    String[] info = playerOptions[i].split("-");
                    turnEntities[i] = new NetworkTurnEntity(info[1]);
                }

            }

            gameInstance = new NetworkedReversiGameInstance(gameBoard, turnEntities[0], turnEntities[1]);
        }
        else if (boardGameOption.getShortName().equals("tttoe")) {
            gameBoard = new GameBoard(3);

            for (int i = 0; i < playerOptions.length; i++) {
                if (playerOptions[i].equals("Player"))
                    turnEntities[i] = new TTToeTurnEntityPlayer("You");
                else if(playerOptions[i].equals("Network"))
                    turnEntities[i] = new NetworkTurnEntity("Network player");
            }

            gameInstance = new NetworkedTicTacToeGameInstance(gameBoard, turnEntities[0], turnEntities[1]);
        }

        return gameInstance;
    }
}
