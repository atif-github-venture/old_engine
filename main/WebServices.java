package JoS.JAVAProjects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.google.gson.JsonObject;

public class WebServices {
	ApplicationParams gOBJ = null;// =

	WebServices(ApplicationParams gOBJTemp) {
		gOBJ = gOBJTemp;
	}

	/*public static void main1(String[] args) {

		try {

			URL url = new URL("http://sadc1lnxwsd6:8083/Ent-LexisNexis-OrderReports/orderReports");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}


	public static void main2(String[] args) {

		try {

			URL url = new URL("http://sadc1lnxwsd6:8083/Ent-LexisNexis-OrderReports/orderReports/save");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			//String input =	"{\"type\" : \"CREDIT\",\"lastName\" : \"Uppala\", \"firstName\" : \"Ram\",\"middleName\" : \"k\", \"dob\" : \"2015-10-22T14:21:53Z\",\"dln\":\"IL12345\",\"gender\":\"M\",\"ssn\":\"11111111\",\"street\" : \"2125 Triad ct\",\"city\" : \"Columbus\",\"state\" : \"OH\",\"zip\" : \"43215\"}";
			String input =	"{\"type\" : \"CREDIT\",\"source\" : \"GW_PC\",\"clientId\":\"11111\",\"lexId\":\"11111\",\"ssn\":\"11111111\"," +
					"\"transactionId\" : \"1234\",\"referenceNum\":\"11111\",\"authCode\":\"11111\",\"status\":\"good\"," +
					"\"vendor\" : \"LN\",\"dateOrdered\" : \"2015-10-22T14:21:53Z\",\"dateReceived\" : \"2015-10-22T14:21:53Z\"," +
					"\"gender\":\"M\",\"dln\":\"IL12345\",\"lastName\" : \"Uppala\",\"firstName\" : \"Ram\",\"middleName\" : \"k\",\"dob\" : \"2015-10-22T14:21:53Z\"," + 
					"\"street\" : \"2125 Triad ct\",\"city\" : \"Columbus\",\"state\" : \"OH\",\"zip\" : \"43215\",\"requestXml\" : \"<products><name>Test</name></products>\"," + 
					"\"responseXml\" : \"<products><name>Test</name></products>\"}";


			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();


			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}
*/
	public String PostParamatersToURLs(String strURL, String xmlFileName,String DestPath) {
		System.out.println("Inside PostParamatersToURLs()...");
		String outputStr = null;
		try {
			System.out.println(strURL);
			String opData = "";
			URL url = new URL(strURL);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setDoOutput(true);
			if (!xmlFileName.isEmpty()) {
				uc.setRequestMethod("POST");
				uc.setDoInput(true);

				uc.setRequestProperty("Content-Type", "application/xml");
				OutputStream out = uc.getOutputStream();
				FileInputStream input = new FileInputStream(xmlFileName);
				byte[] fileData = new byte[input.available()];
				input.read(fileData);
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				bout.write(fileData);
				byte[] b = bout.toByteArray();
				out.write(b);
				input.close();
				out.close();
			} else
				uc.setRequestMethod("GET");
			int rspCode = uc.getResponseCode();
			System.out.println("rspCode:  " + rspCode);
			outputStr += rspCode + ",";
			System.out.println("outputStr:  " + outputStr);
			if ((rspCode == 200) && !xmlFileName.isEmpty()) {
				InputStream is = uc.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);

				String nextLine = br.readLine();

				while (nextLine != null) {
					opData += nextLine;
					nextLine = br.readLine();
				}
				System.out.println("opData:   " + opData);
				new DebugInterface(gOBJ).Debug("Output Response:\n" + opData);

				// if destination path is not mentioned the response is stored
				// along with test results
				if (DestPath == "") {
					File tempDir = new File(
							System.getProperty("java.io.tmpdir"));
					System.out.println("Temp file : " + tempDir);
					String tempDirPath = tempDir.getAbsolutePath()
							+ "\\Run_mm-dd-yyyy_hh-mm-ss_XX";
					DestPath = tempDirPath;
				}
				File DestPathout = new File(DestPath + "\\Response.xml");
				FileOutputStream fos = new FileOutputStream(DestPathout, true);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
						fos));
				try {
					bw.write(opData);
					bw.newLine();
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (opData != "") {
					new DebugInterface(gOBJ)
					.Debug(gOBJ.getgStrTaskID()
							+ " Step - "
							+ gOBJ.getgIntExecOrder()
							+ "-"
							+ gOBJ.getgObjType()
							+ " - The web service call returned response: Pass");
					new Utils(gOBJ).ReportStepToResultDB("The web service call returned response", "Pass");
				} else {
					new DebugInterface(gOBJ)
					.Debug(gOBJ.getgStrTaskID()
							+ " Step - "
							+ gOBJ.getgIntExecOrder()
							+ "-"
							+ gOBJ.getgObjType()
							+ " - The web service call didn't returned response: Fail");
					new Utils(gOBJ).ReportStepToResultDB("The web service call didn't returned response", "Fail");
				}
				return opData;
			} else
				return "";
		}

		catch (IOException e) {
			e.printStackTrace();
		}
		return "";

	}
	/*

private JsonObject uploadToServer() throws IOException, JSONException {
            String query = "https://example.com";
            String json = "{\"key\":1}";

            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            JSONObject jsonObject = new JSONObject(result);


            in.close();
            conn.disconnect();

            return jsonObject;
    }


	public static void main1(String[] args) throws IOException {
		System.out.println("Inside PostParamatersToURLs()...");
		String outputStr = null;
		String strURL = "http://sadc1lnxwsd6:8083/Ent-LexisNexis-OrderReports/orderReports/save";
		String xmlFileName = "C:\\Users\\ahm9997\\Desktop\\dddd.txt"; 
		String DestPath = "";
		try {
			System.out.println(strURL);
			String opData = "";
			URL url = new URL(strURL);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setDoOutput(true);
			if (!xmlFileName.isEmpty()) {
				uc.setRequestMethod("POST");
				uc.setDoInput(true);

				//uc.setRequestProperty("Content-type","text/json; charset=utf-8");
				uc.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
				uc.setRequestProperty("Accept", "application/json");
				OutputStream out = uc.getOutputStream();
				FileInputStream input = new FileInputStream(xmlFileName);
				byte[] fileData = new byte[input.available()];
				input.read(fileData);
				System.out.println("fileData: " + fileData.toString());
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				bout.write(fileData);
				
				byte[] b = bout.toByteArray();
				out.write(b);
				//out.flush();
				System.out.println(out.toString());
				input.close();
				out.close();
			} else
				uc.setRequestMethod("GET");
			int rspCode = uc.getResponseCode();
			System.out.println("rspCode:  " + rspCode);
			outputStr += rspCode + ",";
			System.out.println("outputStr:  " + outputStr);
			if ((rspCode == 200) && !xmlFileName.isEmpty()) {
				InputStream is = uc.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);

				String nextLine = br.readLine();

				while (nextLine != null) {
					opData += nextLine;
					nextLine = br.readLine();
				}
				System.out.println("opData:   " + opData);


				// if destination path is not mentioned the response is stored
				// along with test results
				if (DestPath == "") {
					File tempDir = new File(
							System.getProperty("java.io.tmpdir"));
					System.out.println("Temp file : " + tempDir);
					String tempDirPath = tempDir.getAbsolutePath()
							+ "\\Run_mm-dd-yyyy_hh-mm-ss_XX";
					DestPath = tempDirPath;
				}
				File DestPathout = new File(DestPath + "\\Response.xml");
				FileOutputStream fos = new FileOutputStream(DestPathout, true);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
						fos));
				try {
					bw.write(opData);
					bw.newLine();
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (opData != "") {

				} else {

				}
				return;
			} else
				return;
		}

		catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}*/
}
