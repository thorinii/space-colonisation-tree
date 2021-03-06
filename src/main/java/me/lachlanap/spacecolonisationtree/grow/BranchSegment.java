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
    private final int id;

    public BranchSegment(BranchSegment parent, Point position, int id) {
        this.parent = parent;
        this.position = position;
        this.id = id;
    }

    public BranchSegment getParent() {
        return parent;
    }

    public Point getPosition() {
        return position;
    }

    public int getID() {
        return id;
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

    @Override
    public String toString() {
        return "BranchSegment{" + "position=" + position + '}';
    }
}
