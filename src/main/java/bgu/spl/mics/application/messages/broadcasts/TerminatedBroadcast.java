package bgu.spl.mics.application.messages.broadcasts;

import bgu.spl.mics.Broadcast;

public class TerminatedBroadcast implements Broadcast {
    private final String reason;

    public TerminatedBroadcast(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
