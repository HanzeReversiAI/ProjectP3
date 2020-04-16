package com.hanzereversiai.projectp3.ui;

import javafx.scene.image.Image;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * @author Thomas
 */
public class BoardGameOption {
    private String name;
    private String shortName;

    /**
     * @param name The name of the board game
     * @param shortName The short name of the board game used for identification
     */
    public BoardGameOption(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    /**
     * @return The name of the board game
     */
    public String getName() {
        return name;
    }

    /**
     * @return The short name of the board game used for identification
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * @return The image of the board game
     */
    public Image getImage() {
        return new Image(BoardGameOption.class.getResource("/" + shortName + "-example.png").toExternalForm());
    }

    /**
     * @return The description of the board game
     */
    public String getDescription() {
        InputStream inputStream = getClass().getResourceAsStream("/" + shortName + "-description.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }
}
