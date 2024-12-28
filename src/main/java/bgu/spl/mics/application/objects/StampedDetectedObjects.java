package bgu.spl.mics.application.objects;

import java.util.List;

/**
 * Represents objects detected by the camera at a specific timestamp.
 * Includes the time of detection and a list of detected objects.
 */
public class StampedDetectedObjects {

    private final long timestamp;
    private final List<DetectedObject> detectedObjects;

    /**
     * Constructor for StampedDetectedObjects.
     *
     * @param timestamp       The time the objects were detected.
     * @param detectedObjects The list of detected objects at the given time.
     */
    public StampedDetectedObjects(long timestamp, List<DetectedObject> detectedObjects) {
        this.timestamp = timestamp;
        this.detectedObjects = detectedObjects;
    }

    /**
     * Gets the timestamp of detection.
     *
     * @return The timestamp.
     */
    public long getTimestamp() {
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
