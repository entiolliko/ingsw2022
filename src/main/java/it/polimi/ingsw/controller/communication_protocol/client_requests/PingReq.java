package it.polimi.ingsw.controller.communication_protocol.client_requests;

import it.polimi.ingsw.controller.communication_protocol.ClientRequest;
import it.polimi.ingsw.controller.communication_protocol.ServerAcceptor;

public class PingReq extends ClientRequest {
    @Override
    public void visit(ServerAcceptor acceptor) {
        acceptor.ping();
    }
}
