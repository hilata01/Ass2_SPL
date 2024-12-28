package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.broadcasts.CrashedBroadcast;
import bgu.spl.mics.application.messages.broadcasts.TickBroadcast;
import bgu.spl.mics.application.messages.broadcasts.TerminatedBroadcast;
import bgu.spl.mics.application.messages.events.DetectObjectsEvent;
import bgu.spl.mics.application.messages.events.TrackedObjectsEvent;
import bgu.spl.mics.application.objects.LiDarWorkerTracker;
import bgu.spl.mics.application.objects.StampedDetectedObjects;
import bgu.spl.mics.application.objects.TrackedObject;
import bgu.spl.mics.application.objects.StatisticalFolder;

import java.util.List;

/**
 * LiDarService is responsible for processing data from the LiDAR sensor and
 * sending TrackedObjectsEvents to the Fusion-SLAM service.
 *
 * This service interacts with the LiDarWorkerTracker object to retrieve and process
 * cloud point data and updates the system's StatisticalFolder upon sending its
 * observations.
 */
public class LiDarService extends MicroService {

    private final LiDarWorkerTracker lidarWorkerTracker;
    private final StatisticalFolder stats;

    /**
     * Constructor for LiDarService.
     *
     * @param lidarWorkerTracker A LiDAR Tracker worker object that this service will use to process data.
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
        // Subscribe to TickBroadcast to handle periodic operations
        subscribeBroadcast(TickBroadcast.class, new Callback<TickBroadcast>() {
            @Override
            public void call(TickBroadcast tick) {
                lidarWorkerTracker.updateStatus(tick.getTick());
            }
        });

        // Subscribe to DetectObjectsEvent to process object detection from cameras
        subscribeEvent(DetectObjectsEvent.class, new Callback<DetectObjectsEvent>() {
            @Override
            public void call(DetectObjectsEvent event) {
                StampedDetectedObjects detectedObjects = event.getStampedDetectedObjects();

                // Process detected objects and create tracked objects
                List<TrackedObject> trackedObjects =
                        lidarWorkerTracker.processDetectedObjects(detectedObjects);

                // Send TrackedObjectsEvent to Fusion-SLAM service
                TrackedObjectsEvent trackedEvent = new TrackedObjectsEvent(trackedObjects);
                sendEvent(trackedEvent);

                // Update statistics
                stats.incrementTrackedObjects(trackedObjects.size());
                System.out.println(getName() + " processed and sent TrackedObjectsEvent.");
            }
        });

        // Subscribe to TerminatedBroadcast for graceful termination
        subscribeBroadcast(TerminatedBroadcast.class, terminated -> {
            terminate();
        });

        // Subscribe to CrashedBroadcast
        subscribeBroadcast(CrashedBroadcast.class, crashed -> {
            // Possibly log or update stats about the crash
            terminate();
        });

        // Log initialization
        System.out.println(getName() + " initialized and ready.");
    }
}
