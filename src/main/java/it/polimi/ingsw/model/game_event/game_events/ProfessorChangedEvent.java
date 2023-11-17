package it.polimi.ingsw.model.game_event.game_events;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

public class ProfessorChangedEvent  extends GsonablePrototype implements GameEvent {

    private final String playerID;
    private final TokenEnum token;

    public String getPlayerID() {
        return playerID;
    }

    public TokenEnum getToken() {
        return token;
    }

    public ProfessorChangedEvent(TokenEnum token, String playerID) {
        this.token = token;
        this.playerID = playerID;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {
        eventHandler.professorChanged(this);
    }
}
