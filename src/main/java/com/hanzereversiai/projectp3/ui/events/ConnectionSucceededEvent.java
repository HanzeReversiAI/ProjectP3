package com.hanzereversiai.projectp3.ui.events;

import com.hanzereversiai.projectp3.ui.ConnectionPanel;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class ConnectionSucceededEvent extends Event {
    public static final EventType<ConnectionSucceededEvent> CONNECTION_SUCCEEDED_EVENT_TYPE =
            new EventType<>(Event.ANY, "CONNECTION_SUCCEEDED_EVENT_TYPE");
    private ConnectionPanel connectionPanel;

    public ConnectionSucceededEvent(Object source, EventTarget target) {
        super(source, target, CONNECTION_SUCCEEDED_EVENT_TYPE);

        this.connectionPanel = (ConnectionPanel)target;
    }

    public ConnectionPanel getConnectionPanel() {
        return connectionPanel;
    }
}
