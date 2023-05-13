package app.snapshot_bitcake;

import java.io.Serializable;
import java.util.Map;

public class ABSnapshotResult implements Serializable {

    private final int serventId;
    private final int recordedAmount;
    private final Map<Integer, Integer> sentHistory;
    private final Map<Integer, Integer> receivedHistory;


    public ABSnapshotResult(int serventId, int recordedAmount, Map<Integer, Integer> sentHistory, Map<Integer, Integer> receivedHistory) {
        this.serventId = serventId;
        this.recordedAmount = recordedAmount;
        this.sentHistory = sentHistory;
        this.receivedHistory = receivedHistory;
    }

    public int getServentId() {
        return serventId;
    }

    public int getRecordedAmount() {
        return recordedAmount;
    }

    public Map<Integer, Integer> getSentHistory() {
        return sentHistory;
    }

    public Map<Integer, Integer> getReceivedHistory() {
        return receivedHistory;
    }
}
