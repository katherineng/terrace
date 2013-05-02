package terrace.util;

import java.util.Objects;

import javax.vecmath.*;

@SuppressWarnings("serial")
public class Posn extends Point3i implements Cloneable{

	public Posn(int x, int y) {
		super(x, y, 0);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
	
	public Posn clone(){
		Posn toRet = (Posn) super.clone();
		return toRet;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
