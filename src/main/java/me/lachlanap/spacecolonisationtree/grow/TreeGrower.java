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

    private static final float SEGMENT_LENGTH = 10f;
    private static final Point DEFAULT_DIRECTION = new Point(0, SEGMENT_LENGTH);
    //
    private static final float ATTRACTION_DISTANCE = 50;
    private static final float ATTRACTION_DISTANCE2 = ATTRACTION_DISTANCE * ATTRACTION_DISTANCE;
    //
    private static final float KILL_DISTANCE = 12f;
    private static final float KILL_DISTANCE2 = KILL_DISTANCE * KILL_DISTANCE;
    //
    private static final int MAX_ITERATIONS = 500;

    public Tree grow(PointCloud cloud, Point base) {
        List<BranchSegment> segments = new ArrayList<>();

        BranchSegment trunk = new BranchSegment(null, base);
        segments.add(trunk);

        // Build up to reach
        BranchSegment reachingSeg = trunk;
        while (!cloud.isAnyInReach(reachingSeg.getPosition(), ATTRACTION_DISTANCE)) {
            BranchSegment child = new BranchSegment(reachingSeg,
                                                    reachingSeg.getPosition().add(DEFAULT_DIRECTION));
            segments.add(child);

            reachingSeg = child;
        }


        // Grow
        int i;
        for (i = 0; i < MAX_ITERATIONS && cloud.hasPoints(); i++) {
            //while (cloud.hasPoints()) {
            Map<BranchSegment, Point> grow = new HashMap<>();
            for (Iterator<Point> leafIt = cloud.iterator(); leafIt.hasNext();) {
                Point leaf = leafIt.next();

                BranchSegment closest = null;
                float closestDist2 = Float.MAX_VALUE;

                for (Iterator<BranchSegment> branchIt = segments.iterator(); branchIt.hasNext();) {
                    BranchSegment seg = branchIt.next();

                    float dist2 = leaf.dist2(seg.getPosition());

                    if (dist2 > ATTRACTION_DISTANCE2) {
                        continue;
                    } else if (dist2 < KILL_DISTANCE2) {
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
                Point pos = e.getValue().nor().mul(SEGMENT_LENGTH).add(e.getKey().getPosition());
                BranchSegment seg = new BranchSegment(e.getKey(), pos);
                segments.add(seg);
            }
        }

        if (i >= MAX_ITERATIONS)
            System.out.println("Terminated due to lack of growth");
        else
            System.out.println("Finished due to all space gone at iteration: " + i);

        return new Tree(trunk, segments);
    }
}
