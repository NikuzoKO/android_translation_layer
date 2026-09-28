package android.text.style;

public class URLSpan extends CharacterStyle {

	private String url;

	public URLSpan(String url) {
		this.url = url;
	}

	public String getURL() {
		return url;
	}

	public void onClick(android.view.View arg0) {} // ATL-autostub
}
