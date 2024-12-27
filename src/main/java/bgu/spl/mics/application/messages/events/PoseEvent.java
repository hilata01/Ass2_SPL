package bgu.spl.mics.application.messages.events;

import bgu.spl.mics.Event;

public class PoseEvent<T> implements Event<T> {
    private final String robotId;
    private final long timestamp;

    public PoseEvent(String robotId, long timestamp) {
        this.robotId = robotId;
        this.timestamp = timestamp;
    }

    public String getRobotId() {
        return robotId;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
