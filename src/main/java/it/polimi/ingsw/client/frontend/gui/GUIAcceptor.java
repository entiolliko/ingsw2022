package it.polimi.ingsw.client.frontend.gui;

import it.polimi.ingsw.client.frontend.gui.screen_controllers.*;
import it.polimi.ingsw.controller.communication_protocol.ClientAcceptor;
import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.utility.ConnectionStatusEnum;
import it.polimi.ingsw.model.game_event.GameEvent;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GUIAcceptor implements ClientAcceptor {

    private GUIGameEventAcceptor gameEventAcceptor;

    public GUIAcceptor() {
        gameEventAcceptor = new GUIGameEventAcceptor();
    }

    @Override
    public void serverWelcome(Set<String> availableLobbies, Set<String> loadableLobbies) {
        System.out.println(loadableLobbies);
        availableLobbies.addAll(loadableLobbies);
        Properties.getInstance().setAvailableLobbies(availableLobbies);
        FXMLLoader fxmlLoader = Properties.getInstance().getFxmlLoader();

        try {
            Platform.runLater(() -> {
                try {
                    if (Properties.getInstance().getCurrentScreen().equals("JoinServerScreen")) {
                        JoinServerScreenController controller = fxmlLoader.getController();
                        controller.serverWelcomeResReceived();
                    }
                } catch (Exception e) {
                    System.out.println("Error in the runLater/serverWelcomeResReceiver.\n" + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.out.println("The JoinServerScreenController has launched an error when loading the next screen.\n" + e.getMessage());
        }
    }

    @Override
    public void lobbyJoined(String gameID, Set<String> players) {
        FXMLLoader fxmlLoader = Properties.getInstance().getFxmlLoader();
        try {
            Platform.runLater(() -> {
                try {
                    if (Properties.getInstance().getCurrentScreen().equals("JoinLobbyScreen")) {
                        JoinLobbyScreenController controller = fxmlLoader.getController();
                        controller.lobbyJoinedResReceived(gameID, players);
                    } else if (Properties.getInstance().getCurrentScreen().equals("CreateNewGameScreen")) {
                        CreateNewGameScreenController controller = fxmlLoader.getController();
                        controller.lobbyJoinedResReceived(gameID, players);
                    }
                } catch (Exception e) {
                    System.out.println("Error in the runLater/serverWelcomeResReceiver.\n" + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.out.println("The JoinServerScreenController has launched an error when loading the next screen.\n" + e.getMessage());
        }
    }

    @Override
    public void lobbyStandby(String gameId, Map<String, ConnectionStatusEnum> playersConnections) {
        Properties.getInstance().setLobbyIsInStandBy(true);
        Properties.getInstance().setPlayersConnections(playersConnections);

        FXMLLoader fxmlLoader = Properties.getInstance().getFxmlLoader();
        try {
            Platform.runLater(() -> {
                try {
                    if (Properties.getInstance().getCurrentScreen().equals("JoinLobbyScreen")) {
                        JoinLobbyScreenController controller = fxmlLoader.getController();
                        controller.lobbyJoinedResReceived(gameId, playersConnections.keySet());
                    } else if (Properties.getInstance().getCurrentScreen().equals("MainGameScreen")) {
                        MainGameScreenController controller = fxmlLoader.getController();
                        controller.lobbyStandby(playersConnections);
                    }
                } catch (Exception e) {
                    System.out.println("Error in the runLater/serverWelcomeResReceiver.\n" + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.out.println("The JoinServerScreenController has launched an error when loading the next screen.\n" + e.getMessage());
        }
    }

    @Override
    public void gameStatus(GameDTO gameDTO, List<GameEvent> gameEventList) {
        Properties.getInstance().setLobbyIsInStandBy(false);
        FXMLLoader fxmlLoader = Properties.getInstance().getFxmlLoader();
        Properties.getInstance().setGameDTO(gameDTO);
        for (GameEvent gameEvent : gameEventList)
            gameEventAcceptor.acceptEvent(gameEvent);

        try {
            Platform.runLater(() -> {
                try {
                    if (Properties.getInstance().getCurrentScreen().equals("JoinLobbyScreen")) {
                        Properties.getInstance().setPlayers(gameDTO.getDashboards().keySet().stream().toList());
                        Properties.getInstance().setNumPlayers(gameDTO.getDashboards().keySet().size());

                        JoinLobbyScreenController controller = fxmlLoader.getController();
                        controller.gameStatusResReceived(gameDTO.getDashboards().keySet());
                    } else if (Properties.getInstance().getCurrentScreen().equals("ChooseMagiciansScreen")) {
                        Properties.getInstance().setPlayers(gameDTO.getDashboards().keySet().stream().toList());
                        Properties.getInstance().setNumPlayers(gameDTO.getDashboards().keySet().size());

                        ChooseMagiciansScreenController controller = fxmlLoader.getController();
                        controller.gameStatusResReceived();
                    } else if (Properties.getInstance().getCurrentScreen().equals("MainGameScreen")) {
                        MainGameScreenController controller = fxmlLoader.getController();
                        controller.gameStatus();
                    }
                } catch (Exception e) {
                    System.out.println("There has been an error with the gameStatusResReceived.\n" + e.getMessage());
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            System.out.println("There has been an error with the gameStatusResReceived or with the Platform.runLater().\n" + e.getMessage());
        }
    }

    @Override
    public void disconnected(String reason) {
        FXMLLoader fxmlLoader = Properties.getInstance().getFxmlLoader();
        try {
            Platform.runLater(() -> {
                try {
                    switch (Properties.getInstance().getCurrentScreen()) {
                        case "JoinLobbyScreen":
                            JoinLobbyScreenController joinLobbyController = fxmlLoader.getController();
                            joinLobbyController.disconnectedRed(reason);
                            break;
                        case "CreateNewGameScreen":
                            CreateNewGameScreenController createNewGameController = fxmlLoader.getController();
                            createNewGameController.disconnectedRed(reason);
                            break;
                        case "ChooseGameScreen":
                            ChooseGameScreenController chooseGameScreenController = fxmlLoader.getController();
                            chooseGameScreenController.disconnectedRed(reason);
                            break;
                        case "ChooseMagiciansScreen":
                            ChooseMagiciansScreenController chooseMagiciansScreenController = fxmlLoader.getController();
                            chooseMagiciansScreenController.disconnectedRed(reason);
                            break;
                        case "MainGameScreen":
                            MainGameScreenController mainGameScreenController = fxmlLoader.getController();
                            mainGameScreenController.disconnectedRed(reason);
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Error in the runLater/serverWelcomeResReceiver.\n" + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.out.println("The JoinServerScreenController has launched an error when loading the next screen.\n" + e.getMessage());
        }
    }

    @Override
    public void error(String errorMessage) {
        FXMLLoader fxmlLoader = Properties.getInstance().getFxmlLoader();
        try {
            Platform.runLater(() -> {
                try {
                    switch (Properties.getInstance().getCurrentScreen()) {
                        case "JoinLobbyScreen":
                            JoinLobbyScreenController joinLobbyController = fxmlLoader.getController();
                            joinLobbyController.errorResReceived(errorMessage);
                            break;
                        case "CreateNewGameScreen":
                            CreateNewGameScreenController createNewGameController = fxmlLoader.getController();
                            createNewGameController.errorResReceived(errorMessage);
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Error in the runLater/serverWelcomeResReceiver.\n" + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.out.println("The JoinServerScreenController has launched an error when loading the next screen.\n" + e.getMessage());
        }
    }

    @Override
    public void lobbiesStatus(Set<String> activeLobbies, Set<String> loadableLobbies) {
        activeLobbies.addAll(loadableLobbies);
        Properties.getInstance().setAvailableLobbies(activeLobbies);
        FXMLLoader fxmlLoader = Properties.getInstance().getFxmlLoader();

        try {
            Platform.runLater(() -> {
                try {
                    if (Properties.getInstance().getCurrentScreen().equals("JoinLobbyScreen")) {
                        JoinLobbyScreenController controller = fxmlLoader.getController();
                        controller.updateActiveLobbies();
                    }
                } catch (Exception e) {
                    System.out.println("Error in the runLater/lobbiesStatus.\n" + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.out.println("The JoinServerScreenController has launched an error when loading the next screen.\n" + e.getMessage());
        }
    }
}
