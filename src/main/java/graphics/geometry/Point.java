package graphics.geometry;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point translate(Point dp) {
        return new Point(x + dp.x, y + dp.y);
    }

    public Point difference(Point p) {
        return new Point(Math.abs(x - p.x), Math.abs(y - p.y));
    }
}
