package cs1501_p5;

public class CircularHueMetric implements DistanceMetric_Inter {
    public double colorDistance(Pixel p1, Pixel p2) {
        double distance = 0.0;
        if (p1.getHue() > p2.getHue()) {
            distance = Math.min(p1.getHue() - p2.getHue(), (360-p1.getHue()) + p2.getHue());
        } else {
            distance = Math.min(p2.getHue() - p1.getHue(), (360-p2.getHue()) + p1.getHue());
        }
        return distance;
    }
}