package by.sunnycore.recognition.image.cluster.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import by.sunnycore.recognition.domain.ObjectCluster;
import by.sunnycore.recognition.test.TestUtil;

public class MaximumLikelyHoodMethodTest {
	
	private Logger logger = Logger.getLogger(MaximumLikelyHoodMethodTest.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MaximumLikelyHoodMethodTest t = new MaximumLikelyHoodMethodTest();
		t.executeMaximumLikelyHoodMethod();
	}
	
	public void executeMaximumLikelyHoodMethod(){
		logger.debug("Creating Maximum LikelyHood Method Clusterer.");
		MaximumLikelyHoodMethod m = new MaximumLikelyHoodMethod();
		logger.debug("Loading Teach data from disk.");
		ObjectCluster[] clusters = TestUtil.loadCLustersFromFile(6);
		List<ObjectCluster[]> teachData = new ArrayList<>();
		teachData.add(clusters);
		logger.debug("Start Teaching Clusterer");
		long time = System.currentTimeMillis();
		m.teach(teachData);
		logger.info("Teaching took "+(System.currentTimeMillis()-time)+"ms");
	}

}
