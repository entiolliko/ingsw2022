package it.polimi.ingsw.client.frontend.gui.screen_controllers;

import it.polimi.ingsw.client.frontend.gui.Properties;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChooseMagiciansScreenController implements Initializable {

    private final int NO_CHOICE = -1;
    private final int WIZARD_MAGICIAN = 0;
    private final int KING_MAGICIAN = 1;
    private final int WITCH_MAGICIAN = 2;
    private final int MONK_MAGICIAN = 3;

    @FXML
    private ImageView wizardMagician;
    @FXML
    private ImageView kingMagician;
    @FXML
    private ImageView witchMagician;
    @FXML
    private ImageView monkMagician;
    @FXML
    private Button chooseMagicianButton;

    @FXML
    private Label LabelMag;

    private Stage primaryStage;
    private Scene scene;
    private int chosenMagician;
    private List<ImageView> magicians;


    public void wizardMagicianClicked(MouseEvent mouseEvent) {
        chooseCard(WIZARD_MAGICIAN);
    }

    public void kingMagicianClicked(MouseEvent mouseEvent) {
        chooseCard(KING_MAGICIAN);
    }

    public void witchMagicianClicked(MouseEvent mouseEvent) {
        chooseCard(WITCH_MAGICIAN);
    }

    public void monkMagicianClicked(MouseEvent mouseEvent) {
        chooseCard(MONK_MAGICIAN);
    }

    public void magicianEntered(MouseEvent mouseEvent) {
        if (chosenMagician == NO_CHOICE) {
            ((ImageView) mouseEvent.getSource()).setOpacity(1);
        }
    }
    public void magicianExited(MouseEvent mouseEvent) {
        if (chosenMagician == NO_CHOICE) {
            ((ImageView) mouseEvent.getSource()).setOpacity(0.5);
        }
    }

    public void chooseMagicianButtonCLick(ActionEvent actionEvent) throws Exception {
        switch (chosenMagician) {
            case WIZARD_MAGICIAN:
                Properties.getInstance().setMagician("WizardMagician");
                break;
            case KING_MAGICIAN:
                Properties.getInstance().setMagician("KingMagician");
                break;
            case WITCH_MAGICIAN:
                Properties.getInstance().setMagician("WitchMagician");
                break;
            case MONK_MAGICIAN:
                Properties.getInstance().setMagician("MonkMagician");
                break;
            default:
                throw new RuntimeException("No Magician was chosen when clicked");
        }

        Properties.getInstance().setCurrentScreen("MainGameScreen");
        changeScene("fxml_files/MainGameScreen.fxml", actionEvent);
    }

    private void setOpacity(int i, double opacity) {
        magicians.get(i).setOpacity(opacity);
    }

    private void chooseCard(int magician) {
        if (chosenMagician == NO_CHOICE) {
            chosenMagician = magician;
            setOpacity(chosenMagician, 1);
            if(Properties.getInstance().getGameDTO() != null)
                chooseMagicianButton.setDisable(false);
        } else {
            if (chosenMagician == magician) {
                setOpacity(chosenMagician, 0.5);
                chosenMagician = NO_CHOICE;
                chooseMagicianButton.setDisable(true);
            } else {
                setOpacity(chosenMagician, 0.5);
                chosenMagician = magician;
                setOpacity(chosenMagician, 1);
            }
        }
    }

    private void changeScene(String path, ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(path));
        Properties.getInstance().setFxmlLoader(fxmlLoader);

        scene = new Scene(fxmlLoader.load());
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void gameStatusResReceived() throws Exception {
        if (chosenMagician != NO_CHOICE)
            chooseMagicianButton.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chosenMagician = NO_CHOICE;

        chooseMagicianButton.setDisable(true);
        wizardMagician.setOpacity(0.5);
        kingMagician.setOpacity(0.5);
        witchMagician.setOpacity(0.5);
        monkMagician.setOpacity(0.5);

        magicians = new ArrayList<>();
        magicians.add(wizardMagician);
        magicians.add(kingMagician);
        magicians.add(witchMagician);
        magicians.add(monkMagician);

        LabelMag.setText("Your GameID is: " + Properties.getInstance().getGameID() + "\nPlease choose your magician.");
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
