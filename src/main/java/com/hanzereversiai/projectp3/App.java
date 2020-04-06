package com.hanzereversiai.projectp3;

import com.hanzereversiai.projectp3.ui.ConnectionPanelController;
import com.hanzereversiai.projectp3.ui.ConnectionPanelModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/connection-panel.fxml"));
        Parent root = loader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("Tournament framework");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
