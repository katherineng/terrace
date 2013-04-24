package gui;


public class Camera {
	double zoom;
	double fovy;
	double theta;
	double phi;
	Vector3 center;
	Vector3 up;
	
	public Camera(Vector3 center, Vector3 up, double theta, double phi, double fovy, double zoom){
		this.zoom = zoom;
		this.fovy = fovy;
		this.theta = theta;
		this.phi = phi;
		this.center = center;
		this.up = up;
	}
	
	void mouseMove(Vector2 delta) {
	    // Rotate the eye vector around the origin
	    theta += delta.x * 0.01f;
	    phi -= delta.y * 0.01;

	    // Keep theta in [0, 2pi] and phi in [-pi/2, pi/2]
	    theta -= Math.floor(theta / (2*Math.PI)) * (2*Math.PI);
	    phi = Math.max(0.01 - Math.PI / 2, Math.min(Math.PI / 2 - 0.01, phi));

	}

	void mouseWheel(float delta) {
	    zoom *= Math.pow(0.999f, delta);
	}
	
	Vector3 fromAngles() {
	    return new Vector3(Math.cos(theta) * Math.cos(phi), Math.sin(phi), Math.sin(theta) * Math.cos(phi));
	}
}
