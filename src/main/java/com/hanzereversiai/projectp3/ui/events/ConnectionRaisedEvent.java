package com.hanzereversiai.projectp3.ui.events;

import com.hanzereversiai.projectp3.networking.Network;
import com.hanzereversiai.projectp3.ui.ConnectionPanel;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class ConnectionRaisedEvent extends Event {
    public static final EventType<ConnectionRaisedEvent> CONNECTION_RAISED_EVENT_TYPE =
            new EventType<>(Event.ANY, "CONNECTION_RAISED_EVENT_TYPE");
    private ConnectionPanel connectionPanel;
    private Network network;

    public ConnectionRaisedEvent(Object source, EventTarget target, Network network) {
        super(source, target, CONNECTION_RAISED_EVENT_TYPE);

        this.connectionPanel = (ConnectionPanel)target;
        this.network = network;
    }

    public ConnectionPanel getConnectionPanel() {
        return connectionPanel;
    }

    public Network getNetwork() {
        return network;
    }
}
