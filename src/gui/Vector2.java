package gui;

public class Vector2 {

	double x, y;
	
	public Vector2(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2 minus(Vector2 other){
		return new Vector2(x - other.x, y - other.y);
	}
}
