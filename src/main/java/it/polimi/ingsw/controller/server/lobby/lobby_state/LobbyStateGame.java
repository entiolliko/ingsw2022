package it.polimi.ingsw.controller.server.lobby.lobby_state;

import it.polimi.ingsw.controller.communication_protocol.server_responses.GameStatusRes;
import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.exceptions.LobbyFullException;
import it.polimi.ingsw.controller.exceptions.ServerException;
import it.polimi.ingsw.controller.server.connection.Connection;
import it.polimi.ingsw.controller.server.lobby.Lobby;
import it.polimi.ingsw.model.visitor.player_visitor_command.PlayerVisitorCommand;
import it.polimi.ingsw.debug_utility.DebugLogger;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.game_events.GameStartedEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 * The lobby is in this state when the game is currently one, i.e.: all the players are connected
 */
public class LobbyStateGame extends LobbyState {
    public LobbyStateGame(Lobby lobby) {
        super(lobby);
        if (lobby.getGameState() == null) {
            lobby.startGameUp();
        }
        //welcome message
        lobby.getBuffer().acceptEvent(new GameStartedEvent());
        sendDefaultResponse();
        DebugLogger.log("Game is on!", Level.INFO);
    }

    /**
     * throws an exception because when the game is on, nobody is allowed to join, for the lobby is full
     * @param sender the player who wants to join
     * @throws ServerException thrown because the lobby is currently full
     */
    @Override
    public void onAddPlayer(Connection sender) throws ServerException {
        throw new LobbyFullException("lobby is full");
    }

    /**
     * kicks the player from the game and puts the game on stand-by until everybody is back
     * @param toRemove player to remove
     */
    @Override
    public void onRemovePlayer(Connection toRemove) {
        lobby.disconnectPlayer(toRemove);
        Set<String> originalPlayers = new HashSet<>(lobby.getGameState().getGame().getPlayersClockwise());

        lobby.setLobbyState(new LobbyStateStandBy(lobby, originalPlayers));
    }

    /**
     * executes the command
     * @param command command to execute
     */
    @Override
    public void onExecuteCommand(PlayerVisitorCommand command) {
        lobby.executeGameCommand(command);
    }

    /**
     * sends a game dto alongside a list of all game events
     */
    public void sendDefaultResponse() {
        GameDTO gameDTO = lobby.getGameState().getGame().getDTO();
        List<GameEvent> events = lobby.getBuffer().flush();
        lobby.updateAllPlayers(new GameStatusRes(gameDTO, events));
    }
}
