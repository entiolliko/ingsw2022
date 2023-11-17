package it.polimi.ingsw.controller.server.lobby.lobby_state;

import it.polimi.ingsw.controller.communication_protocol.server_responses.LobbyStandbyRes;
import it.polimi.ingsw.controller.exceptions.LobbyFullException;
import it.polimi.ingsw.controller.exceptions.ServerException;
import it.polimi.ingsw.controller.server.connection.Connection;
import it.polimi.ingsw.controller.server.lobby.Lobby;
import it.polimi.ingsw.controller.utility.ConnectionStatusEnum;
import it.polimi.ingsw.debug_utility.DebugLogger;
import it.polimi.ingsw.model.visitor.player_visitor_command.PlayerVisitorCommand;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

/**
 * The lobby enters this state when during the game a player disconnects.
 * In this state the game is paused and no command can be sent
 * All players may leave, but only players who were in the lobby when the game started might join (i.e.: the original player)
 */
public class LobbyStateStandBy extends LobbyState {
    private final Set<String> playersOfTheGame;

    public LobbyStateStandBy(Lobby lobby, Set<String> players) {
        super(lobby);
        playersOfTheGame = new HashSet<>(players);
        sendDefaultMessage();
        DebugLogger.log("stand by...", Level.INFO);
    }

    /**
     * adds the player if they were among the original players; otherwise it throws an exception
     * @param sender player who wants to join
     * @throws ServerException thrown when the joiner isn't one of the original players
     */
    @Override
    public void onAddPlayer(Connection sender) throws ServerException {
        if (!playersOfTheGame.contains(sender.getName())) throw new LobbyFullException("you don't belong here");
        lobby.addPlayer(sender);

        if (lobby.getConnectedPlayers().keySet().containsAll(playersOfTheGame)) {
            lobby.setLobbyState(new LobbyStateGame(lobby));

        } else {
            sendDefaultMessage();
        }
    }

    /**
     * kicks player from the lobby
     * @param toRemove player to remove
     */
    @Override
    public void onRemovePlayer(Connection toRemove) {
        lobby.disconnectPlayer(toRemove);
        sendDefaultMessage();
    }

    /**
     * throws an exception, since the game is paused
     * @param command command to execute
     * @throws ServerException thrown because game is paused and commands cannot be issued
     */
    @Override
    public void onExecuteCommand(PlayerVisitorCommand command) throws ServerException {
        throw new ServerException("game is currently in stand-by, try later");
    }

    /**
     * sends all players a list of the original players alongside their connection status
     */
    @Override
    public void sendDefaultResponse() {
        sendDefaultMessage();
    }

    private void sendDefaultMessage() {
        DebugLogger.log("playersOfTheGame :" + playersOfTheGame + " vs currently connected :" + lobby.getConnectedPlayers().keySet(), Level.INFO);
        lobby.updateAllPlayers(new LobbyStandbyRes(lobby.getGameID(), createLobbyStandbyArg()));
    }

    private Map<String, ConnectionStatusEnum> createLobbyStandbyArg() {

        Map<String, ConnectionStatusEnum> returnValue = new HashMap<>();
        playersOfTheGame
                .forEach(player ->
                        returnValue
                                .put(player,
                                        lobby.getConnectedPlayers().containsKey(player) ? ConnectionStatusEnum.ONLINE : ConnectionStatusEnum.OFFLINE));
        return returnValue;
    }

}
