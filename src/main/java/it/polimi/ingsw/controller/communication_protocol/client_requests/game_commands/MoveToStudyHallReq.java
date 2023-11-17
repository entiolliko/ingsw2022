package it.polimi.ingsw.controller.communication_protocol.client_requests.game_commands;

import it.polimi.ingsw.controller.communication_protocol.ServerAcceptor;
import it.polimi.ingsw.controller.communication_protocol.ClientRequest;
import it.polimi.ingsw.model.dashboard.TokenEnum;

import java.util.Objects;

public class MoveToStudyHallReq extends ClientRequest {
    private final TokenEnum colour;

    public MoveToStudyHallReq(TokenEnum colour) {
        this.colour = colour;
    }

    @Override
    public void visit(ServerAcceptor acceptor) {
        acceptor.moveToStudyHall(colour);
    }

}
