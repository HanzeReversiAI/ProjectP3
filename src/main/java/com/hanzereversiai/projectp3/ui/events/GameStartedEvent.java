package com.hanzereversiai.projectp3.ui.events;

import com.hanzereversiai.projectp3.ui.BoardGameOption;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class GameStartedEvent extends Event {
    public static final EventType<GameStartedEvent> GAME_STARTED_EVENT_EVENT_TYPE =
            new EventType<>(Event.ANY, "GAME_STARTED_EVENT_EVENT_TYPE");
    private BoardGameOption boardGameOption;
    private String playerOneOption;
    private String playerTwoOption;

    public GameStartedEvent(Object source, EventTarget target, BoardGameOption boardGameOption,
                            String playerOneOption, String playerTwoOption) {
        super(source, target, GAME_STARTED_EVENT_EVENT_TYPE);

        this.boardGameOption = boardGameOption;
        this.playerOneOption = playerOneOption;
        this.playerTwoOption = playerTwoOption;
    }

    public BoardGameOption getBoardGameOption() {
        return boardGameOption;
    }

    public String getPlayerOneOption() {
        return playerOneOption;
    }

    public String getPlayerTwoOption() {
        return playerTwoOption;
    }
}
