package me.lachlanap.spacecolonisationtree.grow;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import me.lachlanap.spacecolonisationtree.AABB;

/**
 *
 * @author lachlan
 */
public class BranchQuadTree {

    public static final int MAX = 32;
    public final AABB area;
    private final BranchSegment[] branches;
    private int size;
    private BranchQuadTree nw;
    private BranchQuadTree ne;
    private BranchQuadTree sw;
    private BranchQuadTree se;

    public BranchQuadTree(AABB area) {
        this.area = area;
        branches = new BranchSegment[MAX];
        size = 0;
    }

    public boolean isSubdivided() {
        return size >= MAX && nw != null;
    }

    public BranchQuadTree getNw() {
        return nw;
    }

    public BranchQuadTree getNe() {
        return ne;
    }

    public BranchQuadTree getSw() {
        return sw;
    }

    public BranchQuadTree getSe() {
        return se;
    }

    public boolean insert(BranchSegment b) {
        if (!area.containsPoint(b.getPosition()))
            return false;

        if (size < MAX) {
            branches[size] = b;
            size++;
            return true;
        } else {
            for (int i = 0; i < size; i++) {
                BranchSegment existing = branches[i];
                if (existing.equals(b))
                    return true;
            }
        }

        if (!isSubdivided())
            subdivide();

        if (nw.insert(b))
            return true;
        if (ne.insert(b))
            return true;
        if (sw.insert(b))
            return true;
        if (se.insert(b))
            return true;

        throw new IllegalStateException("Something odd happened when inserting " + b
                + ". " + this + " " + nw + " " + ne + " " + sw + " " + se);
    }

    public Set<BranchSegment> getInRange(AABB range) {
        if (!area.intersectsAABB(range))
            return Collections.emptySet();

        Set<BranchSegment> inRange = new HashSet<>();
        getInRange(range, inRange);

        return inRange;
    }

    private void getInRange(AABB range, Set<BranchSegment> store) {
        if (!area.intersectsAABB(range))
            return;

        if (isSubdivided()) {
            nw.getInRange(range, store);
            ne.getInRange(range, store);
            sw.getInRange(range, store);
            se.getInRange(range, store);
        } else {
            for (int i = 0; i < size; i++) {
                BranchSegment b = branches[i];
                if (range.containsPoint(b.getPosition()))
                    store.add(b);
            }
        }
    }

    private void subdivide() {
        nw = new BranchQuadTree(new AABB(area.left, area.top,
                                         area.left + area.extents.x, area.top + area.extents.y));
        ne = new BranchQuadTree(new AABB(area.left + area.extents.x, area.top,
                                         area.right, area.top + area.extents.y));
        sw = new BranchQuadTree(new AABB(area.left, area.top + area.extents.y,
                                         area.left + area.extents.x, area.bottom));
        se = new BranchQuadTree(new AABB(area.left + area.extents.x, area.top + area.extents.y,
                                         area.right, area.bottom));

        //nw = new BranchQuadTree(new AABB(area.centre.add(-extents.x, -extents.y), extents));
        //ne = new BranchQuadTree(new AABB(area.centre.add(extents.x, -extents.y), extents));
        //sw = new BranchQuadTree(new AABB(area.centre.add(-extents.x, extents.y), extents));
        //se = new BranchQuadTree(new AABB(area.centre.add(extents.x, extents.y), extents));

        for (int i = 0; i < size; i++) {
            BranchSegment b = branches[i];

            if (nw.insert(b))
                continue;
            if (ne.insert(b))
                continue;
            if (sw.insert(b))
                continue;
            if (se.insert(b))
                continue;
        }
    }

    @Override
    public String toString() {
        return getInRange(area).toString() + ": " + area;
    }
}
