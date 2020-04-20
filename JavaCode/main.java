package dataStructure;

import java.util.ArrayList;


/***
 * 
 * 
 * This is example code to use the class TestFrame in order to run an operational testing session.
 * To apply to a subject, you need to: 
 * - define a profile; in the example there are 4 partitions, with a uniform profile generated (0.25 per partition). The profile can be written to a file or generated randomly (each value in [0,1], and the sum normalized to 1)
 * - change the endpoint of the service API to invoke (in the example there is the localhost of subject under test, the NLP-Prose APIs
 * - Define input classes. The TestFrame class defines some example classes to generate Strings with different features. This can be customised depending on the need.
 * - Set a baseDir, in which to place the possible profile.txt and to write the results.  
 */
public class main {

	//CHANGE THIS
	public static String baseDir = "base/directory";  

	public static void main(String[] args) {

		int numberOfTests = 10;
		
		//Define the Operational Profile. In the example, 4 partitions
		int numberOfPartitions = 4; 
		double[] profile = new double[numberOfPartitions];

		ProfileGenerator pg = new ProfileGenerator(numberOfPartitions); 
		try {
			//profile = pg.getUniformProfile();
			profile = pg.getRandomProfile();
			//profile = pg.getProfileFromFile(baseDir+"profile.txt");  //FORMAT: "NamePartition Vale", one row per partition

		} catch (Exception e1) {e1.printStackTrace();} 
		
		/** 
		 *Create the test frames (which are the partitions) 
		 */
		//Create an input class for each input of the method under test 
		//(a test frame is a set of input classes)
		//Hence, a list of input classes for each partition
		
		ArrayList<ArrayList<InputClass>> ic = populateInputClasses(); 
		
		// Specify the URLs of the end point of the API method and the request type  
		ArrayList<String> URLs = new ArrayList<String>();
		ArrayList<String> ReqTypes = new ArrayList<String>();
		
		//Example
		URLs.add("http://localhost:8060/api/sentences"); //Example URL for partition 1
		ReqTypes.add("POST"); 
		URLs.add("http://localhost:8060/api/status"); //Example URL for partition 2
		ReqTypes.add("GET");
		URLs.add("http://localhost:8060/api/sentences"); //Example URL for partition 3
		ReqTypes.add("POST");
		URLs.add("http://localhost:8060/api/status"); //Example URL for partition 4
		ReqTypes.add("GET");
				
		
		TestGenerator tg = new TestGenerator(); 
		ArrayList<TestFrame> partitions = tg.createPartitions(URLs, ReqTypes, ic, profile); //create partitions and assign the profile: // profile[0] is ic.get(0),profile[1] is ic.get(1), ... the order is preseved  
		//Run the tests; start the service before invoking this.
		TestRunner tr = new TestRunner(); // It includes the response code Parsing too.
		String outputPathString = baseDir+"/output.txt";				
		tr.runTests(numberOfTests, partitions, outputPathString);
		
		// The above code is for testing the service under a given profile: 
		// In order to emulate demands under an operational profile that varies by x% from the testing one
		// invoke the ProfileGeneartor.getTrueProfile method, passing the profile to be varied as input and the 'x' factor, and repeat the above process (test geneartion and execution)
		// E.g.: 	double[] profile2 = new double[numberOfPartitions];
		//			profile2 =  pg.getTrueProfile(profile, 0.3); //Variation factor: 0.3
	}

	private static ArrayList<ArrayList<InputClass>> populateInputClasses() {
		ArrayList<ArrayList<InputClass>> ic = new ArrayList<ArrayList<InputClass>>();
		//Example: creation of 8 classes for 4 partitions; 
		ArrayList<InputClass> icList1 = new ArrayList<InputClass>();
		// a class is characterised by a nema, a type (e.g., "range"), a min and a max that can be used to generate th test case
		icList1.add(new InputClass("name", "s_range", "3", "20")); // these example classes are defined in TestFrame, but have to be redefined depending on the subject under test
		icList1.add(new InputClass("name", "range", "3", "20"));
		// others...
		
		ic.add(icList1);
		
		
		ArrayList<InputClass> icList2 = new ArrayList<InputClass>(); 
		icList2.add(new InputClass("name", "s_range", "3", "20")); // these example classes are defined in TestFrame, but have to be redefined depending on the subject under test
		icList2.add(new InputClass("name", "range", "3", "20"));
		// others...
		
		ic.add(icList2);
		
	
		ArrayList<InputClass> icList3 = new ArrayList<InputClass>(); 
		icList3.add(new InputClass("name", "s_range", "3", "20")); // these example classes are defined in TestFrame, but have to be redefined depending on the subject under test
		icList3.add(new InputClass("name", "range", "3", "20"));
		// others...
	
		ic.add(icList3);
		
		
		ArrayList<InputClass> icList4 = new ArrayList<InputClass>(); 
		icList4.add(new InputClass("name", "s_range", "3", "20")); // these example classes are defined in TestFrame, but have to be redefined depending on the subject under test
		icList4.add(new InputClass("name", "range", "3", "20"));
		// others...
	
		ic.add(icList4);
	
		return ic;
	}
 	
	
	 
}
