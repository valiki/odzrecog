package by.sunnycore.recognition.image;

import java.awt.image.BufferedImage;

public interface IImageSplitter<T> {
	T[] split(BufferedImage source);
}
