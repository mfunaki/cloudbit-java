package net.mayoct.hcp.littlebits.persistence;

/**
 * Cloudbit class manages cloudBit data returned from
 * GET method to /device/{device_id} entry point.
 * See http://developer.littlebitscloud.cc/#-devices-device-id-
 * @author mfunaki
 */
public class Cloudbit {
	// "id": "000001"
	private String deviceId;

	// "label": "000001"
	private String label;
	
	// "subscribers": []
	// "subscriptions": []

	// (different from the doc) "user_id": "1" -> 1
	private String userId;
	
	// "wifi": {}
	
	// (not in the doc) "is_connected": false
	private boolean connected;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	@Override
	public String toString() {
		return "Cloudbit ["
				+ (deviceId != null ? "deviceId=" + deviceId + ", " : "")
				+ (label != null ? "label=" + label + ", " : "")
				+ (userId != null ? "userId=" + userId + ", " : "")
				+ "connected=" + connected + "]";
	}
}
