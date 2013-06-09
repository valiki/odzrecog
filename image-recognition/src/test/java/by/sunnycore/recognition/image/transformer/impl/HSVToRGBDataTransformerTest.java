package by.sunnycore.recognition.image.transformer.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class HSVToRGBDataTransformerTest {

	@Test
	public void test(){
		short[][] hsv = new short[][]{{337},{28},{26}};
		HSVToRGBDataTransformer transformer = new HSVToRGBDataTransformer();
		short[][] rgb = transformer.transform(hsv);
		assertEquals(66, rgb[0][0]);
		assertEquals(45, rgb[1][0]);
		assertEquals(53, rgb[2][0]);
	}

}
