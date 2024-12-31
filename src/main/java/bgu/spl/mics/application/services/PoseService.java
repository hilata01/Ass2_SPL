package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.broadcasts.CrashedBroadcast;
import bgu.spl.mics.application.messages.broadcasts.TerminatedBroadcast;
import bgu.spl.mics.application.messages.broadcasts.TickBroadcast;
import bgu.spl.mics.application.messages.events.PoseEvent;
import bgu.spl.mics.application.objects.GPSIMU;
import bgu.spl.mics.application.objects.Pose;

import java.util.List;

/**
 * PoseService is responsible for maintaining the robot's current pose (position and orientation)
 * and broadcasting PoseEvents at every tick.
 */
public class PoseService extends MicroService {
    private GPSIMU gpsimu;

    /**
     * Constructor for PoseService.
     *
     * @param gpsimu The GPSIMU object that provides the robot's pose data.
     */
    public PoseService(GPSIMU gpsimu) {
        super("PoseService" + gpsimu.getClass().getName()); //Should check again!
        this.gpsimu = gpsimu;
    }

    /**
     * Initializes the PoseService.
     * Subscribes to TickBroadcast and sends PoseEvents at every tick based on the current pose.
     */
    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, tick ->{
            int currTick = tick.getTick();
            List<Pose> currPoseList = gpsimu.getPostList();
            /*
            float poseUpdateX = currPoseList.get(currTick).getX();
            float poseUpdateY = currPoseList.get(currTick).getY();
            float poseUpdateYAW = currPoseList.get(currTick).getYaw();
            Pose currPose = new Pose(poseUpdateX,poseUpdateY,poseUpdateYAW,currTick);

             */
            sendEvent(new PoseEvent<>(currPoseList.get(currTick))); //dont know about <> in PoseEvent. also in other Events


        });

        subscribeBroadcast(TerminatedBroadcast.class, terminated ->{
            Thread.currentThread().interrupt();
        });
        subscribeBroadcast(CrashedBroadcast.class, crashed -> {
            Thread.currentThread().interrupt();
        });
    }
}
