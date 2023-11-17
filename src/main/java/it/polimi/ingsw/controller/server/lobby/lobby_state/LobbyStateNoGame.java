package it.polimi.ingsw.controller.server.lobby.lobby_state;

import it.polimi.ingsw.controller.communication_protocol.server_responses.LobbyJoinedRes;
import it.polimi.ingsw.controller.exceptions.NameAlreadyUsedException;
import it.polimi.ingsw.controller.exceptions.ServerException;
import it.polimi.ingsw.controller.server.connection.Connection;
import it.polimi.ingsw.controller.server.lobby.Lobby;
import it.polimi.ingsw.debug_utility.DebugLogger;
import it.polimi.ingsw.model.visitor.player_visitor_command.PlayerVisitorCommand;

import java.util.logging.Level;

/**
 * This is the state a fresh lobby starts in.
 * In this state all players may freely join as well as leave.
 * Once the desired number of players (declared when instantiating the lobby) is reached,
 * the lobby enters the game state and the game itself starts
 */
public class LobbyStateNoGame extends LobbyState {
    public LobbyStateNoGame(Lobby lobby) {
        super(lobby);
        DebugLogger.log("something's wrong, I can feel it!", Level.INFO);
        sendDefaultResponse();
    }

    /**
     * adds a player to the lobby if nobody else currently has their name
     * @param sender player who wants to join
     * @throws NameAlreadyUsedException thrown when there already is a player with the joiner name
     */
    @Override
    public void onAddPlayer(Connection sender) throws NameAlreadyUsedException {
        lobby.addPlayer(sender);
        if (lobby.getConnectedPlayers().keySet().size() >= lobby.getNumberOfPlayers()) {
            lobby.setLobbyState(new LobbyStateGame(lobby));
        } else {
            sendDefaultResponse();
        }
    }

    /**
     * kicks a player from the lobby
     * @param toRemove player to remove
     */
    @Override
    public void onRemovePlayer(Connection toRemove) {
        lobby.disconnectPlayer(toRemove);
        sendDefaultResponse();
    }

    /**
     * throws an exception since the game hasn't begun
     * @param command command to execute
     * @throws ServerException thrown because the game hasn't begun
     */
    @Override
    public void onExecuteCommand(PlayerVisitorCommand command) throws ServerException {
        throw new ServerException("I like your spirit! But sadly we have to wait for the others to join before we can play");
    }

    /**
     * sends all players a list of the players inside the lobby
     */
    public void sendDefaultResponse() {
        lobby.updateAllPlayers(new LobbyJoinedRes(lobby.getGameID(), lobby.getConnectedPlayers().keySet()));
    }
}
