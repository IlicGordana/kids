package servent.message.snapshot;

import app.ServentInfo;
import app.snapshot_bitcake.ABSnapshotResult;
import servent.message.BasicMessage;
import servent.message.MessageType;

import java.util.List;

public class ABTellMasage extends BasicMessage {

    private ABSnapshotResult abSnapshotResult;

    public ABTellMasage(ServentInfo originalSenderInfo, ServentInfo receiverInfo, ABSnapshotResult abSnapshotResult) {
        super(MessageType.AB_TELL, originalSenderInfo, receiverInfo);
        this.abSnapshotResult = abSnapshotResult;
    }

    public ABTellMasage(MessageType type, ServentInfo originalSenderInfo, ServentInfo receiverInfo, String messageText) {
        super(type, originalSenderInfo, receiverInfo, messageText);
    }

    protected ABTellMasage(MessageType type, ServentInfo originalSenderInfo, ServentInfo receiverInfo, boolean white, List<ServentInfo> routeList, String messageText, int messageId) {
        super(type, originalSenderInfo, receiverInfo, white, routeList, messageText, messageId);
    }

    public ABSnapshotResult getAbSnapshotResult() {
        return abSnapshotResult;
    }
}
