package it.polimi.ingsw.model.game_event.game_events;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

public class CoinsChangedEvent extends GsonablePrototype implements GameEvent {
    private final String playerId;
    private final int currentValue;

    public CoinsChangedEvent(String playerId, int currentValue) {
        this.playerId = playerId;
        this.currentValue = currentValue;
    }

    public String getPlayerId() {
        return playerId;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.coinsChanged(this);
    }
}
