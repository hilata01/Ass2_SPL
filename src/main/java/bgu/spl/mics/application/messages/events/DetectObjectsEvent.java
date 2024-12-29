package bgu.spl.mics.application.messages.events;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.StampedDetectedObjects;

public class DetectObjectsEvent<T> implements Event<T> {
    private final StampedDetectedObjects stampedDetectedObjects;

    /**
     * Constructor for DetectObjectsEvent.
     *
     * @param stampedDetectedObjects The stamped detected objects data for the event.
     */
    public DetectObjectsEvent(StampedDetectedObjects stampedDetectedObjects) {
        this.stampedDetectedObjects = stampedDetectedObjects;
    }

    /**
     * Gets the stamped detected objects associated with this event.
     *
     * @return The StampedDetectedObjects instance.
     */
    public StampedDetectedObjects getStampedDetectedObjects() {
        return stampedDetectedObjects;
    }
}
