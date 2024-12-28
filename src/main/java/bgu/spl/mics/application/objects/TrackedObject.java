package bgu.spl.mics.application.objects;

import java.util.List;

/**
 * Represents an object tracked by the LiDAR.
 * Contains details such as the object's ID, timestamp, description, and coordinates.
 */
public class TrackedObject {

    private final String id;
    private final long timestamp;
    private final String description;
    private final List<CloudPoint> coordinates;

    /**
     * Constructor for TrackedObject.
     *
     * @param id          The unique ID of the tracked object.
     * @param timestamp   The time the object was tracked.
     * @param description A description of the tracked object.
     * @param coordinates The list of coordinates (cloud points) associated with the object.
     */
    public TrackedObject(String id, long timestamp, String description, List<CloudPoint> coordinates) {
        this.id = id;
        this.timestamp = timestamp;
        this.description = description;
        this.coordinates = coordinates;
    }

    /**
     * Gets the ID of the tracked object.
     *
     * @return The object's ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the timestamp of when the object was tracked.
     *
     * @return The timestamp.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the description of the tracked object.
     *
     * @return The object's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the list of coordinates (cloud points) of the tracked object.
     *
     * @return The object's coordinates.
     */
    public List<CloudPoint> getCoordinates() {
        return coordinates;
    }

    @Override
    public String toString() {
        return "TrackedObject{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", description='" + description + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }
}
