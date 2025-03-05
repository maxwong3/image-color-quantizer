package cs1501_p5;

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.BufferedWriter;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ColorQuantizer implements ColorQuantizer_Inter {
    Pixel[][] pixelArray;
    ColorMapGenerator_Inter generator;
    File file;
    public ColorQuantizer(Pixel[][] pixelArray, ColorMapGenerator_Inter gen) {
        this.pixelArray = pixelArray;
        generator = gen;
    }
    public ColorQuantizer(String bmpFilename, ColorMapGenerator_Inter gen) {
        try {
            file = new File(bmpFilename);
            BufferedImage image = ImageIO.read(file);
            pixelArray = Util.convertBitmapToPixelMatrix(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        generator = gen;
    }
    /**
     * Performs color quantization using the color map generator specified when
     * this quantizer was constructed.
     *
     * @param numColors number of colors to use for color quantization
     * @return A two dimensional array where each index represents the pixel
     * from the original bitmap image and contains a Pixel representing its
     * color after quantization
     */
    public Pixel[][] quantizeTo2DArray(int numColors) {
        Pixel[][] quantized = new Pixel[pixelArray.length][pixelArray[0].length];
        Pixel[] colorPalette = generator.generateColorPalette(pixelArray, numColors);
        Map<Pixel, Pixel> colorMap = generator.generateColorMap(pixelArray, colorPalette);
        for (int i = 0; i < pixelArray.length; i++) {
            for (int j = 0; j < pixelArray[i].length; j++) {
                quantized[i][j] = colorMap.get(pixelArray[i][j]);
            }
        }
        return quantized;
    }

    /**
     * Performs color quantization using the color map generator specified when
     * this quantizer was constructed. Rather than returning the pixel array,
     * this method writes the resulting image in bmp format to the specified
     * file.
     *
     * @param numColors number of colors to use for color quantization
     * @param fileName File to write resulting image to
     */
    public void quantizeToBMP(String fileName, int numColors) {
        Pixel[][] quantized = new Pixel[pixelArray.length][pixelArray[0].length];
        Pixel[] colorPalette = generator.generateColorPalette(pixelArray, numColors);
        Map<Pixel, Pixel> colorMap = generator.generateColorMap(pixelArray, colorPalette);
        for (int i = 0; i < pixelArray.length; i++) {
            for (int j = 0; j < pixelArray[i].length; j++) {
                quantized[i][j] = colorMap.get(pixelArray[i][j]);
            }
        }
        Util.savePixelMatrixToFile(fileName, quantized);
    }
}