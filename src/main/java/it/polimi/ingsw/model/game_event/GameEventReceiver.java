package it.polimi.ingsw.model.game_event;

public interface GameEventReceiver {
    /**
     * Accepts the GameEvent as per the Visitor Pattern
     * @param event The event to be accepted
     */
    void acceptEvent(GameEvent event);
}
