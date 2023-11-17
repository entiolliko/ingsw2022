package it.polimi.ingsw.controller.ping_protocol;

public abstract class TimeBomb extends RegularExecutor {
    protected int timer;
    protected static final int MAX_COUNT_DOWN = 3;

    protected TimeBomb() {
        super();
        this.timer = MAX_COUNT_DOWN;
    }
    protected synchronized void timeTick() {
        if (timer == 0){
            explode();
            deactivate();
        }
        else timer --;
    }
    @Override
    public synchronized void activate() {
        refresh();
        super.activate();
    }
    protected abstract void explode();

    public synchronized void refresh() {
        timer = MAX_COUNT_DOWN;
    }
}
