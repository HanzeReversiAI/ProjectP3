package com.hanzereversiai.projectp3.networking;

import com.hanzereversiai.projectp3.GameFactory;
import com.hanzereversiai.projectp3.ui.BoardGameOption;
import com.hanzereversiai.projectp3.ui.GamePanelController;
import com.hanzereversiai.projectp3.ui.UIHelper;
import com.thowv.javafxgridgameboard.AbstractGameInstance;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventHandlerHelper {
    private final static Pattern listPattern = Pattern.compile("\"(.*?)\"[,\\]]");
    private final static Pattern gameTypePattern = Pattern.compile("GAMETYPE: \"(.*?)\"");
    private final static Pattern opponentPattern = Pattern.compile("OPPONENT: \"(.*?)\"");
    private final static Pattern startingPlayerPattern = Pattern.compile("PLAYERTOMOVE: \"(.*?)\"");

    public static void handleMatch(String input, Parent rootObject) {
        BoardGameOption boardGameOption;
        String opponentName;

        // Game type matching
        Matcher matcher = gameTypePattern.matcher(input);

        if (!matcher.find())
            return;

        if (matcher.group(1).equals("Tic-tac-toe"))
            boardGameOption = new BoardGameOption("Tic-tac-toe", "tttoe");
        else if (matcher.group(1).equals("Reversi"))
            boardGameOption = new BoardGameOption("Reversi", "reversi");
        else
            throw new IllegalArgumentException("Unsupported Game: " + matcher.group(1));

        // Opponent matching
        matcher = opponentPattern.matcher(input);

        if (!matcher.find())
            return;

        opponentName = matcher.group(1);

        // Stating player matching
        matcher = startingPlayerPattern.matcher(input);

        if (!matcher.find())
            return;

        String playerOne, playerTwo;

        if (matcher.group(1).equals(NetworkSingleton.getNetworkInstance().getUsername())) {
            playerOne = "Player";
            playerTwo = "Network-" + opponentName;
        }
        else {
            playerOne = "Network-" + opponentName;
            playerTwo = "Player";
        }

        Platform.runLater(() -> {
            FXMLLoader gamePanelLoader = UIHelper.switchScene(rootObject.getScene(), "game-panel");

            AbstractGameInstance gameInstance = GameFactory.buildNetworkedGameInstance(
                    boardGameOption, playerOne, playerTwo);

            // Set the game board in the panel
            GamePanelController gamePanelController = gamePanelLoader != null ? gamePanelLoader.getController() : null;
            if (gamePanelController != null)
                ((GamePanelController)gamePanelLoader.getController()).setGameInstance(gameInstance);
        });
    }
}
