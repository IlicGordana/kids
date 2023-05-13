package servent.handler.snapshot;

import app.AppConfig;
import app.snapshot_bitcake.SnapshotCollector;
import servent.handler.MessageHandler;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.snapshot.ABTellMasage;

public class ABTellHandler  implements MessageHandler {
    private Message clientMessage;
    private SnapshotCollector snapshotCollector;

    public ABTellHandler(Message clientMessage, SnapshotCollector snapshotCollector) {
        this.clientMessage = clientMessage;
        this.snapshotCollector = snapshotCollector;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.AB_TELL) {
            ABTellMasage lyTellMessage = (ABTellMasage) clientMessage;

            snapshotCollector.addABSnapshotInfo(
                    lyTellMessage.getOriginalSenderInfo().getId(),
                    lyTellMessage.getAbSnapshotResult());
        } else {
            AppConfig.timestampedErrorPrint("Tell amount handler got: " + clientMessage);
        }
    }
}
