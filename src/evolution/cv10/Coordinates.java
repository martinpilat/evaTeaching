package evolution.cv10;

public class Coordinates {

    public double x;
    public double y;

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    static final double conv = Math.PI / 180.0;

    public static double distance(Coordinates c1, Coordinates c2) {

        double dlat = conv * (c1.x - c2.x);
        double dlng = conv * (c1.y - c2.y);
        double sq1 = Math.sin(dlat / 2);
        sq1 = sq1 * sq1;
        double sq2 = Math.sin(dlng / 2);
        sq2 = sq2 * sq2;
        return 6371.01 * 2 * Math.asin(Math.sqrt(sq1 + Math.cos(c1.x * conv) * Math.cos(c2.x * conv) * sq2));
    }

    public String toString() {
        return y + "," + x + ",5000";
    }
}
