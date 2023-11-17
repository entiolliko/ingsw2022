package it.polimi.ingsw.model.state;

import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.visitor.VisitorCommand;
import it.polimi.ingsw.model.visitor.player_visitor_command.PlayerVisitorCommand;
import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.debug_utility.DebugLogger;
import it.polimi.ingsw.model.Game;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

public abstract class State extends GsonablePrototype {
    protected Game game;
    protected Set<String> allowedCommands;
    protected List<String> players;
    protected int currentPlayer;

    protected State(Game game, List<String> players) {
        this.game = game;
        allowedCommands = new TreeSet<>();
        this.players = players;
        this.currentPlayer = 0;
    }

    protected static String getNameOfCommand(VisitorCommand command) {
        return command.getClass().getSimpleName();
    }

    /**
     * Runs the given command as per the Visitor Command
     * @param command The command to be run
     * @return The new state after the command was run
     * @throws ReloadGameException Thrown when there is an intern error
     */
    public synchronized State run(PlayerVisitorCommand command) throws ReloadGameException {
        String currentPlayerName = players.get(currentPlayer);
        DebugLogger.log("expected " + players.get(currentPlayer) + "but was " + command.getPlayerID(), Level.WARNING);
        DebugLogger.log("mylist" + players + "????" + game.getPlayersClockwise(), Level.WARNING);
        String commandSender = command.getPlayerID();
        State next;
        if (!currentPlayerName.equals(commandSender)){
            throw new ReloadGameException("hey man let "+currentPlayerName+" play");
        }
        if (!this.allows(command)){
            throw new ReloadGameException("NICE TRY but no :)");
        }
        game.accept(command);
        next = this.nextState(command);
        game.setCurrentState(next.getClass().getSimpleName());
        game.setCurrentPlayer(next.getCurrentPlayer());
        return next;
    }

    protected boolean allows(PlayerVisitorCommand command) {
        return allowedCommands.contains(getNameOfCommand(command));
    }

    protected abstract State nextState(PlayerVisitorCommand command);

    protected Set<String> getAllowedCommands() {
        return allowedCommands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return currentPlayer == state.currentPlayer && Objects.equals(game, state.game) && Objects.equals(allowedCommands, state.allowedCommands) && Objects.equals(players, state.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, allowedCommands, players, currentPlayer);
    }

    @Override
    public String toString() {
        return "State{" +
                "game=" + game +
                ", allowedCommands=" + allowedCommands +
                ", players=" + players +
                ", currentPlayer=" + currentPlayer +
                '}';
    }

    public synchronized String getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    public synchronized Game getGame() {
        return game;
    }

    public synchronized void setGame(Game game) {
        this.game = game;
    }
    public synchronized boolean isOver() {
        return false;
    }
}
