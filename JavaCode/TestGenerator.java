package dataStructure;

import java.util.ArrayList;

public class TestGenerator {

	public ArrayList<TestFrame> createPartitions(ArrayList<String> URLs, ArrayList<String> reqTypes,
			ArrayList<ArrayList<InputClass>> _ic, double[] profile) {

		ArrayList<TestFrame> partitions = new ArrayList<TestFrame>(); 
		//create partitions
		for (int i=0; i< URLs.size(); i++) {
			TestFrame tf = new TestFrame();
			tf.ic = _ic.get(i); 
			tf.setUrl(URLs.get(i));
			tf.setReqType(reqTypes.get(i));
			tf.setPayload(tf.selectTC());
			tf.setFailureProb(profile[i]);
			partitions.add(tf);
			
		}
	
		return partitions;
	}
}
