package it.polimi.ingsw.model.game_event.game_events.character_cards;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

public class BagTokenToCardEvent extends GsonablePrototype implements GameEvent {
    TokenEnum token;
    String cardName;

    public BagTokenToCardEvent(TokenEnum token, String cardName) {
        this.token = token;
        this.cardName = cardName;
    }

    public TokenEnum getToken() {
        return token;
    }

    public String getCardName() {
        return cardName;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.bagToCard(this);
    }
}
