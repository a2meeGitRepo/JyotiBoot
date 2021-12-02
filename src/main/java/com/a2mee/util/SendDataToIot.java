package com.a2mee.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;


@PropertySource("classpath:sql.properties")
public class SendDataToIot {
//private String iotUrl="http://beta-api.superaxis.in/740274707cb6f67332d1586323807698/updateInspectionRejects?parameqid=%s&paraminspectionTimestamp=%s&parammouldcode=%s&paramproductcode=%s&paramrejectReasonCode=%s&paramrejectionQty=%s&paramtimestamp=%s";


	//String url="http://beta-api.superaxis.in/740274707cb6f67332d1586323807698/updateInspectionRejects?parameqid=2245&paraminspectionTimestamp=20180906024549&parammouldcode=ECO202&paramproductcode=XYZ+JKL1%401&paramrejectReasonCode=2&paramrejectionQty=15&paramtimestamp=20180107162808";
	public void sendGET(String parameqid, String paraminspectionTimestamp, String parammouldcode,
			String paramproductcode, String paramrejectReasonCode, String paramrejectionQty, String paramtimestamp)
			throws IOException {
		String grt_URL="http://beta-api.superaxis.in/740274707cb6f67332d1586323807698/updateInspectionRejects?parameqid="+parameqid+"&paraminspectionTimestamp="+paraminspectionTimestamp+"&parammouldcode="+parammouldcode+"&paramproductcode="+paramproductcode+"&paramrejectReasonCode="+paramrejectReasonCode+"&paramrejectionQty="+paramrejectionQty+"&paramtimestamp="+paramtimestamp;
         String url=grt_URL.replace(" ", "%20");
		/*System.out.println("url===:"+iotUrl);
		String GET_URL = String.format(iotUrl, parameqid, paraminspectionTimestamp, parammouldcode, paramproductcode,
				paramrejectReasonCode, paramrejectionQty, paramtimestamp);*/
		//URL obj = new URL(grt_URL);......final 
         System.out.println("url=="+url);
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		String encoded = Base64.getEncoder().encodeToString(("renataapiuser:GI5INsr9Xh").getBytes());
		con.setRequestProperty("Authorization", "Basic " + encoded);
		con.setRequestMethod("GET");
		con.connect();

		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println(response.toString());
		} else {
			System.out.println("GET request not worked");
		}

	}

}
