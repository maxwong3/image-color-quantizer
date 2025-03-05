package cs1501_p5;
import java.util.Map;
import java.util.HashMap;

public class BucketingMapGenerator implements ColorMapGenerator_Inter {
    public Pixel[] generateColorPalette(Pixel[][] pixelArray, int numColors) {
        double colorSpace = 1 << 24;
        double bucketSize = colorSpace / numColors;
        Pixel[] palette = new Pixel[numColors];
        for (int i = 0; i < numColors; i++) {
            double bucketMiddle = ((bucketSize*i)+(bucketSize/2));
            int red = (int)bucketMiddle >> 16 & 0xFF;
            int green = (int)bucketMiddle >> 8 & 0xFF;
            int blue = (int)bucketMiddle & 0xFF; 
            // color divided by i (the ith bucket) gets the size of one bucket, divided by 2 to get the size of half a bucket (subtracted from color to get middle of ith bucket)
            Pixel newPixel = new Pixel(red, green, blue);
            palette[i] = newPixel;
        }
        return palette;
    }
    public Map<Pixel, Pixel> generateColorMap(Pixel[][] pixelArray, Pixel[] initialColorPalette) {
        HashMap<Pixel, Pixel> colorMap = new HashMap<>();

        for (int i = 0; i < pixelArray.length; i++) {
            for (int j = 0; j < pixelArray[i].length; j++) {
                int rgb = pixelArray[i][j].getRed() * 65536 + pixelArray[i][j].getGreen() * 256 + pixelArray[i][j].getBlue();
                Pixel quantPixel = initialColorPalette[0];
                for (Pixel pixel : initialColorPalette) {
                    if (rgb <= (pixel.getRed() * 65536 + pixel.getGreen() * 256 + pixel.getBlue())) {
                        quantPixel = pixel;
                    }
                }
                colorMap.put(pixelArray[i][j], quantPixel);
            }
        }

        return colorMap;
    }
}
