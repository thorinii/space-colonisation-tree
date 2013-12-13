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

    public PointCloud makeElliptical(int count, float radius, float xScale, float yScale, Point centre) {
        PointCloud cloud = new PointCloud();

        for (int i = 0; i < count; i++) {
            double mag = random.nextDouble() * radius;
            double angle = random.nextDouble() * 2 * Math.PI;

            Point p = new Point((float) (mag * Math.cos(angle)) * xScale + centre.x,
                                (float) (mag * Math.sin(angle)) * yScale + centre.y);
            cloud.add(p);
        }

        return cloud;
    }

    public PointCloud makeTreeish(int count, float radius, float xScale, Point centre) {
        PointCloud cloud = new PointCloud();

        cloud.addCloud(makeElliptical(count * 2 / 3, radius, xScale, 1, centre.add(0, radius)));
        cloud.addCloud(makeElliptical(count * 1 / 3, radius, xScale * 4, 0.4f, centre.add(0, -radius / 2)));

        return cloud;
    }
}
