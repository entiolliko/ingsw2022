package it.polimi.ingsw.controller.server.connection;

import it.polimi.ingsw.controller.ping_protocol.TimeBomb;

/**
 * TimeBomb that, when it explodes, severs the connection with the client
 */
public class ServerPingTimer extends TimeBomb {
    private final Connection connection;

    public ServerPingTimer(Connection connection) {
        super();
        this.connection = connection;
    }

    @Override
    protected void explode() {
        connection.kick("you did not ping fast enough");
    }

}
