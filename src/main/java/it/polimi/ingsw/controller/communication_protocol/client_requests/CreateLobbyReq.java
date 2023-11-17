package it.polimi.ingsw.controller.communication_protocol.client_requests;

import it.polimi.ingsw.controller.communication_protocol.ServerAcceptor;
import it.polimi.ingsw.controller.communication_protocol.ClientRequest;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;

import java.util.Objects;

public class CreateLobbyReq extends ClientRequest {
    private final String hostName;
    private final int numberOfPlayers;
    private final TypeOfGame gameMode;

    public CreateLobbyReq(String hostName, int numberOfPlayers, TypeOfGame gameMode) {
        this.hostName = hostName;
        this.numberOfPlayers = numberOfPlayers;
        this.gameMode = gameMode;
    }

    @Override
    public void visit(ServerAcceptor acceptor) {
        acceptor.createLobby(hostName, numberOfPlayers, gameMode);
    }

}
