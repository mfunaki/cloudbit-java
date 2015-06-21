package net.mayoct.hcp.littlebits;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.mayoct.hcp.littlebits.persistence.Cloudbit;

import org.json.JSONObject;

public class CloudbitAPIAccessor {
	protected static final String API_ENTRY = "https://api-http.littlebitscloud.cc";

	public CloudbitAPIAccessor() {
		// nothing to do
	}
	
	public Cloudbit getCloudbit(String deviceId, String accessToken) 
			throws IOException {
		
		URL url = new URL(API_ENTRY + "/devices/" + deviceId);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("GET");
		urlConnection.setDoOutput(true);
		urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
		urlConnection.setRequestProperty("Accept", "application/vnd.littlebits.v2+json");
		urlConnection.connect();
		
		int responseCode = urlConnection.getResponseCode();
		if (responseCode != 200 && responseCode != 201) {
			String message = "(" + responseCode + "): " +
					urlConnection.getResponseMessage();
			throw new IOException("getCloudbit" + message);
		}
		
		Cloudbit cloudbit = new Cloudbit();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(urlConnection.getInputStream()));
		String line;
		StringBuffer body = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			body.append(line);
		}
		
		JSONObject json = new JSONObject(body.toString());
		cloudbit.setDeviceId(json.getString("id"));
		cloudbit.setLabel(json.getString("label"));
		try {
			cloudbit.setUserId(json.getString("user_id"));
		} catch (Exception e) {
			int userId = json.getInt("user_id");
			cloudbit.setUserId(Integer.toString(userId));
		}
		cloudbit.setConnected(json.getBoolean("is_connected"));
		
		return cloudbit;
	}
	
	public static void main(String[] args) {
		String deviceId = args[0];
		String accessToken = args[1];
		
		CloudbitAPIAccessor accessor = new CloudbitAPIAccessor();
		try {
			Cloudbit cloudbit = accessor.getCloudbit(deviceId, accessToken);
			System.out.println(cloudbit.toString());
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
	}

}
