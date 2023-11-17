package it.polimi.ingsw.client.frontend.gui.screen_controllers;

import it.polimi.ingsw.client.frontend.gui.Properties;
import it.polimi.ingsw.controller.communication_protocol.client_requests.JoinLobbyReq;
import it.polimi.ingsw.controller.server.lobby.Lobby;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class JoinLobbyScreenController implements Initializable {

    @FXML
    private TextField playerUsername;
    @FXML
    private TextField lobbyName;
    @FXML
    private AnchorPane activeLobbies;

    private ActionEvent actionEvent;
    private Stage primaryStage;
    private Scene scene;

    public void goBackButtonClick(ActionEvent actionEvent) throws Exception {
        Properties.getInstance().setCurrentScreen("ChooseGameScreen");
        changeScene("fxml_files/ChooseGameScreen.fxml", actionEvent);
    }

    public void connectButtonClick(ActionEvent actionEvent) throws Exception {
        this.actionEvent = actionEvent;

        if(lobbyName.getText() == "" || playerUsername.getText() == "") {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Format Error");
            alert.setContentText("Please complete both fields.");
            alert.showAndWait();
            return;
        }
        if(playerUsername.getText().length() > 10) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Format Error");
            alert.setContentText("Your username must be at most 10 letters long.");
            alert.showAndWait();
            return;
        }

        Properties.getInstance().getConnector().sendRequest(new JoinLobbyReq(playerUsername.getText(), lobbyName.getText()));
    }

    public void lobbyJoinedResReceived(String gameID, Set<String> players) throws Exception {
        moveToNextScreen();
    }

    public void gameStatusResReceived(Set<String> players) throws Exception {
        moveToNextScreen();
    }

    private void moveToNextScreen() throws Exception {
        Properties.getInstance().setUsername(playerUsername.getText());
        Properties.getInstance().setGameID(lobbyName.getText());
        Properties.getInstance().setCurrentScreen("ChooseMagiciansScreen");
        changeScene("fxml_files/ChooseMagiciansScreen.fxml", this.actionEvent);
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

    public void errorResReceived(String error) throws Exception {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Connection Error");
        alert.setContentText("There has been an error.\n" + error);
        alert.showAndWait();
    }

    private void changeScene(String path, ActionEvent actionEvent) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(path));
        Properties.getInstance().setFxmlLoader(fxmlLoader);

        scene = new Scene(fxmlLoader.load());
        primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void updateActiveLobbies(){
        List<String> lobbiesNames = Properties.getInstance().getAvailableLobbies().stream().toList();
        for(int i = 0; i < lobbiesNames.size(); i++){
            Label label = new Label();
            label.setText(lobbiesNames.get(i));
            label.setPrefHeight(50);
            label.setAlignment(Pos.TOP_CENTER);
            if(activeLobbies.getChildren().size() > 0)
                label.setLayoutY(activeLobbies.getChildren().get(activeLobbies.getChildren().size() - 1).getLayoutY() + 60);
            label.setPrefWidth(activeLobbies.getPrefWidth());
            activeLobbies.getChildren().add(label);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateActiveLobbies();
    }
}
