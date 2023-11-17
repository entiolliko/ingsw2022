package it.polimi.ingsw.controller.communication_protocol.client_requests.game_commands;

import it.polimi.ingsw.controller.communication_protocol.ServerAcceptor;
import it.polimi.ingsw.controller.communication_protocol.ClientRequest;

import java.util.Objects;

public class PlayApprenticeCardReq extends ClientRequest {
    private final int cardId;

    public PlayApprenticeCardReq(int cardId) {
        this.cardId = cardId;
    }

    @Override
    public void visit(ServerAcceptor acceptor) {
        acceptor.playApprenticeCard(cardId);
    }

}
