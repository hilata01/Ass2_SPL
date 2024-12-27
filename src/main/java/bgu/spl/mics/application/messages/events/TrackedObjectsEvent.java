package bgu.spl.mics.application.messages.events;

import bgu.spl.mics.Event;

public class TrackedObjectsEvent<T> implements Event<T> {
    private final String source;
    private final String data;

    public TrackedObjectsEvent(String source, String data) {
        this.source = source;
        this.data = data;
    }

    public String getSource() {
        return source;
    }

    public String getData() {
        return data;
    }
}
