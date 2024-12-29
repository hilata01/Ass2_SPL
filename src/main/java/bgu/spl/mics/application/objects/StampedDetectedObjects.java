package bgu.spl.mics.application.objects;

import java.util.List;

/**
 * Represents objects detected by the camera at a specific timestamp.
 * Includes the time of detection and a list of detected objects.
 */
public class StampedDetectedObjects {
    private final int timestamp; // The time the object was detected
    private final List<DetectedObject> detectedObjects; // List of objects that were detected at 'timestamp'

    /**
     * Constructor for StampedDetectedObjects.
     *
     * @param timestamp       The time the objects were detected.
     * @param detectedObjects The list of detected objects at the given time.
     */
    public StampedDetectedObjects(int timestamp, List<DetectedObject> detectedObjects) {
        this.timestamp = timestamp;
        this.detectedObjects = detectedObjects;
    }

    /**
     * Gets the timestamp of detection.
     *
     * @return The timestamp.
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the list of detected objects.
     *
     * @return The list of detected objects.
     */
    public List<DetectedObject> getDetectedObjects() {
        return detectedObjects;
    }
}
