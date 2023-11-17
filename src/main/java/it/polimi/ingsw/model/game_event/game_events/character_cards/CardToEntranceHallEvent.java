package it.polimi.ingsw.model.game_event.game_events.character_cards;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

import java.util.Map;

public class CardToEntranceHallEvent extends GsonablePrototype implements GameEvent {
    Map<TokenEnum, Integer> tokens;
    String cardName;
    String playerID;

    public CardToEntranceHallEvent(String playerID, Map<TokenEnum, Integer> tokens, String cardName) {
        this.tokens = tokens;
        this.cardName = cardName;
        this.playerID = playerID;
    }

    public Map<TokenEnum, Integer> getTokens() {
        return tokens;
    }

    public String getCardName() {
        return cardName;
    }

    public String getPlayerID() {
        return playerID;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.cardToEntranceHall(this);
    }
}
