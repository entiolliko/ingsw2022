package it.polimi.ingsw.model.game_event.game_events;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

public class GameStartedEvent extends GsonablePrototype implements GameEvent {
    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.gameStarted(this);
    }
}
