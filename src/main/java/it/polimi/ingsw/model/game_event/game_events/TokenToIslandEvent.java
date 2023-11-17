package it.polimi.ingsw.model.game_event.game_events;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

import java.util.Objects;

public class TokenToIslandEvent extends GsonablePrototype implements GameEvent {
    private final TokenEnum token;
    private final Integer islandIndex;
    private final String playerID;

    public TokenToIslandEvent(String playerID, Integer islandIndex, TokenEnum toMove) {
        this.token = toMove;
        this.islandIndex = islandIndex;
        this.playerID = playerID;
    }

    public TokenEnum getToken() {
        return token;
    }

    public Integer getIslandIndex() {
        return islandIndex;
    }

    public String getPlayerID() {
        return playerID;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.tokenToIsland(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TokenToIslandEvent that = (TokenToIslandEvent) o;
        return token == that.token && Objects.equals(islandIndex, that.islandIndex) && Objects.equals(playerID, that.playerID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), token, islandIndex, playerID);
    }
}
