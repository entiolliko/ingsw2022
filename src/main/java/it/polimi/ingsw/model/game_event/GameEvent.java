package it.polimi.ingsw.model.game_event;

import it.polimi.ingsw.custom_json_builder.Gsonable;

public interface GameEvent extends Gsonable {
    /**
     * Visits the GameEventHandler as per the Visitor Pattern
     * @param eventHandler The GameEventHandler to be visited
     */
    void visit(GameEventHandler eventHandler);
}
