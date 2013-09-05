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

        // Build up to reach
        BranchSegment reachingSeg = trunk;
        while (!cloud.isAnyInReach(reachingSeg.getPosition(), attractionDistance)) {
            BranchSegment child = new BranchSegment(reachingSeg,
                                                    reachingSeg.getPosition().add(defaultDirection));
            segments.add(child);

            reachingSeg = child;
        }


        // Grow
        int i;
        for (i = 0; i < maxIterations && cloud.hasPoints(); i++) {
            //while (cloud.hasPoints()) {
            Map<BranchSegment, Point> grow = new HashMap<>();
            for (Iterator<Point> leafIt = cloud.iterator(); leafIt.hasNext();) {
                Point leaf = leafIt.next();

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

                if (closest != null) {
                    Point dir = grow.get(closest);
                    if (dir == null)
                        dir = new Point(0, 0);

                    grow.put(closest, dir.add(leaf.sub(closest.getPosition())));
                }
            }

            for (Map.Entry<BranchSegment, Point> e : grow.entrySet()) {
                Point pos = e.getValue().nor().mul(segmentLength).add(e.getKey().getPosition());
                BranchSegment seg = new BranchSegment(e.getKey(), pos);
                segments.add(seg);
            }
        }

        if (i >= maxIterations)
            System.out.println("Terminated due to lack of growth");
        else
            System.out.println("Finished due to all space gone at iteration: " + i);

        return new Tree(trunk, segments);
    }
}
