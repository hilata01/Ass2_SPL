package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.broadcasts.CrashedBroadcast;
import bgu.spl.mics.application.messages.broadcasts.TickBroadcast;
import bgu.spl.mics.application.messages.broadcasts.TerminatedBroadcast;
import bgu.spl.mics.application.messages.events.DetectObjectsEvent;
import bgu.spl.mics.application.messages.events.TrackedObjectsEvent;
import bgu.spl.mics.application.objects.LiDarWorkerTracker;
import bgu.spl.mics.application.objects.StatisticalFolder;
import bgu.spl.mics.application.objects.TrackedObject;
import bgu.spl.mics.application.objects.StampedDetectedObjects;

import java.util.List;

/**
 * LiDarService is responsible for processing data from the LiDAR sensor and
 * sending TrackedObjectsEvents to the Fusion-SLAM service.
 */
public class LiDarService extends MicroService {

    private final LiDarWorkerTracker lidarWorkerTracker;

    /**
     * Constructor for LiDarService.
     *
     * @param lidarWorkerTracker A LiDAR worker tracker for processing data.
     */
    public LiDarService(LiDarWorkerTracker lidarWorkerTracker) {
        super("LiDarService-" + lidarWorkerTracker.getId());
        this.lidarWorkerTracker = lidarWorkerTracker;
    }

    /**
     * Initializes the LiDarService.
     * Registers the service to handle TickBroadcasts and DetectObjectsEvents.
     */
    @Override
    protected void initialize() {
        // Subscribes to TickBroadcast to update worker status periodically.
        subscribeBroadcast(TickBroadcast.class, tick -> {
            lidarWorkerTracker.updateStatus(tick.getTick());
        });


        subscribeEvent(DetectObjectsEvent.class, detectedEvent ->{
            //TO DO - When we should send the coordinates
            int lidarTime = lidarWorkerTracker.getFrequency();
            int currTick =

            sendEvent(new TrackedObjectsEvent(null));
        });

        subscribeBroadcast(TerminatedBroadcast.class, terminated ->{
            Thread.currentThread().interrupt();
                });

        subscribeBroadcast(CrashedBroadcast.class, crashed ->{
            Thread.currentThread().interrupt();
        });
    }
}
