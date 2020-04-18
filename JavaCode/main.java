package dataStructure;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;


/***
 * 
 * 
 * This is example code to use the class TestFrame in order to run an operational testing session.
 * To apply to a subject, you need to: 
 * - define a profile; in the example there are 4 partitions, with a uniform profile (0.25 per partition). The profile can be written to a file or generaed randomly
 * - paste the endpoint of the service API to invoke (in the example there is the localhost of subject under test, the NLP-Prose APIs
 * - Define input classes. The TestFrame class defines some example classes to generate Strings with different features. This can be customised depending on the need.
 * - Set a baseDir, in which to place the possible profile.txt and to write the results.  
 */
public class main {

	public static String baseDir = "/base/directory";

	public static void main(String[] args) {

		
		
		int numberOfTests = 10;
		//Define the Operational Profile. In the example, 4 partitions
		int numberOfPartitions = 4; 
		double[] profile = new double[numberOfPartitions];
		//by default, we set it as uniform.
		try {
			profile = main.getUniformProfile(numberOfPartitions);
		} catch (Exception e1) {e1.printStackTrace();} 
		
		//To get the prfile from a file, store a file in baseDir/profile.txt and invoke getProfileFromFile
		
		/** 
		 *Create the test frames (which are the partitions) 
		 */
		//Create an input class for each input of the method under test 
		//(a test frame is a set of input classes)
		//Hence, a list of input classes for each partition
		ArrayList<InputClass> icList1 = new ArrayList<InputClass>();
		
		// a class is characterised by a nema, a type (e.g., "range"), a min and a max that can be used to generate th test case
		icList1.add(new InputClass("name", "s_range", "3", "20")); // these example classes are defined in TestFrame, but have to be redefined depending on the subject under test
		icList1.add(new InputClass("name", "range", "3", "20"));
		// others...
		
		ArrayList<InputClass> icList2 = new ArrayList<InputClass>(); 
		icList2.add(new InputClass("name", "s_range", "3", "20")); // these example classes are defined in TestFrame, but have to be redefined depending on the subject under test
		icList2.add(new InputClass("name", "range", "3", "20"));
		// others...
		
	
		ArrayList<InputClass> icList3 = new ArrayList<InputClass>(); 
		icList3.add(new InputClass("name", "s_range", "3", "20")); // these example classes are defined in TestFrame, but have to be redefined depending on the subject under test
		icList3.add(new InputClass("name", "range", "3", "20"));
		// others...
	
		ArrayList<InputClass> icList4 = new ArrayList<InputClass>(); 
		icList4.add(new InputClass("name", "s_range", "3", "20")); // these example classes are defined in TestFrame, but have to be redefined depending on the subject under test
		icList4.add(new InputClass("name", "range", "3", "20"));
		// others...
	
		
		
		//create partition 1
		TestFrame tf1 = new TestFrame();
		//attach the list of input classes and usage probability (i.e., from the desired operational profile)
		tf1.ic = icList1; 
		tf1.setFailureProb(profile[0]);
		tf1.setUrl("http://localhost:8060/api/sentences"); //Example URL
		tf1.setPayload(tf1.selectTC());
		tf1.setReqType("POST");
		// it is ready to run, if this partition is selected
		
		//create partition 2
		TestFrame tf2 = new TestFrame();
		//attach the list of input classes and usage probability (i.e., from the desired operational profile)
		tf2.ic = icList2; 
		tf2.setFailureProb(profile[0]);
		tf2.setUrl("http://localhost:8060/api/status");
		tf2.setReqType("GET");
		tf2.setPayload(tf2.selectTC());
		// it is ready to run, if this partition is selected
		
		//create partition 3
		TestFrame tf3 = new TestFrame();
		//attach the list of input classes and usage probability (i.e., from the desired operational profile)
		tf3.ic = icList3; 
		tf3.setFailureProb(profile[0]);
		tf3.setUrl("http://localhost:8060/api/sentences");
		tf3.setReqType("POST");
		tf3.setPayload(tf3.selectTC());
		// it is ready to run, if this partition is selected
		
		//create partition 4
		TestFrame tf4 = new TestFrame();
		//attach the list of input classes and usage probability (i.e., from the desired operational profile)
		tf4.ic = icList4; 
		tf4.setFailureProb(profile[0]);
		tf4.setUrl("http://localhost:8060/api/status");
		tf4.setReqType("GET");
		tf4.setPayload(tf4.selectTC());
		// it is ready to run, if this partition is selected
		
		ArrayList<TestFrame> partitions = new ArrayList<TestFrame>();
		partitions.add(tf1);
		partitions.add(tf2);
		partitions.add(tf3);
		partitions.add(tf4);
		
		// Operational testing: select a partition with a probability proportional to the profile
		double[] cumulativePVector = new double[numberOfPartitions];
		cumulativePVector[0] = profile[0];
		for (int i=1; i<numberOfPartitions;i++)
				cumulativePVector[i] = cumulativePVector[i-1] + profile[i];
			
		int indexCurrentTest=0;
		while(indexCurrentTest < numberOfTests){
			double rand = Math.random(); ///*cumulativePVector[numberOfPartitions-1];
			int indexToExecute=-1;		
			for(int index =0; index<numberOfPartitions;index++){
				if (rand <= cumulativePVector[index]) {
					indexToExecute = index;//selected partition
					break;
					}
				}
			try {
				int responseCode;
				String outFile = baseDir+"/resutls.txt";
				FileWriter output  = new FileWriter(outFile, true);
				PrintWriter pw = new PrintWriter(new BufferedWriter(output));
				if (partitions.get(indexToExecute).getReqType().equals("POST")) {
					responseCode = partitions.get(indexToExecute).sendPost();
					pw.println("\nSending 'GET' request to URL : " + partitions.get(indexToExecute).getUrl());
					pw.println("Partition: " + indexToExecute);
					pw.println("Response Code : " + responseCode); 	// The printed response code allows distinguishing correct from failed demands
	
				}
				if (partitions.get(indexToExecute).getReqType().equals("GET")) {
					responseCode= partitions.get(indexToExecute).sendGet();
					pw.println("\nSending 'PUT' request to URL : " + partitions.get(indexToExecute).getUrl());
					pw.println("Payload "+partitions.get(indexToExecute).getPayload());
					pw.println("Partition: " + indexToExecute);
					pw.println("Response Code : " + responseCode); 	// The printed response code allows distinguishing correct from failed demands
				}
			}catch (Exception e) {e.printStackTrace();}
		indexCurrentTest++;
		}
		// NOTE: To emulate demands under an operational profile that varies by x% from the estiamted one
		// invoke the getTrueProfile method, passing the profile to be varied as input and the 'x' factor
	}
 	
	
	 private static double[] getTrueProfile(double[] profileToBeVaried , double profileVariationFactor) throws Exception {
		   
		   double[] newProf  = new double[profileToBeVaried .length];
			 double tolerance = 0.0001;
		    //RANDOM GENERATION OF VARIED PROFILE
		   double sum=0;
		   for (int i=0; i< profileToBeVaried .length; i++){
			   newProf[i] = profileToBeVaried[i] + (-1 + 2*Math.random())*profileVariationFactor;
			   if (newProf[i]<0) newProf[i] = 0; 
			   sum += newProf[i];
		   }
		   //Normalize
		   for (int i=0; i< profileToBeVaried .length; i++) newProf[i] = newProf[i]/sum;
		   sum=0;
		   for (int i=0; i< profileToBeVaried .length; i++) sum+= newProf[i];
		   
		 	if (profileToBeVaried.length ==0)
			 	  throw new Exception("FATAL ERROR: The number of operations to test for this service is 0. Terimating...");

			System.out.println("...CHECK WELL-FORMEDNESS...");
			if ((sum> 1.00 + tolerance) || (sum < 1 - tolerance))
				throw new Exception("Sum of occurrence probabilities of test cases is not 1");
			System.out.println(" ... DONE\n");	 
		   return newProf;
		}
	 
	 private static double[] getUniformProfile(int numberOfPartitions) throws Exception {
		 double[] profile  = new double[numberOfPartitions];
	 	 //UNIFORM PROFILE 
	 	 for (int i=0; i< numberOfPartitions; i++) profile[i] = 1/(double)numberOfPartitions;
	 	 //RANDOM PROFILE
	/* 	   double sum=0;
	 	   for (int i=0; i< opNumber; i++) {
	 		   profile[i] = Math.random();
	 		   sum += profile[i]; 
	 	   }
	 	   for (int i=0; i< opNumber; i++) profile[i] = profile[i]/sum;
	 */ 
	 	if (numberOfPartitions ==0)
	 	  throw new Exception("FATAL ERROR: The number of partitions for this service is 0. Terimating...");
	 
	 	return profile;
	 }
	 
	 private static double[] getProfileFromFile(int numberOfPartitions) throws Exception {
		 double tolerance= 0.0001;
		 double[] profile  = new double[numberOfPartitions];
		 Path profilePath = Paths.get(baseDir+"/profile.txt");
	 	ArrayList<String> profileInString = null;
	 	try {
	 		profileInString = (ArrayList<String>) Files.readAllLines(profilePath, StandardCharsets.UTF_8);
		} catch (IOException e) {e.printStackTrace();}
	 	//CONSISTENCY CHECK
	 	if(profileInString.size()!=numberOfPartitions)
	 		  throw new Exception("FATAL ERROR: The number of partitions in the profile.txt file is not equal to the numberof partitions. Terimating..."); 	
	 	Iterator<String> it = profileInString.iterator();
	 	StringTokenizer st; String line;
		ArrayList<String> partitionName = new ArrayList<String>();
		int index=0;
		System.out.println("Reading initial profile ...");
		//System.out.println("Note on the intput file format: DELIMITER FOR PARTITION NAME IN THE 'PARTITION MAPPING FILE' IS ']' - THE PARTITION NAME MUST END BY ']' (this character cannot be in the partition name)");
		double sum=0.0; 
		while (it.hasNext()){
			line = new String(it.next());		
			st = new StringTokenizer(line);
			partitionName.add(st.nextToken()); // 1: UNUSED IN THIS VERSION OF THE CODE 
			profile[index] = Double.parseDouble(st.nextToken());// 2
			sum+= profile[index];
			index++;
		}	
		System.out.println("...CHECK WELL-FORMEDNESS...");
		if ((sum> 1.00 + tolerance) || (sum < 1 - tolerance))
			throw new Exception("Sum of occurrence probabilities of test cases is not 1");
		System.out.println(" ... DONE\n");
		return profile;
	 	}
	 
}
