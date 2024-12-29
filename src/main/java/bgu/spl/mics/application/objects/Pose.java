package bgu.spl.mics.application.objects;

/**
 * Represents the robot's pose (position and orientation) in the environment.
 * Includes x, y coordinates and the yaw angle relative to a global coordinate system.
 */
public class Pose {
    private float x;
    private float y;
    private float yaw; // The orientation angle relative to the charging station's coordinate system
    private int timestamp; // The time when the robot reached the pose

    public Pose(float x, float y, float yaw, int timestamp) {
        this.x = x;
        this.y = y;
        this.yaw = yaw;
        this.timestamp = timestamp;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getYaw() {
        return yaw;
    }

    public int getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Pose{" +
                "x=" + x +
                ", y=" + y +
                ", yaw=" + yaw +
                ", timestamp=" + timestamp +
                '}';
    }
}
