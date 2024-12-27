package bgu.spl.mics.application.messages.broadcasts;

import bgu.spl.mics.Broadcast;

public class MapUpdateBroadcast implements Broadcast {
    private final String mapId;
    private final long timestamp;

    public MapUpdateBroadcast(String mapId, long timestamp) {
        this.mapId = mapId;
        this.timestamp = timestamp;
    }

    public String getMapId() {
        return mapId;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
