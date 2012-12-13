package by.sunnycore.recognition.image.util;

import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

public class JAIUtil {
	public static PlanarImage createRGBHistogramImage(BufferedImage source){
		ParameterBlock pb = new ParameterBlock();
        int[] bins = { 256 };
        double[] low = { 0.0D };
        double[] high = { 256.0D };
        pb.addSource(source);
        pb.add(null);
        pb.add(1);
        pb.add(1);
        pb.add(bins);
        pb.add(low);
        pb.add(high);
        PlanarImage op = JAI.create("histogram", pb, null);
        return op;
	}
}
