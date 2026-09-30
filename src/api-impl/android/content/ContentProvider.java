package android.content;

import android.atl.ATLContactsContentProvider;
import android.atl.ATLLoadedApp;
import android.atl.ATLMediaContentProvider;
import android.atl.ATLProvider;
import android.content.pm.PackageParser;
import android.content.pm.ProviderInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public abstract class ContentProvider {

	static final HashMap<String, ATLProvider> atl_providers = new HashMap<>();

	static void createContentProviders() {
		atl_providers.put("media", new ATLProvider(new ATLMediaContentProvider()));
		atl_providers.put("com.android.contacts", new ATLProvider(new ATLContactsContentProvider()));
		ATLLoadedApp primary = ATLLoadedApp.getPrimaryApplication();
		for (PackageParser.Provider provider_parsed : primary.pkg.providers) {
			String process_name = provider_parsed.info.processName;
			if (process_name != null && process_name.contains(":")) {
				/* NOTE: even if it doesn't contain `:`, if it's not null we probably
				 * need to check what it's requesting; `:` means it wants us to spawn
				 * a new process, which we currently don't support */
				System.out.println("not creating provider " + provider_parsed.className + ", it wants to be started in a new process (" + process_name + ")");
				continue;
			}
			atl_providers.put(provider_parsed.info.authority,
			                  new ATLProvider(primary, provider_parsed));
		}
		// getContentProvider() initializes the content providers,
		// we also opportunistically remove the ones that failed to load
		atl_providers.values().removeIf(new Predicate<ATLProvider>() {
			@Override
			public boolean test(ATLProvider atlProvider) {
				return atlProvider.getContentProvider() == null;
			}
		});
	}

	static ContentProvider atl_get_content_provider(String authority) {
		ATLProvider atlProvider = atl_providers.get(authority);
		return atlProvider == null ? null : atlProvider.getContentProvider();
	}

	public boolean onCreate() { return false; }

	public Context getContext() {
		return ATLLoadedApp.getPrimaryApplication().getApplication();
	}

	public abstract Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder);

	public abstract Uri insert(Uri uri, ContentValues values);

	public abstract int update(Uri uri, ContentValues values, String selection, String[] selectionArgs);

	public abstract int delete(Uri uri, String selection, String[] selectionArgs);

	public abstract String getType(Uri uri);

	public abstract ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException;

	public AssetFileDescriptor openAssetFile(Uri uri, String mode) throws FileNotFoundException {
		ParcelFileDescriptor fd = openFile(uri, mode);
		return fd != null ? new AssetFileDescriptor(fd, 0, -1) : null;
	}

	public void attachInfo(Context context, ProviderInfo provider) {}

	public String getCallingPackage() {
		return ATLLoadedApp.getPrimaryApplication().pkg.packageName;
	}

	public android.content.res.AssetFileDescriptor openTypedAssetFile(android.net.Uri arg0, java.lang.String arg1, android.os.Bundle arg2) { return ((Object)this) instanceof android.content.res.AssetFileDescriptor ? (android.content.res.AssetFileDescriptor)(Object)this : null; } // ATL-autostub
	public android.database.Cursor query(android.net.Uri arg0, java.lang.String[] arg1, android.os.Bundle arg2, android.os.CancellationSignal arg3) { return ((Object)this) instanceof android.database.Cursor ? (android.database.Cursor)(Object)this : null; } // ATL-autostub
	public android.database.Cursor query(android.net.Uri arg0, java.lang.String[] arg1, java.lang.String arg2, java.lang.String[] arg3, java.lang.String arg4, android.os.CancellationSignal arg5) { return ((Object)this) instanceof android.database.Cursor ? (android.database.Cursor)(Object)this : null; } // ATL-autostub
	public android.os.Bundle call(java.lang.String arg0, java.lang.String arg1, android.os.Bundle arg2) { return ((Object)this) instanceof android.os.Bundle ? (android.os.Bundle)(Object)this : null; } // ATL-autostub
	public android.os.ParcelFileDescriptor openFile(android.net.Uri arg0, java.lang.String arg1, android.os.CancellationSignal arg2) { return ((Object)this) instanceof android.os.ParcelFileDescriptor ? (android.os.ParcelFileDescriptor)(Object)this : null; } // ATL-autostub
	public boolean isTemporary() { return false; } // ATL-autostub
	public int bulkInsert(android.net.Uri arg0, android.content.ContentValues[] arg1) { return 0; } // ATL-autostub
	public java.lang.String[] getStreamTypes(android.net.Uri arg0, java.lang.String arg1) { return null; } // ATL-autostub
	public void onConfigurationChanged(android.content.res.Configuration arg0) {} // ATL-autostub
	public void onLowMemory() {} // ATL-autostub
	public void onTrimMemory(int arg0) {} // ATL-autostub
	public void shutdown() {} // ATL-autostub
}
