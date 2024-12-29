package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a LiDAR tracker worker responsible for tracking objects and managing LiDAR data.
 * Tracks objects and sends observations to the Fusion-SLAM service.
 */
public class LiDarWorkerTracker {
    private final int id;
    private final int frequency; // Time interval at which the LiDar sends new events
    private STATUS status;
    private List<TrackedObject> lastTrackedObjects;

    /**
     * Constructor for LiDarTrackerWorker.
     *
     * @param id        The unique ID of the LiDAR tracker worker.
     * @param frequency The frequency (in ticks) at which the worker sends new events.
     */
    public LiDarWorkerTracker(int id, int frequency) {
        this.id = id;
        this.frequency = frequency;
        this.status = STATUS.UP;
        this.lastTrackedObjects = new ArrayList<>();
    }

    /**
     * Processes a DetectObjectsEvent by retrieving cloud points and creating tracked objects.
     *
     * @param detectedObjects The list of detected objects from the camera.
     * @param lidarDatabase   The LiDAR database containing cloud points.
     * @return A list of tracked objects created from the detected objects.
     */
    public List<TrackedObject> processDetectObjectsEvent(List<DetectedObject> detectedObjects, LiDarDataBase lidarDatabase) {
        List<TrackedObject> trackedObjects = new ArrayList<>();

        for (DetectedObject detectedObject : detectedObjects) {
            StampedCloudPoints cloudPoints = lidarDatabase.getCloudPoints(detectedObject.getId());

            if (cloudPoints != null) {
                TrackedObject trackedObject = new TrackedObject(
                        detectedObject.getId(),
                        cloudPoints.getTimestamp(),
                        detectedObject.getDescription(),
                        cloudPoints.getCloudPoints()
                );
                trackedObjects.add(trackedObject);
            }
        }

        lastTrackedObjects = trackedObjects;
        return trackedObjects;
    }

    /**
     * Updates the status of the LiDAR tracker based on certain conditions.
     *
     * @param tick The current tick of the system.
     */
    public void updateStatus(long tick) {
        if (tick % 100 == 0) {
            status = (status == STATUS.UP) ? STATUS.ERROR : STATUS.UP;
        }
    }

    /**
     * Gets the last tracked objects by this worker.
     *
     * @return The list of last tracked objects.
     */
    public List<TrackedObject> getLastTrackedObjects() {
        return lastTrackedObjects;
    }

    /**
     * Gets the ID of the LiDAR tracker worker.
     *
     * @return The worker's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the frequency of the LiDAR tracker worker.
     *
     * @return The worker's frequency.
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Gets the current status of the LiDAR tracker worker.
     *
     * @return The worker's status.
     */
    public STATUS getStatus() {
        return status;
    }
}
