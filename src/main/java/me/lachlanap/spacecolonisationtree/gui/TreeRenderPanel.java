package me.lachlanap.spacecolonisationtree.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import me.lachlanap.spacecolonisationtree.grow.PointCloud;
import me.lachlanap.spacecolonisationtree.grow.Tree;
import me.lachlanap.spacecolonisationtree.render.PointCloudRenderer;
import me.lachlanap.spacecolonisationtree.render.TreeRenderer;

/**
 *
 * @author lachlan
 */
public class TreeRenderPanel extends JPanel {

    private final PointCloudRenderer pcr = new PointCloudRenderer();
    private final TreeRenderer tr = new TreeRenderer();
    private PointCloud pointCloud;
    private Tree tree;

    public TreeRenderPanel() {
        setBackground(Color.WHITE);
    }

    public void setPointCloud(PointCloud pointCloud) {
        this.pointCloud = pointCloud;
        repaint();
    }

    public void setTree(Tree tree) {
        this.tree = tree;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render((Graphics2D) g, getWidth(), getHeight());
    }

    public void renderToImage() throws IOException {
        BufferedImage img = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = img.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 500, 500);

        render(graphics, 500, 500);

        graphics.dispose();
        ImageIO.write(img, "png", new File("./tree.png"));
    }

    private void render(Graphics2D graphics, int width, int height) {
        if (pointCloud != null)
            pcr.render(pointCloud, graphics, 500, width, height);
        if (tree != null)
            tr.render(tree, graphics, 500, width, height);
    }
}
