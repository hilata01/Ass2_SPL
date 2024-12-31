package bgu.spl.mics.application.messages.events;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Pose;

public class PoseEvent<T> implements Event<T> {
    private final Pose pose;

    public PoseEvent(Pose pose) {
        this.pose = pose;
    }

    public Pose getPose() {
        return pose;
    }

}
