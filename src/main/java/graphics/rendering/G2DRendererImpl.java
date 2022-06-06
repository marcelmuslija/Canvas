package graphics.rendering;

import graphics.geometry.Point;

import java.awt.Graphics2D;

public class G2DRendererImpl implements Renderer {
    private final Graphics2D g2d;

    public G2DRendererImpl(Graphics2D g2d) {
        this.g2d = g2d;
    }

    @Override
    public void drawLine(Point s, Point e) {

    }

    @Override
    public void fillPolygon(Point[] points) {

    }
}
