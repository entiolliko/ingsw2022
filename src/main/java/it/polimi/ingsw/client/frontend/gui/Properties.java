package it.polimi.ingsw.client.frontend.gui;

import it.polimi.ingsw.client.backend.DefaultConnector;
import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.utility.ConnectionStatusEnum;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.util.*;

public class Properties {

    private static Properties instance = null;
    private DefaultConnector connector;
    private GUIAcceptor acceptor;
    private Stage primaryStage;
    private FXMLLoader fxmlLoader;
    private FXMLLoader popUpFxmlLoader;
    private String currentScreen;

    private Set<String> availableLobbies;
    private GameDTO gameDTO;
    private boolean lobbyIsInStandBy;
    private Map<String, ConnectionStatusEnum> playersConnections;

    private String username; //Initially null
    private String gameID; //Initially null
    private String magician;
    private List<String> players;
    private int numPlayers;

    private Properties(){
        username = null;
        gameID = null;
        connector = new DefaultConnector();
        acceptor = new GUIAcceptor();
        connector.addObserver(acceptor);
        lobbyIsInStandBy = false;
        playersConnections = null;
        primaryStage = null;
        fxmlLoader = null;
        popUpFxmlLoader = null;
        gameDTO = null;
        magician = null;
    }
    public static Properties getInstance(){
        if(instance == null)
            instance = new Properties();
        return instance;
    }

    public DefaultConnector getConnector() {
        return connector;
    }

    public GUIAcceptor getAcceptor() {
        return acceptor;
    }
    public void setAcceptor(GUIAcceptor acceptor) {
        this.acceptor = acceptor;
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }
    public void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }

    public FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }
    public void setFxmlLoader(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
    }

    public String getCurrentScreen() {
        return currentScreen;
    }
    public void setCurrentScreen(String currentScreen) {
        this.currentScreen = currentScreen;
    }

    public Set<String> getAvailableLobbies() {
        return availableLobbies;
    }
    public void setAvailableLobbies(Set<String> availableLobbies) {
        this.availableLobbies = availableLobbies;
    }

    public GameDTO getGameDTO() {
        return gameDTO;
    }
    public void setGameDTO(GameDTO gameDTO) {
        this.gameDTO = gameDTO;
    }

    public boolean isLobbyIsInStandBy() {
        return lobbyIsInStandBy;
    }
    public void setLobbyIsInStandBy(boolean lobbyIsInStandBy) {
        this.lobbyIsInStandBy = lobbyIsInStandBy;
    }

    public Map<String, ConnectionStatusEnum> getPlayersConnections() {
        return playersConnections;
    }
    public void setPlayersConnections(Map<String, ConnectionStatusEnum> playersConnections) {
        this.playersConnections = playersConnections;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getGameID() {
        return gameID;
    }
    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getMagician() {
        return magician;
    }
    public void setMagician(String magician) {
        this.magician = magician;
    }

    public List<String> getPlayers() {
        return players;
    }
    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public int getNumPlayers() {
        return numPlayers;
    }
    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }
}
