package android.media;

public class AudioAttributes {

	int streamType;

	public int getFlags() { return 0; }

	public int getUsage() { return 0; }

	public class Builder {

		public Builder setContentType(int content_type) {
			return this;
		}

		public Builder setUsage(int usage) {
			return this;
		}

		public Builder setFlags(int flags) {
			return this;
		}

		public Builder setLegacyStreamType(int legacy_stream_type) {
			return this;
		}

		public AudioAttributes build() {
			return new AudioAttributes();
		}
	
	public android.media.AudioAttributes.Builder setAllowedCapturePolicy(int arg0) { return ((Object)this) instanceof android.media.AudioAttributes.Builder ? (android.media.AudioAttributes.Builder)(Object)this : null; } // ATL-autostub
	public android.media.AudioAttributes.Builder setHapticChannelsMuted(boolean arg0) { return ((Object)this) instanceof android.media.AudioAttributes.Builder ? (android.media.AudioAttributes.Builder)(Object)this : null; } // ATL-autostub
	public android.media.AudioAttributes.Builder setIsContentSpatialized(boolean arg0) { return ((Object)this) instanceof android.media.AudioAttributes.Builder ? (android.media.AudioAttributes.Builder)(Object)this : null; } // ATL-autostub
	public android.media.AudioAttributes.Builder setSpatializationBehavior(int arg0) { return ((Object)this) instanceof android.media.AudioAttributes.Builder ? (android.media.AudioAttributes.Builder)(Object)this : null; } // ATL-autostub
}
}
