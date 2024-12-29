package bgu.spl.mics.application.objects;

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
    private final List<StampedDetectedObjects> detectedObjectsList; // Time-stamped detected objects

    /**
     * Constructor for Camera.
     *
     * @param id                The unique ID of the camera.
     * @param frequency         The time interval (in ticks) at which the camera operates.
     * @param detectedObjectsList The list of stamped detected objects associated with this camera.
     */
    // The arguments for initialization will be filled from the configuration file
    public Camera(int id, int frequency, List<StampedDetectedObjects> detectedObjectsList) {
        this.id = id;
        this.frequency = frequency;
        this.status = STATUS.UP;
        this.detectedObjectsList = detectedObjectsList;
    }

    /**
     * Detects objects at the specified tick.
     *
     * @param tick The current tick.
     * @return A StampedDetectedObjects instance if objects are detected at this tick; otherwise, an empty instance.
     */
    public StampedDetectedObjects detectObjects(int tick) {
        if (status == STATUS.UP) {
            // Find the objects detected at the current tick
            for (StampedDetectedObjects stampedDetectedObjects : detectedObjectsList) {
                if (stampedDetectedObjects.getTimestamp() == tick) {
                    return stampedDetectedObjects;
                }
            }
        }

        // If status is not up or no objects are detected at the current tick, return an empty instance
        return new StampedDetectedObjects(tick, new ArrayList<>());
    }

    /**
     * Gets the unique ID of the camera.
     *
     * @return The camera's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the frequency at which the camera operates.
     *
     * @return The camera's frequency.
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Gets the operational status of the camera.
     *
     * @return The camera's status.
     */
    public STATUS getStatus() {
        return status;
    }

    /**
     * Sets the operational status of the camera.
     *
     * @param status The new status of the camera.
     */
    public void setStatus(STATUS status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Camera{" +
                "id=" + id +
                ", frequency=" + frequency +
                ", status=" + status +
                ", detectedObjectsList=" + detectedObjectsList +
                '}';
    }
}
