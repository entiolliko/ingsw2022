package it.polimi.ingsw.model.game_event.game_events;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

import java.util.List;
import java.util.Objects;

public class GameIsOverEvent extends GsonablePrototype implements GameEvent {
    private final List<String> winner;

    public GameIsOverEvent(List<String> winner) {
        this.winner = winner;
    }

    public List<String> getWinner() {
        return winner;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.gameIsOver(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GameIsOverEvent that = (GameIsOverEvent) o;
        return Objects.equals(winner, that.winner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), winner);
    }
}
