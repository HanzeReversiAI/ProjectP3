package com.hanzereversiai.projectp3.networking;

import com.hanzereversiai.projectp3.GameFactory;
import com.hanzereversiai.projectp3.ui.BoardGameOption;
import com.hanzereversiai.projectp3.ui.GamePanelController;
import com.hanzereversiai.projectp3.ui.UIHelper;
import com.thowv.javafxgridgameboard.AbstractGameInstance;
import com.thowv.javafxgridgameboard.AbstractTurnEntity;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * NetworkHandler is used to update the local game about events happening from the network.
 */
public class NetworkHandler {
    // REGEX Patterns
    private final static Pattern gameTypePattern = Pattern.compile("GAMETYPE: \"(.*?)\"");
    private final static Pattern opponentPattern = Pattern.compile("OPPONENT: \"(.*?)\"");
    private final static Pattern startingPlayerPattern = Pattern.compile("PLAYERTOMOVE: \"(.*?)\"");
    private final static Pattern playerPattern = Pattern.compile("PLAYER: \"(.*?)\",");
    private final static Pattern movePattern = Pattern.compile("MOVE: \"(.*?)\",");

    private Network network;
    private NetworkedGameInstance gameInstance;
    private Parent rootUIObject; // UI Root object needed to be able to switch scenes

    private boolean localPlayerDidLastMove; // Used to keep track of a player passing

    /**
     * Constructor
     * @param network A Network instance
     */
    public NetworkHandler(Network network) {
        this.network = network;
        subscribeAll(network.getDelegateInputListener());
    }

    /**
     * Handle an incoming match
     *
     * Sets the correct scene based on a tracked root JavaFX scene.
     * Then sets up a gameInstance, and hands it over to UI.
     * @param input SVR GAME MATCH {GAMETYPE: "<gameType>", PLAYERTOMOVE: "<startingPlayer>", OPPONENT: "<opponent>"}
     */
    public void handleMatch(String input) {
        if (rootUIObject != null) {
            BoardGameOption boardGameOption;
            String opponentName;

            // Check what game type we are playing
            Matcher matcher = gameTypePattern.matcher(input);

            if (!matcher.find())
                throw new IllegalArgumentException("No game type provided"); // We can't start a game without knowing which game to start

            if (matcher.group(1).equals("Tic-tac-toe"))
                boardGameOption = new BoardGameOption("Tic-tac-toe", "tttoe");
            else if (matcher.group(1).equals("Reversi"))
                boardGameOption = new BoardGameOption("Reversi", "reversi");
            else
                throw new IllegalArgumentException("Unsupported Game: " + matcher.group(1)); // We can't start a game we don't support

            // Check who our opponent is.
            matcher = opponentPattern.matcher(input);

            if (!matcher.find())
                throw new IllegalArgumentException("No opponent provided"); // We can't start a game without an opponent

            opponentName = matcher.group(1);

            // Check which player is first
            matcher = startingPlayerPattern.matcher(input);

            if (!matcher.find())
                throw new IllegalArgumentException("No starting player provided"); // We can't start a game without a starting player

            String playerOne, playerTwo;

            if (matcher.group(1).equals(NetworkSingleton.getNetworkInstance().getUsername())) {
                playerOne = "Player";
                playerTwo = "Network-" + opponentName;
            }
            else {
                playerOne = "Network-" + opponentName;
                playerTwo = "Player";
            }

            // Initialize the gameInstance
            AbstractGameInstance abstractGameInstance = GameFactory.buildNetworkedGameInstance(
                    boardGameOption, playerOne, playerTwo, network.getAiDepthAmount());

            // Keep track of it locally
            this.gameInstance = (NetworkedGameInstance) abstractGameInstance;

            // Hand it over to the UI
            Platform.runLater(() -> {
                FXMLLoader gamePanelLoader = UIHelper.switchScene(rootUIObject.getScene(), "game-panel");

                // Set the game board in the panel
                GamePanelController gamePanelController = gamePanelLoader != null ? gamePanelLoader.getController() : null;
                if (gamePanelController != null)
                    ((GamePanelController)gamePanelLoader.getController()).setGameInstance(abstractGameInstance);
            });
        } else throw new IllegalStateException(); // We can't start a game without a UI root object
    }

    /**
     * Handle getting a turn from Network
     *
     * Because we can also know it is out turn when an opponent makes a move, this is only used when the opponent passes.
     * @param input SVR GAME YOURTURN {TURNMESSAGE: ""}
     */
    public synchronized void handleTurn(String input) {
        if (gameInstance != null) {
            if (gameInstance.getCurrentTurnEntity() instanceof NetworkTurnEntity) {
                // If the local player did the last turn, but the server gives us another turn it must mean our opponent passed.
                if (localPlayerDidLastMove) {
                    System.out.println("NETWORK: OPPONENT PASSED");
                    gameInstance.passTurn();
                    localPlayerDidLastMove = false;
                }
            }
        } else throw new IllegalStateException();
    }

    /**
     * Handle a move coming from the network
     *
     * The server sends moves from both players back, so we also get our own moves back.
     * @param input SVR GAME MOVE {PLAYER: "<player>", DETAILS: "", MOVE: "<move>"}
     */
    public synchronized void handleMove(String input) {
        if (gameInstance != null) {
            if (gameInstance.getCurrentTurnEntity() instanceof NetworkTurnEntity) {
                localPlayerDidLastMove = false;
                int move;

                // Check which player is making a move
                Matcher matcher = playerPattern.matcher(input);
                if (!matcher.find())
                    throw new IllegalArgumentException("No player that makes the move provided");

                // Check if the move is meant for this player by looking at the username
                String player = matcher.group(1);

                // If it is not, it means we did the last move, we can than skip the rest.
                if (player.equals(NetworkSingleton.getNetworkInstance().getUsername())) {
                    localPlayerDidLastMove = true;
                    return;
                }

                // Check which move is being made.
                matcher = movePattern.matcher(input);
                if (!matcher.find())
                    throw new IllegalArgumentException();

                move = Integer.parseInt(matcher.group(1));

                // Convert the move number (1 dimensional int) to our Gameboard coordinates
                int width = gameInstance.getGameBoard().getSize();
                int x = move % width;
                int y = move / width;

                // Tell the gameInstance to do the move.
                gameInstance.doTurnFromNetwork(x, y);
            }
        } else throw new IllegalStateException();
    }

    /**
     * Handle getting a win from the server
     * @param input SVR GAME WIN {PLAYERONESCORE: "<score>", PLAYERTWOSCORE: "<score></score>", COMMENT: "<comment>>"}
     */
    public void handleWin(String input) {
        gameInstance.end(getLocalTurnEntity(), getNonLocalTurnEntity());
        this.gameInstance = null;
    }

    /**
     * Handle getting a loss from the server
     * @param input SVR GAME LOSS {PLAYERONESCORE: "<score>", PLAYERTWOSCORE: "<score></score>", COMMENT: "<comment>>"}
     */
    public void handleLoss(String input) {
        gameInstance.end(getNonLocalTurnEntity(), getLocalTurnEntity());
        this.gameInstance = null;
    }

    /**
     * Handle getting a draw from the server
     * @param input SVR GAME DRAW {PLAYERONESCORE: "<score>", PLAYERTWOSCORE: "<score></score>", COMMENT: "<comment>>"}
     */
    public void handleDraw(String input) {
        gameInstance.end(new AbstractTurnEntity[]{getLocalTurnEntity(), getNonLocalTurnEntity()});
        this.gameInstance = null;
    }

    private AbstractTurnEntity getLocalTurnEntity() {
        for (AbstractTurnEntity entity : gameInstance.getTurnEntities()) {
            if (!(entity instanceof NetworkTurnEntity)) return entity;
        }
        throw new IllegalStateException();
    }

    private AbstractTurnEntity getNonLocalTurnEntity() {
        for (AbstractTurnEntity entity : gameInstance.getTurnEntities()) {
            if ((entity instanceof NetworkTurnEntity)) return entity;
        }
        throw new IllegalStateException();
    }

    /**
     * Subscribe to inputs from the network
     * @param delegateInputListener DelegateInputListener instance to subscribe to.
     */
    public void subscribeAll(DelegateInputListener delegateInputListener) {
        delegateInputListener.SUBSCRIBE_MATCH(this::handleMatch, this.hashCode());
        delegateInputListener.SUBSCRIBE_MOVE(this::handleMove, this.hashCode());
        delegateInputListener.SUBSCRIBE_YOURTURN(this::handleTurn, this.hashCode());
        delegateInputListener.SUBSCRIBE_WIN(this::handleWin, this.hashCode());
        delegateInputListener.SUBSCRIBE_LOSS(this::handleLoss, this.hashCode());
        delegateInputListener.SUBSCRIBE_DRAW(this::handleDraw, this.hashCode());
    }
    /**
     * Unsubscribe to inputs from the network
     * @param delegateInputListener DelegateInputListener instance to unsubscribe from.
     */
    public void unsubscribeAll(DelegateInputListener delegateInputListener) {
        delegateInputListener.UNSUBSCRIBE_MATCH(this.hashCode());
        delegateInputListener.UNSUBSCRIBE_MOVE(this.hashCode());
        delegateInputListener.UNSUBSCRIBE_YOURTURN(this.hashCode());
        delegateInputListener.UNSUBSCRIBE_WIN(this.hashCode());
        delegateInputListener.UNSUBSCRIBE_LOSS(this.hashCode());
        delegateInputListener.UNSUBSCRIBE_DRAW(this.hashCode());
    }

    /**
     * Gets the NetworkGameInstance that should be updated from the NetworkHandler. This should be the active game.
     * @return The NetworkGameInstance kept track of by the NetworkHandler
     */
    public NetworkedGameInstance getNetworkedGameInstance() {
        return gameInstance;
    }
    /**
     * Sets the NetworkGameInstance that should be updated from the NetworkHandler. This should be the active game.
     * @param networkedGameInstance The NetworkGameInstance that should be kept track of by the NetworkHandler.
     */
    public void setNetworkedGameInstance(NetworkedGameInstance networkedGameInstance) {
        this.gameInstance = networkedGameInstance;
    }

    /**
     * Sets the root Java FX UI object that can be used to switch scenes.
     * @param rootUIObject Java FX Parent
     */
    public void setRootUIObject(Parent rootUIObject) {
        this.rootUIObject = rootUIObject;
    }
}
