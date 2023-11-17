package it.polimi.ingsw.client.frontend.gui.screen_controllers;

import it.polimi.ingsw.client.frontend.gui.Properties;
import it.polimi.ingsw.controller.communication_protocol.client_requests.CreateLobbyReq;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class CreateNewGameScreenController implements Initializable {
    @FXML
    private TextField playerName;
    @FXML
    private RadioButton normalGameSelected, expertGameSelected;
    @FXML
    private ChoiceBox<Integer> numberOfPlayers;

    private Integer[] numberToSelect = {2,3,4};
    private Stage primaryStage;
    private Scene scene;
    private Parent root;
    private final Properties properties = Properties.getInstance();
    private ActionEvent actionEvent;

    public void goBackButtonClick(ActionEvent actionEvent) throws Exception{
        properties.setCurrentScreen("ChooseGameScreen");
        changeScene("fxml_files/ChooseGameScreen.fxml", actionEvent);
    }

    public void createGameButtonClick(ActionEvent actionEvent) throws Exception{
        this.actionEvent = actionEvent;

        if(!checkVariables())
            return;

        properties.setUsername(playerName.getText());

        properties.getConnector().sendRequest(
                new CreateLobbyReq(playerName.getText(), numberOfPlayers.getValue(), (normalGameSelected.isSelected() ? TypeOfGame.NORMAL: TypeOfGame.EXPERT)));
    }

    public void lobbyJoinedResReceived(String gameID, Set<String> players) throws Exception {
        properties.setCurrentScreen("ChooseMagiciansScreen");
        properties.setGameID(gameID);
        changeScene("fxml_files/ChooseMagiciansScreen.fxml", this.actionEvent);
    }

    public void errorResReceived(String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("There has been an error.\n" + error);
        alert.showAndWait();
    }

    private boolean checkVariables(){
        boolean correct = true;
        String error = "";

        if(numberOfPlayers.getValue() == null){
            error = "Please select the number of players";
            correct = false;
        }
        if(!normalGameSelected.isSelected() && !expertGameSelected.isSelected()) {
            error = "Please select the type of the game";
            correct = false;
        }
        if(playerName.getText() == "") {
           error = "Please insert your name";
           correct = false;
        }
        if(playerName.getText().length() > 10) {
            error = "Your username must be al most 10 letters long";
            correct = false;
        }

        if(!correct){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(error);
            alert.showAndWait();
        }
        return correct;
    }

    private void changeScene(String path, ActionEvent actionEvent) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(path));
        properties.setFxmlLoader(fxmlLoader);

        scene = new Scene(fxmlLoader.load());
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numberOfPlayers.getItems().addAll(numberToSelect);
    }

    public void disconnectedRed(String reason) throws IOException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("You have disconnected from the server.\nReason: " + reason);
        alert.showAndWait();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_files/EntryScreen.fxml"));
        properties.setFxmlLoader(fxmlLoader);

        Scene scene = new Scene(fxmlLoader.load());
        Stage primaryStage = Properties.getInstance().getPrimaryStage();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
