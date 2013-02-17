package by.sunnycore.recognition.image.splitter;

import java.awt.image.BufferedImage;
/**
 * splits image into some parts defined by the implementation
 * 
 * @author Valiantsin Shukaila
 *
 * @param <T>
 */
public interface ImageSplitter<T> {
	T[] split(BufferedImage source);
}
