package it.polimi.ingsw.model.game_event;


import java.util.Set;

public interface GameEventCreator {
    Set<GameEventReceiver> popReceivers();
    void addEventObserver(GameEventReceiver eventObserver);
}
