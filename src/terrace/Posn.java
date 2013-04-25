package terrace;

public class Posn {
	public final int x;
	public final int y;

	public Posn(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		return (x * 31) ^ y;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this.getClass() == o.getClass()) {
			Posn p = (Posn) o;
			return this.x == p.x && this.y == p.y;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
