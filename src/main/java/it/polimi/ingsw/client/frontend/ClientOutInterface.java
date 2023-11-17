package it.polimi.ingsw.client.frontend;

import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.utility.ConnectionStatusEnum;
import it.polimi.ingsw.model.game_event.GameEvent;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ClientOutInterface {
    void promptMenu();

    void promptServerConnection(Set<String> availableLobbies, Set<String> loadableLobbies);

    void promptLobby(String gameID, Set<String> lobbyStatus);

    void promptLobbyStandby(String lobbyStatus, Map<String, ConnectionStatusEnum> playersConnections);

    void promptGame(GameDTO game);

    void promptEvent(List<GameEvent> event);

    void promptText(String text);

    void promptError(String errorMsg);

    void setPlayerName(String name);

    void promptLobbiesStatus(Set<String> activeLobbies, Set<String> loadableLobbies);
}
