package it.polimi.ingsw.controller.communication_protocol.server_responses;

import it.polimi.ingsw.controller.communication_protocol.ClientAcceptor;
import it.polimi.ingsw.controller.communication_protocol.ServerResponse;

import java.util.Objects;

public class DisconnectedRes extends ServerResponse {
    private final String reason;

    public DisconnectedRes(String reason) {
        this.reason = reason;
    }

    public DisconnectedRes() {
        reason = null;
    }

    @Override
    public void visit(ClientAcceptor acceptor) {
        acceptor.disconnected(reason);
    }

    @Override
    public boolean isDisconnectReq() {
        return true;
    }


}
