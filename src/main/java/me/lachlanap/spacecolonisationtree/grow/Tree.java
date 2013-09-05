package me.lachlanap.spacecolonisationtree.grow;

import java.util.List;

/**
 *
 * @author lachlan
 */
public class Tree {

    private final BranchSegment trunk;
    private final List<BranchSegment> segments;

    public Tree(BranchSegment trunk, List<BranchSegment> segments) {
        this.trunk = trunk;
        this.segments = segments;
    }

    public BranchSegment getTrunk() {
        return trunk;
    }

    public List<BranchSegment> getSegments() {
        return segments;
    }
}
