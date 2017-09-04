package com.hjnerp.websocket;

import java.io.Reader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class PacketReader {

	private Thread readerThread;
	private ExecutorService listenerExecutor;

	private HjHttpConnection connection;

	volatile boolean done;
	private String connectionID = null;

	protected PacketReader(final HjHttpConnection connection) {
		this.connection = connection;
		this.init();
	}

	protected void init() {
		done = false;
		connectionID = null;

		readerThread = new Thread() {
			public void run() {
				parsePackets(this);
			}
		};
		readerThread.setName("hjhttp Packet Reader )");
		readerThread.setDaemon(true);

		// Create an executor to deliver incoming packets to listeners. We'll
		// use a single
		// thread with an unbounded queue.
		listenerExecutor = Executors
				.newSingleThreadExecutor(new ThreadFactory() {

					public Thread newThread(Runnable runnable) {
						Thread thread = new Thread(runnable,
								"hjhttp Listener Processor ");
						thread.setDaemon(true);
						return thread;
					}
				});

		// resetParser();
	}

	private void parsePackets(Thread thread) {
		try {
			String rcd;
			do { 
				rcd = readLine(connection.reader);

			} while (!done && thread == readerThread);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	synchronized public void startup() {
		readerThread.start();
		// try {
		// // 等待线程可能会唤醒等待时间或通知之前
		// // (although this is a rare thing). Therefore, we continue waiting
		// // until either a connectionID has been set (and hence a notify was
		// // made) or the total wait time has elapsed.
		// int waitTime = SmackConfiguration.getPacketReplyTimeout();
		// wait(3 * waitTime);
		// } catch (InterruptedException ie) {
		// // Ignore.
		// }
		// if (connectionID == null) {
		// throw new XMPPException(
		// "Connection failed. No response from server.");
		// } else {
		// connection.connectionID = connectionID;
		// }
	}

	private static String readLine(Reader dis) throws Exception {
		int readChar = dis.read();
		if (readChar == -1)
			return null;
		StringBuilder string = new StringBuilder("");
		while (readChar != '\n') {
			if (readChar != '\r')
				string.append((char) readChar);
			readChar = dis.read();
			if (readChar == -1)
				return null;
		}
		return string.toString();
	}

}
