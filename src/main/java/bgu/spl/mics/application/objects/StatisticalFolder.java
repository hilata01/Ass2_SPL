package bgu.spl.mics.application.objects;

/**
 * Holds statistical information about the system's operation.
 * This class aggregates metrics such as the runtime of the system,
 * the number of objects detected and tracked, and the number of landmarks identified.
 */
public class StatisticalFolder {
    private int systemRuntime; // Total runtime of the system, measured in ticks
    private int numDetectedObjects; // Count of objects detected in all cameras
    private int numTrackedObjects; // Count of objects tracked by all LiDAR workers
    private int numLandmarks; // Count of all landmarks identified and mapped

    /**
     * Constructor for StatisticalFolder.
     * Initializes all metrics to zero.
     */
    public StatisticalFolder() {
        this.systemRuntime = 0;
        this.numDetectedObjects = 0;
        this.numTrackedObjects = 0;
        this.numLandmarks = 0;
    }

    /**
     * Increments the system runtime by a specified number of ticks.
     *
     * @param ticks The number of ticks to add.
     */
    public synchronized void incrementSystemRuntime(int ticks) {
        this.systemRuntime += ticks;
    }

    /**
     * Increments the number of detected objects by a specified amount.
     *
     * @param count The number of objects detected.
     */
    public synchronized void incrementDetectedObjects(int count) {
        this.numDetectedObjects += count;
    }

    /**
     * Increments the number of tracked objects by a specified amount.
     *
     * @param count The number of objects tracked.
     */
    public synchronized void incrementTrackedObjects(int count) {
        this.numTrackedObjects += count;
    }

    /**
     * Increments the number of landmarks by a specified amount.
     *
     * @param count The number of new landmarks identified.
     */
    public synchronized void incrementLandmarks(int count) {
        this.numLandmarks += count;
    }

    /**
     * Gets the total system runtime in ticks.
     *
     * @return The system runtime.
     */
    public synchronized int getSystemRuntime() {
        return systemRuntime;
    }

    /**
     * Gets the total number of detected objects.
     *
     * @return The number of detected objects.
     */
    public synchronized int getNumDetectedObjects() {
        return numDetectedObjects;
    }

    /**
     * Gets the total number of tracked objects.
     *
     * @return The number of tracked objects.
     */
    public synchronized int getNumTrackedObjects() {
        return numTrackedObjects;
    }

    /**
     * Gets the total number of landmarks identified.
     *
     * @return The number of landmarks.
     */
    public synchronized int getNumLandmarks() {
        return numLandmarks;
    }
}
