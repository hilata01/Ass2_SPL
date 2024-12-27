package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.broadcasts.TerminatedBroadcast;
import bgu.spl.mics.application.messages.broadcasts.TickBroadcast;

/**
 * TimeService acts as the global timer for the system, broadcasting TickBroadcast messages
 * at regular intervals and controlling the simulation's duration.
 */
public class TimeService extends MicroService {
    private int tickTime;
    private int duration;

    /**
     * Constructor for TimeService.
     *
     * @param TickTime  The duration of each tick in milliseconds.
     * @param Duration  The total number of ticks before the service terminates.
     */
    public TimeService(int TickTime, int Duration) {
        super("TimeService");
        this.tickTime = TickTime;
        this.duration = Duration;
    }

    /**
     * Initializes the TimeService.
     * Starts broadcasting TickBroadcast messages and terminates after the specified duration.
     */
    @Override
    protected void initialize() {
        Thread thread = new Thread(() -> {
            try {
                for (int tick = 1; tick <= duration; tick++) {
                    sendBroadcast(new TickBroadcast(tick));
                    Thread.sleep(tickTime);
                }
                sendBroadcast(new TerminatedBroadcast("TimeService completed"));
                terminate();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        thread.start();

        // Subscribe to TerminatedBroadcast to terminate gracefully
        subscribeBroadcast(TerminatedBroadcast.class, broadcast -> terminate());
    }
}
