package it.polimi.ingsw.controller.server.lobby;

import it.polimi.ingsw.controller.exceptions.ServerException;
import it.polimi.ingsw.controller.server.Service;
import it.polimi.ingsw.controller.server.connection.Connection;
import it.polimi.ingsw.model.visitor.player_visitor_command.PlayerVisitorCommand;

/**
 * interface through which the server interacts with the lobby
 * the commands' behaviour depends on the lobby current state
 */
public interface ILobby extends Service {
    /**
     * a player wants to join
     * @param sender player who wants to join
     * @throws ServerException thrown for a variety of reasons, but mostly because:
     *      - somebody with the same name is already inside the lobby
     *      - the lobby is full (i.e.: the game is currently on)
     *      - the game is in stand-by but the player is not an original player
     */
    void onAddPlayer(Connection sender) throws ServerException;

    /**
     * a player wants to leave
     * @param toRemove player to remove
     */
    void onRemovePlayer(Connection toRemove);

    /**
     * a player wants to make a game move
     * @param command command to execute
     * @throws ServerException can be thrown for a variety of reasons, but mostly because the game has yet to start or is currently paused     */
    void onExecuteCommand(PlayerVisitorCommand command) throws ServerException;
}
