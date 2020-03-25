package com.hanzereversiai.projectp3;

import com.hanzereversiai.projectp3.networking.Network;

public class AppLauncher {
    // Why we need a separate launcher: https://stackoverflow.com/questions/52653836/maven-shade-javafx-runtime-components-are-missing
    public static void main(String[] args) {
        App.main(args);
    }
}
