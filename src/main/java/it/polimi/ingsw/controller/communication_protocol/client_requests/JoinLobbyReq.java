package it.polimi.ingsw.controller.communication_protocol.client_requests;

import it.polimi.ingsw.controller.communication_protocol.ServerAcceptor;
import it.polimi.ingsw.controller.communication_protocol.ClientRequest;

import java.util.Objects;

public class JoinLobbyReq extends ClientRequest {
    private final String desiredName;
    private final String gameId;

    public JoinLobbyReq(String desiredName, String gameId) {
        this.desiredName = desiredName;
        this.gameId = gameId;
    }

    @Override
    public void visit(ServerAcceptor acceptor) {
        acceptor.joinLobby(desiredName, gameId);
    }

}
