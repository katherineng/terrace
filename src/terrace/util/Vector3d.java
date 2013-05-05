package terrace.util;

import java.util.Objects;

public class Vector3d {
	public double x, y, z;
	
	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		if (!(o instanceof Vector3d)) return false;
		
		Vector3d v = (Vector3d) o;
		return x == v.x && y == v.y && z == v.z;
	}
}
