package com.hanzereversiai.projectp3.ui;

import com.thowv.javafxgridgameboard.GameBoard;
import javafx.scene.layout.BorderPane;

public class GamePanelController {
    public BorderPane borderPane;

    public void setGameBoard(GameBoard gameBoard) {
        borderPane.setCenter(gameBoard);
    }
}
