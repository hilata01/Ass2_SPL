package bgu.spl.mics.application.objects;

import java.util.List;

/**
 * LiDarDataBase is a singleton class responsible for managing LiDAR data.
 * It provides access to cloud point data and other relevant information for tracked objects.
 */
public class LiDarDataBase {
    private List<StampedCloudPoints> cloudPoints; // The coordinates for every object per time
    private final String filePath;

    private LiDarDataBase(String filePath) {
        this.filePath = filePath;
    }

    private static class LidarDataBaseHolder {
        private static LiDarDataBase INSTANCE;
    }

    public static synchronized void initialize(String filePath) {
        if (LidarDataBaseHolder.INSTANCE == null) {
            LidarDataBaseHolder.INSTANCE = new LiDarDataBase(filePath);
        } else {
            throw new IllegalStateException("Instance already initialized");
        }
    }
    /**
     * Returns the singleton instance of LiDarDataBase.
     *
     * @param filePath The path to the LiDAR data file.
     * @return The singleton instance of LiDarDataBase.
     */
    public static LiDarDataBase getInstance(String filePath) {
        if (LidarDataBaseHolder.INSTANCE == null) {
            throw new IllegalStateException("Singleton not initialized. Call initialize() first.");
        }
        return LidarDataBaseHolder.INSTANCE;
    }
}
