package by.sunnycore.recognition.image.cluster;

import java.util.List;

import by.sunnycore.recognition.domain.ObjectCluster;

/**
 * Defines interface for the method that should teached before it's usage
 * 
 * @author Valiantsin Shukaila
 * 
 */
public interface TeachableMethod {
	void teach(List<ObjectCluster[]> teachData);
}
