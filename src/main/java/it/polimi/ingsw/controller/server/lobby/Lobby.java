package it.polimi.ingsw.controller.server.lobby;

import it.polimi.ingsw.controller.communication_protocol.ServerResponse;
import it.polimi.ingsw.controller.communication_protocol.server_responses.GameStatusRes;
import it.polimi.ingsw.controller.exceptions.InvalidArgsException;
import it.polimi.ingsw.controller.exceptions.NameAlreadyUsedException;
import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.controller.exceptions.ServerException;
import it.polimi.ingsw.controller.files_storage.GameSaver;
import it.polimi.ingsw.controller.utility.EventBuffer;
import it.polimi.ingsw.controller.server.connection.Connection;
import it.polimi.ingsw.controller.server.lobby.lobby_state.LobbyState;
import it.polimi.ingsw.controller.server.lobby.lobby_state.LobbyStateNoGame;
import it.polimi.ingsw.controller.server.lobby.lobby_state.LobbyStateStandBy;
import it.polimi.ingsw.debug_utility.DebugLogger;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import it.polimi.ingsw.model.game_event.game_events.CommandFailedEvent;
import it.polimi.ingsw.model.state.PlayApprenticeCardState;
import it.polimi.ingsw.model.state.State;
import it.polimi.ingsw.model.visitor.player_visitor_command.PlayerVisitorCommand;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Stream;

/**
 * a lobby contains all the relevant info for creating a game when the time is due (i.e., the desired number of players has been reached)
 * the lobby behaviour is determined by its state
 */
public abstract class Lobby implements ILobby {

    private final String gameID;
    private final int numberOfPlayers;
    private final TypeOfGame gameMode;
    private final Map<String, Connection> connectedPlayers;
    protected LobbyState lobbyState;
    private State gameState;
    private EventBuffer buffer;

    /**
     * creates a new lobby in a no game state
     * @param gameID given game id
     * @param numberOfPlayers desired number of players
     * @param gameMode desire game mode
     * @throws InvalidArgsException thrown when the number of desired players is none of the following: 2, 3 or 4
     */
    protected Lobby(String gameID, int numberOfPlayers, TypeOfGame gameMode) throws InvalidArgsException {

        this.gameID = gameID;


        this.numberOfPlayers = numberOfPlayers;
        if (!Set.of(2, 3, 4).contains(this.numberOfPlayers))
            throw new InvalidArgsException("invalid number of players! (" + this.numberOfPlayers + ")");
        this.gameMode = gameMode;
        this.connectedPlayers = new HashMap<>();

        this.lobbyState = new LobbyStateNoGame(this);
    }

    /**
     * used for loading a lobby with the given gameID
     * @param gameID gameID of the gameFile
     */
    protected Lobby(String gameID) {
        this.gameID = gameID;
        this.buffer = new EventBuffer();
        reloadGameState();
        DebugLogger.log("bro was " + this.gameID, Level.INFO);
        Game game = this.gameState.getGame();
        game.setGameID(this.gameID);
        saveGameState();
        DebugLogger.log("bro is " + this.gameState.getGame().getGameID(), Level.INFO);
        this.numberOfPlayers = game.getPlayersClockwise().size();
        this.gameMode = game.getGameBoard().getCharacterCardList().isEmpty() ? TypeOfGame.NORMAL : TypeOfGame.EXPERT;
        this.connectedPlayers = new HashMap<>();

        this.lobbyState = new LobbyStateStandBy(this, new HashSet<>(game.getPlayersClockwise()));



    }


    public synchronized void onAddPlayer(Connection sender) throws ServerException {
        lobbyState.onAddPlayer(sender);
    }

    public synchronized void onRemovePlayer(Connection toRemove) {
        lobbyState.onRemovePlayer(toRemove);
    }

    public synchronized void onExecuteCommand(PlayerVisitorCommand command) throws ServerException {
        lobbyState.onExecuteCommand(command);
    }

    /**
     * changes the state of the lobby to the desired one
     * @param lobbyState new LobbyState
     */
    public synchronized void setLobbyState(LobbyState lobbyState) {
        this.lobbyState = lobbyState;
    }

    /**
     * returns the lobby gameID
     * @return lobby gameID
     */
    public String getGameID() {
        return gameID;
    }

    /**
     * returns the number of players for the game
     * @return the number of players for the game
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * returns the game alongside the state machine
     * @return game state machine
     */
    public synchronized State getGameState() {
        return gameState;
    }

    /**
     * returns the collection of connected players
     * @return the collection of connected players
     */
    public synchronized Map<String, Connection> getConnectedPlayers() {
        return connectedPlayers;
    }

    /**
     * adds player to the lobby
     * @param sender player who wants to join
     * @throws NameAlreadyUsedException thrown if there is already a player with such a name
     */
    public synchronized void addPlayer(Connection sender) throws NameAlreadyUsedException {

        if (connectedPlayers.containsKey(sender.getName()))
            throw new NameAlreadyUsedException("name \"" + sender.getName() + "\" is already in use");
        connectedPlayers.put(sender.getName(), sender);
        DebugLogger.log("added new member: " + sender.getName(), Level.INFO);
    }

    /**
     * sends all players the given server response
     * @param response message to send
     */
    public synchronized void updateAllPlayers(ServerResponse response) {
        for (Connection playerConnection : connectedPlayers.values()) {
            DebugLogger.log("notifying " + playerConnection.getName(), Level.INFO);
            playerConnection.send(response);
        }
    }

    /**
     * creates the game instance and saves it on disk
     */
    public synchronized void startGameUp() {
        List<String> players = shuffleList(connectedPlayers.keySet().stream().toList());
        players = shuffleList(players);
        List<String> magicians = Stream.of("A", "B", "C", "D").limit(numberOfPlayers).toList();
        List<String> teams = numberOfPlayers == 3 ? List.of("A", "B", "C") : Stream.of("A", "B", "A", "B").limit(numberOfPlayers).toList();
        Game game = new Game(players, magicians, teams, gameID, gameMode);
        gameState = new PlayApprenticeCardState(game);

        this.buffer = new EventBuffer();
        game.addEventObserver(buffer);

        saveGameState();
    }

    /**
     * kicks a player from the lobby
     * @param toRemove player to remove
     */
    public synchronized void disconnectPlayer(Connection toRemove) {
        String nameToBeRemoved = toRemove.getName();
        connectedPlayers.remove(nameToBeRemoved);
        toRemove.disconnect();
    }


    private List<String> shuffleList(List<String> list) {
        ArrayList<String> temp = new ArrayList<>(list);
        Collections.shuffle(temp);
        return new ArrayList<>(temp);
    }

    /**
     * executes the given game command, if an error occurs during computation it reloads the game to a previous save.
     * This is safe because the last save is the game state just before the faulty command was sent.
     * @param command command to execute
     */
    public synchronized void executeGameCommand(PlayerVisitorCommand command) {
        DebugLogger.log("Lobby : received game command", Level.INFO);
        try {
            gameState = gameState.run(command);
            lobbyState.sendDefaultResponse();
        } catch (ReloadGameException e) {
            gameExceptionRoutine(command, e);
        } finally {
            saveGameState();
        }
    }

    private void gameExceptionRoutine(PlayerVisitorCommand command, ReloadGameException e) {
        e.printStackTrace();
        reloadGameState();
        buffer.empty();
        buffer.acceptEvent(new CommandFailedEvent(e.getMessage() + ", nothing has changed"));
        connectedPlayers.get(command.getPlayerID()).send(new GameStatusRes(gameState.getGame().getDTO(), buffer.flush()));
    }

    /**
     * saves the game state on disk
     */
    public void saveGameState() {
        GameSaver.saveGame(gameState);
    }

    /**
     * reloads the game to its last game save
     */
    public void reloadGameState() {
        DebugLogger.log("Reloading game in lobby#" + gameID, Level.WARNING);
        try {
            gameState = GameSaver.loadGame(gameID);
            gameState.getGame().addEventObserver(buffer);
        } catch (IOException e) {
            DebugLogger.log("WARNING! failed to load game" + gameID + "!!!", Level.SEVERE);
            e.printStackTrace();
        }
    }

    /**
     * passes the event buffer
     * @return the event buffer
     */
    public EventBuffer getBuffer() {
        return buffer;
    }
}

