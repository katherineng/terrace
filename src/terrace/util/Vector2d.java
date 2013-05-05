package terrace.util;

import java.util.Objects;

public class Vector2d {
	public double x, y;
	
	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		if (!(o instanceof Vector2d)) return false;
		
		Vector2d v = (Vector2d) o;
		return x == v.x && y == v.y;
	}
}
