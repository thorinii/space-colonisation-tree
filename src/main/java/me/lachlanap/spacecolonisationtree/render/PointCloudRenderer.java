package me.lachlanap.spacecolonisationtree.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import me.lachlanap.spacecolonisationtree.Point;
import me.lachlanap.spacecolonisationtree.grow.PointCloud;

/**
 *
 * @author lachlan
 */
public class PointCloudRenderer {

    private static final float POINT_DIAMETER = 10f;

    public void render(PointCloud cloud, Graphics2D graphics, float worldWidth, int imgWidth, int imgHeight) {
        Ellipse2D.Float pointShape = new Ellipse2D.Float(0, 0, POINT_DIAMETER, POINT_DIAMETER);

        graphics.setColor(Color.GREEN);
        for (Point point : cloud) {
            pointShape.x = point.x / worldWidth * imgWidth + imgWidth / 2 - POINT_DIAMETER / 2;
            pointShape.y = imgHeight - point.y / worldWidth * imgWidth - POINT_DIAMETER / 2;

            graphics.fill(pointShape);
        }
    }
}
