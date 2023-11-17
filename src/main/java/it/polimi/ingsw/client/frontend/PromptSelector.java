package it.polimi.ingsw.client.frontend;

import it.polimi.ingsw.controller.communication_protocol.ClientAcceptor;
import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.utility.ConnectionStatusEnum;
import it.polimi.ingsw.model.game_event.GameEvent;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PromptSelector implements ClientAcceptor {
    private ClientOutInterface screen;

    public void setScreen(ClientOutInterface screen) {
        this.screen = screen;
    }

    @Override
    public void serverWelcome(Set<String> availableLobbies, Set<String> loadableLobbies) {
        screen.promptServerConnection(availableLobbies, loadableLobbies);
    }

    @Override
    public void lobbyJoined(String gameID, Set<String> players) {
        screen.promptLobby(gameID, players);

    }

    @Override
    public void lobbyStandby(String gameId, Map<String, ConnectionStatusEnum> playersConnections) {
        screen.promptLobbyStandby(gameId, playersConnections);
    }

    @Override
    public void gameStatus(GameDTO gameDTO, List<GameEvent> gameEventList) {
        screen.promptGame(gameDTO);
        screen.promptEvent(gameEventList);

    }

    @Override
    public void disconnected(String reason) {
        screen.promptText("disconnected :(" + (Objects.isNull(reason) ? "" : "...reason was: " + reason ));
    }

    @Override
    public void error(String errorMessage) {
        screen.promptError(errorMessage);
    }

    @Override
    public void lobbiesStatus(Set<String> activeLobbies, Set<String> loadableLobbies) {
        screen.promptLobbiesStatus(activeLobbies, loadableLobbies);
    }
}
