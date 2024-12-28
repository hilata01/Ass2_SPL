package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.objects.STATUS;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a camera sensor on the robot.
 * Responsible for detecting objects in the environment.
 */
public class Camera {

    private final int id;
    private final int frequency;
    private STATUS status;
    private final List<StampedDetectedObjects> detectedObjectsList;

    /**
     * Constructor for Camera.
     *
     * @param id          The unique ID of the camera.
     * @param frequency   The time interval (in ticks) at which the camera operates.
     */
    public Camera(int id, int frequency) {
        this.id = id;
        this.frequency = frequency;
        this.status = STATUS.UP;
        this.detectedObjectsList = new ArrayList<>();
    }

    /**
     * Detects objects based on the current state of the camera.
     *
     * @return A list of DetectedObject instances if the camera is operational; an empty list otherwise.
     */
    public List<DetectedObject> detectObjects() {
        if (status != STATUS.UP) {
            return new ArrayList<>();
        }

        // Simulate detection logic (populate detectedObjectsList with dummy data for now)
        List<DetectedObject> detectedObjects = new ArrayList<>();

        detectedObjects.add(new DetectedObject("Object1", "Description1"));
        detectedObjects.add(new DetectedObject("Object2", "Description2"));

        // Add timestamped detections
        detectedObjectsList.add(new StampedDetectedObjects(System.currentTimeMillis(), detectedObjects));

        return detectedObjects;
    }

    /**
     * Gets the ID of the camera.
     *
     * @return The camera's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the frequency of the camera.
     *
     * @return The camera's frequency.
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Gets the operational status of the camera.
     *
     * @return The camera's current STATUS.
     */
    public STATUS getStatus() {
        return status;
    }

    /**
     * Sets the operational status of the camera.
     *
     * @param status The new status for the camera.
     */
    public void setStatus(STATUS status) {
        this.status = status;
    }

    /**
     * Gets the list of all detected objects with timestamps.
     *
     * @return A list of StampedDetectedObjects.
     */
    public List<StampedDetectedObjects> getDetectedObjectsList() {
        return detectedObjectsList;
    }
}
