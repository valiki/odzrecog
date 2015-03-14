package by.sunnycore.recognition.image.util;

import java.util.Arrays;

public class Point {
	
	private int[] data;

	public Point(int[] data) {
		this.data = data;
	}
	
	public int[] getData() {
		return data;
	}

	public void setData(int[] data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		return true;
	}
	
	
	
}
