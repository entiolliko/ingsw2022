package it.polimi.ingsw.controller.communication_protocol.client_requests.game_commands;

import it.polimi.ingsw.controller.communication_protocol.ServerAcceptor;
import it.polimi.ingsw.controller.communication_protocol.ClientRequest;

import java.util.Objects;

public class MoveMotherNatureReq extends ClientRequest {
    private final int movementValue;

    public MoveMotherNatureReq(int movementValue) {
        this.movementValue = movementValue;
    }

    @Override
    public void visit(ServerAcceptor acceptor) {
        acceptor.moveMotherNature(movementValue);
    }

}
