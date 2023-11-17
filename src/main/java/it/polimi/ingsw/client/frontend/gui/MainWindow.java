package it.polimi.ingsw.client.frontend.gui;

import it.polimi.ingsw.controller.communication_protocol.client_requests.LeaveReq;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;


public class MainWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_files/EntryScreen.fxml"));
        Properties.getInstance().setFxmlLoader(fxmlLoader);
        Properties.getInstance().setCurrentScreen("EntryScreen");

        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("Eriantys");
        primaryStage.setResizable(false);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setFullScreen(true);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
                System.exit(0);
            }
        });
        Properties.getInstance().setPrimaryStage(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
