package android.net;

public class NetworkRequest {
	private final int[] capabilities;
	private final int[] transports;

	private NetworkRequest(int[] capabilities, int[] transports) {
		this.capabilities = capabilities;
		this.transports = transports;
	}

	public boolean hasCapability(int capability) {
		for (int c : capabilities)
			if (c == capability)
				return true;
		return false;
	}

	public boolean hasTransport(int transportType) {
		for (int t : transports)
			if (t == transportType)
				return true;
		return false;
	}

	public int[] getCapabilities() {
		return capabilities.clone();
	}

	public int[] getTransportTypes() {
		return transports.clone();
	}

	public static class Builder {
		private java.util.ArrayList<Integer> capabilities = new java.util.ArrayList<Integer>();
		private java.util.ArrayList<Integer> transports = new java.util.ArrayList<Integer>();

		public Builder() {
			capabilities.add(NetworkCapabilities.NET_CAPABILITY_INTERNET);
			capabilities.add(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED);
			capabilities.add(NetworkCapabilities.NET_CAPABILITY_TRUSTED);
			capabilities.add(NetworkCapabilities.NET_CAPABILITY_NOT_VPN);
		}

		public NetworkRequest build() {
			int[] c = new int[capabilities.size()];
			for (int i = 0; i < c.length; i++)
				c[i] = capabilities.get(i);
			int[] t = new int[transports.size()];
			for (int i = 0; i < t.length; i++)
				t[i] = transports.get(i);
			return new NetworkRequest(c, t);
		}

		public Builder addCapability(int capability) {
			if (!capabilities.contains(capability))
				capabilities.add(capability);
			return this;
		}

		public Builder removeCapability(int capability) {
			capabilities.remove((Integer)capability);
			return this;
		}

		public Builder clearCapabilities() {
			capabilities.clear();
			return this;
		}

		public Builder addTransportType(int transportType) {
			if (!transports.contains(transportType))
				transports.add(transportType);
			return this;
		}

		public Builder removeTransportType(int transportType) {
			transports.remove((Integer)transportType);
			return this;
		}

		public Builder setIncludeOtherUidNetworks(boolean include) {
			return this;
		}
	}
}
