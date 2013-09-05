package me.lachlanap.spacecolonisationtree;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import me.lachlanap.spacecolonisationtree.grow.PointCloud;
import me.lachlanap.spacecolonisationtree.grow.PointCloudFactory;
import me.lachlanap.spacecolonisationtree.grow.Tree;
import me.lachlanap.spacecolonisationtree.grow.TreeGrower;
import me.lachlanap.spacecolonisationtree.render.PointCloudRenderer;
import me.lachlanap.spacecolonisationtree.render.TreeRenderer;

/**
 * @author Lachlan Phillips
 */
public class Main {

    public static void main(String[] args) throws IOException {
        PointCloud cloud = PointCloudFactory.makeSpherical(500, 200, new Point(0, 250));

        TreeGrower grower = new TreeGrower();
        Tree grown = grower.grow(cloud, new Point(0, 10));
        render(cloud, grown);
    }

    private static void render(PointCloud cloud, Tree grown) throws IOException {
        BufferedImage img = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = img.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 500, 500);

        renderPointCloud(graphics, cloud);
        renderTree(graphics, grown);

        graphics.dispose();
        ImageIO.write(img, "png", new File("./tree.png"));
    }

    private static void renderPointCloud(Graphics2D graphics, PointCloud cloud) {
        PointCloudRenderer renderer = new PointCloudRenderer();
        renderer.render(cloud, graphics, 500, 500, 500);
    }

    private static void renderTree(Graphics2D graphics, Tree grown) {
        TreeRenderer renderer = new TreeRenderer();
        renderer.render(grown, graphics, 500, 500, 500);
    }
}
