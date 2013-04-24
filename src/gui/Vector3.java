package gui;

public class Vector3 {
	
	double x, y, z;
	
	public Vector3(double x, double y, double z){
		this.x=x;
		this.y=y;
		this.z=z;		
	}
	public Vector3(){}
	

	
	Vector3 mult(double num){
		return new Vector3(num*x, num*y, num*z);
	}
	
	Vector3 minus(Vector3 other){
		return new Vector3(x - other.x, y - other.y, z-other.z);
	}

}
