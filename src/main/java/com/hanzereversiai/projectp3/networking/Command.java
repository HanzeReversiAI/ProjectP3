package com.hanzereversiai.projectp3.networking;

public enum Command {
    LOGIN,
    LOGOUT,
    ;

    public static void sendCommand(Connection connection, Command command) {
        sendCommand(connection, command, "");
    }

    public static void sendCommand(Connection connection, Command command, String argument) {
        switch (command) {
            case LOGIN:
                connection.send("login" + argument);
                break;
            case LOGOUT:
                connection.send("logout");
                break;
        }


    }
}

