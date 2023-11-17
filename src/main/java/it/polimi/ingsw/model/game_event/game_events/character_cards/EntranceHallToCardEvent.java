package it.polimi.ingsw.model.game_event.game_events.character_cards;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

import java.util.Map;

public class EntranceHallToCardEvent extends GsonablePrototype implements GameEvent {
    String cardName;
    Map<TokenEnum, Integer> tokens;

    public EntranceHallToCardEvent(String cardName, Map<TokenEnum, Integer> tokens, String playerID) {
        this.cardName = cardName;
        this.tokens = tokens;
        this.playerID = playerID;
    }

    public String getCardName() {
        return cardName;
    }

    public Map<TokenEnum, Integer> getTokens() {
        return tokens;
    }

    public String getPlayerID() {
        return playerID;
    }

    String playerID;
    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.entranceHallToCard(this);
    }
}
