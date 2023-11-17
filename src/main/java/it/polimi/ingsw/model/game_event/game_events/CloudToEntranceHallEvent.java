package it.polimi.ingsw.model.game_event.game_events;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

import java.util.Map;
import java.util.Objects;

public class CloudToEntranceHallEvent extends GsonablePrototype implements GameEvent {
    private final int cloudIndex;
    private final String receivingPlayer;
    private final Map<TokenEnum, Integer> content;

    public CloudToEntranceHallEvent(int cloudIndex, String receivingPlayer, Map<TokenEnum, Integer> content) {
        this.cloudIndex = cloudIndex;
        this.receivingPlayer = receivingPlayer;
        this.content = content;
    }

    public int getCloudIndex() {
        return cloudIndex;
    }

    public String getReceivingPlayer() {
        return receivingPlayer;
    }

    public Map<TokenEnum, Integer> getContent() {
        return content;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.cloudToEntranceHall(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CloudToEntranceHallEvent that = (CloudToEntranceHallEvent) o;
        return cloudIndex == that.cloudIndex && Objects.equals(receivingPlayer, that.receivingPlayer) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cloudIndex, receivingPlayer, content);
    }
}
