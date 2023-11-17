package it.polimi.ingsw.model.state;

import it.polimi.ingsw.model.visitor.player_visitor_command.PlayerVisitorCommand;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.game_event.game_events.GameIsOverEvent;

import java.util.List;

public class GameOverState extends State {
    public GameOverState(Game game, List<String> players) {
        super(game, players);
        game.notifyObservers(new GameIsOverEvent(game.getLeads()));
    }

    @Override
    protected State nextState(PlayerVisitorCommand command) {
        return this;
    }

    @Override
    public synchronized boolean isOver() {
        return true;
    }
}
