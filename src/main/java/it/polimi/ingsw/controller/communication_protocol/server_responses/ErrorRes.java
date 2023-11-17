package it.polimi.ingsw.controller.communication_protocol.server_responses;

import it.polimi.ingsw.controller.communication_protocol.ClientAcceptor;
import it.polimi.ingsw.controller.communication_protocol.ServerResponse;

public class ErrorRes extends ServerResponse {
    private final String errorMessage;

    public ErrorRes(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public void visit(ClientAcceptor acceptor) {
        acceptor.error(errorMessage);
    }

}
