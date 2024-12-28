package bgu.spl.mics.application.objects;

/**
 * Represents an object detected by the camera.
 * Contains details such as the object's ID and description.
 */
public class DetectedObject {

    private final String id;
    private final String description;

    /**
     * Constructor for DetectedObject.
     *
     * @param id          The unique ID of the detected object.
     * @param description A description of the detected object.
     */
    public DetectedObject(String id, String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * Gets the ID of the detected object.
     *
     * @return The object's ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the description of the detected object.
     *
     * @return The object's description.
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "DetectedObject{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
