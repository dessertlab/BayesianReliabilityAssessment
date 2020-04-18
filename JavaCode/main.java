package dataStructure;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/***
 * 
 * 
 * This is example code to use the class TestFrame in order to run an operational testing session.
 * To apply to a subject, you need to: 
 * - define a profile; in the example there are 4 partitions, with a uniform profile (0.25 per partition)
 * - paste the endpoint of the service API to invoke (in the example there is the localhost of subject under test, the NLP-Prose APIs
 * - Define input classes. The TestFrame class defines some example classes to generate Strings with different features. This can be customised depending on the need.  
 */
public class main {

	public static void main(String[] args) {

		
		
		int numberOfTests = 10;
		//Define the Operational Prodile
		int numberOfPartitions = 4; 
		double[] profile = new double[numberOfPartitions];
		//by default, we set it as uniform
		profile[0] = 1.0/numberOfPartitions;
		profile[1] = 1.0/numberOfPartitions;
		profile[2] = 1.0/numberOfPartitions;
		profile[3] = 1.0/numberOfPartitions;
		
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
				if (partitions.get(indexToExecute).getReqType().equals("POST"))
					partitions.get(indexToExecute).sendPost();
				if (partitions.get(indexToExecute).getReqType().equals("GET"))
					partitions.get(indexToExecute).sendGet();
				
			}catch (Exception e) {e.printStackTrace();}
		indexCurrentTest++;
		}
	}
	// The printed response code allows distinguishing correct from failed demands 
	
}
