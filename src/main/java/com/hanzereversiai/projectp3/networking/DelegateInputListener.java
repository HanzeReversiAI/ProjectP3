package com.hanzereversiai.projectp3.networking;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

public class DelegateInputListener implements InputListener {

    private DelegateInputListenerListener move_inputListener;
    private DelegateInputListenerListener playerlist_inputListener;
    private DelegateInputListenerListener gamelist_inputListener;
    private DelegateInputListenerListener challenge_inputListener;
    private DelegateInputListenerListener gameMatch_inputListener;
    private DelegateInputListenerListener gameYourTurn_inputListener;
    private DelegateInputListenerListener gameWIN_inputListener;
    private DelegateInputListenerListener gameLOSS_inputListener;


    public DelegateInputListener(Network network) {
        network.subscribe(this);

        move_inputListener = new DelegateInputListenerListener();
        playerlist_inputListener = new DelegateInputListenerListener();
        gamelist_inputListener = new DelegateInputListenerListener();
        challenge_inputListener = new DelegateInputListenerListener();
        gameMatch_inputListener = new DelegateInputListenerListener();
        gameYourTurn_inputListener = new DelegateInputListenerListener();
        gameWIN_inputListener = new DelegateInputListenerListener();
        gameLOSS_inputListener = new DelegateInputListenerListener();
    }

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
        }
    }

    public void SUBSCRIBE_MOVE(InputListener inputListener, int hash) {
        move_inputListener.subscribe(inputListener, hash);
    }
    public void UNSUBSCRIBE_MOVE(int hash) {
        move_inputListener.unsubscribe(hash);
    }

    public void SUBSCRIBE_PLAYERLIST(InputListener inputListener, int hash) {
        playerlist_inputListener.subscribe(inputListener, hash);
    }
    public void UNSUBSCRIBE_PLAYERLIST(int hash) {
        playerlist_inputListener.unsubscribe(hash);
    }

    public void SUBSCRIBE_GAMELIST(InputListener inputListener, int hash) {
        gamelist_inputListener.subscribe(inputListener, hash);
    }
    public void UNSUBSCRIBE_GAMELIST(int hash) {
        gamelist_inputListener.unsubscribe(hash);
    }

    public void SUBSCRIBE_CHALLENGE(InputListener inputListener, int hash) {
        challenge_inputListener.subscribe(inputListener, hash);
    }
    public void UNSUBSCRIBE_CHALLENGE(int hash) {
        challenge_inputListener.unsubscribe(hash);
    }

    public void SUBSCRIBE_MATCH(InputListener inputListener, int hash) {
        gameMatch_inputListener.subscribe(inputListener, hash);
    }
    public void UNSUBSCRIBE_MATCH(int hash) {
        gameMatch_inputListener.unsubscribe(hash);
    }

    public void SUBSCRIBE_YOURTURN(InputListener inputListener, int hash) {
        gameYourTurn_inputListener.subscribe(inputListener, hash);
    }
    public void UNSUBSCRIBE_YOURTURN(int hash) {
        gameYourTurn_inputListener.unsubscribe(hash);
    }

    public void SUBSCRIBE_WIN(InputListener inputListener, int hash) {
        gameWIN_inputListener.subscribe(inputListener, hash);
    }
    public void UNSUBSCRIBE_WIN(int hash) {
        gameWIN_inputListener.unsubscribe(hash);
    }

    public void SUBSCRIBE_LOSS(InputListener inputListener, int hash) {
        gameLOSS_inputListener.subscribe(inputListener, hash);
    }
    public void UNSUBSCRIBE_LOSS(int hash) {
        gameLOSS_inputListener.unsubscribe(hash);
    }


    class DelegateInputListenerListener implements InputListener {
        private HashMap<Integer, InputListener> inputListeners;

        DelegateInputListenerListener() {
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
