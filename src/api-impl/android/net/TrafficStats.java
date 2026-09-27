package android.net;

import java.io.FileDescriptor;
import java.net.DatagramSocket;
import java.net.Socket;

public class TrafficStats {
	public static final int UNSUPPORTED = -1;

	private static final ThreadLocal<Integer> thread_stats_tag = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			return -1;
		}
	};

	public static void setThreadStatsTag(int tag) {
		thread_stats_tag.set(tag);
	}

	public static int getAndSetThreadStatsTag(int tag) {
		int old = thread_stats_tag.get();
		thread_stats_tag.set(tag);
		return old;
	}

	public static int getThreadStatsTag() {
		return thread_stats_tag.get();
	}

	public static void clearThreadStatsTag() {
		thread_stats_tag.set(-1);
	}

	public static void setThreadStatsUid(int uid) {}

	public static int getThreadStatsUid() {
		return -1;
	}

	public static void clearThreadStatsUid() {}

	public static void tagSocket(Socket socket) {}

	public static void untagSocket(Socket socket) {}

	public static void tagDatagramSocket(DatagramSocket socket) {}

	public static void untagDatagramSocket(DatagramSocket socket) {}

	public static void tagFileDescriptor(FileDescriptor fd) {}

	public static void untagFileDescriptor(FileDescriptor fd) {}

	public static void incrementOperationCount(int operationCount) {}

	public static void incrementOperationCount(int tag, int operationCount) {}

	public static long getUidRxBytes(int uid) {
		return UNSUPPORTED;
	}

	public static long getUidTxBytes(int uid) {
		return UNSUPPORTED;
	}

	public static long getUidRxPackets(int uid) {
		return UNSUPPORTED;
	}

	public static long getUidTxPackets(int uid) {
		return UNSUPPORTED;
	}

	public static long getTotalRxBytes() {
		return UNSUPPORTED;
	}

	public static long getTotalTxBytes() {
		return UNSUPPORTED;
	}

	public static long getTotalRxPackets() {
		return UNSUPPORTED;
	}

	public static long getTotalTxPackets() {
		return UNSUPPORTED;
	}

	public static long getMobileRxBytes() {
		return UNSUPPORTED;
	}

	public static long getMobileTxBytes() {
		return UNSUPPORTED;
	}
}
