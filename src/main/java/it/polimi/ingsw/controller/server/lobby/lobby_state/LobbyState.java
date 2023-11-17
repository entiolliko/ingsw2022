package it.polimi.ingsw.controller.server.lobby.lobby_state;

import it.polimi.ingsw.controller.exceptions.ServerException;
import it.polimi.ingsw.controller.server.connection.Connection;
import it.polimi.ingsw.controller.server.lobby.Lobby;
import it.polimi.ingsw.model.visitor.player_visitor_command.PlayerVisitorCommand;

/**
 * The behaviour of the lobby is determined by its state.
 * This is the abstract class, the prototype, of a state in such a state machine
 * the various methods below are reactions to a request and their behaviour depends on the particular implementation
 */
public abstract class LobbyState {
    protected final Lobby lobby;

    protected LobbyState(Lobby lobby) {
        this.lobby = lobby;
    }

    public abstract void onAddPlayer(Connection sender) throws ServerException;

    public abstract void onRemovePlayer(Connection toRemove);

    public abstract void onExecuteCommand(PlayerVisitorCommand command) throws ServerException;

    public abstract void sendDefaultResponse();

}
