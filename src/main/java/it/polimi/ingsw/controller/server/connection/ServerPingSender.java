package it.polimi.ingsw.controller.server.connection;

import it.polimi.ingsw.controller.communication_protocol.server_responses.ServerPing;
import it.polimi.ingsw.controller.ping_protocol.RegularExecutor;

/**
 * a Regular sender that regularly sends ping responses
 */
public class ServerPingSender extends RegularExecutor {
    private final Connection connection;

    public ServerPingSender(Connection connection) {
        this.connection = connection;
    }

    @Override
    protected void timeTick() {
        connection.send(new ServerPing());
    }
}
