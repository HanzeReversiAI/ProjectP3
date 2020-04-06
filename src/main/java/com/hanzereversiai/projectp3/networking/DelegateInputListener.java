package com.hanzereversiai.projectp3.networking;

import java.util.ArrayList;

public class DelegateInputListener implements InputListener {

    private DelegateInputListenerListener move_inputListener;

    public DelegateInputListener(Network network) {
        network.Subscribe(this::handleInput);

        move_inputListener = new DelegateInputListenerListener();
    }

    @Override
    public void handleInput(String input) {
        if (input.startsWith("SVR GAME MOVE")) {
            move_inputListener.handleInput(input);
        }
    }

    public void SUBSCRIBE_MOVE(InputListener inputListener) {
        move_inputListener.subscribe(inputListener);
    }

    class DelegateInputListenerListener implements InputListener {
        private ArrayList<InputListener> inputListeners;

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
