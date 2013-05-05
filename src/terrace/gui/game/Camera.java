package terrace.gui.game;

import terrace.util.Vector2d;
import terrace.util.Vector3d;

public class Camera {
	double zoom;
	double fovx;
	double theta;
	double phi;
	Vector3d center;
	Vector3d up;
	
	Camera(Vector3d center, Vector3d up, double theta, double phi, double fovx, double zoom){
		this.zoom = zoom;
		this.fovx = fovx;
		this.theta = theta;
		this.phi = phi;
		this.center = center;
		this.up = up;
	}
	
	/**
	 * Method for rotating aroud center point. 
	 * @param delta - the amount by which the mouse is moving
	 */
	void mouseMove(Vector2d delta) {
	    // Rotate the eye vector around the origin
	    theta += delta.x * 0.01f;
	    phi -= delta.y * 0.01;

	    // Keep theta in [0, 2pi] and phi in [-pi/2, pi/2]
	    theta -= Math.floor(theta / (2*Math.PI)) * (2*Math.PI);
	    phi = Math.max(-Math.PI / 2, Math.min(-Math.PI / 10, phi));

	}

	/**
	 * Method for zooming
	 * @param delta - the amount by which the user wants to zoom
	 */
	void mouseWheel(float delta) {
	    zoom *= Math.pow(0.9f, delta);
	    zoom = Math.max(0.5, Math.min(2, zoom));
	}
	
	/**
	 * utility function 
	 */
	Vector3d fromAngles() {
	    return new Vector3d(Math.cos(theta) * Math.cos(phi), Math.sin(phi), Math.sin(theta) * Math.cos(phi));
	}
}
