package it.polimi.ingsw.controller.communication_protocol.client_requests.game_commands;

import it.polimi.ingsw.controller.communication_protocol.ServerAcceptor;
import it.polimi.ingsw.controller.communication_protocol.ClientRequest;

import java.util.Objects;

public class PickCloudReq extends ClientRequest {
    private final int cloudIndex;

    public PickCloudReq(int cloudIndex) {
        this.cloudIndex = cloudIndex;
    }

    @Override
    public void visit(ServerAcceptor acceptor) {
        acceptor.pickCloud(cloudIndex);
    }

}
