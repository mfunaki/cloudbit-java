package net.mayoct.hcp.littlebits.persistence;

public class CloudbitEvent {
	private String eventId;
	// device_id:"000001"
	private String eventBitId;
	// user_id:<Int>
	private int eventUserId = -1;
	// timestamp:<Int>
	private int eventTimestamp = -1;
	// type:"amplitude"
	private String eventType;
	
	// payload
	// absolute:*
	private int payloadAbsolute = -1;
	// percent:*
	private int payloadPercent = -1;
	// delta:'ignite', 'sustain' | 'ignite'
	private String payloadDelta;
	// level:*
	private String payloadLevel;
	
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventBitId() {
		return eventBitId;
	}
	public void setEventBitId(String eventBitId) {
		this.eventBitId = eventBitId;
	}

	public int getEventUserId() {
		return eventUserId;
	}
	public void setEventUserId(int eventUserId) {
		this.eventUserId = eventUserId;
	}

	public int getEventTimestamp() {
		return eventTimestamp;
	}
	public void setEventTimestamp(int eventTimestamp) {
		this.eventTimestamp = eventTimestamp;
	}

	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public int getPayloadAbsolute() {
		return payloadAbsolute;
	}
	public void setPayloadAbsolute(int payloadAbsolute) {
		this.payloadAbsolute = payloadAbsolute;
	}

	public int getPayloadPercent() {
		return payloadPercent;
	}
	public void setPayloadPercent(int payloadPercent) {
		this.payloadPercent = payloadPercent;
	}

	public String getPayloadDelta() {
		return payloadDelta;
	}
	public void setPayloadDelta(String payloadDelta) {
		this.payloadDelta = payloadDelta;
	}

	public String getPayloadLevel() {
		return payloadLevel;
	}
	public void setPayloadLevel(String payloadLevel) {
		this.payloadLevel = payloadLevel;
	}
	
	@Override
	public String toString() {
		return "CloudbitEvent ["
				+ (eventId != null ? "eventId=" + eventId + ", " : "")
				+ (eventBitId != null ? "eventBitId=" + eventBitId + ", " : "")
				+ "eventUserId="
				+ eventUserId
				+ ", eventTimestamp="
				+ eventTimestamp
				+ ", "
				+ (eventType != null ? "eventType=" + eventType + ", " : "")
				+ "payloadAbsolute="
				+ payloadAbsolute
				+ ", payloadPercent="
				+ payloadPercent
				+ ", "
				+ (payloadDelta != null ? "payloadDelta=" + payloadDelta + ", "
						: "")
				+ (payloadLevel != null ? "payloadLevel=" + payloadLevel : "")
				+ "]";
	}
}
