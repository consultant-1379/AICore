package com.ericsson.nms.security.aicore.upgrade;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import com.ericsson.oss.itpf.sdk.recording.EventLevel;
import com.ericsson.oss.itpf.sdk.recording.SystemRecorder;
import com.ericsson.oss.itpf.sdk.upgrade.UpgradeEvent;
import com.ericsson.oss.itpf.sdk.upgrade.UpgradePhase;

@ApplicationScoped
public class UpgradeEventObserver {

	@Inject
	private SystemRecorder systemRecorder;

	public void upgradeNotificationObserver(@Observes final UpgradeEvent event) {

		final UpgradePhase phase = event.getPhase();
		switch (phase) {
		case SERVICE_INSTANCE_UPGRADE_PREPARE:
		case SERVICE_CLUSTER_UPGRADE_PREPARE:
		case SERVICE_CLUSTER_UPGRADE_FAILED:
		case SERVICE_CLUSTER_UPGRADE_FINISHED_SUCCESSFULLY:
		case SERVICE_INSTANCE_UPGRADE_FAILED:
		case SERVICE_INSTANCE_UPGRADE_FINISHED_SUCCESSFULLY:
			event.accept("OK");
			recordEvent("Auto-Integration Config Storage Service has accepted upgrade event", phase);
			break;

		default:
			event.reject("Unexpected UpgradePhase");
			recordEvent("Auto-Integration Config Storage Service has rejected event", phase);
			break;
		}
	}

	/**
	 * Records Event
	 * 
	 * @param event
	 *            The event to record
	 */
	private void recordEvent(final String eventDesc, final UpgradePhase phase) {
		systemRecorder.recordEvent(eventDesc + " : " + phase.toString(),
				EventLevel.COARSE, "Upgrade Event : " + phase.toString(),
				"Auto-Integration Config Storage Service", "");
	}

	/**
	 * For Unit Test purposes only
	 * 
	 * @param systemRecorder
	 */
	protected void setSystemRecorder(final SystemRecorder systemRecorder) {
		this.systemRecorder = systemRecorder;
	}
}
