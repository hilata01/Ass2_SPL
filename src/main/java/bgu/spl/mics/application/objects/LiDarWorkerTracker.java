package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a LiDAR worker that processes detected objects and converts them into tracked objects.
 */
public class LiDarWorkerTracker {

    private final int id;
    private final int frequency;
    private STATUS status;

    /**
     * Constructor for LiDarWorkerTracker.
     *
     * @param id        The unique ID of the LiDAR worker.
     * @param frequency The time interval (in ticks) at which the LiDAR processes data.
     */
    public LiDarWorkerTracker(int id, int frequency) {
        this.id = id;
        this.frequency = frequency;
        this.status = STATUS.UP;
    }

    /**
     * Processes detected objects from the camera and converts them into tracked objects.
     *
     * @param detectedObjects The detected objects with a timestamp.
     * @return A list of tracked objects.
     */
    public List<TrackedObject> processDetectedObjects(StampedDetectedObjects detectedObjects) {
        List<TrackedObject> trackedObjects = new ArrayList<>();

        // Simulate processing of each detected object
        for (DetectedObject detectedObject : detectedObjects.getDetectedObjects()) {
            TrackedObject trackedObject = new TrackedObject(
                    detectedObject.getId(),
                    detectedObjects.getTimestamp(),
                    detectedObject.getDescription(),
                    generateCoordinates()
            );
            trackedObjects.add(trackedObject);
        }

        return trackedObjects;
    }

    /**
     * Updates the status of the LiDAR worker based on the current tick.
     *
     * @param tick The current tick.
     */
    public void updateStatus(long tick) {
        // Example logic: If tick is a multiple of some value, simulate a status change
        if (tick % 100 == 0) {
            this.status = (this.status == STATUS.UP) ? STATUS.ERROR : STATUS.UP;
        }
    }

    /**
     * Generates dummy coordinates for a tracked object.
     *
     * @return A list of cloud points representing the coordinates.
     */
    private List<CloudPoint> generateCoordinates() {
        List<CloudPoint> coordinates = new ArrayList<>();
        coordinates.add(new CloudPoint(1.0, 2.0));
        coordinates.add(new CloudPoint(3.0, 4.0));
        return coordinates;
    }

    /**
     * Gets the ID of the LiDAR worker.
     *
     * @return The ID of the LiDAR worker.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the processing frequency of the LiDAR worker.
     *
     * @return The processing frequency.
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Gets the current status of the LiDAR worker.
     *
     * @return The current status.
     */
    public STATUS getStatus() {
        return status;
    }

    /**
     * Sets the status of the LiDAR worker.
     *
     * @param status The new status.
     */
    public void setStatus(STATUS status) {
        this.status = status;
    }
}
