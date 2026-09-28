package android.net;

import android.app.PendingIntent;
import android.os.Handler;
import android.os.Looper;
import java.util.HashMap;
import java.util.Map;

class ProxyInfo {}

public class ConnectivityManager {
	public static final String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	public static final String EXTRA_NO_CONNECTIVITY = "noConnectivity";
	public static final int TYPE_MOBILE = 0;
	public static final int TYPE_WIFI = 1;
	public static final int TYPE_ETHERNET = 9;
	public static final int RESTRICT_BACKGROUND_STATUS_DISABLED = 1;

	private static final Network DEFAULT_NETWORK = new Network();

	/* all registered callbacks, across ConnectivityManager instances */
	private static final Map<NetworkCallback, Handler> callbacks = new HashMap<NetworkCallback, Handler>();
	private static boolean monitor_registered = false;

	public static class NetworkCallback {
		public static final int FLAG_INCLUDE_LOCATION_INFO = 1;

		public NetworkCallback() {}

		public NetworkCallback(int flags) {}

		public void onAvailable(Network network) {}

		public void onLosing(Network network, int maxMsToLive) {}

		public void onLost(Network network) {}

		public void onUnavailable() {}

		public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {}

		public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {}

		public void onBlockedStatusChanged(Network network, boolean blocked) {}
	}

	/* receives GNetworkMonitor's network-changed signal (see native code) and fans it out */
	private static class Monitor extends NetworkCallback {
		@Override
		public void onAvailable(Network network) {
			dispatchAll(true);
		}

		@Override
		public void onLost(Network network) {
			dispatchAll(false);
		}
	}

	public NetworkInfo getNetworkInfo(int networkType) {
		return new NetworkInfo(nativeGetNetworkAvailable());
	}

	public NetworkInfo getNetworkInfo(Network network) {
		return new NetworkInfo(nativeGetNetworkAvailable());
	}

	public NetworkInfo getActiveNetworkInfo() {
		return new NetworkInfo(nativeGetNetworkAvailable());
	}

	private native void nativeRegisterMonitor(NetworkCallback monitor);

	public native boolean isActiveNetworkMetered();

	protected native boolean nativeGetNetworkAvailable();

	public NetworkInfo[] getAllNetworkInfo() {
		return new NetworkInfo[] {getActiveNetworkInfo()};
	}

	public Network getActiveNetwork() {
		return nativeGetNetworkAvailable() ? DEFAULT_NETWORK : null;
	}

	public Network[] getAllNetworks() {
		return nativeGetNetworkAvailable() ? new Network[] {DEFAULT_NETWORK} : new Network[0];
	}

	public NetworkCapabilities getNetworkCapabilities(Network network) {
		if (network == null || !nativeGetNetworkAvailable())
			return null;
		return new NetworkCapabilities(true, isActiveNetworkMetered());
	}

	public LinkProperties getLinkProperties(Network network) {
		return network != null ? new LinkProperties() : null;
	}

	public boolean bindProcessToNetwork(Network network) {
		return true;
	}

	public Network getBoundNetworkForProcess() {
		return null;
	}

	public int getRestrictBackgroundStatus() {
		return RESTRICT_BACKGROUND_STATUS_DISABLED;
	}

	public void registerNetworkCallback(NetworkRequest request, NetworkCallback callback) {
		addCallback(callback, null);
	}

	public void registerNetworkCallback(NetworkRequest request, NetworkCallback callback, Handler handler) {
		addCallback(callback, handler);
	}

	public void registerNetworkCallback(NetworkRequest request, PendingIntent operation) {}

	public void registerDefaultNetworkCallback(NetworkCallback callback) {
		addCallback(callback, null);
	}

	public void registerDefaultNetworkCallback(NetworkCallback callback, Handler handler) {
		addCallback(callback, handler);
	}

	public void registerBestMatchingNetworkCallback(NetworkRequest request, NetworkCallback callback, Handler handler) {
		addCallback(callback, handler);
	}

	public void requestNetwork(NetworkRequest request, NetworkCallback callback) {
		addCallback(callback, null);
	}

	public void requestNetwork(NetworkRequest request, NetworkCallback callback, Handler handler) {
		addCallback(callback, handler);
	}

	public void requestNetwork(NetworkRequest request, NetworkCallback callback, int timeoutMs) {
		addCallback(callback, null);
	}

	public void requestNetwork(NetworkRequest request, NetworkCallback callback, Handler handler, int timeoutMs) {
		addCallback(callback, handler);
	}

	public void unregisterNetworkCallback(NetworkCallback callback) {
		synchronized (callbacks) {
			callbacks.remove(callback);
		}
	}

	public void unregisterNetworkCallback(PendingIntent operation) {}

	public ProxyInfo getDefaultProxy() {
		return null;
	}

	/* Like Android: a new callback immediately learns about the current network. */
	private void addCallback(final NetworkCallback callback, Handler handler) {
		if (callback == null)
			return;
		if (handler == null)
			handler = new Handler(Looper.getMainLooper());
		synchronized (callbacks) {
			callbacks.put(callback, handler);
			if (!monitor_registered) {
				monitor_registered = true;
				nativeRegisterMonitor(new Monitor());
			}
		}
		if (nativeGetNetworkAvailable()) {
			final boolean metered = isActiveNetworkMetered();
			handler.post(new Runnable() {
				@Override
				public void run() {
					notifyAvailable(callback, metered);
				}
			});
		}
	}

	private static void notifyAvailable(NetworkCallback callback, boolean metered) {
		synchronized (callbacks) {
			if (!callbacks.containsKey(callback))
				return;
		}
		callback.onAvailable(DEFAULT_NETWORK);
		callback.onCapabilitiesChanged(DEFAULT_NETWORK, new NetworkCapabilities(true, metered));
		callback.onLinkPropertiesChanged(DEFAULT_NETWORK, new LinkProperties());
		callback.onBlockedStatusChanged(DEFAULT_NETWORK, false);
	}

	private static void dispatchAll(final boolean available) {
		HashMap<NetworkCallback, Handler> copy;
		synchronized (callbacks) {
			copy = new HashMap<NetworkCallback, Handler>(callbacks);
		}
		for (final Map.Entry<NetworkCallback, Handler> entry : copy.entrySet()) {
			entry.getValue().post(new Runnable() {
				@Override
				public void run() {
					if (available)
						notifyAvailable(entry.getKey(), false);
					else
						entry.getKey().onLost(DEFAULT_NETWORK);
				}
			});
		}
	}
}
