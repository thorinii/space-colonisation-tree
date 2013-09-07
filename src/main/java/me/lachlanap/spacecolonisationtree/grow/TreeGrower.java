package me.lachlanap.spacecolonisationtree.grow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.lachlanap.spacecolonisationtree.AABB;
import me.lachlanap.spacecolonisationtree.Point;

/**
 *
 * @author lachlan
 */
public class TreeGrower {

    public static final int QUADTREE_EXTENTS = 900;
    private final float segmentLength;
    private final Point defaultDirection;
    //
    private final float attractionDistance;
    private final float attractionDistanceSq;
    //
    private final float killDistance;
    private final float killDistanceSq;
    //
    private final float gravityBias;
    //
    private final int maxIterations;
    //
    private BranchQuadTree quadTree;

    public TreeGrower(float segmentLength, float attractionDistance, float killDistance, float gravityBias,
            int maxIterations) {
        this.segmentLength = segmentLength;
        this.defaultDirection = new Point(0, segmentLength);

        this.attractionDistance = attractionDistance;
        this.attractionDistanceSq = attractionDistance * attractionDistance;

        this.killDistance = killDistance;
        this.killDistanceSq = killDistance * killDistance;

        this.gravityBias = gravityBias;

        this.maxIterations = maxIterations;
    }

    public Tree grow(PointCloud cloud, Point base) {
        List<BranchSegment> segments = new ArrayList<>();

        BranchSegment trunk = new BranchSegment(null, base);
        segments.add(trunk);

        growUpToReach(trunk, cloud, segments);
        int iterations = growTree(cloud, segments);

        if (iterations >= maxIterations)
            System.out.println("Terminated due to lack of growth");
        else
            System.out.println("Finished due to all space gone at iteration: " + iterations);

        return new Tree(trunk, segments);
    }

    private void growUpToReach(BranchSegment trunk, PointCloud cloud, List<BranchSegment> segments) {
        BranchSegment reachingSeg = trunk;
        while (!cloud.isAnyInReach(reachingSeg.getPosition(), attractionDistance)) {
            BranchSegment child = new BranchSegment(reachingSeg,
                                                    reachingSeg.getPosition().add(defaultDirection));
            segments.add(child);

            reachingSeg = child;
        }
    }

    private int growTree(PointCloud cloud, List<BranchSegment> segments) {
        quadTree = new BranchQuadTree(new AABB(Point.ZERO, new Point(QUADTREE_EXTENTS, QUADTREE_EXTENTS)));
        for (BranchSegment seg : segments)
            quadTree.insert(seg);

        int itrCount;
        boolean changed = true;
        for (itrCount = 0; itrCount < maxIterations && changed && cloud.hasPoints(); itrCount++) {
            changed = false;

            Map<BranchSegment, Point> grow = new HashMap<>();
            findGrowList(cloud, grow);

            for (Map.Entry<BranchSegment, Point> e : grow.entrySet()) {
                Point pos = e.getValue().nor().mul(segmentLength).add(e.getKey().getPosition());
                BranchSegment seg = new BranchSegment(e.getKey(), pos);
                if (segments.add(seg))
                    quadTree.insert(seg);

                changed = true;
            }
        }

        return itrCount;
    }

    private void findGrowList(PointCloud cloud, Map<BranchSegment, Point> growList) {
        for (Iterator<Point> leafIt = cloud.iterator(); leafIt.hasNext();) {
            Point leaf = leafIt.next();
            BranchSegment closest = findClosestToLeaf(leaf, leafIt);

            if (closest != null) {
                Point dir = growList.get(closest);
                if (dir == null)
                    dir = new Point(0, 0);

                dir = new Point(dir.x, dir.y + gravityBias);

                growList.put(closest, dir.add(leaf.sub(closest.getPosition())));
            }
        }
    }

    private BranchSegment findClosestToLeaf(Point leaf, Iterator<Point> leafIt) {
        BranchSegment closest = null;
        float closestDist2 = Float.MAX_VALUE;

        Set<BranchSegment> inRange = quadTree.getInRange(new AABB(leaf, new Point(attractionDistance,
                                                                                  attractionDistance)));

        for (BranchSegment seg : inRange) {
            float dist2 = leaf.dist2(seg.getPosition());

            if (dist2 > attractionDistanceSq) {
                continue;
            } else if (dist2 < killDistanceSq) {
                leafIt.remove();
                break;
            } else {
                if (dist2 < closestDist2) {
                    closest = seg;
                    closestDist2 = dist2;
                }
            }
        }
        return closest;
    }

    public BranchQuadTree getQuadTree() {
        return quadTree;
    }
}
