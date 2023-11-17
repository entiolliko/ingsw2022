package it.polimi.ingsw.controller.ping_protocol;

import it.polimi.ingsw.debug_utility.DebugLogger;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public abstract class RegularExecutor {
    protected ScheduledExecutorService executor;
    private static final int TIME_INTERVAL = 1000;

    protected abstract void timeTick();

    public synchronized void activate() {
        DebugLogger.log("booting up", Level.FINEST);
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::timeTick, 500, TIME_INTERVAL, TimeUnit.MILLISECONDS);
    }

    public synchronized void deactivate() {
        DebugLogger.log("shutting down", Level.FINEST);
        if (!Objects.isNull(executor))
            executor.shutdownNow();
    }

}
