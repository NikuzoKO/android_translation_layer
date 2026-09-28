package android.webkit;

public class WebChromeClient {

	public android.graphics.Bitmap getDefaultVideoPoster() { return ((Object)this) instanceof android.graphics.Bitmap ? (android.graphics.Bitmap)(Object)this : null; } // ATL-autostub
	public android.view.View getVideoLoadingProgressView() { return ((Object)this) instanceof android.view.View ? (android.view.View)(Object)this : null; } // ATL-autostub
	public boolean onCreateWindow(android.webkit.WebView arg0, boolean arg1, boolean arg2, android.os.Message arg3) { return false; } // ATL-autostub
	public void getVisitedHistory(android.webkit.ValueCallback arg0) {} // ATL-autostub
	public void onCloseWindow(android.webkit.WebView arg0) {} // ATL-autostub
	public void onGeolocationPermissionsHidePrompt() {} // ATL-autostub
	public void onHideCustomView() {} // ATL-autostub
	public void onProgressChanged(android.webkit.WebView arg0, int arg1) {} // ATL-autostub
	public void onReceivedIcon(android.webkit.WebView arg0, android.graphics.Bitmap arg1) {} // ATL-autostub
	public void onReceivedTitle(android.webkit.WebView arg0, java.lang.String arg1) {} // ATL-autostub
	public void onReceivedTouchIconUrl(android.webkit.WebView arg0, java.lang.String arg1, boolean arg2) {} // ATL-autostub
	public void onRequestFocus(android.webkit.WebView arg0) {} // ATL-autostub
}
