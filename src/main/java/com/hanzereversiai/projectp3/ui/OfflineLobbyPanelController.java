package com.hanzereversiai.projectp3.ui;

import com.hanzereversiai.projectp3.GameFactory;
import com.thowv.javafxgridgameboard.AbstractGameInstance;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

public class OfflineLobbyPanelController {
    public ImageView gameBoardImageView;
    public TextArea gameInformationTextArea;
    public ComboBox<BoardGameOption> boardGameComboBox;
    public ComboBox<String> playerOneComboBox;
    public ComboBox<String> playerTwoComboBox;
    public Button startButton;

    @FXML
    public void initialize() {
        // Board game combo box
        ObservableList<BoardGameOption> boardGameOptionsList = FXCollections.observableArrayList(
                new BoardGameOption("Reversi", "reversi"),
                new BoardGameOption("Tic Tac Toe", "tttoe")
        );

        boardGameComboBox.getItems().addAll(boardGameOptionsList);
        boardGameComboBox.valueProperty().addListener(this::onBoardGameComboBoxValueChanged);
        boardGameComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(BoardGameOption object) {
                return object.getName();
            }

            @Override
            public BoardGameOption fromString(String string) {
                return boardGameComboBox.getItems().stream().filter(ap ->
                        ap.getName().equals(string)).findFirst().orElse(null);
            }
        });

        // Player one combo box
        playerOneComboBox.getItems().addAll("Player", "AI");
        playerOneComboBox.getSelectionModel().selectFirst();

        // Player two combo box
        playerTwoComboBox.getItems().addAll("Player", "AI");
        playerTwoComboBox.getSelectionModel().selectLast();

        // Finalization
        boardGameComboBox.getSelectionModel().selectFirst();
    }

    private void onBoardGameComboBoxValueChanged(ObservableValue<? extends BoardGameOption> observableValue,
                                                 BoardGameOption oldVal, BoardGameOption newVal) {
        gameBoardImageView.setImage(newVal.getImage());
        gameInformationTextArea.setText(newVal.getDescription());
    }

    public void onStartButtonActivated(ActionEvent actionEvent) {
        FXMLLoader gamePanelLoader = UIHelper.switchScene(actionEvent, "game-panel");

        AbstractGameInstance gameInstance = GameFactory.buildGameInstance(boardGameComboBox.getValue(),
                playerOneComboBox.getValue(), playerTwoComboBox.getValue());

        // Set the game board in the panel
        GamePanelController gamePanelController = gamePanelLoader != null ? gamePanelLoader.getController() : null;
        if (gamePanelController != null)
            ((GamePanelController)gamePanelLoader.getController()).setGameBoard(gameInstance.getGameBoard());

        Platform.runLater(gameInstance::start);
    }
}
