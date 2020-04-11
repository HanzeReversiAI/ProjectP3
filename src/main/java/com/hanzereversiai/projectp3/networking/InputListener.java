package com.hanzereversiai.projectp3.networking;

/**
 * Interface used to subscribe to a InputListener.
 *
 * This means that any class implementing this must contain a handleInput function.
 * Or provide a function accepting a single String.
 *
 */
public interface InputListener {
    void handleInput(String input);
}
