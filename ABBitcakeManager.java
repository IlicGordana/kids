package app.snapshot_bitcake;

import app.AppConfig;
import app.CausalBroadcastShared;
import servent.message.CausalBroadcastMessage;
import servent.message.Message;
import servent.message.snapshot.ABTellMasage;
import servent.message.util.MessageUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public class ABBitcakeManager implements BitcakeManager{

    private final AtomicInteger currentAmount = new AtomicInteger(1000);

    private Map<Integer, Integer> sentHistory = new ConcurrentHashMap<>();
    private Map<Integer, Integer> receivedHistory = new ConcurrentHashMap<>();

    public int recordedAmount = 0;


    public ABBitcakeManager() {
        for(Integer neighbor : AppConfig.myServentInfo.getNeighbors()) {
            sentHistory.put(neighbor, 0);
            receivedHistory.put(neighbor, 0);
        }
    }

    public void initSnapshot(int collectorId, SnapshotCollector snapshotCollector) {
        recordedAmount = getCurrentBitcakeAmount();

        ABSnapshotResult snapshotResult = new ABSnapshotResult(
                AppConfig.myServentInfo.getId(), recordedAmount, sentHistory, receivedHistory);

        if (collectorId == AppConfig.myServentInfo.getId()) {
            snapshotCollector.addABSnapshotInfo(
                    AppConfig.myServentInfo.getId(),
                    snapshotResult);
        } else {

            Message tellMessage = new ABTellMasage(
                    AppConfig.myServentInfo, AppConfig.getInfoById(collectorId), snapshotResult);
            MessageUtil.sendMessage(tellMessage);
        }

        Map<Integer, Integer> myClock = new ConcurrentHashMap<>(CausalBroadcastShared.getVectorClock());

        Message broadcastMessage = new CausalBroadcastMessage(AppConfig.myServentInfo, null, myClock);
        for (Integer neighbor : AppConfig.myServentInfo.getNeighbors()) {

            broadcastMessage = broadcastMessage.changeReceiver(neighbor);
            MessageUtil.sendMessage(broadcastMessage);
        }

        broadcastMessage = broadcastMessage.changeReceiver(AppConfig.myServentInfo.getId());
        MessageUtil.sendMessage(broadcastMessage);
        CausalBroadcastShared.commitCausalMessage(broadcastMessage);

    }

//    public void handleToken(Message clientMessage, SnapshotCollector snapshotCollector, int currentBitcakeAmount) {
//
//        ABSnapshotResult snapshotResult = new ABSnapshotResult(AppConfig.myServentInfo.getId(),currentBitcakeAmount, sentHistory, receivedHistory);
//        if (AppConfig.myServentInfo.getId() == clientMessage.getOriginalSenderInfo().getId()){
//            //dodam svoj rez
//            snapshotCollector.addABSnapshotInfo(clientMessage.getOriginalSenderInfo().getId(),snapshotResult);
//        }else {
//            Message tellMessage = new ABTellMasage(
//                    AppConfig.myServentInfo, null, snapshotResult);
//            for(Integer neighbor:AppConfig.myServentInfo.getNeighbors()) {
//
//                tellMessage = tellMessage.changeReceiver(neighbor);
//                MessageUtil.sendMessage(tellMessage);
//            }
//        }
//
//    }




    @Override
    public void takeSomeBitcakes(int amount) {
        currentAmount.getAndAdd(-amount);

    }

    @Override
    public void addSomeBitcakes(int amount) {
        currentAmount.getAndAdd(amount);

    }

    @Override
    public int getCurrentBitcakeAmount() {
        return currentAmount.get();
    }

    private class MapValueUpdater implements BiFunction<Integer, Integer, Integer> {

        private int valueToAdd;

        public MapValueUpdater(int valueToAdd) {
            this.valueToAdd = valueToAdd;
        }

        @Override
        public Integer apply(Integer key, Integer oldValue) {
            return oldValue + valueToAdd;
        }
    }

    public void recordSentHistory(int neighbor, int amount) {
        sentHistory.compute(neighbor, new MapValueUpdater(amount));
    }

    public void recordReceivedHistory(int neighbor, int amount) {
        receivedHistory.compute(neighbor, new MapValueUpdater(amount));
    }
}
