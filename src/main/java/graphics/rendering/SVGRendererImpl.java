package graphics.rendering;

import graphics.util.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SVGRendererImpl implements Renderer {

    private final List<String> lines = new ArrayList<>();
    private final String filename;

    public SVGRendererImpl(String filename) {
        this.filename = filename;
        lines.add("<svg  xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">");
    }

    public void close() throws IOException {
        lines.add("</svg>");
    }

    @Override
    public void drawLine(Point s, Point e) {
        lines.add(String.format("<Line x1=\"%d\" y1=\"%d\" x2=\"%d\"   y2=\"%d\" style=\"stroke:#0000ff;\"/>",
                s.getX(),
                s.getY(),
                e.getX(),
                e.getY()));
    }

    @Override
    public void fillPolygon(Point[] points) {
        StringBuilder polygonTagBuilder = new StringBuilder("<polygon points=\"");
        for (Point point : points) {
            polygonTagBuilder.append(point.getX()).append(",").append(point.getY());
        }
        polygonTagBuilder.append("\" style=\"stroke:#ff0000; fill:#ff00ff;\"/>");
        lines.add(polygonTagBuilder.toString());
    }
}
