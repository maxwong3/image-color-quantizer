package cs1501_p5;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class ClusteringMapGenerator implements ColorMapGenerator_Inter {
    DistanceMetric_Inter distMetric;
    Double[][] closestCentroidDist;
    public ClusteringMapGenerator(DistanceMetric_Inter metric) {
        distMetric = metric;
    }

    public Pixel[] generateColorPalette(Pixel[][] pixelArray, int numColors) {
        Pixel[] palette = new Pixel[numColors];
        palette[0] = pixelArray[0][0];

        if (numColors == 1) {
            return palette;
        }

        Pixel maxDistance = pixelArray[0][0];
        closestCentroidDist = new Double[pixelArray.length][pixelArray[0].length];

        int maxI = 0;
        int maxJ = 0;

        for (int i = 0; i < pixelArray.length; i++) {
            for (int j = 0; j < pixelArray[i].length; j++) {
                closestCentroidDist[i][j] = distMetric.colorDistance(palette[0], pixelArray[i][j]);
                if (closestCentroidDist[i][j] > distMetric.colorDistance(palette[0], maxDistance)) {
                    maxDistance = pixelArray[i][j];
                    maxI = i;
                    maxJ = j;
                }
            }
        }

        for (int k = 1; k < numColors; k++) {
            for (int i = 0; i < closestCentroidDist.length; i++) {
                for (int j = 0; j < closestCentroidDist[i].length; j++) {
                    if (closestCentroidDist[i][j] >= 0.0 && closestCentroidDist[i][j] > distMetric.colorDistance(palette[k-1], pixelArray[i][j])) closestCentroidDist[i][j] = distMetric.colorDistance(palette[k-1], pixelArray[i][j]);
                    if (closestCentroidDist[i][j] >= 0.0 && closestCentroidDist[i][j] > closestCentroidDist[maxI][maxJ]) {
                        maxDistance = pixelArray[i][j];
                        maxI = i;
                        maxJ = j;
                    }
                    if (closestCentroidDist[i][j] >= 0.0 && closestCentroidDist[i][j] == distMetric.colorDistance(palette[k-1], maxDistance)) {
                        if (getRGB(pixelArray[i][j]) > getRGB(maxDistance)) {
                            maxDistance = pixelArray[i][j];
                            maxI = i;
                            maxJ = j;
                        }
                    }
                }   
            }
            
            palette[k] = maxDistance;
            maxDistance = pixelArray[maxI][maxJ];
            closestCentroidDist[maxI][maxJ] = -1.0;

        }
        return palette;
    }

    public Map<Pixel, Pixel> generateColorMap(Pixel[][] pixelArray, Pixel[] initialColorPalette) {
        Map<Pixel, Pixel> colorMap = new HashMap<>();
        // stores list of pixels for each centroid (from initialcolorpalette)
        Map<Pixel, ArrayList<Pixel>> clusters = new HashMap<>();
        for (int i = 0; i < pixelArray.length; i++) {
            for (int j = 0; j < pixelArray[i].length; j++) {
                Pixel centroid = closestCentroid(pixelArray[i][j], initialColorPalette);
                colorMap.put(pixelArray[i][j], centroid);
                if (!(clusters.containsKey(centroid))) {
                    clusters.put(centroid, new ArrayList<Pixel>());
                }
                clusters.get(centroid).add(pixelArray[i][j]);
            }
        }
        Pixel[] newCentroids = new Pixel[initialColorPalette.length];
        int count = 0;
        boolean continueLloyds = false;
        for (Pixel centroid : clusters.keySet()) {
            int numPixels = clusters.get(centroid).size();
            double avgRed = 0.0;
            double avgGreen = 0.0;
            double avgBlue = 0.0;
            for (int i = 0; i < numPixels; i++) {
                avgRed += (double)clusters.get(centroid).get(i).getRed();
                avgGreen += (double)clusters.get(centroid).get(i).getGreen();
                avgBlue += (double)clusters.get(centroid).get(i).getBlue();
            }
            avgRed = avgRed / (double)numPixels;
            avgGreen = avgGreen / (double)numPixels;
            avgBlue = avgBlue / (double)numPixels;
            Pixel newCentroid = new Pixel((int)avgRed, (int)avgGreen, (int)avgBlue);
            newCentroids[count] = newCentroid;
            if (getRGB(centroid) != getRGB(newCentroid)) {
                continueLloyds = true;
            }
            count++;
        }
        if (continueLloyds == true) {
            return generateColorMap(pixelArray, newCentroids);
        }
        return colorMap;
    }

    private int getRGB(Pixel p) {
        int rgb = 0;
        rgb += (p.getRed() * 65536 + p.getGreen() * 256 + p.getBlue());
        return rgb;
    }

    private Pixel closestCentroid(Pixel p, Pixel[] initialColorPalette) {
        double closestLength = Double.MAX_VALUE;
        Pixel closest = initialColorPalette[0]; 
        for (int i = 0; i < initialColorPalette.length; i++) {
            if (distMetric.colorDistance(p, initialColorPalette[i]) < closestLength) {
                closestLength = distMetric.colorDistance(p, initialColorPalette[i]);
                closest = initialColorPalette[i];
            } else if (distMetric.colorDistance(p, initialColorPalette[i]) == closestLength) {
                if (getRGB(p) > getRGB(closest)) {
                   closestLength = distMetric.colorDistance(p, initialColorPalette[i]);
                   closest = initialColorPalette[i]; 
                }
            }
        }
        return closest;
    }
}