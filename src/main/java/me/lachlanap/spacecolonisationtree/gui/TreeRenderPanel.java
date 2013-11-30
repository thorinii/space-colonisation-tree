package me.lachlanap.spacecolonisationtree.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import me.lachlanap.spacecolonisationtree.grow.BranchQuadTree;
import me.lachlanap.spacecolonisationtree.grow.PointCloud;
import me.lachlanap.spacecolonisationtree.grow.Tree;
import me.lachlanap.spacecolonisationtree.grow.World;
import me.lachlanap.spacecolonisationtree.gui.Frame;
import me.lachlanap.spacecolonisationtree.render.PointCloudRenderer;
import me.lachlanap.spacecolonisationtree.render.TreeRenderer;

/**
 *
 * @author lachlan
 */
public class TreeRenderPanel extends JPanel {

    private final PointCloudRenderer pcr = new PointCloudRenderer();
    private final TreeRenderer tr = new TreeRenderer();

    private final List<Point> bases;

    private PointCloud pointCloud;
    private World world = new World();
    private BranchQuadTree quadTree;

    public TreeRenderPanel() {
        bases = new ArrayList<>();
        setBackground(Color.WHITE);

        BasePointEditMouseHandler bpemh = new BasePointEditMouseHandler();
        addMouseListener(bpemh);
        addMouseMotionListener(bpemh);
    }

    public void setPointCloud(PointCloud pointCloud) {
        this.pointCloud = pointCloud;
        repaint();
    }

    public void setWorld(World world) {
        this.world = world;
        repaint();
    }

    @Deprecated
    public void setTree(Tree tree) {
        this.world = new World();
        world.addTree(tree);
        repaint();
    }

    public void setQuadTree(BranchQuadTree quadTree) {
        this.quadTree = quadTree;
        repaint();
    }

    public List<Point> getBases() {
        return bases;
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
        if (quadTree != null && false)
            renderQuadTree(graphics, 500, width);

        int id = 0;
        for (Tree tree : world.getTrees()) {
            tr.render(tree, graphics, 500, width, id);
            id++;
        }

        for (Point p : bases) {
            Point actual = new Point(p);
            actual.x = (int) (actual.x / 500f * width + width / 2);
            actual.y = (int) (actual.y / 500f * width);

            graphics.setColor(Color.BLACK);
            graphics.drawLine(actual.x, actual.y - 10,
                              actual.x, actual.y + 10);
            graphics.drawLine(actual.x - 10, actual.y,
                              actual.x + 10, actual.y);
        }

        {
            if (getMousePosition() == null)
                return;
            Point actual = new Point(getMousePosition());

            actual.y = getHeight() - actual.y;

            graphics.setColor(Color.GRAY);
            graphics.drawLine(actual.x, actual.y - 10,
                              actual.x, actual.y + 10);
            graphics.drawLine(actual.x - 10, actual.y,
                              actual.x + 10, actual.y);
        }
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

    private class BasePointEditMouseHandler extends MouseAdapter {

        private Point selected;

        @Override
        public void mousePressed(MouseEvent e) {
            Point mouse = getMouse(e);

            for (Point p : bases) {
                if (p.distance(mouse) < 10) {
                    selected = p;
                    break;
                }
            }

            if (e.getButton() == MouseEvent.BUTTON1) {
                if (selected == null) {
                    bases.add(mouse);
                    selected = mouse;
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                if (selected != null) {
                    bases.remove(selected);
                    selected = null;
                }
            }

            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (selected == null)
                return;

            Point mouse = getMouse(e);
            selected.setLocation(mouse);

            repaint();
            ((Frame) SwingUtilities.getWindowAncestor(TreeRenderPanel.this)).render(true);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            selected = null;
            repaint();
            ((Frame) SwingUtilities.getWindowAncestor(TreeRenderPanel.this)).render();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            repaint();
        }

        private Point getMouse(MouseEvent e) {
            Point mouse = new Point(e.getX(), getHeight() - e.getY());

            int width = getWidth() / 2;

            mouse.x = (int) ((mouse.x - width / 2) * 500f / width);

            mouse.y = (int) (mouse.y * 500f / width);

            //mouse.x = (int) (mouse.x / 500f * width + width / 2);
            //mouse.y = (int) (mouse.y / 500f * width);

            return mouse;
        }
    }
}
