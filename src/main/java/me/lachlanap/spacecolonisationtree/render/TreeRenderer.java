package me.lachlanap.spacecolonisationtree.render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import me.lachlanap.spacecolonisationtree.grow.BranchSegment;
import me.lachlanap.spacecolonisationtree.grow.Tree;

/**
 *
 * @author lachlan
 */
public class TreeRenderer {

    private static final float POINT_DIAMETER = 4f;

    public void render(Tree tree, Graphics2D graphics, float worldWidth, int imgWidth, int imgHeight) {
        Ellipse2D.Float pointShape = new Ellipse2D.Float(0, 0, POINT_DIAMETER, POINT_DIAMETER);
        Line2D.Float connectorLine = new Line2D.Float(0, 0, 0, 0);

        for (BranchSegment segment : tree.getSegments()) {
            if (segment.getParent() != null) {
                BranchSegment parent = segment.getParent();

                connectorLine.x1 = parent.getPosition().x / worldWidth * imgWidth + imgWidth / 2;
                connectorLine.y1 = imgHeight - parent.getPosition().y / worldWidth * imgWidth;
                connectorLine.x2 = segment.getPosition().x / worldWidth * imgWidth + imgWidth / 2;
                connectorLine.y2 = imgHeight - segment.getPosition().y / worldWidth * imgWidth;

                graphics.setColor(Color.ORANGE);
                graphics.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                graphics.draw(connectorLine);
            }


            pointShape.x = segment.getPosition().x / worldWidth * imgWidth + imgWidth / 2 - POINT_DIAMETER / 2;
            pointShape.y = imgHeight - segment.getPosition().y / worldWidth * imgWidth - POINT_DIAMETER / 2;

            graphics.setColor(Color.BLACK);
            graphics.fill(pointShape);
        }
    }
}
