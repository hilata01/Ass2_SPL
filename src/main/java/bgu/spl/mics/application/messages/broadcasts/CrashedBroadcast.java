package bgu.spl.mics.application.messages.broadcasts;

import bgu.spl.mics.Broadcast;

/**
 * Represents a broadcast message sent when a sensor or service crashes.
 * Notifies other services to terminate operations due to the crash.
 */
public class CrashedBroadcast implements Broadcast {

    private final String crashedServiceName;
    private final String reason;

    /**
     * Constructor for CrashedBroadcast.
     *
     * @param crashedServiceName The name of the service or sensor that crashed.
     * @param reason              The reason for the crash.
     */
    public CrashedBroadcast(String crashedServiceName, String reason) {
        this.crashedServiceName = crashedServiceName;
        this.reason = reason;
    }

    /**
     * Gets the name of the crashed service.
     *
     * @return The crashed service name.
     */
    public String getCrashedServiceName() {
        return crashedServiceName;
    }

    /**
     * Gets the reason for the crash.
     *
     * @return The crash reason.
     */
    public String getReason() {
        return reason;
    }
}
