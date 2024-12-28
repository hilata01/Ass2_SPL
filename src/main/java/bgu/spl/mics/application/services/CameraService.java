package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.broadcasts.TickBroadcast;
import bgu.spl.mics.application.messages.broadcasts.TerminatedBroadcast;
import bgu.spl.mics.application.messages.broadcasts.CrashedBroadcast;
import bgu.spl.mics.application.messages.events.DetectObjectsEvent;
import bgu.spl.mics.application.objects.*;

import java.util.List;

/**
 * CameraService is responsible for processing data from the camera and
 * sending DetectObjectsEvents to LiDAR workers.
 *
 * This service interacts with the Camera object to detect objects and updates
 * the system's StatisticalFolder upon sending its observations.
 */
public class CameraService extends MicroService {

    private final Camera camera;
    private final StatisticalFolder stats;

    /**
     * Constructor for CameraService.
     *
     * @param camera The Camera object that this service will use to detect objects.
     * @param stats  The StatisticalFolder for updating statistics.
     */
    public CameraService(Camera camera, StatisticalFolder stats) {
        super("CameraService-" + camera.getId());
        this.camera = camera;
        this.stats = stats;
    }

    /**
     * Initializes the CameraService.
     * Registers the service to handle TickBroadcasts and sets up callbacks for sending
     * DetectObjectsEvents.
     */
    @Override
    protected void initialize() {
        // Subscribe to TickBroadcast to trigger actions at every tick
        subscribeBroadcast(TickBroadcast.class, new Callback<TickBroadcast>() {
            @Override
            public void call(TickBroadcast tick) {
                processTick(tick.getTick());
            }
        });

        // Subscribe to TerminatedBroadcast for graceful termination
        subscribeBroadcast(TerminatedBroadcast.class, terminated -> {
            // Cleanup, finalize, etc.
            terminate();
        });

        // Subscribe to CrashedBroadcast
        subscribeBroadcast(CrashedBroadcast.class, crashed -> {
            // Possibly log or update stats about the crash
            terminate();
        });

        // Log initialization
        System.out.println(getName() + " initialized and waiting for ticks.");
    }

    /**
     * Processes the tick to check if the camera should send a DetectObjectsEvent.
     *
     * @param tick The current tick.
     */
    private void processTick(long tick) {
        // Check if the camera is operational and due to send data
        if (camera.getStatus() == STATUS.UP && tick % camera.getFrequency() == 0) {
            List<DetectedObject> detectedObjects = camera.detectObjects();

            if (!detectedObjects.isEmpty()) {
                // Create StampedDetectedObjects
                StampedDetectedObjects stampedDetectedObjects =
                        new StampedDetectedObjects(tick, detectedObjects);

                // Send a DetectObjectsEvent to LiDAR workers
                DetectObjectsEvent<StampedDetectedObjects> event =
                        new DetectObjectsEvent<>(stampedDetectedObjects);

                sendEvent(event);

                // Update statistics
                stats.incrementDetectedObjects(detectedObjects.size());

                // Log detection
                System.out.println(getName() + " sent DetectObjectsEvent at tick " + tick);
            }
        }
    }
}
