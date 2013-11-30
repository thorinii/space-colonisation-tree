package me.lachlanap.spacecolonisationtree.grow;

import java.util.*;
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

    public World grow(PointCloud cloud, List<Point> bases) {
        List<List<BranchSegment>> treesSegments = new ArrayList<>();
        List<BranchSegment> trunks = new ArrayList<>();

        int treeID = 0;
        for (Point base : bases) {
            BranchSegment trunk = new BranchSegment(null, base, treeID);
            trunks.add(trunk);

            List<BranchSegment> segments = new ArrayList<>();
            segments.add(trunk);
            treesSegments.add(segments);

            treeID++;
        }

        growToReach(trunks, cloud, treesSegments);
        int iterations = growTree(cloud, treesSegments);

        if (iterations >= maxIterations)
            System.out.println("Terminated due to lack of growth");
        else
            System.out.println("Finished due to all space gone at iteration: " + iterations);

        World world = new World();
        for (int i = 0; i < trunks.size(); i++) {
            BranchSegment trunk = trunks.get(i);
            List<BranchSegment> segments = treesSegments.get(i);
            world.addTree(new Tree(trunk, segments));
        }
        return world;
    }

    private void growToReach(List<BranchSegment> trunks, PointCloud cloud, List<List<BranchSegment>> treesSegments) {
        for (int i = 0; i < trunks.size(); i++) {
            BranchSegment reachingUpSeg = trunks.get(i);
            BranchSegment reachingDownSeg = trunks.get(i);
            List<BranchSegment> segments = treesSegments.get(i);
            int itrs = 0;

            while (!cloud.isAnyInReach(reachingUpSeg.getPosition(), attractionDistance)
                   && !cloud.isAnyInReach(reachingDownSeg.getPosition(), attractionDistance)) {
                BranchSegment upChild = new BranchSegment(reachingUpSeg,
                                                          reachingUpSeg.getPosition().add(defaultDirection),
                                                          i);
                segments.add(upChild);
                reachingUpSeg = upChild;

                if (itrs % 2 == 0) {
                    BranchSegment downChild = new BranchSegment(reachingDownSeg,
                                                                reachingDownSeg.getPosition().sub(defaultDirection),
                                                                i);
                    segments.add(downChild);
                    reachingDownSeg = downChild;
                }

                if (itrs++ > 100)
                    break;
            }
        }
    }

    private int growTree(PointCloud cloud, List<List<BranchSegment>> treesSegments) {
        quadTree = new BranchQuadTree(new AABB(Point.ZERO, new Point(QUADTREE_EXTENTS, QUADTREE_EXTENTS)));
        for (List<BranchSegment> segments : treesSegments)
            for (BranchSegment seg : segments)
                quadTree.insert(seg);

        int itrCount;
        boolean changed = true;
        for (itrCount = 0; itrCount < maxIterations && changed && cloud.hasPoints(); itrCount++) {
            changed = false;

            for (int i = 0; i < treesSegments.size(); i++) {
                List<BranchSegment> segments = treesSegments.get(i);

                Map<BranchSegment, Point> grow = new HashMap<>();
                findGrowList(cloud, grow, i);

                for (Map.Entry<BranchSegment, Point> e : grow.entrySet()) {
                    Point pos = e.getValue().nor().mul(segmentLength).add(e.getKey().getPosition());
                    BranchSegment seg = new BranchSegment(e.getKey(), pos, i);
                    if (segments.add(seg))
                        quadTree.insert(seg);

                    changed = true;
                }
            }
        }

        return itrCount;
    }

    private void findGrowList(PointCloud cloud, Map<BranchSegment, Point> growList, int id) {
        for (Iterator<Point> leafIt = cloud.iterator(); leafIt.hasNext();) {
            Point leaf = leafIt.next();
            BranchSegment closest = findClosestToLeaf(leaf, leafIt, id);

            if (closest != null) {
                Point dir = growList.get(closest);
                if (dir == null)
                    dir = new Point(0, 0);

                dir = new Point(dir.x, dir.y + gravityBias);

                growList.put(closest, dir.add(leaf.sub(closest.getPosition())));
            }
        }
    }

    private BranchSegment findClosestToLeaf(Point leaf, Iterator<Point> leafIt, int id) {
        BranchSegment closest = null;
        float closestDist2 = Float.MAX_VALUE;

        Set<BranchSegment> inRange = quadTree.getInRange(new AABB(leaf, new Point(attractionDistance,
                                                                                  attractionDistance)));

        for (BranchSegment seg : inRange) {
            if (seg.getID() != id)
                continue;

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
