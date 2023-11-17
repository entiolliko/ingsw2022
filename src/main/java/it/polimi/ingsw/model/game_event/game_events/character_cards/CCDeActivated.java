package it.polimi.ingsw.model.game_event.game_events.character_cards;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

public class CCDeActivated extends GsonablePrototype implements GameEvent {
    private final String cardName;

    public CCDeActivated(String cardName) {
        this.cardName = cardName;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.ccDeActivated(this);
    }

    public String getCardName() {
        return cardName;
    }
}
