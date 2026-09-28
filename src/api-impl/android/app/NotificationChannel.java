package android.app;

import android.media.AudioAttributes;
import android.net.Uri;

public class NotificationChannel {

	public NotificationChannel(String id, CharSequence name, int importance) {}

	public void setLockscreenVisibility(int a) {}
	public void setShowBadge(boolean a) {}
	public void setGroup(String grp) {}
	public void enableLights(boolean en) {}
	public void setLightColor(int color) {}
	public void setVibrationPattern(long[] pattern) {}
	public void enableVibration(boolean en) {}
	public void setSound(Uri uri, AudioAttributes attrs) {}
	public boolean shouldShowLights() { return false; }
	public int getLightColor() { return 0; }
	public boolean shouldVibrate() { return false; }
	public Uri getSound() { return null; }
	public void setDescription(String description) {}
	public void setBypassDnd(boolean bypassDnd) {}

	public android.media.AudioAttributes getAudioAttributes() { return ((Object)this) instanceof android.media.AudioAttributes ? (android.media.AudioAttributes)(Object)this : null; } // ATL-autostub
	public android.os.VibrationEffect getVibrationEffect() { return ((Object)this) instanceof android.os.VibrationEffect ? (android.os.VibrationEffect)(Object)this : null; } // ATL-autostub
	public boolean canBypassDnd() { return false; } // ATL-autostub
	public boolean canShowBadge() { return false; } // ATL-autostub
	public boolean hasUserSetSound() { return false; } // ATL-autostub
	public int getImportance() { return 0; } // ATL-autostub
	public int getLockscreenVisibility() { return 0; } // ATL-autostub
	public java.lang.CharSequence getName() { return null; } // ATL-autostub
	public java.lang.String getConversationId() { return null; } // ATL-autostub
	public java.lang.String getGroup() { return null; } // ATL-autostub
	public java.lang.String getId() { return null; } // ATL-autostub
	public java.lang.String getParentChannelId() { return null; } // ATL-autostub
	public long[] getVibrationPattern() { return null; } // ATL-autostub
	public void setConversationId(java.lang.String arg0, java.lang.String arg1) {} // ATL-autostub
	public void setName(java.lang.CharSequence arg0) {} // ATL-autostub
	public void setVibrationEffect(android.os.VibrationEffect arg0) {} // ATL-autostub
}
