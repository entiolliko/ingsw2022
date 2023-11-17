package it.polimi.ingsw.model.game_event.game_events;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

import java.util.Map;
import java.util.Objects;

public class BagToCloudEvent extends GsonablePrototype implements GameEvent {
    private final int cloudIndex;
    private final Map <TokenEnum, Integer> tokens;

    public BagToCloudEvent(int cloudIndex, Map<TokenEnum, Integer> tokens) {
        this.cloudIndex = cloudIndex;
        this.tokens = tokens;
    }

    public int getCloudIndex() {
        return cloudIndex;
    }

    public Map<TokenEnum, Integer> getTokens() {
        return tokens;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.bagToCloud(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BagToCloudEvent that = (BagToCloudEvent) o;
        return cloudIndex == that.cloudIndex && tokens == that.tokens;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cloudIndex, tokens);
    }
}
