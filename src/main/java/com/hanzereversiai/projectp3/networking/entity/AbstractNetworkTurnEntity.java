package com.hanzereversiai.projectp3.networking.entity;

import com.hanzereversiai.projectp3.networking.InputListener;
import com.hanzereversiai.projectp3.networking.Network;
import com.thowv.javafxgridgameboard.AbstractGameInstance;
import com.thowv.javafxgridgameboard.AbstractTurnEntity;
import com.thowv.javafxgridgameboard.GameBoardTile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractNetworkTurnEntity extends AbstractTurnEntity {

    private NetworkTurnListener networkTurnListener;
    private String username;

    public AbstractNetworkTurnEntity(Network network) {
        super(EntityType.AI);
        networkTurnListener = new NetworkTurnListener(this);
        network.getDelegateInputListener().SUBSCRIBE_MOVE(networkTurnListener);
        username = network.getUsername();
    }

    @Override
    public abstract void takeTurn(AbstractGameInstance abstractGameInstance);

    GameBoardTile getGameTile(AbstractGameInstance abstractGameInstance) {
        int width = abstractGameInstance.getGameBoard().getSize();
        int tileNumber = networkTurnListener.move;

        int y = tileNumber / width;
        int x = tileNumber % width;
        return abstractGameInstance.getGameBoard().getTile(x, y);
    }

    private class NetworkTurnListener implements InputListener {

        private final Pattern playerPattern = Pattern.compile("PLAYER: \"(.*?)\",");
        private final Pattern movePattern = Pattern.compile("MOVE: \"(.*?)\",");

        AbstractNetworkTurnEntity abstractNetworkTurnEntity;

        NetworkTurnListener(AbstractNetworkTurnEntity abstractNetworkTurnEntity) {
            this.abstractNetworkTurnEntity = abstractNetworkTurnEntity;
        }

        private int move;

        @Override
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

        public int getMove() {
            return move;
        }
    }

}
