package me.lachlanap.spacecolonisationtree.grow;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import me.lachlanap.spacecolonisationtree.Point;

/**
 *
 * @author lachlan
 */
public class PointCloud implements Iterable<Point> {

    private final Set<Point> points = new HashSet<>();

    @Override
    public Iterator<Point> iterator() {
        return points.iterator();
    }

    public void add(Point p) {
        points.add(p);
    }

    public boolean isAnyInReach(Point reach, float distance) {
        float d2 = distance * distance;

        for (Point p : points) {
            if (reach.dist2(p) <= d2)
                return true;
        }

        return false;
    }

    public boolean hasPoints() {
        return !points.isEmpty();
    }

    @Override
    public PointCloud clone() {
        PointCloud cloud = new PointCloud();
        cloud.points.addAll(points);
        return cloud;
    }
}
