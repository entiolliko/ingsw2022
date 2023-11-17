package it.polimi.ingsw.model.state;

import it.polimi.ingsw.model.visitor.player_visitor_command.PlayerVisitorCommand;
import it.polimi.ingsw.model.Game;

import java.util.List;

public class PlaceTokensState extends ActionPhaseState {
    private int placedTokens;

    public PlaceTokensState(Game game, List<String> players, int currentPlayer) {
        super(game, players, currentPlayer);
        this.allowedCommands.add("MoveToIsland");
        this.allowedCommands.add("MoveToStudyHall");
        this.placedTokens = 0;
    }

    @Override
    protected State nextState(PlayerVisitorCommand command) {
        String commandName = getNameOfCommand(command);
        if ("MoveToIsland".equals(commandName) || "MoveToStudyHall".equals(commandName)) {
            this.placedTokens++;
            if (this.placedTokens >= (players.size() == 3 ? 4 : 3)) {
                return new MoveMotherNatureState(game, players, currentPlayer);
            } else {
                return this;
            }
        } else {
            return this;
        }
    }
}
