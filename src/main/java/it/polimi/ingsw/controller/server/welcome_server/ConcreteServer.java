package it.polimi.ingsw.controller.server.welcome_server;

import it.polimi.ingsw.controller.exceptions.ServerException;
import it.polimi.ingsw.controller.server.connection.Connection;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;

public class ConcreteServer extends Server {
    /**
     * Implementation of the Server abstract class that cares to implement the methods in the Service interface which
     * need to be overridden
     */
    public ConcreteServer(int port) {
        super(port);
    }

    @Override
    public void createLobby(Connection sender, String hostName, int numberOfPlayers, TypeOfGame gameMode) throws ServerException {
        String gameId = super.addNewLobby(numberOfPlayers, gameMode);
        sender.setName(hostName);
        super.addToLobby(gameId, sender);
    }

    @Override
    public void leave(Connection sender) {
        super.disconnect(sender);
    }

    @Override
    public void joinLobby(Connection sender, String desiredName, String gameId) throws ServerException {
        sender.setName(desiredName);
        super.addToLobby(gameId, sender);
    }

}
