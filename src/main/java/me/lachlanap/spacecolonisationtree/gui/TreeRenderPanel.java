package me.lachlanap.spacecolonisationtree.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import me.lachlanap.spacecolonisationtree.grow.BranchQuadTree;
import me.lachlanap.spacecolonisationtree.grow.PointCloud;
import me.lachlanap.spacecolonisationtree.grow.Tree;
import me.lachlanap.spacecolonisationtree.grow.TreeGrower;
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
    private BranchQuadTree quadTree;

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

    public void setQuadTree(BranchQuadTree quadTree) {
        this.quadTree = quadTree;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render((Graphics2D) g, getWidth() / 2, getHeight());
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
        graphics.translate(0, height);
        graphics.scale(1, -1);

        if (pointCloud != null)
            pcr.render(pointCloud, graphics, 500, width);
        if (quadTree != null)
            renderQuadTree(graphics, 500, width);
        if (tree != null)
            tr.render(tree, graphics, 500, width);
    }

    private void renderQuadTree(Graphics2D graphics, int worldWidth, int imgWidth) {
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(1));

        renderQuadTree(quadTree, graphics,
                       quadTree.area.left / worldWidth * imgWidth + imgWidth / 2,
                       quadTree.area.top / worldWidth * imgWidth,
                       quadTree.area.extents.x * 2 / worldWidth * imgWidth,
                       quadTree.area.extents.y * 2 / worldWidth * imgWidth);
    }

    private void renderQuadTree(BranchQuadTree tree, Graphics2D graphics, float x, float y, float width, float height) {
        Rectangle2D.Float rect = new Rectangle2D.Float(x, y, width, height);

        if (tree.isSubdivided()) {
            renderQuadTree(tree.getNw(), graphics, x, y, width / 2, height / 2);
            renderQuadTree(tree.getNe(), graphics, x + width / 2, y, width / 2, height / 2);
            renderQuadTree(tree.getSw(), graphics, x, y + height / 2, width / 2, height / 2);
            renderQuadTree(tree.getSe(), graphics, x + width / 2, y + height / 2, width / 2, height / 2);
        }

        graphics.draw(rect);
    }
}
