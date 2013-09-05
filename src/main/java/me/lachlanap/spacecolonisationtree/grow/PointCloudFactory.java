package me.lachlanap.spacecolonisationtree.grow;

import java.util.Random;
import me.lachlanap.spacecolonisationtree.Point;

/**
 *
 * @author lachlan
 */
public class PointCloudFactory {

    private final Random random;

    public PointCloudFactory(int seed) {
        this.random = new Random(seed);
    }

    public PointCloud makeSpherical(int count, float radius, float xScale, Point centre) {
        PointCloud cloud = new PointCloud();

        for (int i = 0; i < count; i++) {
            double mag = random.nextDouble() * radius;
            double angle = random.nextDouble() * 2 * Math.PI;

            Point p = new Point((float) (mag * Math.cos(angle)) * xScale + centre.x,
                                (float) (mag * Math.sin(angle)) + centre.y);
            cloud.add(p);
        }

        return cloud;
    }
}
