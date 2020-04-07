package com.hanzereversiai.projectp3.ui;

import com.thowv.javafxgridgameboard.GameBoard;
import javafx.scene.layout.BorderPane;

public class GamePanelController {
    public BorderPane borderPane;
    private GameBoard gameBoard;

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        borderPane.setCenter(gameBoard);
    }
}
