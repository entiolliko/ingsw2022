package it.polimi.ingsw.model.state;

import it.polimi.ingsw.model.visitor.player_visitor_command.PlayerVisitorCommand;
import it.polimi.ingsw.model.Game;

import java.util.List;

public class MoveMotherNatureState extends ActionPhaseState {
    public MoveMotherNatureState(Game game, List<String> players, int currentPlayer) {
        super(game, players, currentPlayer);
        this.allowedCommands.add("MoveMotherNature");
    }

    @Override
    protected State nextState(PlayerVisitorCommand command) {

        String commandName = getNameOfCommand(command);
        if ("MoveMotherNature".equals(commandName)) {
            if (game.instantGameOverCondition())
                return new GameOverState(game, players);
            else
                return new PickACloudState(game, players, currentPlayer);
        } else {
            return this;
        }
    }
}
