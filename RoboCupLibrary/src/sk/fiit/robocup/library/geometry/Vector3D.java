package sk.fiit.robocup.library.geometry;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.io.Serializable;

/**
 *  Vector3D.java
 *
 *@Title        Jim
 *@author       $Author: marosurbanec $
 */
public class Vector3D implements Serializable {
	private static final long serialVersionUID = 7333255435947209920L;

	public static final Vector3D ZERO_VECTOR = Vector3D.cartesian(0, 0, 0);
	
	public static final Vector3D X_AXIS = Vector3D.cartesian(1, 0, 0);
    public static final Vector3D Y_AXIS = Vector3D.cartesian(0, 1, 0);
    public static final Vector3D Z_AXIS = Vector3D.cartesian(0, 0, 1);
    
	private double x;
	private double y;
	private double z;
	
	private double r;
	private double phi;
	private double theta;
	
	private Vector3D(){}
	
	//added by team17
	//toto by bolo zbytocne, keby sa tato trieda zmeni na "riadne" immutable triedu
	public Vector3D clone() {
		Vector3D newVector = new Vector3D();
		newVector.x = this.x;
		newVector.y = this.y;
		newVector.z = this.z;
		newVector.r = this.r;
		newVector.phi = this.phi;
		newVector.theta = this.theta;
		return newVector;
	}
	
	public static Vector3D fromVector3(Vector3 v3) {
		return cartesian(v3.getX(), v3.getY(), v3.getZ());
	}
	
	public static Vector3D cartesian(Vector2 xy, double z){
		return cartesian(xy.getX(),xy.getY(),z);
	}
	
	public static Vector3D cartesian(double x, double y, double z){
		Vector3D vector = new Vector3D();
		vector.x = x;
		vector.y = y;
		vector.z = z;
		vector.calculateSpherical();
		return vector;
	}

	private void calculateSpherical(){
		r = sqrt(x*x + y*y + z*z);
		theta = asin(z / r);
		if (r == 0.0) theta = 0.0;
		phi = atan2(y, x);
		phi -= PI / 2.0d;
		
		phi = Angles.normalize(phi);
		theta = Angles.normalize(theta);
	}
	
	public static Vector3D spherical(double r, double phi, double theta){
		Vector3D vector = new Vector3D();
		vector.r = r;
		vector.phi = phi;
		vector.theta = theta;
		vector.calculateCartesian();
		return vector;
	}
	
	private void calculateCartesian(){
		x = cos(theta)*sin(phi) * -r;
		x = (int)(x*1000.0) / 1000.0;
		y = cos(theta)*cos(phi) * r;
		y = (int)(y*1000.0) / 1000.0;
		z = sin(theta) * r;
		z = (int)(z*1000.0) / 1000.0;
	}
	

	public double getX(){
		return x;
	}

	public Vector3D setX(double x){
		return cartesian(x, y, z);
	}

	public double getY(){
		return y;
	}

	public Vector3D setY(double y){
		return cartesian(x, y, z);
	}

	public double getZ(){
		return z;
	}

	public Vector3D setZ(double z){
		return cartesian(x, y, z);
	}
	
	public Vector3D addX(double x){
		return cartesian(this.x + x, y, z);
	}

	public Vector3D addY(double y){
		return cartesian(x, this.y + y, z);
	}

	public Vector3D addZ(double z){
		return cartesian(x, y, this.z + z);
	}

	public double getR(){
		return r;
	}

	public Vector3D setR(double r){
		return spherical(r, phi, theta);
	}

	public double getPhi(){
		return phi;
	}

	public Vector3D setPhi(double phi){
		return spherical(r, phi, theta);
	}

	public double getTheta(){
		return theta;
	}

	public Vector3D setTheta(double theta){
		return spherical(r, phi, theta);
	}
	
	public Vector3D add(Vector3D anotherVector){
		return cartesian(x+anotherVector.x, y+anotherVector.y, z+anotherVector.z);
	}
	
	public Vector3D subtract(Vector3D anotherVector){
		return cartesian(x-anotherVector.x, y-anotherVector.y, z-anotherVector.z);
	}
	
	public Vector3D multiply(Number scale){
		return cartesian(x*scale.doubleValue(), y*scale.doubleValue(), z*scale.doubleValue());
	}
	
	public Vector3D divide(Number scale){
		return multiply(1.0d/scale.doubleValue());
	}
	
	public Vector3D negate(){
		return cartesian(-x, -y, -z);
	}
	
	public Vector3D toUnitVector(){
		return cartesian(x / r, y / r, z / r);
	}
	
	public Vector3D rotateOverX(double angleInRad){
		return cartesian(x,  y*cos(angleInRad) - z*sin(angleInRad), y*sin(angleInRad) + z*cos(angleInRad));
	}
	
	public Vector3D rotateOverY(double angleInRad){
	    return cartesian(x*cos(angleInRad) + z*sin(angleInRad), y, -x*sin(angleInRad) + z*cos(angleInRad));
	}
	
	public Vector3D rotateOverZ(double angleInRad){
		return cartesian(x*cos(angleInRad) - y*sin(angleInRad), y*cos(angleInRad) + x*sin(angleInRad), z);
	}

	public Vector3D crossProduct(Vector3D anotherVector){
		return cartesian(
					y*anotherVector.z - z*anotherVector.y,
					z*anotherVector.x - x*anotherVector.z,
					x*anotherVector.y - y*anotherVector.x);
	}
	
	public double dotProduct(Vector3D anotherVector){
		return x*anotherVector.x + y*anotherVector.y + z*anotherVector.z;
	}
	
    public Point3D asPoint3D() {
        return new Point3D(x, y, z);
    }
    
    public double getXYDistanceFrom(Vector3D b) {
        return (Math.sqrt(Math.pow(x - b.x, 2) + Math.pow(y - b.y, 2)));
    }
	
	@Override
	public String toString(){
		return String.format("x, y,z: [%.3f, %.3f, %.3f] r, phi, theta: [%.3f, %.3f, %.3f]", 
					x, y, z, r, phi, theta);
//		return String.format("x, y,z: [%.2f, %.2f, %.2f]", 
//					x, y, z);
	}

	@Override
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Vector3D other = (Vector3D) obj;
		return abs(x - other.x) < 0.01 && abs(y - other.y) < 0.01 && abs(z - other.z) < 0.01;
	}
	
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	/**
     * Return normalized vector
     *
     * @author Jan Hudec
     * @author Metod Rybar
     * @return normalized vector
     *
     */
    public Vector3D normalize() {
        return getR() != 0 ? divide(getR()) : ZERO_VECTOR;
    }
    
    /**
     * Return rotated vector
     *
     * @author Jan Hudec
     * @author Metod Rybar
     * @return rotated vector
     *
     */
    public Vector3D rotateOver(Vector3D axis, double angleInRad) {
        if (angleInRad == 0)
            return this;

        axis = axis.normalize();
        double sin_a = sin(angleInRad);
        double cos_a = cos(angleInRad);
        double one_minus_cos_a = 1 - cos_a;
        double x2 = axis.getX() * axis.getX();
        double y2 = axis.getY() * axis.getY();
        double z2 = axis.getZ() * axis.getZ();
        double xy = axis.getX() * axis.getY();
        double yz = axis.getY() * axis.getZ();
        double xz = axis.getX() * axis.getZ();

        double[][] matrix = new double[3][3];

        matrix[0][0] = x2 * one_minus_cos_a + cos_a;
        matrix[0][1] = xy * one_minus_cos_a - axis.getZ() * sin_a;
        matrix[0][2] = xz * one_minus_cos_a + axis.getY() * sin_a;

        matrix[1][0] = xz * one_minus_cos_a + axis.getZ() * sin_a;
        matrix[1][1] = y2 * one_minus_cos_a + cos_a;
        matrix[1][2] = yz * one_minus_cos_a - axis.getX() * sin_a;

        matrix[2][0] = xz * one_minus_cos_a - axis.getY() * sin_a;
        matrix[2][1] = yz * one_minus_cos_a + axis.getX() * sin_a;
        matrix[2][2] = z2 * one_minus_cos_a + cos_a;

        double newX = this.getX() * matrix[0][0] + this.getY() * matrix[1][0]
                + this.getZ() * matrix[2][0];
        double newY = this.getX() * matrix[0][1] + this.getY() * matrix[1][1]
                + this.getZ() * matrix[2][1];
        double newZ = this.getX() * matrix[0][2] + this.getY() * matrix[1][2]
                + this.getZ() * matrix[2][2];

        return cartesian(newX, newY, newZ);
    }
    
    /**
     * Return flattened vector
     *
     * @author Jan Hudec
     * @author Metod Rybar
     * @return flattened vector
     *
     */
    public Vector3D flatten(){
        return cartesian(x, y, 0.0);
    }
    
}