package it.polimi.ingsw.model.game_event.game_events.character_cards;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

public class CardTokenToIslandEvent extends GsonablePrototype implements GameEvent {
    String cardName;
    TokenEnum token;
    int islandIndex;

    public CardTokenToIslandEvent(String cardName, TokenEnum token, int islandIndex) {
        this.cardName = cardName;
        this.token = token;
        this.islandIndex = islandIndex;
    }

    public String getCardName() {
        return cardName;
    }

    public TokenEnum getToken() {
        return token;
    }

    public int getIslandIndex() {
        return islandIndex;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.cardTokenToIsland(this);
    }
}
