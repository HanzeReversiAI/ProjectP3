package com.hanzereversiai.projectp3.networking;

public enum Command {
    LOGIN,
    LOGOUT,
    GET_GAMELIST,
    GET_PLAYERLIST,
    SUBSCRIBE,
    MOVE,
    FORFEIT,
    CHALLENGE,
    ACCEPT_CHALLENGE,
    HELP,
    HELP_COMMAND,
    ;

    public static void sendCommand(Connection connection, Command command) {
        sendCommand(connection, command, "");
    }

    public static void sendCommand(Connection connection, Command command, String argument) {
        System.out.println("DEBUG_SEND: " + command + " Arg: " + argument);
        switch (command) {
            case LOGIN:
                connection.send("login " + argument);
                break;
            case LOGOUT:
                connection.send("logout");
                break;
            case GET_GAMELIST:
                connection.send("get gamelist");
                break;
            case GET_PLAYERLIST:
                connection.send("get playerlist");
                break;
            case SUBSCRIBE:
                connection.send("subscribe " + argument);
                break;
            case MOVE:
                connection.send("move " + argument);
                break;
            case FORFEIT:
                connection.send("forfeit");
                break;
            case CHALLENGE:
                connection.send("challenge " + argument);
                break;
            case ACCEPT_CHALLENGE:
                connection.send("challenge accept " + argument);
                break;
            case HELP:
                connection.send("help");
                break;
            case HELP_COMMAND:
                connection.send("help" + argument);
                break;
        }
    }
}
