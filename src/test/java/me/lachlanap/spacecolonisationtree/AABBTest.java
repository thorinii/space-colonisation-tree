package me.lachlanap.spacecolonisationtree;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author lachlan
 */
public class AABBTest {

    @Test
    public void testContainsPoint() {
        AABB aabb = new AABB(0, 0, 10, 10);

        assertFalse(aabb.containsPoint(new Point(-1, -1)));
        assertFalse(aabb.containsPoint(new Point(1, -1)));
        assertFalse(aabb.containsPoint(new Point(-1, 1)));

        assertTrue(aabb.containsPoint(new Point(1, 1)));
    }

    @Test
    public void testIntersectsAABB() {
    }
}
