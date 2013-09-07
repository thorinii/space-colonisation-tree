package me.lachlanap.spacecolonisationtree;

import java.util.Objects;

/**
 *
 * @author lachlan
 */
public class AABB {

    public static final AABB ZERO = new AABB(Point.ZERO, Point.ZERO);
    public final Point centre;
    public final Point extents;
    public final float left;
    public final float top;
    public final float right;
    public final float bottom;

    public AABB(float x1, float y1, float x2, float y2) {
        this(new Point((x1 + x2) / 2f, (y1 + y2) / 2f),
             new Point((x2 - x1) / 2f, (y2 - y1) / 2f));
    }

    public AABB(Point centre, Point extents) {
        this.centre = centre;
        this.extents = extents;

        left = centre.x - extents.x;
        top = centre.y - extents.y;
        right = centre.x + extents.x;
        bottom = centre.y + extents.y;
    }

    public boolean containsPoint(Point p) {
        return (p.x >= left && p.x <= right)
                && (p.y >= top && p.y <= bottom);
    }

    public boolean intersectsAABB(AABB o) {
        return !(o.left >= right || o.right <= left
                || o.top >= bottom || o.bottom <= top);
    }

    public float getArea() {
        return extents.x * extents.y * 4;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.centre);
        hash = 41 * hash + Objects.hashCode(this.extents);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final AABB other = (AABB) obj;
        if (!Objects.equals(this.centre, other.centre))
            return false;
        if (!Objects.equals(this.extents, other.extents))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AABB{" + "centre=" + centre + ", extents=" + extents + '}';
    }
}
