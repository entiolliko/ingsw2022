package it.polimi.ingsw.client.frontend.gui.screen_controllers;

import it.polimi.ingsw.client.frontend.gui.Properties;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class JoinServerScreenController {
    @FXML
    private TextField serverIP;
    @FXML
    private TextField serverPort;

    ActionEvent actionEvent;

    public void goBackButtonClick(ActionEvent actionEvent) throws Exception {
        Properties.getInstance().setCurrentScreen("EntryScreen");
        changeScene("fxml_files/EntryScreen.fxml", actionEvent);
    }

    public void connectButtonClick(ActionEvent actionEvent) throws Exception {
        this.actionEvent = actionEvent;

        String ipAddress = serverIP.getText();
        int portNumber;
        try {
            portNumber = Integer.parseInt(serverPort.getText());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Format Error");
            alert.setContentText("Please insert a number for the port");
            alert.showAndWait();
            return;
        }

        try{
            Properties.getInstance().getConnector().connectToServer(ipAddress, portNumber);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Server Error");
            alert.setContentText("We couldn't connect to the server.\nPlease check the ip or the port.\n Inserted IP: "
                    + ipAddress + " Port Number: " + portNumber);
            alert.showAndWait();
            return;
        }

    }

    public void serverWelcomeResReceived() throws Exception {
        Properties.getInstance().setCurrentScreen("ChooseGameScreen");
        changeScene("fxml_files/ChooseGameScreen.fxml", this.actionEvent);
    }

    private void changeScene(String path, ActionEvent actionEvent) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(path));
        Properties.getInstance().setFxmlLoader(fxmlLoader);

        Scene scene = new Scene(fxmlLoader.load());
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
