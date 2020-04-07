package com.hanzereversiai.projectp3.networking;

import java.util.ArrayList;

public class DelegateInputListener implements InputListener {

    private DelegateInputListenerListener move_inputListener;
    private DelegateInputListenerListener playerlist_inputListener;
    private DelegateInputListenerListener gamelist_inputListener;
    private DelegateInputListenerListener challenge_inputListener;
    private DelegateInputListenerListener gameMatch_inputListener;
    private DelegateInputListenerListener gameYourTurn_inputListener;

    public DelegateInputListener(Network network) {
        network.Subscribe(this);

        move_inputListener = new DelegateInputListenerListener();
        playerlist_inputListener = new DelegateInputListenerListener();
        gamelist_inputListener = new DelegateInputListenerListener();
        challenge_inputListener = new DelegateInputListenerListener();
        gameMatch_inputListener = new DelegateInputListenerListener();
        gameYourTurn_inputListener = new DelegateInputListenerListener();
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
        }
    }

    public void SUBSCRIBE_MOVE(InputListener inputListener) {
        move_inputListener.subscribe(inputListener);
    }
    public void SUBSCRIBE_PLAYERLIST(InputListener inputListener) {
        playerlist_inputListener.subscribe(inputListener);
    }
    public void SUBSCRIBE_GAMELIST(InputListener inputListener) {
        gamelist_inputListener.subscribe(inputListener);
    }
    public void SUBSCRIBE_CHALLENGE(InputListener inputListener) {
        challenge_inputListener.subscribe(inputListener);
    }
    public void SUBSCRIBE_MATCH(InputListener inputListener) {
        gameMatch_inputListener.subscribe(inputListener);
    }
    public void SUBSCRIBE_YOURTURN(InputListener inputListener) {
        gameYourTurn_inputListener.subscribe(inputListener);
    }

    class DelegateInputListenerListener implements InputListener {
        private ArrayList<InputListener> inputListeners;

        DelegateInputListenerListener() {
            inputListeners = new ArrayList<>();
        }


        @Override
        public void handleInput(String input) {
            for (InputListener inputListener: inputListeners) {
                inputListener.handleInput(input);
            }
        }

        public void subscribe(InputListener inputListener) {
            inputListeners.add(inputListener);
        }
    }
}
