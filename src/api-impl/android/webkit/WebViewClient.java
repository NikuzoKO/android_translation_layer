package android.webkit;

public class WebViewClient {
	public void onPageStarted(WebView view, String url) {}
	public void onPageFinished(WebView view, String url) {}

	public android.webkit.WebResourceResponse shouldInterceptRequest(android.webkit.WebView arg0, android.webkit.WebResourceRequest arg1) { return ((Object)this) instanceof android.webkit.WebResourceResponse ? (android.webkit.WebResourceResponse)(Object)this : null; } // ATL-autostub
	public boolean shouldOverrideKeyEvent(android.webkit.WebView arg0, android.view.KeyEvent arg1) { return false; } // ATL-autostub
	public boolean shouldOverrideUrlLoading(android.webkit.WebView arg0, android.webkit.WebResourceRequest arg1) { return false; } // ATL-autostub
	public boolean shouldOverrideUrlLoading(android.webkit.WebView arg0, java.lang.String arg1) { return false; } // ATL-autostub
	public void doUpdateVisitedHistory(android.webkit.WebView arg0, java.lang.String arg1, boolean arg2) {} // ATL-autostub
	public void onFormResubmission(android.webkit.WebView arg0, android.os.Message arg1, android.os.Message arg2) {} // ATL-autostub
	public void onLoadResource(android.webkit.WebView arg0, java.lang.String arg1) {} // ATL-autostub
	public void onPageCommitVisible(android.webkit.WebView arg0, java.lang.String arg1) {} // ATL-autostub
	public void onPageStarted(android.webkit.WebView arg0, java.lang.String arg1, android.graphics.Bitmap arg2) {} // ATL-autostub
	public void onReceivedError(android.webkit.WebView arg0, int arg1, java.lang.String arg2, java.lang.String arg3) {} // ATL-autostub
	public void onReceivedHttpError(android.webkit.WebView arg0, android.webkit.WebResourceRequest arg1, android.webkit.WebResourceResponse arg2) {} // ATL-autostub
	public void onReceivedLoginRequest(android.webkit.WebView arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3) {} // ATL-autostub
	public void onScaleChanged(android.webkit.WebView arg0, float arg1, float arg2) {} // ATL-autostub
	public void onUnhandledKeyEvent(android.webkit.WebView arg0, android.view.KeyEvent arg1) {} // ATL-autostub
}
