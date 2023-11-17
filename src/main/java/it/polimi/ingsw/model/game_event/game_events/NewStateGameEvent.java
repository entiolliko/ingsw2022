package it.polimi.ingsw.model.game_event.game_events;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

import java.util.Objects;

public class NewStateGameEvent extends GsonablePrototype implements GameEvent {
    private final String newStateName;

    public NewStateGameEvent(String newStateName) {
        this.newStateName = newStateName;
    }

    public String getNewStateName() {
        return newStateName;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.newState(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NewStateGameEvent that = (NewStateGameEvent) o;
        return Objects.equals(newStateName, that.newStateName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), newStateName);
    }
}
