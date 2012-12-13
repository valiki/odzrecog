package by.sunnycore.recognition.image;

import java.awt.image.BufferedImage;

import by.sunnycore.recognition.domain.ObjectCluster;

/**
 * the clusterer of the image pixels
 * 
 * @author val
 * 
 */
public interface IImageClusterer {
	/**
	 * clusterizes the source image pixels
	 * 
	 * @param source
	 * @return
	 */
	ObjectCluster[] cluster(BufferedImage source);
}
