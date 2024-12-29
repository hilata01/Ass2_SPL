package bgu.spl.mics.application.messages.events;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.TrackedObject;

import java.util.List;

/**
 * Represents an event sent by the LiDAR worker to the Fusion-SLAM service.
 * Contains a list of tracked objects to be processed.
 */
public class TrackedObjectsEvent implements Event<List<TrackedObject>> {

    private final List<TrackedObject> trackedObjects;

    /**
     * Constructor for TrackedObjectsEvent.
     *
     * @param trackedObjects The list of tracked objects to include in the event.
     */
    public TrackedObjectsEvent(List<TrackedObject> trackedObjects) {
        this.trackedObjects = trackedObjects;
    }

    /**
     * Gets the list of tracked objects associated with this event.
     *
     * @return The list of tracked objects.
     */
    public List<TrackedObject> getTrackedObjects() {
        return trackedObjects;
    }

    @Override
    public String toString() {
        return "TrackedObjectsEvent{" +
                "trackedObjects=" + trackedObjects +
                '}';
    }
}
