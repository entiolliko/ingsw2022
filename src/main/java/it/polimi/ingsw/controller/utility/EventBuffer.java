package it.polimi.ingsw.controller.utility;

import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventReceiver;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * buffer that stores game events ordered from first to last
 */
public class EventBuffer implements GameEventReceiver {
    private Deque<GameEvent> buffer;

    /**
     * creates a new buffer
     */
    public EventBuffer() {
        this.buffer = new ArrayDeque<>();
    }

    /**
     * empties the buffer
     */
    public synchronized void empty() {
        buffer = new ArrayDeque<>();
    }

    /**
     * returns the list of all events saved ordered from first to last
     * @return list of all events
     */
    public synchronized List<GameEvent> flush() {
        List<GameEvent> returnValue = new LinkedList<>();
        while (!buffer.isEmpty()) returnValue.add(buffer.pollFirst());
        return returnValue;
    }

    /**
     * adds an event to the list
     * @param event new event
     */
    @Override
    public synchronized void acceptEvent(GameEvent event) {
        buffer.add(event);
    }

}
