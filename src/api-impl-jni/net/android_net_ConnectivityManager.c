#include <gio/gio.h>
#include <stdio.h>
#include <string.h>

#include "../defines.h"
#include "../util.h"

#include "../generated_headers/android_net_ConnectivityManager.h"

JNIEXPORT jboolean JNICALL Java_android_net_ConnectivityManager_isActiveNetworkMetered(JNIEnv *env, jobject this)
{
	return g_network_monitor_get_network_metered(g_network_monitor_get_default());
}

/* A default route in the main table, IPv4 (/proc/net/route) or IPv6 (/proc/net/ipv6_route).
 * GNetworkMonitorNM reports no network whenever NetworkManager's connectivity is below
 * "limited", and NM's connectivity check only counts default routes it configured in the
 * main table: on mobile data with pmOS's wwan policy (route table 1430) it stays "local only"
 * although the kernel routes everything through the modem. */
static gboolean have_default_route(void)
{
	char line[256];
	gboolean found = FALSE;
	FILE *f = fopen("/proc/net/route", "r");
	if (f) {
		char iface[32], dest[16];
		unsigned flags;
		while (!found && fgets(line, sizeof(line), f))
			if (sscanf(line, "%31s %15s %*s %x", iface, dest, &flags) == 3 && !strcmp(dest, "00000000") && (flags & 0x1) /* RTF_UP */ && strcmp(iface, "lo"))
				found = TRUE;
		fclose(f);
	}
	f = found ? NULL : fopen("/proc/net/ipv6_route", "r");
	if (f) {
		char dest[40], iface[32];
		unsigned plen, flags;
		while (!found && fgets(line, sizeof(line), f))
			if (sscanf(line, "%39s %x %*s %*s %*s %*s %*s %*s %x %31s", dest, &plen, &flags, iface) == 4 && plen == 0 && !strcmp(dest, "00000000000000000000000000000000") && (flags & 0x1) && !(flags & 0x200) /* RTF_REJECT */ && strcmp(iface, "lo"))
				found = TRUE;
		fclose(f);
	}
	return found;
}

static gboolean network_available(void)
{
	return g_network_monitor_get_network_available(g_network_monitor_get_default()) || have_default_route();
}

JNIEXPORT jboolean JNICALL Java_android_net_ConnectivityManager_nativeGetNetworkAvailable(JNIEnv *env, jobject this)
{
	return network_available();
}

static void on_network_changed(GNetworkMonitor *self, gboolean available, jobject callback)
{
	JNIEnv *env = get_jni_env();
	jmethodID method;
	/* the signal's flag follows NM's connectivity, see have_default_route() */
	if (available || network_available()) {
		method = _METHOD(_CLASS(callback), "onAvailable", "(Landroid/net/Network;)V");
	} else {
		method = _METHOD(_CLASS(callback), "onLost", "(Landroid/net/Network;)V");
	}
	(*env)->CallVoidMethod(env, callback, method, NULL);
	if ((*env)->ExceptionCheck(env))
		(*env)->ExceptionDescribe(env);
}

JNIEXPORT void JNICALL Java_android_net_ConnectivityManager_nativeRegisterMonitor(JNIEnv *env, jobject this, jobject callback)
{
	g_signal_connect(g_network_monitor_get_default(), "network-changed", G_CALLBACK(on_network_changed), _REF(callback));
}
