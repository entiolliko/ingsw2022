package it.polimi.ingsw.model.game_event.game_events.character_cards;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

public class CardTokenToStudyHallEvent extends GsonablePrototype implements GameEvent {

    String cardName;
    TokenEnum token;
    String playerID;

    public CardTokenToStudyHallEvent(String cardName, TokenEnum token, String playerID) {
        this.cardName = cardName;
        this.token = token;
        this.playerID = playerID;
    }

    public String getCardName() {
        return cardName;
    }

    public TokenEnum getToken() {
        return token;
    }

    public String getPlayerID() {
        return playerID;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.cardTokenToStudyHall(this);
    }
}
