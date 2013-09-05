package me.lachlanap.spacecolonisationtree;

/**
 *
 * @author lachlan
 */
public class Point {

    public final float x, y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point add(Point o) {
        return new Point(x + o.x, y + o.y);
    }

    public Point sub(Point o) {
        return new Point(x - o.x, y - o.y);
    }

    public float dist2(Point p2) {
        float x = this.x - p2.x;
        float y = this.y - p2.y;
        return x * x + y * y;
    }

    public float dist(Point p2) {
        return (float) Math.sqrt(dist2(p2));
    }

    public Point nor() {
        float mag = (float) Math.sqrt(x * x + y * y);
        return new Point(x / mag, y / mag);
    }

    public Point mul(float f) {
        return new Point(x * f, y * f);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Float.floatToIntBits(this.x);
        hash = 37 * hash + Float.floatToIntBits(this.y);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Point other = (Point) obj;
        if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x))
            return false;
        if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
    }
}
