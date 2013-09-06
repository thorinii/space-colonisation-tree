package me.lachlanap.spacecolonisationtree.grow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import me.lachlanap.spacecolonisationtree.Point;

/**
 *
 * @author lachlan
 */
public class TreeGrower {

    private final float segmentLength;
    private final Point defaultDirection;
    //
    private final float attractionDistance;
    private final float attractionDistanceSq;
    //
    private final float killDistance;
    private final float killDistanceSq;
    //
    private final int maxIterations;

    public TreeGrower(float segmentLength, float attractionDistance, float killDistance, int maxIterations) {
        this.segmentLength = segmentLength;
        this.defaultDirection = new Point(0, segmentLength);

        this.attractionDistance = attractionDistance;
        this.attractionDistanceSq = attractionDistance * attractionDistance;

        this.killDistance = killDistance;
        this.killDistanceSq = killDistance * killDistance;

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
        int itrCount;
        for (itrCount = 0; itrCount < maxIterations && cloud.hasPoints(); itrCount++) {
            Map<BranchSegment, Point> grow = new HashMap<>();
            findGrowList(cloud, segments, grow);

            for (Map.Entry<BranchSegment, Point> e : grow.entrySet()) {
                Point pos = e.getValue().nor().mul(segmentLength).add(e.getKey().getPosition());
                BranchSegment seg = new BranchSegment(e.getKey(), pos);
                segments.add(seg);
            }
        }

        return itrCount;
    }

    private void findGrowList(PointCloud cloud, List<BranchSegment> segments, Map<BranchSegment, Point> growList) {
        for (Iterator<Point> leafIt = cloud.iterator(); leafIt.hasNext();) {
            Point leaf = leafIt.next();
            BranchSegment closest = findClosestToLeaf(segments, leaf, leafIt);

            if (closest != null) {
                Point dir = growList.get(closest);
                if (dir == null)
                    dir = new Point(0, 0);

                growList.put(closest, dir.add(leaf.sub(closest.getPosition())));
            }
        }
    }

    private BranchSegment findClosestToLeaf(List<BranchSegment> segments, Point leaf, Iterator<Point> leafIt) {
        BranchSegment closest = null;
        float closestDist2 = Float.MAX_VALUE;

        for (Iterator<BranchSegment> branchIt = segments.iterator(); branchIt.hasNext();) {
            BranchSegment seg = branchIt.next();

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
}
