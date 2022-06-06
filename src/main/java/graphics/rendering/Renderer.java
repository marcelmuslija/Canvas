package graphics.rendering;

import graphics.geometry.Point;

public interface Renderer {

    void drawLine(Point s, Point e);
    void fillPolygon(Point[] points);

}
