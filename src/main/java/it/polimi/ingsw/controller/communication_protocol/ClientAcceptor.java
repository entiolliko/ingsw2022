package it.polimi.ingsw.controller.communication_protocol;

import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.utility.ConnectionStatusEnum;
import it.polimi.ingsw.model.game_event.GameEvent;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ClientAcceptor {
    default void accept(ServerResponse response) {
        response.visit(this);
    }

    void serverWelcome(Set<String> availableLobbies, Set<String> loadableLobbies);

    void lobbyJoined(String gameID, Set<String> players);

    void lobbyStandby(String gameId, Map<String, ConnectionStatusEnum> playersConnections);

    void gameStatus(GameDTO gameDTO, List<GameEvent> gameEventList);

    void disconnected(String reason);

    void error(String errorMessage);

    void lobbiesStatus(Set<String> activeLobbies, Set<String> loadableLobbies);
}
