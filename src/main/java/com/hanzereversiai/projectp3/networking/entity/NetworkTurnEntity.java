package com.hanzereversiai.projectp3.networking.entity;

import com.hanzereversiai.projectp3.networking.NetworkSingleton;
import com.hanzereversiai.projectp3.networking.NetworkedGameInstance;
import com.thowv.javafxgridgameboard.AbstractGameInstance;
import com.thowv.javafxgridgameboard.AbstractTurnEntity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkTurnEntity extends AbstractTurnEntity {

    private final static Pattern playerPattern = Pattern.compile("PLAYER: \"(.*?)\",");
    private final static Pattern movePattern = Pattern.compile("MOVE: \"(.*?)\",");
    private String username;

    public NetworkTurnEntity() {
        super(EntityType.AI);
        username = NetworkSingleton.getNetworkInstance().getUsername();
    }

    public void handleInput(String input, AbstractGameInstance abstractGameInstance) {
        Matcher m = playerPattern.matcher(input);
        int move = -1;

        System.out.println(input);

        if (m.find()) {
            String player = m.group(1);
            if (!player.equals(username)) {
                m = movePattern.matcher(input);
                if (m.find()) {
                    System.out.println("G: " + m.group(1));

                    move = Integer.parseInt(m.group(1));
                } else {
                    throw new IllegalArgumentException();
                }

                int width = abstractGameInstance.getGameBoard().getSize();

                int x = move % width;
                int y = move / width;

                if (abstractGameInstance instanceof NetworkedGameInstance) {
                    ((NetworkedGameInstance) abstractGameInstance).doTurnFromNetwork(x, y);
                } else {
                    throw new IllegalStateException();
                }
            }
        } else {
            throw new IllegalArgumentException();
        }


    }

    @Override
    public void takeTurn(AbstractGameInstance abstractGameInstance) {

    }
}
