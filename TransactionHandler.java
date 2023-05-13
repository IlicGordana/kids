package servent.handler;

import app.AppConfig;
import app.snapshot_bitcake.ABBitcakeManager;
import app.snapshot_bitcake.BitcakeManager;
import servent.message.Message;
import servent.message.MessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionHandler implements MessageHandler {

	private Message clientMessage;
	private BitcakeManager bitcakeManager;


	
	public TransactionHandler(Message clientMessage, BitcakeManager bitcakeManager) {
		this.clientMessage = clientMessage;
		this.bitcakeManager = bitcakeManager;
	}

	@Override
	public void run() {
		if (clientMessage.getMessageType() == MessageType.TRANSACTION) {
			String amountString = clientMessage.getMessageText();

			int amountNumber = 0;
			try {
				amountNumber = Integer.parseInt(amountString);
			} catch (NumberFormatException e) {
				AppConfig.timestampedErrorPrint("Couldn't parse amount: " + amountString);
				return;
			}

			bitcakeManager.addSomeBitcakes(amountNumber);

			synchronized (AppConfig.colorLock) {
				if (bitcakeManager instanceof ABBitcakeManager) {
					ABBitcakeManager abBitcakeManager = (ABBitcakeManager) bitcakeManager;

					abBitcakeManager.recordSentHistory(clientMessage.getOriginalSenderInfo().getId(), amountNumber);

				} else {

					AppConfig.timestampedErrorPrint("Transaction handler got: " + clientMessage);
				}
			}
		}

	}}
