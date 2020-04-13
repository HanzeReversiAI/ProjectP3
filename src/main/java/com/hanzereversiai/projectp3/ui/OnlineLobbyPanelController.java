package com.hanzereversiai.projectp3.ui;

import com.hanzereversiai.projectp3.networking.Command;
import com.hanzereversiai.projectp3.networking.Network;
import com.hanzereversiai.projectp3.networking.NetworkSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;

public class OnlineLobbyPanelController {


    public SplitPane lobbyPanelRoot;
    public VBox playerList;
    public VBox subscriptionList;
    public Button refreshPlayerListButton;

    @FXML
    public void initialize() {
        playerList.getChildren().clear();
        subscriptionList.getChildren().clear();

        Network network = NetworkSingleton.getNetworkInstance();

        network.getNetworkHandler().setRootUIObject(lobbyPanelRoot);
        network.getOnlineLobbyPanelNetworkHandler().setOnlineLobbyPanelController(this);

        network.sendCommand(Command.GET_GAMELIST);
        network.sendCommand(Command.GET_PLAYERLIST);
    }

    public void onRefreshPlayerListButtonActivated(ActionEvent actionEvent) {
        NetworkSingleton.getNetworkInstance().sendCommand(Command.GET_PLAYERLIST);
    }





}
