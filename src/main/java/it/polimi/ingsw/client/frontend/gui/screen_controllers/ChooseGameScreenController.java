package it.polimi.ingsw.client.frontend.gui.screen_controllers;

import com.sun.javafx.tk.quantum.PrimaryTimer;
import it.polimi.ingsw.client.frontend.gui.Properties;
import it.polimi.ingsw.controller.communication_protocol.client_requests.LeaveReq;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class ChooseGameScreenController {

    public void createNewGameButtonClick(ActionEvent actionEvent) throws Exception {
        Properties.getInstance().setCurrentScreen("CreateNewGameScreen");
        changeScene("fxml_files/CreateNewGameScreen.fxml", actionEvent);
    }

    public void joinLobbyButtonClick(ActionEvent actionEvent) throws Exception {
        Properties.getInstance().setCurrentScreen("JoinLobbyScreen");
        changeScene("fxml_files/JoinLobbyScreen.fxml", actionEvent);
    }

    public void goBackButtonClick(ActionEvent actionEvent) throws Exception {
        Properties.getInstance().setCurrentScreen("JoinServerScreen");
        Properties.getInstance().getConnector().sendRequest(new LeaveReq());
        changeScene("fxml_files/JoinServerScreen.fxml", actionEvent);
    }

    private void changeScene(String path, ActionEvent actionEvent) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(path));
        Properties.getInstance().setFxmlLoader(fxmlLoader);

        Scene scene = new Scene(fxmlLoader.load());
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void disconnectedRed(String reason) throws IOException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("You have disconnected from the server.\nReason: " + reason);
        alert.showAndWait();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_files/EntryScreen.fxml"));
        Properties.getInstance().setFxmlLoader(fxmlLoader);

        Scene scene = new Scene(fxmlLoader.load());
        Stage primaryStage = Properties.getInstance().getPrimaryStage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
