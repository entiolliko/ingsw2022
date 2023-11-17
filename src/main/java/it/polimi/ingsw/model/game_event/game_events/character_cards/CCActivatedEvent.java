package it.polimi.ingsw.model.game_event.game_events.character_cards;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

public class CCActivatedEvent extends GsonablePrototype implements GameEvent {
    private final String cardName;
    private final String description;

    public CCActivatedEvent(String cardName) {
        this(cardName, "somebody here loves to waste money");
    }

    public CCActivatedEvent(String cardName, String description) {
        this.cardName = cardName;
        this.description = description;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.ccActivated(this);
    }

    public String getCardName() {
        return cardName;
    }

    public String getDescription() {
        return description;
    }
}
