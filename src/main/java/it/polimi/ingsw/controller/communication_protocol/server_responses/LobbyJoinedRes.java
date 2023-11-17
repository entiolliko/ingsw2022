package it.polimi.ingsw.controller.communication_protocol.server_responses;

import it.polimi.ingsw.controller.communication_protocol.ClientAcceptor;
import it.polimi.ingsw.controller.communication_protocol.ServerResponse;

import java.util.Set;

public class LobbyJoinedRes extends ServerResponse {
    private final String gameID;
    private final Set<String> players;

    public LobbyJoinedRes(String gameID, Set<String> players) {
        this.gameID = gameID;
        this.players = players;
    }

    @Override
    public void visit(ClientAcceptor acceptor) {
        acceptor.lobbyJoined(gameID, players);
    }
}
