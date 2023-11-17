package it.polimi.ingsw.model.game_event.game_events;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

import java.util.Objects;

public class MoveMotherNatureEvent extends GsonablePrototype implements GameEvent {
    private final Integer from;
    private final Integer to;

    public MoveMotherNatureEvent(Integer from, Integer to) {
        this.from = from;
        this.to = to;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.moveMotherNature(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MoveMotherNatureEvent that = (MoveMotherNatureEvent) o;
        return Objects.equals(from, that.from) && Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), from, to);
    }
}
