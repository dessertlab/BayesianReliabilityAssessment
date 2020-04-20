package dataStructure;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class ProfileGenerator {
 
	private double[] generatedProfile; 
	private int numberOfPartitions;
	
	ProfileGenerator(int _numberOfPartitions){
		 numberOfPartitions = _numberOfPartitions;
		this.generatedProfile = new double[this.numberOfPartitions] ;
	}
	
	public  double[] getTrueProfile(double[] profileToBeVaried, double  profileVariationFactor) throws Exception {
		 
		double[] newProfile = new double[this.numberOfPartitions];
		if (profileToBeVaried.length ==0 || profileToBeVaried.length!=this.numberOfPartitions)
		 	  throw new Exception("FATAL ERROR: The length of teh profile to be varied is illegal. Terimating...");
	   
		 double tolerance = 0.0001;
	    //RANDOM GENERATION OF VARIED PROFILE
	   double sum=0;
	   for (int i=0; i< this.numberOfPartitions; i++){
		   newProfile[i] = profileToBeVaried[i] + (-1 + 2*Math.random())*profileVariationFactor;
		   if (newProfile[i]<0) newProfile[i] = 0; 
		   sum += newProfile[i];
	   }
	   //Normalize
	   for (int i=0; i< this.numberOfPartitions; i++) newProfile[i] = newProfile[i]/sum;
	   sum=0;
	   for (int i=0; i< this.numberOfPartitions; i++) sum+= newProfile[i];
	   
	 	
		System.out.println("...CHECK WELL-FORMEDNESS...");
		if ((sum> 1.00 + tolerance) || (sum < 1 - tolerance))
			throw new Exception("Sum of occurrence probabilities of test cases is not 1");
		System.out.println(" ... DONE\n");	 
	   return newProfile;
		}
	 
	 public double[] getUniformProfile() throws Exception {
		 for (int i=0; i< numberOfPartitions; i++) this.generatedProfile[i] = 1/(double)numberOfPartitions;
	 	 return this.generatedProfile ;
	 }
	 
	 public double[] getRandomProfile() throws Exception {
		 double sum=0;
	 	   for (int i=0; i< numberOfPartitions; i++) {
	 		  this.generatedProfile [i] = Math.random();
	 		   sum += this.generatedProfile [i]; 
	 	   }
	 	   for (int i=0; i< numberOfPartitions; i++) this.generatedProfile [i] = this.generatedProfile [i]/sum;
	 	return this.generatedProfile ;
	 }
	 
	 public  double[] getProfileFromFile(String profilePathString) throws Exception {
		 double tolerance= 0.0001;
		 Path profilePath = Paths.get(profilePathString); 
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
			this.generatedProfile[index] = Double.parseDouble(st.nextToken());// 2
			sum+= this.generatedProfile[index];
			index++;
		}	
		System.out.println("...CHECK WELL-FORMEDNESS...");
		if ((sum> 1.00 + tolerance) || (sum < 1 - tolerance))
			throw new Exception("Sum of occurrence probabilities of test cases is not 1");
		System.out.println(" ... DONE\n");
		return this.generatedProfile;
	 	}
	 
	 		
}

