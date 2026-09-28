package android.net;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import javax.net.SocketFactory;

/* The one network ATL knows about: the host's default route. */
public class Network {
	public long getNetworkHandle() {
		return 1L;
	}

	public InetAddress[] getAllByName(String host) throws UnknownHostException {
		return InetAddress.getAllByName(host);
	}

	public InetAddress getByName(String host) throws UnknownHostException {
		return InetAddress.getByName(host);
	}

	public SocketFactory getSocketFactory() {
		return SocketFactory.getDefault();
	}

	public URLConnection openConnection(URL url) throws IOException {
		return url.openConnection();
	}

	public void bindSocket(Socket socket) throws IOException {}

	public void bindSocket(DatagramSocket socket) throws IOException {}

	public void bindSocket(java.io.FileDescriptor fd) throws IOException {}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Network;
	}

	@Override
	public int hashCode() {
		return 100;
	}

	@Override
	public String toString() {
		return "100";
	}
}
