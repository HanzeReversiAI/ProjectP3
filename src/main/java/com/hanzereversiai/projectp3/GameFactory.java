package com.hanzereversiai.projectp3;

import com.hanzereversiai.projectp3.TTToeTurnAI.TTToeAdvancedAI;
import com.hanzereversiai.projectp3.reversiai.ReversiAi;
import com.hanzereversiai.projectp3.ui.BoardGameOption;
import com.thowv.javafxgridgameboard.AbstractGameInstance;
import com.thowv.javafxgridgameboard.AbstractTurnEntity;
import com.thowv.javafxgridgameboard.GameBoard;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiGameInstance;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiTurnEntityAI;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiTurnEntityPlayer;
import com.thowv.javafxgridgameboard.premades.tictactoe.TTToeGameInstance;
import com.thowv.javafxgridgameboard.premades.tictactoe.TTToeTurnEntityAI;
import com.thowv.javafxgridgameboard.premades.tictactoe.TTToeTurnEntityPlayer;

public class GameFactory {
    public static AbstractGameInstance buildGameInstance(BoardGameOption boardGameOption, String playerOneOption, String playerTwoOption) {
        return buildGameInstance(boardGameOption, new String[] { playerOneOption, playerTwoOption });
    }

    private static AbstractGameInstance buildGameInstance(BoardGameOption boardGameOption, String[] playerOptions) {
        GameBoard gameBoard;
        AbstractTurnEntity[] turnEntities = new AbstractTurnEntity[2];
        AbstractGameInstance gameInstance = null;

        if (boardGameOption.getShortName().equals("reversi")) {
            gameBoard = new GameBoard(8);

            for (int i = 0; i < playerOptions.length; i++) {
                if (playerOptions[i].equals("Player"))
                    turnEntities[i] = new ReversiTurnEntityPlayer();
                else if(playerOptions[i].equals("AI"))
//                    turnEntities[i] = new ReversiTurnEntityAI();
                    turnEntities[i] = new ReversiAi();
            }

            gameInstance = new ReversiGameInstance(gameBoard, turnEntities[0], turnEntities[1]);
        }
        else if (boardGameOption.getShortName().equals("tttoe")) {
            gameBoard = new GameBoard(3);

            for (int i = 0; i < playerOptions.length; i++) {
                if (playerOptions[i].equals("Player"))
                    turnEntities[i] = new TTToeTurnEntityPlayer();
                else if(playerOptions[i].equals("AI"))
                    turnEntities[i] = new TTToeAdvancedAI();
            }

            gameInstance = new TTToeGameInstance(gameBoard, turnEntities[0], turnEntities[1]);
        }

        return gameInstance;
    }
}
