package com.hanzereversiai.projectp3.ui;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.StringConverter;

public class OfflineLobbyContentSkin extends SkinBase<OfflineLobbyContent> {

    private ImageView imageView;
    private Label infoLabel;

    public OfflineLobbyContentSkin(OfflineLobbyContent offlineLobbyContent) {
        super(offlineLobbyContent);

        HBox rootHBox = new HBox();
        rootHBox.setSpacing(20);
        getChildren().add(rootHBox);

        // Options box
        StackPane optionsStackPane = new StackPane();
        HBox.setHgrow(optionsStackPane, Priority.ALWAYS);
        rootHBox.getChildren().add(optionsStackPane);

        GridPane optionsGridPane = new GridPane();
        optionsGridPane.getStyleClass().addAll("lightly-shaded");
        optionsGridPane.setHgap(5);
        optionsGridPane.setVgap(10);
        optionsGridPane.setAlignment(Pos.CENTER);
        optionsStackPane.getChildren().add(optionsGridPane);

        // Board game option
        Label boardGameLabel = new Label("Board game:");
        boardGameLabel.getStyleClass().add("light-text");
        optionsGridPane.add(boardGameLabel, 0, 0);

        ObservableList<BoardGameOption> boardGameOptionsList = FXCollections.observableArrayList(
                new BoardGameOption("Reversi", "reversi"),
                new BoardGameOption("Tic Tac Toe", "tttoe")
        );
        ComboBox<BoardGameOption> boardGameComboBox = new ComboBox<>(boardGameOptionsList);
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
        optionsGridPane.add(boardGameComboBox, 1, 0);

        // Player one option
        Label playerOneLabel = new Label("Player one type:");
        playerOneLabel.getStyleClass().add("light-text");
        optionsGridPane.add(playerOneLabel, 0, 1);

        String[] playerOptionsList = {"Player", "AI"};
        ComboBox<String> playerOneComboBox = new ComboBox<>(FXCollections.observableArrayList(playerOptionsList));
        playerOneComboBox.getSelectionModel().selectFirst();
        optionsGridPane.add(playerOneComboBox, 1, 1);

        // Player two option
        Label playerTwoLabel = new Label("Player two type:");
        playerTwoLabel.getStyleClass().add("light-text");
        optionsGridPane.add(playerTwoLabel, 0, 2);

        ComboBox<String> playerTwoComboBox = new ComboBox<>(FXCollections.observableArrayList(playerOptionsList));
        playerTwoComboBox.getSelectionModel().selectLast();
        optionsGridPane.add(playerTwoComboBox, 1, 2);

        // Play button
        Button startButton = new Button("Start game!");
        startButton.setOnAction(e -> offlineLobbyContent.getOfflineLobbyContentBehavior().onStartButtonActivated(
                boardGameComboBox.getValue(), playerOneComboBox.getValue(), playerTwoComboBox.getValue()));
        GridPane.setHalignment(startButton, HPos.CENTER);
        optionsGridPane.add(startButton, 0, 3, 2, 1);

        // Information box
        VBox infoVBox = new VBox();
        rootHBox.getChildren().add(infoVBox);

        imageView = new ImageView(new Image(OfflineLobbyContentSkin.class.getResource("/reversi-example.png").toExternalForm()));
        VBox.setVgrow(imageView, Priority.ALWAYS);
        infoVBox.getChildren().add(imageView);

        StackPane infoTextStackPane = new StackPane();
        infoTextStackPane.getStyleClass().addAll("info-text-stack-pane", "lightly-shaded");
        VBox.setVgrow(infoTextStackPane, Priority.ALWAYS);
        infoVBox.getChildren().add(infoTextStackPane);

        infoLabel = new Label("Game information right here...");
        infoLabel.getStyleClass().add("light-text");
        infoLabel.setWrapText(true);
        infoLabel.prefWidthProperty().bind(infoVBox.widthProperty());
        StackPane.setAlignment(infoLabel, Pos.TOP_CENTER);
        infoTextStackPane.getChildren().add(infoLabel);

        // Finalization
        boardGameComboBox.getSelectionModel().selectFirst();
    }

    private void onBoardGameComboBoxValueChanged(ObservableValue<? extends BoardGameOption> observableValue,
                                                 BoardGameOption oldVal, BoardGameOption newVal) {
        imageView.setImage(newVal.getImage());
        infoLabel.setText(newVal.getDescription());
    }
}
