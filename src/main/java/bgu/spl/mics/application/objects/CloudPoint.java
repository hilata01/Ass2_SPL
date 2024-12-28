package bgu.spl.mics.application.objects;

/**
 * Represents a single point in 3D space as detected by the LiDAR.
 */
public class CloudPoint {

    private final double x;
    private final double y;

    /**
     * Constructor for CloudPoint.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     */
    public CloudPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x-coordinate of the point.
     *
     * @return The x-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the point.
     *
     * @return The y-coordinate.
     */
    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "CloudPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
