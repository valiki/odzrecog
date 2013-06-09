package by.sunnycore.recognition.image.transformer.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RGBToHSVDataTransformerTest {

	@Test
	public void test(){
		short[][] rgb = new short[][]{{67},{48},{55}};
		RGBToHSVDataTransformer transformer = new RGBToHSVDataTransformer();
		short[][] hsv = transformer.transform(rgb);
		assertEquals(337, hsv[0][0]);
		assertEquals(28, hsv[1][0]);
		assertEquals(26, hsv[2][0]);
		rgb = new short[][]{{100},{150},{145}};
		hsv = transformer.transform(rgb);
		assertEquals(174, hsv[0][0]);
		assertEquals(33, hsv[1][0]);
		assertEquals(58, hsv[2][0]);
	}
}
