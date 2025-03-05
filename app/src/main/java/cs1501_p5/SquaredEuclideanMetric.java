package cs1501_p5;

public class SquaredEuclideanMetric implements DistanceMetric_Inter {
    public double colorDistance(Pixel p1, Pixel p2) {
        double distance = Math.pow((p1.getRed() - p2.getRed()), 2) + Math.pow((p1.getGreen() - p2.getGreen()), 2) + Math.pow((p1.getBlue() - p2.getBlue()), 2);
        return distance;
    }
}