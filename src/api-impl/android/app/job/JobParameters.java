package android.app.job;

import android.net.Uri;
import android.os.Parcelable;
import android.os.PersistableBundle;

public class JobParameters implements Parcelable {

	public static final Creator<JobParameters> CREATOR = null;

	JobInfo jobInfo;

	JobParameters(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}

	public PersistableBundle getExtras() {
		return jobInfo.getExtras();
	}

	public Uri[] getTriggeredContentUris() {
		return new Uri[0];
	}

	public String[] getTriggeredContentAuthorities() {
		return new String[0];
	}

	public android.app.job.JobWorkItem dequeueWork() { return ((Object)this) instanceof android.app.job.JobWorkItem ? (android.app.job.JobWorkItem)(Object)this : null; } // ATL-autostub
	public android.net.Network getNetwork() { return ((Object)this) instanceof android.net.Network ? (android.net.Network)(Object)this : null; } // ATL-autostub
	public boolean isOverrideDeadlineExpired() { return false; } // ATL-autostub
	public int getJobId() { return 0; } // ATL-autostub
	public int getStopReason() { return 0; } // ATL-autostub
	public void completeWork(android.app.job.JobWorkItem arg0) {} // ATL-autostub
}
