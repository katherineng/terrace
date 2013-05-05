package terrace.util;

import java.util.Objects;

public class Posn {
	public final int x, y;
	
	public Posn(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {return x;}
	
	public int getY() {return y;}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Posn))
			return false;
		Posn other = (Posn) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
