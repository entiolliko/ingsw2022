package it.polimi.ingsw.model.state;

import it.polimi.ingsw.model.visitor.player_visitor_command.PlayerVisitorCommand;
import it.polimi.ingsw.model.Game;

public class PlayApprenticeCardState extends State {

    public PlayApprenticeCardState(Game game) {
        super(game, game.getPlayersClockwise());
        this.allowedCommands.add("PlayApprenticeCard");
    }

    @Override
    protected State nextState(PlayerVisitorCommand command) {
        currentPlayer++;
        if (currentPlayer >= players.size()) {
            return new PlaceTokensState(game, game.getOrderedPlayers(), 0);
        } else {
            return this;
        }
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
