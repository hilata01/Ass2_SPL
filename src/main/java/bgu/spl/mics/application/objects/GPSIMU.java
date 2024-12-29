package bgu.spl.mics.application.objects;

import java.util.List;

/**
 * Represents the robot's GPS and IMU system.
 * Provides information about the robot's position and movement.
 */
public class GPSIMU {
    private int currentTick; // The current time
    private STATUS status;
    private List<Pose> postList; // Represents time-stamped poses;

    public GPSIMU(int currentTick, STATUS status, List<Pose> postList) {
        this.currentTick = currentTick;
        this.status = status;
        this.postList = postList;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public STATUS getStatus() {
        return status;
    }

    public List<Pose> getPostList() {
        return postList;
    }

    @Override
    public String toString() {
        return "GPSIMU{" +
                "currentTick=" + currentTick +
                ", status=" + status +
                ", postList=" + postList +
                '}';
    }
}
