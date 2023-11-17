package it.polimi.ingsw.controller.communication_protocol.server_responses;

import it.polimi.ingsw.controller.communication_protocol.ClientAcceptor;
import it.polimi.ingsw.controller.communication_protocol.ServerResponse;
import it.polimi.ingsw.controller.utility.ConnectionStatusEnum;

import java.util.Map;

public class LobbyStandbyRes extends ServerResponse {
    private final String gameId;
    private final Map<String, ConnectionStatusEnum> playersConnections;

    public LobbyStandbyRes(String gameId, Map<String, ConnectionStatusEnum> playersConnections) {
        this.gameId = gameId;
        this.playersConnections = playersConnections;
    }

    @Override
    public void visit(ClientAcceptor acceptor) {
        acceptor.lobbyStandby(gameId, playersConnections);
    }
}
