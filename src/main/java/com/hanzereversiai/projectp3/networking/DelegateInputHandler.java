package com.hanzereversiai.projectp3.networking;

import java.util.HashMap;

/**
 * Delegator for InputHandler. Used to subscribe to specific network commands.
 *
 * By seperating network commands early on we avoid a lot of checking later on.
 *
 * @author Mike
 */
public class DelegateInputHandler implements InputListener {

    private DelegateInputListener move_inputListener;
    private DelegateInputListener playerlist_inputListener;
    private DelegateInputListener gamelist_inputListener;
    private DelegateInputListener challenge_inputListener;
    private DelegateInputListener gameMatch_inputListener;
    private DelegateInputListener gameYourTurn_inputListener;
    private DelegateInputListener gameWIN_inputListener;
    private DelegateInputListener gameLOSS_inputListener;
    private DelegateInputListener gameDRAW_inputListener;

    /**
     * Constructor
     * @param network Network instance that the DelegateInputHandler can subscribe to.
     */
    public DelegateInputHandler(Network network) {
        network.subscribe(this);

        move_inputListener = new DelegateInputListener();
        playerlist_inputListener = new DelegateInputListener();
        gamelist_inputListener = new DelegateInputListener();
        challenge_inputListener = new DelegateInputListener();
        gameMatch_inputListener = new DelegateInputListener();
        gameYourTurn_inputListener = new DelegateInputListener();
        gameWIN_inputListener = new DelegateInputListener();
        gameLOSS_inputListener = new DelegateInputListener();
        gameDRAW_inputListener = new DelegateInputListener();
    }

    /**
     * Gets a command and checks it, then sends it on to the right inputListener(s)
     * @param input The network input
     */
    @Override
    public void handleInput(String input) {
        if (input.startsWith("SVR GAME MOVE")) {
            move_inputListener.handleInput(input);
        } else if (input.startsWith("SVR PLAYERLIST")) {
            playerlist_inputListener.handleInput(input);
        } else if (input.startsWith("SVR GAMELIST")) {
            gamelist_inputListener.handleInput(input);
        } else if (input.startsWith("SVR GAME CHALLENGE")) {
            challenge_inputListener.handleInput(input);
        } else if (input.startsWith("SVR GAME MATCH")) {
            gameMatch_inputListener.handleInput(input);
        } else if (input.startsWith("SVR GAME YOURTURN")) {
            gameYourTurn_inputListener.handleInput(input);
        } else if (input.startsWith("SVR GAME WIN")) {
            gameWIN_inputListener.handleInput(input);
        } else if (input.startsWith("SVR GAME LOSS")) {
            gameLOSS_inputListener.handleInput(input);
        } else if (input.startsWith("SVR GAME DRAW")) {
            gameDRAW_inputListener.handleInput(input);
        }
    }

    //region (UN)SUBSCRIBE METHODS

    /**
     * Subscribe to the MOVE command
     * @param inputListener The InputListener to notify of an incoming command.
     * @param hash The Hash of the subscribed class, used to keep track of subscriptions
     */
    public void SUBSCRIBE_MOVE(InputListener inputListener, int hash) {
        move_inputListener.subscribe(inputListener, hash);
    }
    /**
     * Unsubscribe to the MOVE command
     * @param hash The hash of the class that is unsubscribing.
     */
    public void UNSUBSCRIBE_MOVE(int hash) {
        move_inputListener.unsubscribe(hash);
    }

    /**
     * Subscribe to the PLAYERLIST command
     * @param inputListener The InputListener to notify of an incoming command.
     * @param hash The Hash of the subscribed class, used to keep track of subscriptions
     */
    public void SUBSCRIBE_PLAYERLIST(InputListener inputListener, int hash) {
        playerlist_inputListener.subscribe(inputListener, hash);
    }
    /**
     * Unsubscribe to the PLAYERLIST command
     * @param hash The hash of the class that is unsubscribing.
     */
    public void UNSUBSCRIBE_PLAYERLIST(int hash) {
        playerlist_inputListener.unsubscribe(hash);
    }

    /**
     * Subscribe to the GAMELIST command
     * @param inputListener The InputListener to notify of an incoming command.
     * @param hash The Hash of the subscribed class, used to keep track of subscriptions
     */
    public void SUBSCRIBE_GAMELIST(InputListener inputListener, int hash) {
        gamelist_inputListener.subscribe(inputListener, hash);
    }
    /**
     * Unsubscribe to the GAMELIST command
     * @param hash The hash of the class that is unsubscribing.
     */
    public void UNSUBSCRIBE_GAMELIST(int hash) {
        gamelist_inputListener.unsubscribe(hash);
    }

    /**
     * Subscribe to the CHALLENGE command
     * @param inputListener The InputListener to notify of an incoming command.
     * @param hash The Hash of the subscribed class, used to keep track of subscriptions
     */
    public void SUBSCRIBE_CHALLENGE(InputListener inputListener, int hash) {
        challenge_inputListener.subscribe(inputListener, hash);
    }
    /**
     * Unsubscribe to the CHALLENGE command
     * @param hash The hash of the class that is unsubscribing.
     */
    public void UNSUBSCRIBE_CHALLENGE(int hash) {
        challenge_inputListener.unsubscribe(hash);
    }

    /**
     * Subscribe to the MATCH command
     * @param inputListener The InputListener to notify of an incoming command.
     * @param hash The Hash of the subscribed class, used to keep track of subscriptions
     */
    public void SUBSCRIBE_MATCH(InputListener inputListener, int hash) {
        gameMatch_inputListener.subscribe(inputListener, hash);
    }
    /**
     * Unsubscribe to the MATCH command
     * @param hash The hash of the class that is unsubscribing.
     */
    public void UNSUBSCRIBE_MATCH(int hash) {
        gameMatch_inputListener.unsubscribe(hash);
    }

    /**
     * Subscribe to the YOURTURN command
     * @param inputListener The InputListener to notify of an incoming command.
     * @param hash The Hash of the subscribed class, used to keep track of subscriptions
     */
    public void SUBSCRIBE_YOURTURN(InputListener inputListener, int hash) {
        gameYourTurn_inputListener.subscribe(inputListener, hash);
    }
    /**
     * Unsubscribe to the YOURTURN command
     * @param hash The hash of the class that is unsubscribing.
     */
    public void UNSUBSCRIBE_YOURTURN(int hash) {
        gameYourTurn_inputListener.unsubscribe(hash);
    }

    /**
     * Subscribe to the WIN command
     * @param inputListener The InputListener to notify of an incoming command.
     * @param hash The Hash of the subscribed class, used to keep track of subscriptions
     */
    public void SUBSCRIBE_WIN(InputListener inputListener, int hash) {
        gameWIN_inputListener.subscribe(inputListener, hash);
    }
    /**
     * Unsubscribe to the WIN command
     * @param hash The hash of the class that is unsubscribing.
     */
    public void UNSUBSCRIBE_WIN(int hash) {
        gameWIN_inputListener.unsubscribe(hash);
    }

    /**
     * Subscribe to the LOSS command
     * @param inputListener The InputListener to notify of an incoming command.
     * @param hash The Hash of the subscribed class, used to keep track of subscriptions
     */
    public void SUBSCRIBE_LOSS(InputListener inputListener, int hash) {
        gameLOSS_inputListener.subscribe(inputListener, hash);
    }
    /**
     * Unsubscribe to the LOSS command
     * @param hash The hash of the class that is unsubscribing.
     */
    public void UNSUBSCRIBE_LOSS(int hash) {
        gameLOSS_inputListener.unsubscribe(hash);
    }

    /**
     * Subscribe to the DRAW command
     * @param inputListener The InputListener to notify of an incoming command.
     * @param hash The Hash of the subscribed class, used to keep track of subscriptions
     */
    public void SUBSCRIBE_DRAW(InputListener inputListener, int hash) {
        gameDRAW_inputListener.subscribe(inputListener, hash);
    }
    /**
     * Unsubscribe to the DRAW command
     * @param hash The hash of the class that is unsubscribing.
     */
    public void UNSUBSCRIBE_DRAW(int hash) {
        gameDRAW_inputListener.unsubscribe(hash);
    }

    //endregion (UN)SUBSCRIBE METHODS

    /**
     * Class used to notify all subscribed InputListeners
     */
    class DelegateInputListener implements InputListener {
        private HashMap<Integer, InputListener> inputListeners;

        DelegateInputListener() {
            inputListeners = new HashMap<>();
        }

        @Override
        public void handleInput(String input) {
            for (InputListener inputListener: inputListeners.values()) {
                inputListener.handleInput(input);
            }
        }

        public void subscribe(InputListener inputListener, int hash) {
            inputListeners.put(hash, inputListener);
        }

        public void unsubscribe(int hash) {
            inputListeners.remove(hash);
        }
    }
}
