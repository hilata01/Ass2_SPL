package bgu.spl.mics.application.objects;

import java.util.List;

/**
 * Represents a group of cloud points corresponding to a specific timestamp.
 * Used by the LiDAR system to store and process point cloud data for tracked objects.
 */
public class StampedCloudPoints {
    private final String id;
    private final int timestamp; // The time the object was detected
    private final List<CloudPoint> cloudPoints;

    public StampedCloudPoints(String id, int timestamp, List<CloudPoint> cloudPoints) {
        this.id = id;
        this.timestamp = timestamp;
        this.cloudPoints = cloudPoints;
    }

    public String getId() {
        return id;
    }

    public int getTimestamp() {return timestamp;}

    public List<CloudPoint> getCloudPoints() {return cloudPoints;}

    @Override
    public String toString() {
        return "StampedCloudPoints{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", cloudPoints=" + cloudPoints +
                '}';
    }
}
