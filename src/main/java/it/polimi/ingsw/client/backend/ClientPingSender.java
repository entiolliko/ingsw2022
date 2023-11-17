package it.polimi.ingsw.client.backend;

import it.polimi.ingsw.controller.communication_protocol.client_requests.PingReq;
import it.polimi.ingsw.controller.ping_protocol.RegularExecutor;

public class ClientPingSender extends RegularExecutor {

    private final Connector connector;

    public ClientPingSender(Connector connector) {
        this.connector = connector;
    }

    @Override
    protected void timeTick() {
        connector.sendRequest(new PingReq());
    }
}
