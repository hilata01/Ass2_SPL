package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
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
    private final StatisticalFolder stats;

    /**
     * Constructor for LiDarService.
     *
     * @param lidarWorkerTracker A LiDAR worker tracker for processing data.
     * @param stats              The StatisticalFolder for updating statistics.
     */
    public LiDarService(LiDarWorkerTracker lidarWorkerTracker, StatisticalFolder stats) {
        super("LiDarService-" + lidarWorkerTracker.getId());
        this.lidarWorkerTracker = lidarWorkerTracker;
        this.stats = stats;
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

        // Subscribes to DetectObjectsEvent to process detected objects from cameras.
        subscribeEvent(DetectObjectsEvent.class, event -> {
            StampedDetectedObjects detectedObjects = event.getStampedDetectedObjects();
            List<TrackedObject> trackedObjects = lidarWorkerTracker.processDetectedObjects(
                    detectedObjects.getDetectedObjects(), detectedObjects.getTimestamp());

            if (!trackedObjects.isEmpty()) {
                sendEvent(new TrackedObjectsEvent(trackedObjects));
                stats.incrementTrackedObjects(trackedObjects.size());
            }
        });

        // Subscribes to TerminatedBroadcast for graceful shutdown.
        subscribeBroadcast(TerminatedBroadcast.class, terminated -> terminate());

        System.out.println(getName() + " initialized.");
    }
}
