package me.lachlanap.spacecolonisationtree.grow;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lachlan
 */
public class World {

    private final List<Tree> trees;

    public World() {
        this.trees = new ArrayList<>();
    }

    public void addTree(Tree tree) {
        trees.add(tree);
    }

    public List<Tree> getTrees() {
        return trees;
    }
}
