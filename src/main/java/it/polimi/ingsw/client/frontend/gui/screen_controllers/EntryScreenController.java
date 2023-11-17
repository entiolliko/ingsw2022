package it.polimi.ingsw.client.frontend.gui.screen_controllers;

import it.polimi.ingsw.client.frontend.gui.Properties;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EntryScreenController {

    public void enterButtonClick(ActionEvent actionEvent) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_files/JoinServerScreen.fxml"));

        Properties.getInstance().setFxmlLoader(fxmlLoader);
        Properties.getInstance().setCurrentScreen("JoinServerScreen");

        Scene scene = new Scene(fxmlLoader.load());
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void quitButtonClick(ActionEvent actionEvent) {
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        primaryStage.close();
    }
}
