package com.hanzereversiai.projectp3.ui;

import com.hanzereversiai.projectp3.GameFactory;
import com.hanzereversiai.projectp3.networking.NetworkSingleton;
import com.thowv.javafxgridgameboard.AbstractGameInstance;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

/**
 * @author Thomas
 */
public class OfflineLobbyPanelController {
    public ImageView gameBoardImageView;
    public TextArea gameInformationTextArea;
    public ComboBox<BoardGameOption> boardGameComboBox;
    public ComboBox<String> playerOneComboBox;
    public ComboBox<String> playerTwoComboBox;
    public Button startButton;
    public TextField aiDepthTextField;
    public Label errorLabel;

    @FXML
    public void initialize() {
        errorLabel.setText("");
        aiDepthTextField.setText(String.valueOf(NetworkSingleton.getNetworkInstance().getAiDepthAmount()));

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
        int amount;

        // Check for errors
        errorLabel.setText("");

        try {
            amount = Integer.parseInt(aiDepthTextField.getText());

            if (amount < 0) {
                errorLabel.setText("Value must be positive!");
                return;
            }

            if (amount != 0)
                NetworkSingleton.getNetworkInstance().setAiDepthAmount(amount);
        }
        catch (NumberFormatException e) {
            errorLabel.setText("Value must be a number!");
            return;
        }

        // Start game
        FXMLLoader gamePanelLoader = UIHelper.switchScene(actionEvent, "game-panel");

        AbstractGameInstance gameInstance = GameFactory.buildGameInstance(boardGameComboBox.getValue(),
                playerOneComboBox.getValue(), playerTwoComboBox.getValue(), amount);

        // Set the game board in the panel
        GamePanelController gamePanelController = gamePanelLoader != null ? gamePanelLoader.getController() : null;
        if (gamePanelController != null)
            ((GamePanelController)gamePanelLoader.getController()).setGameInstance(gameInstance);
    }
}
