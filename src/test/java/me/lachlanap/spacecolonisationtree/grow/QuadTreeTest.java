package me.lachlanap.spacecolonisationtree.grow;

import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Test;
import me.lachlanap.spacecolonisationtree.AABB;
import me.lachlanap.spacecolonisationtree.Point;

/**
 *
 * @author lachlan
 */
public class QuadTreeTest {

    @Test
    public void testSubdivision() {
        QuadTree tree = new QuadTree(new AABB(5, 5, 10, 10));

        tree.insert(new Point(6, 6));
        tree.insert(new Point(6, 7));
        tree.insert(new Point(7, 6));
        tree.insert(new Point(7, 7));
        assertFalse(tree.isSubdivided());

        tree.insert(new Point(7, 8));
        assertTrue(tree.isSubdivided());
    }

    @Test
    public void testPointFiltering() {
        QuadTree tree = new QuadTree(new AABB(5, 5, 10, 10));

        assertFalse(tree.insert(Point.ZERO));
        assertTrue(tree.insert(new Point(6, 6)));
    }

    @Test
    public void testBasicQuery() {
        QuadTree tree = new QuadTree(new AABB(0, 0, 10, 10));

        tree.insert(new Point(1, 1));
        tree.insert(new Point(6, 6));
        tree.insert(new Point(6, 7));

        Set<Point> query = tree.getInRange(new AABB(0, 0, 3, 3));
        assertTrue(query.contains(new Point(1, 1)));

        query = tree.getInRange(new AABB(3, 3, 10, 10));
        assertTrue(query.contains(new Point(6, 6)));
        assertTrue(query.contains(new Point(6, 7)));
    }

    @Test
    public void testSubdividedQuery() {
        QuadTree tree = new QuadTree(new AABB(0, 0, 10, 10));

        tree.insert(new Point(1, 1));
        tree.insert(new Point(2, 1));
        tree.insert(new Point(1, 2));
        tree.insert(new Point(6, 6));
        tree.insert(new Point(6, 7));

        Set<Point> query = tree.getInRange(new AABB(0, 0, 3, 3));
        assertTrue(query.contains(new Point(1, 1)));
        assertTrue(query.contains(new Point(1, 2)));
        assertTrue(query.contains(new Point(2, 1)));

        query = tree.getInRange(new AABB(3, 3, 10, 10));
        assertTrue(query.contains(new Point(6, 6)));
        assertTrue(query.contains(new Point(6, 7)));
    }
}
