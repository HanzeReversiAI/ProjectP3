package com.hanzereversiai.projectp3.ui.events;

import com.hanzereversiai.projectp3.networking.Network;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class ConnectionRaisedEvent extends Event {
    public static final EventType<ConnectionRaisedEvent> CONNECTION_RAISED_EVENT_EVENT_TYPE =
            new EventType<>(Event.ANY, "CONNECTION_RAISED_EVENT_EVENT_TYPE");
    private Network network;
    private String username;

    public ConnectionRaisedEvent(Object source, EventTarget target, Network network, String username) {
        super(source, target, CONNECTION_RAISED_EVENT_EVENT_TYPE);

        this.network = network;
        this.username = username;
    }

    public Network getNetwork() {
        return network;
    }

    public String getUsername() {
        return username;
    }
}
