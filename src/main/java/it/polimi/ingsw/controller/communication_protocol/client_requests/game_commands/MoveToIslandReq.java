package it.polimi.ingsw.controller.communication_protocol.client_requests.game_commands;

import it.polimi.ingsw.controller.communication_protocol.ServerAcceptor;
import it.polimi.ingsw.controller.communication_protocol.ClientRequest;
import it.polimi.ingsw.model.dashboard.TokenEnum;

import java.util.Objects;

public class MoveToIslandReq extends ClientRequest {
    private final TokenEnum colour;
    private final int islandIndex;

    public MoveToIslandReq(TokenEnum colour, int islandIndex) {
        this.colour = colour;
        this.islandIndex = islandIndex;
    }

    @Override
    public void visit(ServerAcceptor acceptor) {
        acceptor.moveToIsland(colour, islandIndex);
    }

}
