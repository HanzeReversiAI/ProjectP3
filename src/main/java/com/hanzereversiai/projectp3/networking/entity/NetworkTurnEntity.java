package com.hanzereversiai.projectp3.networking.entity;

import com.hanzereversiai.projectp3.networking.InputListener;
import com.hanzereversiai.projectp3.networking.NetworkSingleton;
import com.thowv.javafxgridgameboard.AbstractGameInstance;
import com.thowv.javafxgridgameboard.AbstractTurnEntity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkTurnEntity extends AbstractTurnEntity implements InputListener {

    private final static Pattern playerPattern = Pattern.compile("PLAYER: \"(.*?)\",");
    private final static Pattern movePattern = Pattern.compile("MOVE: \"(.*?)\",");
    private String username;
    private int move;

    public NetworkTurnEntity() {
        super(EntityType.AI);
        username = NetworkSingleton.getNetworkInstance().getUsername();
    }

    @Override
    public void takeTurn(AbstractGameInstance abstractGameInstance) {
        int width = abstractGameInstance.getGameBoard().getSize();
        int tileNumber = move;

        int x = tileNumber % width;
        int y = tileNumber / width;

        abstractGameInstance.doTurn(x, y);
    }

    public void handleInput(String input) {
        Matcher m = playerPattern.matcher(input);
        if (m.find()) {
            move = Integer.parseInt(m.group(1));
            String player = m.group(1);
            if (!player.equals(username)) {
                m = movePattern.matcher(input);
                if (m.find()) {
                    move = Integer.parseInt(m.group(1));
                } else {
                    throw new IllegalArgumentException();
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

}
