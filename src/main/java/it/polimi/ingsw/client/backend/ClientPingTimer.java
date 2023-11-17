package it.polimi.ingsw.client.backend;

import it.polimi.ingsw.controller.communication_protocol.server_responses.DisconnectedRes;
import it.polimi.ingsw.controller.ping_protocol.TimeBomb;

public class ClientPingTimer extends TimeBomb {
    private final Connector connector;

    public ClientPingTimer(Connector connector) {
        super();
        this.connector = connector;
    }

    @Override
    protected void explode() {
        connector.quit();
        connector.updateAll(new DisconnectedRes("connection was lost"));
    }
}
