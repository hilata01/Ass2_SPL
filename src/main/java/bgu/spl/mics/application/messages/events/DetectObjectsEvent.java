package bgu.spl.mics.application.messages.events;
import bgu.spl.mics.Event;

public class DetectObjectsEvent<T> implements Event<T> {
    private final String sensorId;
    private final long timestamp;

    public DetectObjectsEvent(String sensorId, long timestamp) {
        this.sensorId = sensorId;
        this.timestamp = timestamp;
    }

    public String getSensorId() {
        return sensorId;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
