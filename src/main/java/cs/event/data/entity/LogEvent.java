package cs.event.data.entity;

/**
 * Entity class for log event processor
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "logevent")
public class LogEvent {

	@Id
	@Column(name = "logid")
	private String logId;

	@Column(name = "state")
	private String state;

	@Column(name = "logtimestamp")
	private long logTimestamp;

	@Column(name = "logtype")
	private String logType;

	@Column(name = "hostname")
	private String hostName;

	@Column(name = "alert")
	private String alert;

	@Column(name = "duration")
	private long duration;

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getLogTimestamp() {
		return logTimestamp;
	}

	public void setLogTimestamp(long logTimestamp) {
		this.logTimestamp = logTimestamp;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "LogEvent [logId=" + logId + ", state=" + state + ", logTimestamp=" + logTimestamp + ", logType="
				+ logType + ", hostName=" + hostName + ", alert=" + alert + ", duration=" + duration + "]";
	}
}
