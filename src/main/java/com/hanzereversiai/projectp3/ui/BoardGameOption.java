package com.hanzereversiai.projectp3.ui;

import javafx.scene.image.Image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BoardGameOption {
    private String name;
    private String shortName;

    public BoardGameOption(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public Image getImage() {
        return new Image(BoardGameOption.class.getResource("/" + shortName + "-example.png").toExternalForm());
    }

    public String getDescription() {
        try {
            return Files.readString(Paths.get(
                    BoardGameOption.class.getResource("/" + shortName + "-description.txt").getPath().substring(1)));
        }
        catch (IOException e) {
            return "Could not get description.";
        }
    }
}
