package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.broadcasts.TickBroadcast;
import bgu.spl.mics.application.messages.broadcasts.TerminatedBroadcast;
import bgu.spl.mics.application.messages.broadcasts.CrashedBroadcast;
import bgu.spl.mics.application.messages.events.DetectObjectsEvent;
import bgu.spl.mics.application.objects.*;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * CameraService is responsible for processing data from the camera and
 * sending DetectObjectsEvents to LiDAR workers.
 */
public class CameraService extends MicroService {

    private final Camera camera;

    /**
     * Constructor for CameraService.
     *
     * @param camera The Camera object that this service will use to detect objects.
     */
    public CameraService(Camera camera) {
        super("CameraService-" + camera.getId());
        this.camera = camera;
    }

    /**
     * Initializes the CameraService.
     * Registers the service to handle TickBroadcasts and TerminatedBroadcasts.
     */


    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, tick -> {
            int currentTick = tick.getTick();
            StampedDetectedObjects nextStampedDetectedObjects = camera.getDetectedObjectsList().get(0);
            if(nextStampedDetectedObjects != null){
                int detectionTime = nextStampedDetectedObjects.getTimestamp();
                int sendTime = detectionTime + camera.getFrequency();
                if(sendTime >= currentTick){
                    StampedDetectedObjects currDetectedObject = camera.getDetectedObjectsList().remove(0);
                    int size = currDetectedObject.getDetectedObjects().size();
                    for (int i=0; i<size; i++) {
                        String currID = currDetectedObject.getDetectedObjects().get(i).getId();
                         if (currID.equals("ERROR")){
                           String reasonError = currDetectedObject.getDetectedObjects().get(i).getDescription();
                           sendBroadcast(new CrashedBroadcast(getName(),reasonError));
                           camera.setStatus(STATUS.ERROR);
                           Thread.currentThread().interrupt();
                           return;
                       }
                    }
                    sendEvent(new DetectObjectsEvent(currDetectedObject, camera.getId() ));
                }
            }
            else {
                sendBroadcast(new TerminatedBroadcast(getName()));
                camera.setStatus(STATUS.DOWN);
                Thread.currentThread().interrupt();
            }
        });

        subscribeBroadcast(TerminatedBroadcast.class, terminated ->{
            Thread.currentThread().interrupt();
        });
        subscribeBroadcast(CrashedBroadcast.class, crashed -> {
            Thread.currentThread().interrupt();
        });
    }
}
