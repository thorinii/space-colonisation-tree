package me.lachlanap.spacecolonisationtree.grow;

import java.util.Objects;
import me.lachlanap.spacecolonisationtree.Point;

/**
 *
 * @author lachlan
 */
public class BranchSegment {

    private final BranchSegment parent;
    private final Point position;

    public BranchSegment(BranchSegment parent, Point position) {
        this.parent = parent;
        this.position = position;
    }

    public BranchSegment getParent() {
        return parent;
    }

    public Point getPosition() {
        return position;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.position);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final BranchSegment other = (BranchSegment) obj;
        if (!Objects.equals(this.position, other.position))
            return false;
        return true;
    }
}
