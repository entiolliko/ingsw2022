package it.polimi.ingsw.model.game_event.game_events;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

import java.util.Objects;

public class CurrentPlayerChangedGameEvent extends GsonablePrototype implements GameEvent {
    private final String currentPlayer;

    public CurrentPlayerChangedGameEvent(String player) {
        this.currentPlayer = player;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.currentPlayerChanged(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CurrentPlayerChangedGameEvent that = (CurrentPlayerChangedGameEvent) o;
        return Objects.equals(currentPlayer, that.currentPlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), currentPlayer);
    }
}
