package it.polimi.ingsw.model;

import it.polimi.ingsw.debug_utility.DebugLogger;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventCreator;
import it.polimi.ingsw.model.game_event.GameEventReceiver;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;

public class ModelEventCreator implements GameEventCreator {
    protected Set<GameEventReceiver> gameEvents = new HashSet<>();

    private Set<GameEventReceiver> instance(){
        if(Objects.isNull(gameEvents)){
            gameEvents = new HashSet<>();
        }
        return gameEvents;
    }

    /**
     * Return all the game event receivers
     * @return The game event Receivers
     */
    @Override
    public Set<GameEventReceiver> popReceivers() {
        Set<GameEventReceiver> retval = gameEvents;
        this.gameEvents = new HashSet<>();
        return retval;
    }


    /**
     * Adds a game event receiver
     * @param eventObserver The Game Event Receiver to be added
     */
    @Override
    public void addEventObserver(GameEventReceiver eventObserver) {
        DebugLogger.log("added " + eventObserver, Level.INFO);
        instance().add(eventObserver);

    }

    /**
     * Notifies the observers of the game event
     * @param event The game event to be sent
     */
    public void notifyObservers(GameEvent event) {
        DebugLogger.log("notifying" + event, Level.FINEST);
        instance().forEach(receiver -> receiver.acceptEvent(event));
    }

}
