package graphics.rendering;

import graphics.util.Point;

public interface Renderer {

    void drawLine(Point s, Point e);
    void fillPolygon(Point[] points);

}
