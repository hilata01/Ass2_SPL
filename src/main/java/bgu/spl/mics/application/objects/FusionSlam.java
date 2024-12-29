package bgu.spl.mics.application.objects;

import java.util.List;

/**
 * Manages the fusion of sensor data for simultaneous localization and mapping (SLAM).
 * Combines data from multiple sensors (e.g., LiDAR, camera) to build and update a global map.
 * Implements the Singleton pattern to ensure a single instance of FusionSlam exists.
 */
public class FusionSlam {
    private List<LandMark> landMarks; // Represent the map of the environment
    private List<Pose> poses; // Represent previous poses needed for calculations

    // Singleton instance holder
    private static class FusionSlamHolder {
        private static final FusionSlam INSTANCE = new FusionSlam();
    }

    public FusionSlam(List<LandMark> landMarks, List<Pose> poses) {
        this.landMarks = landMarks;
        this.poses = poses;
    }

    public static FusionSlam getInstance() {return FusionSlamHolder.INSTANCE;}

    public List<LandMark> getLandMarks() {return landMarks;}

    public List<Pose> getPoses() {return poses;}

    @Override
    public String toString() {
        return "FusionSlam{" +
                "landMarks=" + landMarks +
                ", poses=" + poses +
                '}';
    }
}
