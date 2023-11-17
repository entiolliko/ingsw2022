package it.polimi.ingsw.model.game_event.game_events;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

import java.util.Objects;

public class MergedIslandsEvent extends GsonablePrototype implements GameEvent {
    private final int islandIndex1;
    private final int islandIndex2;

    public MergedIslandsEvent(int islandIndex1, int islandIndex2) {
        this.islandIndex1 = islandIndex1;
        this.islandIndex2 = islandIndex2;
    }

    public int getIslandIndex1() {
        return islandIndex1;
    }

    public int getIslandIndex2() {
        return islandIndex2;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {

        eventHandler.mergedIslands(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MergedIslandsEvent that = (MergedIslandsEvent) o;
        return islandIndex1 == that.islandIndex1 && islandIndex2 == that.islandIndex2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), islandIndex1, islandIndex2);
    }
}
