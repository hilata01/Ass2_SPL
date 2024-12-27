package bgu.spl.mics.application.messages.broadcasts;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
    private final long tick;

    public TickBroadcast(long tick) {
        this.tick = tick;
    }

    public long getTick() {
        return tick;
    }
}
