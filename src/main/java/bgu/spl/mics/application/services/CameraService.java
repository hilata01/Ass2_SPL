package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.broadcasts.TickBroadcast;
import bgu.spl.mics.application.messages.broadcasts.TerminatedBroadcast;
import bgu.spl.mics.application.messages.broadcasts.CrashedBroadcast;
import bgu.spl.mics.application.messages.events.DetectObjectsEvent;
import bgu.spl.mics.application.objects.Camera;
import bgu.spl.mics.application.objects.STATUS;
import bgu.spl.mics.application.objects.StatisticalFolder;
import bgu.spl.mics.application.objects.StampedDetectedObjects;

/**
 * CameraService is responsible for processing data from the camera and
 * sending DetectObjectsEvents to LiDAR workers.
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
     * Registers the service to handle TickBroadcasts and TerminatedBroadcasts.
     */
    @Override
    protected void initialize() {
        // Subscribes to TickBroadcast to perform periodic operations.
        // Each TickBroadcast acts as a heartbeat, allowing the service to check
        // if it is time to detect objects based on the camera's frequency.
        subscribeBroadcast(TickBroadcast.class, tick -> {
            int currentTick = tick.getTick();

            // The frequency condition ensures that detection only happens at specified intervals.
            // The status condition ensures that the camera is operational before detecting objects.
            if (camera.getStatus() == STATUS.UP && currentTick % camera.getFrequency() == 0) {
                // Get all the objects that are detected in this current time we're at now
                StampedDetectedObjects stampedDetectedObjects = camera.detectObjects(currentTick);

                // As long as we actually got detected object(s), send an event about it to the subscribers
                if (!stampedDetectedObjects.getDetectedObjects().isEmpty()) {
                    sendEvent(new DetectObjectsEvent<>(stampedDetectedObjects));

                    // Updates statistics to keep track of the number of detected objects.
                    stats.incrementDetectedObjects(stampedDetectedObjects.getDetectedObjects().size());
                    System.out.println("CameraService-" + camera.getId() + " detected objects at tick " + currentTick);
                }
            }
        });
        // Subscribe to the other two broadcasts and terminate if they're being sent
        subscribeBroadcast(TerminatedBroadcast.class, terminated -> terminate());
        subscribeBroadcast(CrashedBroadcast.class, crashed -> terminate());

        System.out.println(getName() + " initialized.");
    }
}
