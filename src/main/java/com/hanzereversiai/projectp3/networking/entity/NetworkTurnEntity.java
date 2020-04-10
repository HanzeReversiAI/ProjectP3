package com.hanzereversiai.projectp3.networking.entity;

import com.hanzereversiai.projectp3.networking.NetworkSingleton;
import com.hanzereversiai.projectp3.networking.NetworkedGameInstance;
import com.thowv.javafxgridgameboard.AbstractGameInstance;
import com.thowv.javafxgridgameboard.AbstractTurnEntity;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiGameInstance;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkTurnEntity extends AbstractTurnEntity {

    private final static Pattern playerPattern = Pattern.compile("PLAYER: \"(.*?)\",");
    private final static Pattern movePattern = Pattern.compile("MOVE: \"(.*?)\",");

    private boolean didMove;

    public NetworkTurnEntity(String name) {
        super(EntityType.AI, name);
    }

    public void handleInput(String input, AbstractGameInstance abstractGameInstance) {
        Matcher matcher = playerPattern.matcher(input);
        int move;

        if (!matcher.find())
            throw new IllegalArgumentException();

        // Check if the move is meant for this player by looking at the username
        String player = matcher.group(1);

        if (player.equals(NetworkSingleton.getNetworkInstance().getUsername()))
            return;

        matcher = movePattern.matcher(input);
        if (!matcher.find())
            throw new IllegalArgumentException();

        move = Integer.parseInt(matcher.group(1));

        int width = abstractGameInstance.getGameBoard().getSize();
        int x = move % width;
        int y = move / width;

        if (abstractGameInstance instanceof NetworkedGameInstance) {
            didMove = true;
            ((NetworkedGameInstance) abstractGameInstance).doTurnFromNetwork(x, y);
        } else
            throw new IllegalStateException();
    }

    public void getTurnFromNetwork(AbstractGameInstance abstractGameInstance) {
        if (didMove)
            didMove = false;
        else {
            if (abstractGameInstance instanceof ReversiGameInstance)
                ((ReversiGameInstance) abstractGameInstance).passTurn();
            else
                abstractGameInstance.switchTurn();
        }
    }

    @Override
    public void takeTurn(AbstractGameInstance abstractGameInstance) { }
}
