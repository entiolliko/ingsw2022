package it.polimi.ingsw.controller.communication_protocol.server_responses;

import it.polimi.ingsw.controller.communication_protocol.ClientAcceptor;
import it.polimi.ingsw.controller.communication_protocol.ServerResponse;

import java.util.Set;

public class LobbiesStatusRes extends ServerResponse {
    private final Set<String> activeLobbies;
    private final Set<String> loadableLobbies;

    public LobbiesStatusRes(Set<String> activeLobbies, Set<String> loadableLobbies) {
        this.activeLobbies = activeLobbies;
        this.loadableLobbies = loadableLobbies;
    }

    @Override
    public void visit(ClientAcceptor acceptor) {
        acceptor.lobbiesStatus(activeLobbies, loadableLobbies);

    }
}
