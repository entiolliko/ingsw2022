package it.polimi.ingsw.model.game_event.game_events;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

import java.util.Objects;

public class TokenToStudyHallEvent extends GsonablePrototype implements GameEvent {
    private final String playerID;
    private final TokenEnum token;
    public TokenToStudyHallEvent(String playerID, TokenEnum tokenToMove) {
        this.playerID = playerID;
        this.token = tokenToMove;
    }

    public String getPlayerID() {
        return playerID;
    }

    public TokenEnum getToken() {
        return token;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.tokenToStudyHall(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TokenToStudyHallEvent that = (TokenToStudyHallEvent) o;
        return Objects.equals(playerID, that.playerID) && token == that.token;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), playerID, token);
    }
}
