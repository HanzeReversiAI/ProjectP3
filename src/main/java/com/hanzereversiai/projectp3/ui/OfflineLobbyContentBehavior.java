package com.hanzereversiai.projectp3.ui;

import com.hanzereversiai.projectp3.ui.events.GameStartedEvent;

public class OfflineLobbyContentBehavior {
    private OfflineLobbyContent offlineLobbyContent;
    private LobbyPanel lobbyPanel;

    public OfflineLobbyContentBehavior(OfflineLobbyContent offlineLobbyContent, LobbyPanel lobbyPanel) {
        this.offlineLobbyContent = offlineLobbyContent;
        this.lobbyPanel = lobbyPanel;
    }

    public void onStartButtonActivated(BoardGameOption boardGameOption, String playerOneOption, String playerTwoOption) {
        lobbyPanel.fireEvent(new GameStartedEvent(this, lobbyPanel, boardGameOption,
                playerOneOption, playerTwoOption));
    }
}
