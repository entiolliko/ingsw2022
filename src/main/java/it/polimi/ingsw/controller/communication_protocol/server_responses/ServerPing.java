package it.polimi.ingsw.controller.communication_protocol.server_responses;

import it.polimi.ingsw.controller.communication_protocol.ClientAcceptor;
import it.polimi.ingsw.controller.communication_protocol.ServerResponse;

public class ServerPing extends ServerResponse {
    @Override
    public void visit(ClientAcceptor acceptor) {
        // nothing needs to be done
    }
}
