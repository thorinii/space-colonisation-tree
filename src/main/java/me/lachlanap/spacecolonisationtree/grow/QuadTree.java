package me.lachlanap.spacecolonisationtree.grow;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import me.lachlanap.spacecolonisationtree.AABB;
import me.lachlanap.spacecolonisationtree.Point;

/**
 *
 * @author lachlan
 */
public class QuadTree {

    public static final int MAX = 4;
    private final AABB area;
    private final Point[] points;
    private int size;
    private QuadTree nw;
    private QuadTree ne;
    private QuadTree sw;
    private QuadTree se;

    public QuadTree(AABB area) {
        this.area = area;
        points = new Point[MAX];
        size = 0;
    }

    public boolean isSubdivided() {
        return nw != null;
    }

    public boolean insert(Point p) {
        if (!area.containsPoint(p))
            return false;

        if (size < MAX) {
            points[size] = p;
            size++;
            return true;
        }

        if (!isSubdivided())
            subdivide();

        if (nw.insert(p))
            return true;
        if (ne.insert(p))
            return true;
        if (sw.insert(p))
            return true;
        if (se.insert(p))
            return true;

        throw new IllegalStateException("Something odd happened when inserting " + p);
    }

    public Set<Point> getInRange(AABB range) {
        if (!area.intersectsAABB(range))
            return Collections.emptySet();

        Set<Point> inRange = new HashSet<>();

        if (isSubdivided()) {
            inRange.addAll(nw.getInRange(range));
            inRange.addAll(ne.getInRange(range));
            inRange.addAll(sw.getInRange(range));
            inRange.addAll(se.getInRange(range));
        } else {
            for (int i = 0; i < size; i++) {
                Point p = points[i];
                if (range.containsPoint(p))
                    inRange.add(p);
            }
        }

        return inRange;
    }

    private void subdivide() {
        Point extents = area.extents.mul(.5f);

        nw = new QuadTree(new AABB(area.centre.add(-extents.x, -extents.y), extents));
        ne = new QuadTree(new AABB(area.centre.add(extents.x, -extents.y), extents));
        sw = new QuadTree(new AABB(area.centre.add(-extents.x, extents.y), extents));
        se = new QuadTree(new AABB(area.centre.add(extents.x, extents.y), extents));

        for (int i = 0; i < size; i++) {
            Point p = points[i];

            if (nw.insert(p))
                continue;
            if (ne.insert(p))
                continue;
            if (sw.insert(p))
                continue;
            if (se.insert(p))
                continue;
        }
    }
}
