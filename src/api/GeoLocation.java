package api;

import java.util.Objects;

/**
 * * This class represents a geo location <x,y,z>, aka Point3D
 */

public class GeoLocation implements geo_location {

    private double x;
    private double y;
    private double z;

    /**
     * default constructor
     */
    public GeoLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * constructor for new point
     *
     * @param g
     */
    public GeoLocation(geo_location g) {
        if(g != null) {
            double fx = g.x();
            double fy = g.y();
            double fz = g.z();
            this.x = fx;
            this.y = fy;
            this.z = fz;
        }
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    /**
     * this method calculate the distance between 2 points
     *
     * @param g
     * @return distance in double.
     */
    @Override
    public double distance(geo_location g) {
        double disX = this.x - g.x();
        double disY = this.y - g.z();
        double disZ = this.z - g.z();
        double dis = (disX * disX + disY * disY + disZ * disZ);
        return Math.sqrt(dis);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoLocation that = (GeoLocation) o;
        return this.x == that.x() &&
                this.y == that.y() &&
                this.z == that.z();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public String toString() {
        return "The location is: " + x + ", " + y + ", " + z;
    }
}