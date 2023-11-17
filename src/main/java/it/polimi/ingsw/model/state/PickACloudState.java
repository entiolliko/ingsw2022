package it.polimi.ingsw.model.state;

import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.visitor.base_commands.PrepareForNextRound;
import it.polimi.ingsw.model.visitor.base_commands.PrepareForNextTurn;
import it.polimi.ingsw.model.visitor.player_visitor_command.PlayerVisitorCommand;
import it.polimi.ingsw.model.Game;

import java.util.List;

public class PickACloudState extends ActionPhaseState {

    public PickACloudState(Game game, List<String> players, int currentPlayer) {
        super(game, players, currentPlayer);
        this.allowedCommands.add("PickCloud");
    }

    @Override
    protected State nextState(PlayerVisitorCommand command) {
        String commandName = getNameOfCommand(command);
        if ("PickCloud".equals(commandName)) {
            currentPlayer++;
            if (game.isGoingToBeOver() && wasLastPlayer())
                return new GameOverState(game, players);

            else if (currentPlayer >= players.size()) {
                prepareForNextRound();
                return new PlayApprenticeCardState(game);

            } else {
                prepareForNextTurn();
                return new PlaceTokensState(game, players, currentPlayer);
            }
        } else {
            return this; //Char Card was played
        }
    }

    private boolean wasLastPlayer() {
        return players.size() == currentPlayer;
    }

    private void prepareForNextTurn() {
        try {
            game.accept(new PrepareForNextTurn());
        } catch (ReloadGameException e) {
            throw new RuntimeException("supreme exception",e);
        }
    }

    private void prepareForNextRound() {
        try {
            game.accept(new PrepareForNextRound());
            if (game.getGameBoard().getBag().isEmpty())
                game.setIsGoingToBeOver(); //Il fill clouds viene fatto in automatico quindi dobbiamo anche controllare se le pedine sono finite.
        } catch (ReloadGameException e) {
            e.printStackTrace();
        }
    }
}
