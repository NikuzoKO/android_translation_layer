package android.net;

/* ATL has one network: the host's default route, reported as validated WiFi internet. */
public final class NetworkCapabilities {
	public static final int NET_CAPABILITY_MMS = 0;
	public static final int NET_CAPABILITY_SUPL = 1;
	public static final int NET_CAPABILITY_DUN = 2;
	public static final int NET_CAPABILITY_FOTA = 3;
	public static final int NET_CAPABILITY_IMS = 4;
	public static final int NET_CAPABILITY_CBS = 5;
	public static final int NET_CAPABILITY_WIFI_P2P = 6;
	public static final int NET_CAPABILITY_IA = 7;
	public static final int NET_CAPABILITY_RCS = 8;
	public static final int NET_CAPABILITY_XCAP = 9;
	public static final int NET_CAPABILITY_EIMS = 10;
	public static final int NET_CAPABILITY_NOT_METERED = 11;
	public static final int NET_CAPABILITY_INTERNET = 12;
	public static final int NET_CAPABILITY_NOT_RESTRICTED = 13;
	public static final int NET_CAPABILITY_TRUSTED = 14;
	public static final int NET_CAPABILITY_NOT_VPN = 15;
	public static final int NET_CAPABILITY_VALIDATED = 16;
	public static final int NET_CAPABILITY_CAPTIVE_PORTAL = 17;
	public static final int NET_CAPABILITY_NOT_ROAMING = 18;
	public static final int NET_CAPABILITY_FOREGROUND = 19;
	public static final int NET_CAPABILITY_NOT_CONGESTED = 20;
	public static final int NET_CAPABILITY_NOT_SUSPENDED = 21;
	public static final int NET_CAPABILITY_TEMPORARILY_NOT_METERED = 25;

	public static final int TRANSPORT_CELLULAR = 0;
	public static final int TRANSPORT_WIFI = 1;
	public static final int TRANSPORT_BLUETOOTH = 2;
	public static final int TRANSPORT_ETHERNET = 3;
	public static final int TRANSPORT_VPN = 4;
	public static final int TRANSPORT_WIFI_AWARE = 5;
	public static final int TRANSPORT_LOWPAN = 6;
	public static final int TRANSPORT_USB = 8;

	private final boolean available;
	private final boolean metered;

	public NetworkCapabilities() {
		this(true, false);
	}

	NetworkCapabilities(boolean available, boolean metered) {
		this.available = available;
		this.metered = metered;
	}

	public boolean hasCapability(int capability) {
		switch (capability) {
			case NET_CAPABILITY_INTERNET:
			case NET_CAPABILITY_VALIDATED:
			case NET_CAPABILITY_NOT_RESTRICTED:
			case NET_CAPABILITY_TRUSTED:
			case NET_CAPABILITY_NOT_VPN:
			case NET_CAPABILITY_NOT_ROAMING:
			case NET_CAPABILITY_FOREGROUND:
			case NET_CAPABILITY_NOT_CONGESTED:
			case NET_CAPABILITY_NOT_SUSPENDED:
				return available;
			case NET_CAPABILITY_NOT_METERED:
				return available && !metered;
			default:
				return false;
		}
	}

	public int[] getCapabilities() {
		java.util.ArrayList<Integer> caps = new java.util.ArrayList<Integer>();
		for (int c = 0; c <= NET_CAPABILITY_TEMPORARILY_NOT_METERED; c++)
			if (hasCapability(c))
				caps.add(c);
		int[] ret = new int[caps.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = caps.get(i);
		return ret;
	}

	public boolean hasTransport(int transportType) {
		return available && transportType == TRANSPORT_WIFI;
	}

	public int[] getTransportTypes() {
		return available ? new int[] {TRANSPORT_WIFI} : new int[0];
	}

	public int getLinkDownstreamBandwidthKbps() {
		return 100000;
	}

	public int getLinkUpstreamBandwidthKbps() {
		return 50000;
	}

	public int getSignalStrength() {
		return -50;
	}

	public int getOwnerUid() {
		return -1;
	}

	public Object getTransportInfo() {
		return null;
	}

	@Override
	public String toString() {
		return "[ Transports: " + (available ? "WIFI" : "") + " Capabilities: INTERNET&VALIDATED" + (metered ? "" : "&NOT_METERED") + " ]";
	}
}
