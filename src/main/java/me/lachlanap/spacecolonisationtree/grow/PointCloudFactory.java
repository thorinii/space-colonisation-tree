package me.lachlanap.spacecolonisationtree.grow;

import java.util.Random;
import me.lachlanap.spacecolonisationtree.Point;

/**
 *
 * @author lachlan
 */
public class PointCloudFactory {

    private static final Random random = new Random(1033L);

    public static PointCloud makeSpherical(int count, float radius, Point centre) {
        PointCloud cloud = new PointCloud();

        for (int i = 0; i < count; i++) {
            double mag = random.nextDouble() * radius;
            double angle = random.nextDouble() * 2 * Math.PI;

            Point p = new Point((float) (mag * Math.cos(angle)) + centre.x,
                                (float) (mag * Math.sin(angle)) + centre.y);
            cloud.add(p);
        }

        return cloud;
    }
}
