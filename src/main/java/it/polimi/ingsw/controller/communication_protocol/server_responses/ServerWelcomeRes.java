package it.polimi.ingsw.controller.communication_protocol.server_responses;

import it.polimi.ingsw.controller.communication_protocol.ClientAcceptor;
import it.polimi.ingsw.controller.communication_protocol.ServerResponse;

import java.util.Set;

public class ServerWelcomeRes extends ServerResponse {
    private final Set<String> lobbies;
    private final Set<String> loadableLobbies;

    public ServerWelcomeRes(Set<String> lobbies, Set<String> loadableLobbies) {
        this.lobbies = lobbies;
        this.loadableLobbies = loadableLobbies;
    }

    @Override
    public void visit(ClientAcceptor acceptor) {
        acceptor.serverWelcome(lobbies, loadableLobbies);
    }
}
