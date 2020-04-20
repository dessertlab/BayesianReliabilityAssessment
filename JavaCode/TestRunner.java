package dataStructure;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TestRunner {

	public void runTests(int numberOfTests, ArrayList<TestFrame> partitions, String outputPathString) {
		// Operational testing: select a partition with a probability proportional to the profile
				double[] cumulativePVector = new double[partitions.size()];
				cumulativePVector[0] = partitions.get(0).getFailureProb(); 
				for (int i=1; i<partitions.size();i++)
						cumulativePVector[i] = cumulativePVector[i-1] + partitions.get(i).getFailureProb();
					
				try {Files.deleteIfExists(Paths.get(outputPathString));} catch (IOException e2) {e2.printStackTrace();}
				String outFile = outputPathString;
				FileWriter output = null;
				try {output = new FileWriter(outFile,true); } catch (IOException e1) {e1.printStackTrace();}
				PrintWriter pw = new PrintWriter(new BufferedWriter(output));
				
				int responseCode = 0;
				int indexCurrentTest=0;
				while(indexCurrentTest < numberOfTests){
					double rand = Math.random(); ///*cumulativePVector[numberOfPartitions-1];
					int indexToExecute=-1;		
					for(int index =0; index<partitions.size();index++){
						if (rand <= cumulativePVector[index]) {
							indexToExecute = index;//selected partition
							break;
							}
						}
					System.out.println("Partition "+indexToExecute);
					try {
						if (partitions.get(indexToExecute).getReqType().equals("POST")) {
							responseCode = partitions.get(indexToExecute).sendPost();
							pw.println("\nSending 'POST' request to URL : " + partitions.get(indexToExecute).getUrl());
							pw.println("index Current Test: " + indexCurrentTest);
							pw.println("Partition: " + indexToExecute);
							pw.println("Response Code : " + responseCode); 	// The printed response code allows distinguishing correct from failed demands
			
						}
						if (partitions.get(indexToExecute).getReqType().equals("GET")) {
							responseCode= partitions.get(indexToExecute).sendGet();
							pw.println("\nSending 'GET' request to URL : " + partitions.get(indexToExecute).getUrl());
							pw.println("Payload "+partitions.get(indexToExecute).getPayload());
							pw.println("index Current Test: " + indexCurrentTest);
							pw.println("Partition: " + indexToExecute);
							pw.println("Response Code : " + responseCode); 	// The printed response code allows distinguishing correct from failed demands
						}
					}catch (Exception e) {e.printStackTrace();}
					indexCurrentTest++;
			}
				pw.close();
	}
}
