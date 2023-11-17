package it.polimi.ingsw.model.game_event.game_events;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;
import it.polimi.ingsw.model.islands.TowerEnum;

import java.util.Objects;

public class TowersToDashBoardEvent extends GsonablePrototype implements GameEvent {
    private final TowerEnum tower;
    private final String playerID;
    private final Integer island;

    public TowersToDashBoardEvent(TowerEnum tower, String playerID, Integer island) {
        this.tower = tower;
        this.playerID = playerID;
        this.island = island;
    }

    public TowerEnum getTower() {
        return tower;
    }

    public String getPlayerID() {
        return playerID;
    }

    public Integer getIsland() {
        return island;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.towersToDashBoard(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TowersToDashBoardEvent that = (TowersToDashBoardEvent) o;
        return tower == that.tower && Objects.equals(playerID, that.playerID) && Objects.equals(island, that.island);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tower, playerID, island);
    }
}
