package dataStructure;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.RandomStringUtils;


public class TestFrame{
	//per il calcolo della distanza
	private String service;

	private String name;
	private String tfID;
	
	public void setSelPayload(String selPayload) {
		this.selPayload = selPayload;
	}

	private double failureProb;
	private double occurrenceProb;

	//inputClass
	public ArrayList<InputClass> ic = new ArrayList<InputClass>();

	private String payload;
	private String reqType;
	private String url;
	private String selPayload;
	private int responseCode;
	private StringBuffer response;

	private int time=1000;

	private String finalToken;

	public TestFrame() {
		super();
		finalToken="";
	}

	public TestFrame(String _name, String _tfID, String _reqType, String _payload) {
		super();
		this.name = _name;
		this.tfID = _tfID;
		this.reqType = _reqType;
		this.payload= _payload;
		finalToken="";
	}

	public TestFrame(String _name, String _tfID, String _reqType, String _payload, String _url) {
		super();
		this.name = _name;
		this.tfID = _tfID;
		this.reqType = _reqType;
		this.payload= _payload;
		finalToken="";
		this.url=_url; 
	}
	
	public TestFrame(String _name, String _tfID, double _failureProb, double _occurrenceProb) {
		super();
		this.name = _name;
		this.tfID = _tfID;
		this.failureProb = _failureProb;
		this.occurrenceProb = _occurrenceProb;
		finalToken="";
	}


	

	public TestFrame(String _service, String _name, String _tfID, String _reqType, double _failureProb, double _occurrenceProb, String _payload) {
		super();
		this.service = _service;
		this.name = _name;
		this.tfID = _tfID;
		this.reqType = _reqType;
		this.failureProb = _failureProb;
		this.occurrenceProb = _occurrenceProb;
		this.payload = _payload;
		finalToken="";
	}

	// HTTP GET request
	public int sendGet() throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		try {
			con.setRequestMethod("GET");

			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization", "Bearer " + finalToken);

			con.setConnectTimeout(time);
			con.setReadTimeout((time));

			responseCode = con.getResponseCode();
					System.out.println("\nSending 'GET' request to URL : " + url);
					System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			String inputLine;
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			//		System.out.println(response.toString());
		}catch (Exception e) {
			if (con.getResponseCode()==500){
				return 500;
			}else
				e.printStackTrace();
			throw e; 
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return responseCode;
	}

	// HTTP POST request
	public int sendPost() throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		try {
			con.setDoOutput(true);
			con.setDoInput(true);
			//con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Content-Type", "text/plain");
			//con.setRequestProperty("Accept", "application/json");
			
			con.setRequestProperty("Authorization", "Bearer " + finalToken);
			con.setRequestMethod("POST");

			con.setConnectTimeout(time);
			con.setReadTimeout(time);

			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(this.payload);
			wr.flush();

			System.out.println("payload "+this.payload);
			responseCode = con.getResponseCode();
					System.out.println("\nSending 'POST' request to URL : " + url);
					System.out.println("Response Code : " + responseCode);

			if(responseCode < 300){
				BufferedReader in = new BufferedReader(
						new InputStreamReader(con.getInputStream()));
				String inputLine;
				response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				//print result
				System.out.println(response.toString());
			}
		}catch (Exception e) {
			if (con.getResponseCode()==500){
				return 500;
			}else
				e.printStackTrace();
			throw e; 
		}  finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return responseCode;
	}

	// HTTP PUT request
	public int sendPut() throws Exception {
		responseCode = 0;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		try {
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Authorization", "Bearer " + finalToken);
			con.setRequestMethod("PUT");

			con.setConnectTimeout(time);
			con.setReadTimeout(time);

			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(this.selPayload);
			wr.flush();

			responseCode = con.getResponseCode();
			//		System.out.println("\nSending 'PUT' request to URL : " + url);
			//		System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			String inputLine;
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			//		System.out.println(response.toString());
		} catch (Exception e) {
			if (con.getResponseCode()==500){
				return 500;
			}else
				e.printStackTrace();
			throw e; 
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
return responseCode;
	}



	/****************Experimental Version*********************/	
	//	private static Scanner scan = new Scanner(System.in);

	/*public double expExtractAndExecuteTestCase(){
		int failedTC = 0;

		for(int i=0; i<30; i++){
			if(extractAndExecuteTestCase())
				failedTC++;
		}

		System.out.println("There are "+failedTC+" on 30 Test Cases executed");

		//		System.out.println("Press ENTER to continue");
		//		scan.nextLine();

		return failedTC/(double)30;
	}*/
	/**************************************/


	public boolean extractAndExecuteTestCase(){
		//		return this.extractAndExecuteTestCaseExperimenationSimulation();
		////		return this.extractAndExecuteTestCaseDummy();
		////		System.out.println("[DEBUG] "+name+" "+this.payload);
		//	}
		//	public boolean extractAndExecuteTestCasebackup(){
		url = this.url;
		selPayload = this.payload;

		if(url.contains("(")||selPayload.contains("(")){
			this.selectTC();
		}

//		System.out.println(this.tfID+") "+this.reqType+" "+this.url);
		//		System.out.println("Payload: "+this.selPayload);

		try {

			if(this.reqType.equals("GET")||this.reqType.equals("get")){
				this.sendGet();
			} else if(this.reqType.equals("POST")||this.reqType.equals("post")){
				this.sendPost();
			} else{
				this.sendPut();
			}

		} catch(java.net.SocketTimeoutException e){
//			Toolkit.getDefaultToolkit().beep();

			//			System.out.println("Press ENTER to continue");
			//			scan.nextLine();

			return false;
			//return true;
		} catch (Exception e) {
			if(responseCode == 0){
				//				Toolkit.getDefaultToolkit().beep();
				//
				//				System.out.println("[0] Press ENTER to continue");
				//				scan.nextLine();
				return false;
			} else if(responseCode >= 500){
				//				/**********************/
				//				try {
				//					Thread.sleep(1000);
				//				} catch (InterruptedException e1) {
				//					// TODO Auto-generated catch block
				//					e1.printStackTrace();
				//				}
				//				/***********************/
				return true;
			} else{
				return false;
			}
		}

		if(responseCode >= 500){
			/**********************/
			//			try {
			//				Thread.sleep(1000);
			//			} catch (InterruptedException e1) {
			//				// TODO Auto-generated catch block
			//				e1.printStackTrace();
			//			}
			/***********************/
			return true;
		} else{
			return false;
		}

	}



	//	Metodo di prova usato in simulazione
	//	private boolean extractAndExecuteTestCaseDummy(){
	//		double rand = Math.random();
	//		if (rand <= this.failureProb && this.failureProb!=0){
	//			return true;
	//		} else{
	//			return false;
	//		}
	//	}

	public boolean extractAndExecuteTestCaseExperimenationSimulation(){
		if (this.failureProb > 0.5){
			return true;
		} else{
			return false;
		}
	}

	public String  selectTC(){
		String sel="";
		String type;

		for(int i=0; i<this.ic.size(); i++){
			type = this.ic.get(i).type;
			switch(type){
			case "range":  sel = String.valueOf(ThreadLocalRandom.current().nextInt(Integer.parseInt(this.ic.get(i).min), Integer.parseInt(this.ic.get(i).max)));
			break;

			case "lower":  sel = String.valueOf(ThreadLocalRandom.current().nextInt(Integer.parseInt(this.ic.get(i).min)-500, Integer.parseInt(this.ic.get(i).min)-1));
			break;

			case "greater":  sel = String.valueOf(ThreadLocalRandom.current().nextInt(Integer.parseInt(this.ic.get(i).min)+1, Integer.parseInt(this.ic.get(i).min)+500));
			break;

			case "different":  sel = randomNotInt();
			break;

			case "symbol":  sel = this.ic.get(i).min;
			break;

			case "empty":	sel = "";
			break;

			case "s_range": sel = RandomStringUtils.randomAlphanumeric(ThreadLocalRandom.current().nextInt(Integer.parseInt(this.ic.get(i).min), Integer.parseInt(this.ic.get(i).max)));
			break;

			case "s_greater" :	sel = RandomStringUtils.randomAlphanumeric(Integer.parseInt(this.ic.get(i).min));
			break;

			case "n_range": sel = RandomStringUtils.randomNumeric(ThreadLocalRandom.current().nextInt(Integer.parseInt(this.ic.get(i).min), Integer.parseInt(this.ic.get(i).max)));
			break;

			case "n_greater" :	sel = RandomStringUtils.randomNumeric(Integer.parseInt(this.ic.get(i).min));
			break;

			case "value": sel = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
			break;

			case "lang": 
				String[] languages = {"ita", "eng", "fra"}; 
				sel = languages[ThreadLocalRandom.current().nextInt(0, languages.length-1)];
				break;

			default: sel = "error";
			break;
			}

/*			if(url.contains(this.ic.get(i).name)){
				url = url.replace(this.ic.get(i).name, sel);
			} else if(selPayload.contains(this.ic.get(i).name)){
				selPayload = selPayload.replace(this.ic.get(i).name, sel);
 	 		}*/

		}
		return sel;

	}

	private String randomNotInt(){
		String[] val= {"1.5","true","a","null"};

		return val[ThreadLocalRandom.current().nextInt(0, 4)];
	}


	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTfID() {
		return tfID;
	}

	public void setTfID(String tfID) {
		this.tfID = tfID;
	}

	public double getFailureProb() {
		return failureProb;
	}

	public void setFailureProb(double failureProb) {
		this.failureProb = failureProb;
	}

	public double getOccurrenceProb() {
		return occurrenceProb;
	}

	public void setOccurrenceProb(double occurrenceProb) {
		this.occurrenceProb = occurrenceProb;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getUrl(){
		return this.url;
	}

	public String getSelPayload(){
		return this.selPayload;
	}

	public String getFinalToken() {
		return finalToken;
	}

	public void setFinalToken(String finalToken) {
		this.finalToken = finalToken;
	}
	public void setUrl(String url) {
		this.url = url;
	}



}
