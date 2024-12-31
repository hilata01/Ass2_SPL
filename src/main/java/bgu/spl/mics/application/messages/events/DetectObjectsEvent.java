package bgu.spl.mics.application.messages.events;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.StampedDetectedObjects;
import com.sun.org.apache.xpath.internal.operations.Bool;

public class DetectObjectsEvent implements Event<Boolean> {
    private final int id;
    private final StampedDetectedObjects stampedDetectedObjects;

    /**
     * Constructor for DetectObjectsEvent.
     *
     * @param stampedDetectedObjects The stamped detected objects data for the event.
     */
    public DetectObjectsEvent(StampedDetectedObjects stampedDetectedObjects, int id) {
        this.stampedDetectedObjects = stampedDetectedObjects;
        this.id = id;
    }

    /**
     * Gets the stamped detected objects associated with this event.
     *
     * @return The StampedDetectedObjects instance.
     */
    public StampedDetectedObjects getStampedDetectedObjects() {
        return stampedDetectedObjects;
    }

    public int getDetectObjectsEventID(){ return id;}
}
