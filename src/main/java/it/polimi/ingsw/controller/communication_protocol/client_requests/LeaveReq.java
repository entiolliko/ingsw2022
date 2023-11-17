package it.polimi.ingsw.controller.communication_protocol.client_requests;

import it.polimi.ingsw.controller.communication_protocol.ServerAcceptor;
import it.polimi.ingsw.controller.communication_protocol.ClientRequest;

public class LeaveReq extends ClientRequest {

    @Override
    public void visit(ServerAcceptor acceptor) {
        acceptor.leave();
    }
}
