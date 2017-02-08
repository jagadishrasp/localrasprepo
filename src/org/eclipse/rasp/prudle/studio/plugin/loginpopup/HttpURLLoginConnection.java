package org.eclipse.rasp.prudle.studio.plugin.loginpopup;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;

public class HttpURLLoginConnection {

	private final String USER_AGENT = "Mozilla/5.0";
	private static final String FILENAME = "E:\\Prudle Studio Dev\\test\\MessagesBundle_en_US05012017144449.properties";

	private String attachmentName = "Message";
	private String attachmentFileName = FILENAME;
	private String crlf = "\r\n";
	private String twoHyphens = "--";
	private String boundary = "*****";

	// HTTP GET request
	public String sendGet(String userId) throws Exception {

		String getUserId = null;
		String url = "http://prudlestudio.com/webapp/ASP.NetMVC5_Full_Version_v1.8.1/Upload/Upload/api/logindetails?id="
				+ userId;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		String tem = response.toString().replaceAll("\\[", "").replaceAll("\\]", "");

		ProjectDetails projectDetails = new Gson().fromJson(tem, ProjectDetails.class);
		System.out.println(projectDetails.getOwnerEmail());

		getUserId = projectDetails.getOwnerEmail();

		return getUserId;

	}

	// HTTP POST request
	public void sendPost() throws Exception {

		HttpURLConnection httpUrlConnection = null;
		File uploadFile = new File(FILENAME);
		String url = "http://prudlestudio.com/webapp/ASP.NetMVC5_Full_Version_v1.8.1/Upload/Upload/api/Upload/user/postuserimage?fname="
				+ uploadFile.getName().replaceFirst("[.][^.]+$", "");
		java.net.URL urlObj = new URL(url);

		httpUrlConnection = (HttpURLConnection) urlObj.openConnection();
		httpUrlConnection.setUseCaches(false);
		httpUrlConnection.setDoOutput(true);

		httpUrlConnection.setRequestMethod("POST");
		httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
		httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
		httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + this.boundary);

		DataOutputStream request = new DataOutputStream(httpUrlConnection.getOutputStream());

		request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
		request.writeBytes("Content-Disposition: form-data; name=\"" + this.attachmentName + "\";filename=\""+ this.attachmentFileName + "\"" + this.crlf);
		request.writeBytes(this.crlf);

		Path path = Paths.get(FILENAME);
		byte[] data = Files.readAllBytes(path);

		request.write(data);

		request.writeBytes(this.crlf);
		request.writeBytes(this.twoHyphens + this.boundary + this.twoHyphens + this.crlf);
		request.flush();
		request.close();

		int responseCode = httpUrlConnection.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		// System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());

	}
}
