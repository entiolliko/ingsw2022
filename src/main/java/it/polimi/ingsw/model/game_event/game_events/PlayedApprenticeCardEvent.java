package it.polimi.ingsw.model.game_event.game_events;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

public class PlayedApprenticeCardEvent extends GsonablePrototype implements GameEvent {
    String playerID;
    Integer cardID;

    public String getPlayerID() {
        return playerID;
    }

    public Integer getCardID() {
        return cardID;
    }

    public PlayedApprenticeCardEvent(String playerID, Integer cardID) {
        this.playerID = playerID;
        this.cardID = cardID;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.playedApprenticeCard(this);

    }
}
